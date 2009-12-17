package com.itao.starlite.ui.jmesa;

import org.jmesa.view.editor.BasicCellEditor;
import org.jmesa.view.editor.CellEditor;

import com.itao.starlite.model.Document;

public class DocumentCellEditor implements CellEditor {

	public Object getValue(Object arg0, String arg1, int arg2) {
		Object value = new BasicCellEditor().getValue(arg0, arg1, arg2);
		if (value == null)
			return "<div style='background-color:#FFCCCC;width:100%;'>-</div>";
		if (value == "")
			return "<div style='background-color:#FFCCCC;width:100%;'>-</div>";
		if (value == "#")
			return "<div style='background-color:#FFCCCC;width:100%;'>-</div>";
		if (value instanceof Document) {
			Document doc = (Document)value;
			return "<div style='background-color:#d0f0c0;width:100%;' title='"+doc.getDocUrl()+"' ><a href='"+doc.getDocUrl()+"' target='_blank'>View</a></div>";
		}
		else return "<div style='background-color:#d0f0c0;width:100%;' title='"+value+"' ><a href='"+value+"' target='_blank'>View</a></div>";
	}

}
