/*******************************************************************************
 * Copyright (c) 2014 MEDEVIT.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     T. Huster - initial API and implementation
 *******************************************************************************/
package at.medevit.elexis.ehc.core;

import java.io.InputStream;

import ch.elexis.data.Mandant;
import ch.elexis.data.Patient;
import ch.elexis.data.Rezept;
import ehealthconnector.cda.documents.ch.CdaCh;

public interface EhcCoreService {
	public CdaCh getCdaChDocument(Patient patient, Mandant mandant);
	
	public CdaCh getDocument(InputStream document);
	
	public void importPatient(ehealthconnector.cda.documents.ch.Patient selectedPatient);
	
	public CdaCh getPrescriptionDocument(Rezept rezept);
}
