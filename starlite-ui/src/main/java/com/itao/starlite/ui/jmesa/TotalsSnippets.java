package com.itao.starlite.ui.jmesa;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jmesa.core.CoreContext;
import org.jmesa.view.component.Column;
import org.jmesa.view.html.HtmlBuilder;
import org.jmesa.view.html.HtmlConstants;
import org.jmesa.view.html.HtmlSnippetsImpl;
import org.jmesa.view.html.component.HtmlColumn;
import org.jmesa.view.html.component.HtmlRow;
import org.jmesa.view.html.component.HtmlTable;
import org.jmesa.view.html.toolbar.Toolbar;

public class TotalsSnippets extends HtmlSnippetsImpl {
	private HtmlTable table;
	private CoreContext coreContext;

	private HashMap<Column, Number> totals = new HashMap<Column, Number>();
	
	public TotalsSnippets(HtmlTable arg0, Toolbar arg1, CoreContext arg2) {
		super(arg0, arg1, arg2);
		this.table = arg0;
		this.coreContext = arg2;
	}

	@Override
	public String body() {
		HtmlBuilder html = new HtmlBuilder();

        int rowcount = 0;

        boolean rowcountIncludePagination = new Boolean(coreContext.getPreference(HtmlConstants.ROWCOUNT_INCLUDE_PAGINATION));
        if (rowcountIncludePagination) {
            int page = coreContext.getLimit().getRowSelect().getPage();
            int maxRows = coreContext.getLimit().getRowSelect().getMaxRows();
            rowcount = (page - 1) * maxRows;
        }

        Collection<?> items = coreContext.getPageItems();
        for (Object item : items) {
            rowcount++;

            HtmlRow row = table.getRow();
            List<Column> columns = row.getColumns();

            html.append(row.getRowRenderer().render(item, rowcount));

            for (Iterator<Column> iter = columns.iterator(); iter.hasNext();) {
                HtmlColumn column = (HtmlColumn) iter.next();
                html.append(column.getCellRenderer().render(item, rowcount));
                addToTotals(column, item, rowcount);
            }

            html.trEnd(1);
        }
        
        HtmlRow row = table.getRow();
        List<Column> columns = row.getColumns();
        html.append(row.getRowRenderer().render("", items.size()+1));
        boolean first = true;
        
        html.trEnd(1);
        return html.toString();
	}

	private void addToTotals(HtmlColumn column, Object item, int rowcount) {
		Object value = column.getCellRenderer().getCellEditor().getValue(item, column.getProperty(), rowcount);
		if (value == null)
			return;
		if (value instanceof Number) {
			doAdd((Number)value, column);
		} else {
			doAddConvertIfPossible(value, column);
		}
	}

	private void doAdd(Number value, HtmlColumn column) {
		Number existing = totals.get(column);
		if (existing == null) {
			existing = new Integer(0);
			totals.put(column, existing);
		}
		
		if (value instanceof Integer) {
			int i = value.intValue();
			if (existing instanceof Double) {
				double dval = existing.doubleValue();
				dval += i;
				totals.put(column, new Double(dval));
			} else if (existing instanceof Integer) {
				int ival = existing.intValue();
				ival += i;
				totals.put(column, new Integer(ival));
			}
		} else if (value instanceof Double) {
			double d = value.doubleValue();
			if (existing instanceof Double) {
				double dval = existing.doubleValue();
				dval += d;
				totals.put(column, new Double(dval));
			} else if (existing instanceof Integer) {
				int ival = existing.intValue();
				d += ival;
				totals.put(column, new Double(d));
			}
		}
	}

	private void doAddConvertIfPossible(Object value, HtmlColumn column) {
		String str = value.toString();
		try {
			Integer i = Integer.parseInt(str);
			doAdd(i, column);
		} catch (NumberFormatException e) {
			try {
				Double d = Double.parseDouble(str);
				doAdd(d, column);
			} catch (NumberFormatException e2) {
				
			}
		} 
	}
}
