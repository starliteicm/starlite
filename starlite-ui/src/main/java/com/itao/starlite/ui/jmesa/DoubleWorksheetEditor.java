package com.itao.starlite.ui.jmesa;

import org.jmesa.worksheet.editor.HtmlWorksheetEditor;

public class DoubleWorksheetEditor extends HtmlWorksheetEditor {

	public Object getValue(Object arg0, String arg1, int arg2) {
		String strVal = (String) super.getValue(arg0, arg1, arg2);
		try {
			Double val = Double.parseDouble(strVal);
			return val;
		} catch (NumberFormatException e) {}
		return null;
	}

}
