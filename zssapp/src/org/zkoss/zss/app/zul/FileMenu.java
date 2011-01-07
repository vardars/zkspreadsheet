/* FileMenu.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Nov 23, 2010 5:43:38 PM , Created by Sam
}}IS_NOTE

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

*/
package org.zkoss.zss.app.zul;

import java.io.ByteArrayOutputStream;

import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zss.app.Consts;
import org.zkoss.zss.app.file.FileHelper;
import org.zkoss.zss.app.zul.ctrl.DesktopWorkbenchContext;
import org.zkoss.zss.app.zul.ctrl.WorkbookCtrl;
import org.zkoss.zss.app.zul.ctrl.WorkspaceContext;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;

/**
 * 
 * @author sam
 *
 */
public class FileMenu extends Menu implements IdSpace {
	
	private Menupopup fileMenupopup;
	private Menuitem newFile;
	private Menuitem openFile;

	private Menuitem saveFile;
	//TODO: not implement yet
	private Menuitem saveFileAs;
	private Menuitem saveFileAndClose;
	//TODO: permission control
	private Menuitem deleteFile;
	private Menuitem importFile;
	private Menuitem exportPdf;
	private boolean _exportToPdfDisabled; /* default false */
	
	private Menuitem exportExcel;
	private boolean _exportToExcelDisabled;
	
	private Menuitem fileReversion;
	private Menuitem print;
	
	public FileMenu() {
		Executions.createComponents(Consts._FileMenu_zul, this, null);
		Components.wireVariables(this, this, '$', true, true);
		Components.addForwards(this, this, '$');

		importFile.setDisabled(!FileHelper.hasImportPermission());
		
		boolean saveDisabled = !FileHelper.hasSavePermission();
		saveFile.setDisabled(saveDisabled);
		saveFileAndClose.setDisabled(saveDisabled);
		
		//TODO: save as not implement yet
		saveFileAs.setDisabled(true);
	}
	
	public void onClick$newFile() {
		getDesktopWorkbenchContext().getWorkbookCtrl().newBook();
		getDesktopWorkbenchContext().fireWorkbookChanged();
	}
	
	public void onClick$openFile() {
		getDesktopWorkbenchContext().getWorkbenchCtrl().openOpenFileDialog();
	}
	
	public void onClick$saveFile() {
		//TODO: refactor duplicate save logic
		DesktopWorkbenchContext workbench = getDesktopWorkbenchContext();
		if (workbench.getWorkbookCtrl().hasFileExtentionName()) {
			workbench.getWorkbookCtrl().save();
			workbench.fireWorkbookSaved();
		} else
			workbench.getWorkbenchCtrl().openSaveFileDialog();
	}
	
	public void onClick$saveFileAs() {
		throw new UiException("save is not implement yet");
	}
	
	public void onClick$saveFileAndClose() {
		//TODO: refactor duplicate save logic
		DesktopWorkbenchContext workbench = getDesktopWorkbenchContext();
		if (workbench.getWorkbookCtrl().hasFileExtentionName()) {
			workbench.getWorkbookCtrl().save();
			workbench.getWorkbookCtrl().close();
			workbench.fireWorkbookSaved();
			workbench.fireWorkbookChanged();
		} else
			workbench.getWorkbenchCtrl().openSaveFileDialog();
	}
	
	public void onClick$deleteFile() {
		DesktopWorkbenchContext workbench = getDesktopWorkbenchContext();
		if(!workbench.getWorkbookCtrl().hasFileExtentionName()) {
			workbench.getWorkbookCtrl().close();
			workbench.fireWorkbookChanged();
			return;
		}
		
		WorkspaceContext.getInstance(Executions.getCurrent().getDesktop()).
			delete(workbench.getWorkbookCtrl().getSrc());
		workbench.getWorkbookCtrl().close();
		workbench.fireWorkbookChanged();
	}
	
	public void onClick$importFile() {
		getDesktopWorkbenchContext().getWorkbenchCtrl().openImportFileDialog();
	}

	public void setExportPdfDisabled(boolean disabled) {
		_exportToPdfDisabled = disabled;
		exportPdf.setDisabled(disabled);
	}
	
	public void onClick$exportPdf() {
		getDesktopWorkbenchContext().getWorkbenchCtrl().openExportPdfDialog();
	}

	public void setExportExcelDisabled(boolean disabled) {
		_exportToExcelDisabled = disabled;
		exportExcel.setDisabled(disabled);
	}
	
	public void onClick$exportExcel() {
		WorkbookCtrl bookCtrl = getDesktopWorkbenchContext().getWorkbookCtrl();
		ByteArrayOutputStream out = bookCtrl.exportToExcel();
		Filedownload.save(out.toByteArray(), "application/file", bookCtrl.getBookName());
	}
	
	public void onClick$fileReversion() {
		throw new UiException("reversion not implement yet");
	}
	
	public void onClick$print() {
		throw new UiException("print not implement yet");
	}
	
	public void onOpen$fileMenupopup() {
		getDesktopWorkbenchContext().getWorkbookCtrl().reGainFocus();
	}
	
	protected DesktopWorkbenchContext getDesktopWorkbenchContext() {
		return Zssapp.getDesktopWorkbenchContext(this);
	}
	
	public void onCreate() {
        final DesktopWorkbenchContext workbenchCtrl = getDesktopWorkbenchContext();
        getDesktopWorkbenchContext().addEventListener(Consts.ON_WORKBOOK_CHANGED, new EventListener() {
            public void onEvent(Event event) throws Exception {
                boolean isOpen = workbenchCtrl.getWorkbookCtrl().hasBook();
                boolean savePermission = FileHelper.hasSavePermission();
                
    			saveFile.setDisabled(!savePermission);
    			//TODO: not implemented yet
    			saveFileAs.setDisabled(true);
    			saveFileAndClose.setDisabled(!savePermission);
    			
    			if (isOpen) {
	    			deleteFile.setDisabled(false);
	    			exportPdf.setDisabled(_exportToPdfDisabled | false);
	    			exportExcel.setDisabled(_exportToExcelDisabled | false);
    			} else {
	    			deleteFile.setDisabled(true);
	    			exportPdf.setDisabled(true);
	    			exportExcel.setDisabled(true);
    			}
    			
    			//TODO: not implemented yet
    			fileReversion.setDisabled(true);
    			print.setDisabled(true);
            }
        });
	}
}