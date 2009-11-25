package com.itao.starlite.ui.jmesa;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jmesa.view.editor.BasicCellEditor;
import org.jmesa.view.editor.CellEditor;

public class DateOrBasicEditor implements CellEditor {
	private static DateFormat df = new SimpleDateFormat("d");
	public Object getValue(Object arg0, String arg1, int arg2) {
		Object value = new BasicCellEditor().getValue(arg0, arg1, arg2);
		if (value instanceof Date) {
			return df.format(value);
		} else 
			return value;
	}
}
