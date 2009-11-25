package com.itao.starlite.ui.jmesa;

import org.apache.commons.lang.StringEscapeUtils;
import org.jmesa.view.editor.BasicCellEditor;
import org.jmesa.view.editor.CellEditor;

public class HtmlEncodeCellEditor implements CellEditor {
	private CellEditor wrappedEditor;
	
	public HtmlEncodeCellEditor() {
		super();
		this.wrappedEditor = new BasicCellEditor();
	}
	
	public HtmlEncodeCellEditor(CellEditor wrappedEditor) {
		this.wrappedEditor = wrappedEditor;
	}
	
	public Object getValue(Object arg0, String arg1, int arg2) {
		Object val = wrappedEditor.getValue(arg0, arg1, arg2);
		return StringEscapeUtils.escapeHtml(val.toString());
	}

}
