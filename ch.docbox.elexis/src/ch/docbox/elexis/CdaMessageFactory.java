/*******************************************************************************
 * Copyright (c) 2010, Oliver Egger, visionary ag
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *    
 *******************************************************************************/
package ch.docbox.elexis;

import java.lang.reflect.Method;

import ch.elexis.data.PersistentObject;
import ch.elexis.data.PersistentObjectFactory;

public class CdaMessageFactory extends PersistentObjectFactory {
	@SuppressWarnings({
		"unchecked", "rawtypes"
	})
	public PersistentObject createFromString(String code){
		try {
			String[] ci = code.split("::"); //$NON-NLS-1$
			Class clazz = Class.forName(ci[0]);
			Method load = clazz.getMethod("load", new Class[] { String.class}); //$NON-NLS-1$
			return (PersistentObject) (load.invoke(null, new Object[] {
				ci[1]
			}));
		} catch (Exception ex) {
			return null;
		}
	}
	
	@SuppressWarnings({
		"rawtypes"
	})
	@Override
	public PersistentObject doCreateTemplate(Class typ){
		try {
			return (PersistentObject) typ.newInstance();
		} catch (Exception ex) {
			return null;
		}
	}
}
