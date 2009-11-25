package com.itao.starlite.ui.jmesa;

import org.jmesa.view.editor.BasicCellEditor;

public class TickCrossCellEditor extends BasicCellEditor {

	@Override
	public Object getValue(Object arg0, String arg1, int arg2) {
		Boolean val = (Boolean) super.getValue(arg0, arg1, arg2);
		if (val == null)
			return "";
		String img;
		if (val.equals(Boolean.TRUE))
			img = "tick.png";
		else
			img = "cross.png";
		return super.getValue(arg0, arg1, arg2);
		//return "<img src='images/icons/"+img+"'/>";
	}

}
