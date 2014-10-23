package ch.elexis.connect.afinion.packages;

import java.util.ResourceBundle;

import ch.elexis.core.ui.importer.div.importers.LabImportUtil;
import ch.elexis.core.ui.importer.div.importers.LabImportUtil.TransientLabResult;
import ch.elexis.core.ui.util.SWTHelper;
import ch.elexis.data.LabItem;
import ch.elexis.data.LabResult;
import ch.elexis.data.Labor;
import ch.elexis.data.Patient;
import ch.rgw.tools.TimeTool;

public class Value {
	private static final String BUNDLE_NAME = "ch.elexis.connect.afinion.packages.valuetexts";
	
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	
	private static String getString(String paramName, String key){
		return RESOURCE_BUNDLE.getString(paramName + "." + key);
	}
	
	public static Value getValue(final String paramName, final String unit) throws PackageException{
		return new Value(paramName, unit);
	}
	
	String _shortName;
	String _longName;
	String _unit;
	LabItem _labItem;
	String _refMann;
	String _refFrau;
	Labor _labor;
	
	public String get_shortName(){
		return _shortName;
	}
	
	public String get_longName(){
		return _longName;
	}
	
	Value(final String paramName, final String unit) throws PackageException{
		_shortName = getString(paramName, "kuerzel");
		_longName = getString(paramName, "text");
		_unit = getString(paramName, "unit");
		if (unit != null && !unit.equals(_unit)) {
			boolean answeredYes =
				SWTHelper.askYesNo("Einheiten Abweichung", "Vom Gerät erhalten: " + unit
					+ "\nErwartet: " + _unit + "\nSoll die vom Gerät mitgesendete Einheit [" + unit
					+ "] übernommen werden?");
			if (answeredYes) {
				_unit = unit;
			}
		}
		_refMann = getString(paramName, "refM");
		_refFrau = getString(paramName, "refF");
	}
	
	private void initialize(){
		_labor = LabImportUtil.getOrCreateLabor(Messages.getString("Value.LabKuerzel"));
		
		_labItem = LabImportUtil.getLabItem(_shortName, _labor);
		
		if (_labItem == null) {
			_labItem =
				new LabItem(_shortName, _longName, _labor, _refMann, _refFrau, _unit,
					LabItem.typ.NUMERIC, Messages.getString("Value.LabName"), "50");
		}
	}
	
	public TransientLabResult fetchValue(Patient patient, String value, String flags, TimeTool date){
		if (_labItem == null) {
			initialize();
		}
		
		// do not set a flag or comment if none is given
		if (flags == null || flags.isEmpty()) {
			return new TransientLabResult.Builder(patient, _labor, _labItem, value).date(date)
				.build();
		}
		
		String comment = "";
		int resultFlags = 0;
		if (flags.equals("1")) {
			// comment = Messages.getString("Value.High");
			resultFlags |= LabResult.PATHOLOGIC;
		}
		if (flags.equals("2")) {
			// comment = Messages.getString("Value.Low");
			resultFlags |= LabResult.PATHOLOGIC;
		}
		if (flags.equals("*") || flags.equals("E")) {
			comment = Messages.getString("Value.Error");
		}
		
		return new TransientLabResult.Builder(patient, _labor, _labItem, value).date(date)
			.comment(comment).flags(resultFlags).build();
	}
}