package com.itao.starlite.ui.jmesa;

import org.jmesa.limit.Limit;
import org.jmesa.view.html.HtmlBuilder;
import org.jmesa.worksheet.WorksheetColumn;
import org.jmesa.worksheet.editor.AbstractWorksheetEditor;

public class SelectWorksheetEditor extends AbstractWorksheetEditor {
	private String[] vals;
	private String[] labels;
	
	private String jsVals;
	private String jsLabels;
	
	public SelectWorksheetEditor(String[] vals) {
		this.vals = vals;
		this.labels = vals;
		processArrays();
	}
	
	public SelectWorksheetEditor(String[] labels, String[] vals) {
		this.labels = labels;
		this.vals = vals;
		processArrays();
	}
    
    private void processArrays() {
		jsLabels = processArray(labels);
		jsVals = processArray(vals);
	}
    
    private String processArray(String[] array) {
    	StringBuilder valsSB = new StringBuilder();
		valsSB.append('[');
		boolean first = true;
		for (String s: array) {
			if (s != null) {
				if (!first) {
					valsSB.append(',');
				} else {
					first = false;
				}
				valsSB.append('\'');
				valsSB.append(s);
				valsSB.append('\'');
			}
		}
		valsSB.append(']');
		return valsSB.toString();
    }

	public Object getValue(Object item, String property, int rowcount) {
        Object value = null;
        
        WorksheetColumn worksheetColumn = getWorksheetColumn(item, property);
        if (worksheetColumn != null) {
            value = worksheetColumn.getChangedValue();
        } else {
            value = getCellEditor().getValue(item, property, rowcount);
        }

        return getWsColumn(worksheetColumn, value, item);
    }
    
    private String getWsColumn(WorksheetColumn worksheetColumn, Object value, Object item) {
    	 HtmlBuilder html = new HtmlBuilder();

         Limit limit = getCoreContext().getLimit();

         html.div();
         
         if (worksheetColumn != null) {
             if (worksheetColumn.hasError()) {
                 html.styleClass("wsColumnError");
                 html.title(worksheetColumn.getError());
             } else {
                 html.styleClass("wsColumnChange");
             }
         } else {
             html.styleClass("wsColumn");
         }
         
         html.onclick(getUniquePropertyJavaScript(item) + "createWsSelectColumn(this, '" + limit.getId() + "'," + UNIQUE_PROPERTY + ",'" 
             + getColumn().getProperty() + "', " + jsVals + ", " + jsLabels + ")");
         html.close();
         html.append(value);
         html.divEnd();

         return html.toString();
//        HtmlBuilder html = new HtmlBuilder();
//        
//        Limit limit = getCoreContext().getLimit();
//        
//        //html.input().type("select");
//        
//        html.select();
//        html.close();
//        for (int i=0; i<vals.length; i++) {
//        	html.option().value(vals[i]);
//        	if (value != null && value.equals(vals[i]))
//        		html.selected();
//        	html.close();
//        	html.append(labels[i]);
//        	
//        	html.optionEnd();
//        }
//        html.selectEnd();
//
////        html.onclick(getUniquePropertyJavaScript(item) + "submitWsCheckboxColumn(this,'" + limit.getId() + "'," + UNIQUE_PROPERTY + ",'" 
////            + getColumn().getProperty() + "')");
////        html.end();
//        
//        return html.toString();
    }
}
