/* StartEditingCommand.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Dec 18, 2007 12:10:40 PM     2007, Created by Dennis.Chen
}}IS_NOTE

Copyright (C) 2007 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under Lesser GPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
 */
package org.zkoss.zss.ui.au.in;

import java.util.Map;

import org.zkoss.lang.Objects;
import org.zkoss.zss.model.Worksheet;
import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.mesg.MZk;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.event.EditboxEditingEvent;
import org.zkoss.zss.ui.impl.Utils;

/**
 * A Command (client to server) ...means the client is editing...
 * 
 * @author kinda lu
 * 
 */
public class EditboxEditingCommand implements Command {
	
	public void process(AuRequest request) {
		final Component comp = request.getComponent();
		if (comp == null)
			throw new UiException(MZk.ILLEGAL_REQUEST_COMPONENT_REQUIRED, this);
		final Map data = (Map) request.getData();
		if (data == null || data.size() != 3)
			throw new UiException(MZk.ILLEGAL_REQUEST_WRONG_DATA, new Object[] {Objects.toString(data), this });
		String token = (String) data.get("token");
		String sheetId = (String) data.get("sheetId");
		String clienttxt = (String) data.get("clienttxt");

		Worksheet sheet = ((Spreadsheet) comp).getSelectedSheet();
		if (!Utils.getSheetUuid(sheet).equals(sheetId)) {
			return;
		}
		EditboxEditingEvent event = new EditboxEditingEvent(org.zkoss.zss.ui.event.Events.ON_EDITBOX_EDITING, comp, sheet, clienttxt);
		Events.postEvent(event);
	}
}
