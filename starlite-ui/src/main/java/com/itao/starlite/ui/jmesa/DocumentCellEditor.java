package com.itao.starlite.ui.jmesa;

import org.jmesa.view.editor.BasicCellEditor;
import org.jmesa.view.editor.CellEditor;

import com.itao.starlite.model.Document;

public class DocumentCellEditor implements CellEditor {

	public Object getValue(Object arg0, String arg1, int arg2) {
		Object value = new BasicCellEditor().getValue(arg0, arg1, arg2);
		if (value == null)
			return "";
		if (value instanceof Document) {
			Document doc = (Document)value;
			return "<a href='"+doc.getDocUrl()+"' target='_blank'>View</a>";
		}
		else return "<a href='"+value+"' target='_blank'>View</a>";
	}

}
