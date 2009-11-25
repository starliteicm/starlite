package com.itao.starlite.ui.jmesa;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.jmesa.view.component.Column;
import org.jmesa.view.html.HtmlBuilder;
import org.jmesa.view.html.HtmlSnippets;
import org.jmesa.view.html.HtmlView;

public class PlainTableView extends HtmlView {
	private List<String> colsToTotal;
	
	private boolean showFilters = false;
	
	public void setColsToTotal(String... colsToTotal) {
		this.colsToTotal = Arrays.asList(colsToTotal);
	}
	
	@Override
	public Object render() {
		HtmlSnippets snippets = getHtmlSnippets();

		HtmlBuilder html = new HtmlBuilder();

		html.append(snippets.themeStart());

		html.append(snippets.tableStart());

		html.append(snippets.theadStart());

//		html.append(snippets.toolbar());

//		html.append(snippets.filter());

		html.append(snippets.header());

		html.append(snippets.theadEnd());

		html.append(snippets.tbodyStart());

		html.append(snippets.body());

		if (colsToTotal != null) {
			List<Column> cols = getTable().getRow().getColumns();
			List<Integer> colsToTotalIndices = new LinkedList<Integer>();
			
			int i=0;
			StringBuilder totalRow = new StringBuilder();
			String rowId = getCoreContext().getLimit().getId() + "Totals";
			totalRow.append("<tr id='" + rowId + "'class='odd'  onmouseover=\"this.className='highlight'\"  onmouseout=\"this.className='odd'\"><td>Totals</td>");
			for (Column c: cols) {
				if (i > 0) {
					if (colsToTotal.contains(c.getProperty())) {
						colsToTotalIndices.add(i);
					}
					totalRow.append("<td id='"+rowId+i+"'></td>");
				}
				i++;
			}
			totalRow.append("</tr>");
			html.append(totalRow.toString());
		}
		
		html.append(snippets.tbodyEnd());

		html.append(snippets.footer());

//		html.append(snippets.statusBar());
		if (showFilters)
			html.append(snippets.filter());

		html.append(snippets.tableEnd());

		html.append(snippets.themeEnd());

		html.append(snippets.initJavascriptLimit());

		return html.toString();

	}

	public PlainTableView showFilters() {
		showFilters = true;
		return this;
	}
	
}
