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
package at.medevit.elexis.ehc.core.internal.document;

import org.openhealthtools.mdht.uml.cda.ch.CDACH;

import ehealthconnector.cda.documents.ch.CdaCh;

public class CdaChImpl extends CdaCh {
	public CdaChImpl(CDACH doc){
		this.doc = doc;
		docRoot.setClinicalDocument(doc);
	}
}
