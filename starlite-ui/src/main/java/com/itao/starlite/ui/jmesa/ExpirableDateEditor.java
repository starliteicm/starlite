package com.itao.starlite.ui.jmesa;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jmesa.view.editor.BasicCellEditor;
import org.jmesa.view.editor.CellEditor;
import org.joda.time.DateMidnight;
import org.joda.time.Days;

public class ExpirableDateEditor implements CellEditor {
	private DateFormat df;
	private int closeLimit = 90;
	private int veryCloseLimit = 30;
	
	public ExpirableDateEditor() {
		df = new SimpleDateFormat("dd/MM/yyyy");
	}
	
	public Object getValue(Object arg0, String arg1, int arg2) {
		Object value = new BasicCellEditor().getValue(arg0, arg1, arg2);
		if (value == null)
			return "X";
		if (value instanceof Date) {
			Date d = (Date)value;
			DateMidnight expires = new DateMidnight(d);
			DateMidnight today = new DateMidnight();
			
			String spanClass = "";
			
			if (expires.isAfter(today)) {
				int days = Days.daysBetween(today, expires).getDays();
				if (days <= veryCloseLimit) {
					spanClass = "veryClose";
				} else if (days <= closeLimit) {
					spanClass = "close";
				}
			} else {
				spanClass = "expired";
			}
			if (spanClass.equals("")) {
				return df.format(d);
			} else {
				return "<span class='"+spanClass+"'>" + df.format(d) + "</span>";
			}
		} else 
			return "Not A Date";
	}

}
