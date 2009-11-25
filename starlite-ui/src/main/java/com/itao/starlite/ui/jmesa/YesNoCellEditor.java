package com.itao.starlite.ui.jmesa;

import org.jmesa.view.editor.BasicCellEditor;

public class YesNoCellEditor extends BasicCellEditor {

	@Override
	public Object getValue(Object arg0, String arg1, int arg2) {
		Boolean val = (Boolean) super.getValue(arg0, arg1, arg2);
		if (val == null)
			return "";
		if (val.equals(Boolean.TRUE))
			return "Yes";
		return "No";
	}

}
