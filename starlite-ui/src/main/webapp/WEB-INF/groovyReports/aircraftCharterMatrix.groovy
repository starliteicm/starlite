import org.jmesa.limit.Limit;
import org.jmesa.facade.TableFacade;
import org.jmesa.facade.TableFacadeImpl;
import org.jmesa.facade.TableFacadeFactory;
import org.jmesa.view.component.Column;
import org.jmesa.view.component.Table;
import org.jmesa.view.editor.BasicCellEditor;
import org.jmesa.view.editor.CellEditor;
import org.jmesa.view.html.HtmlBuilder;
import org.jmesa.view.html.component.HtmlTable;
import org.jmesa.view.html.component.HtmlRow;
import org.jmesa.view.html.HtmlComponentFactory;

import com.itao.starlite.ui.jmesa.ExpirableDateEditor;
import com.itao.starlite.model.CrewMember;
import com.itao.starlite.model.Money;
import org.apache.struts2.ServletActionContext;

import com.itao.jmesa.dsl.entities.Table;

def generate(manager, pageContext) {
	pageContext["title"] = "Aircraft/Charter Matrix Report"
	
	def month = pageContext["request"].getParameter("month")
	if (month.length() == 1)
		month = "0"+month
	def year = pageContext["request"].getParameter("year")
	def period = year + month
	println period
	
	def datePeriod = new org.joda.time.DateMidnight(Integer.valueOf(year), Integer.valueOf(month), 1).toDate()
	def dateFormat = new java.text.SimpleDateFormat("MMM yy")
	
	def cl = manager.getAllCharters()
	
	def report = []
	
	def aircraft = manager.getAllAircraft().aircraftList
	for (a in aircraft) {
		//create a row for each aircraft
		HashMap<String, Object> reportRow = [:]
		//put aircraft reg in aircraft column
		reportRow["aircraft"] = a.getRef()
		//create columns for each charter
		for (c in cl.charterList) {
			reportRow[c.getId().toString()] = 0
		}
		
		def actualsList = manager.getActualsByAircraftByMonth(a.getId(), Integer.valueOf(month), Integer.valueOf(year))
		for (actuals in actualsList) {
			def total = reportRow[actuals.getCharter().getId().toString()]
			
			def val = actuals.getCapt()
			
			reportRow[actuals.getCharter().getId().toString()] = total+val
		}
		report.add(reportRow);
		
	}
	
	def tableBuilder = Table.create("aircraftCharterMatrix", pageContext["VIEW"])
	.of(report)
	.captioned("Aircraft Charter hours - " + dateFormat.format(datePeriod))
	.width(600)
	.withColumns()
		.column("aircraft")
	for (charter in cl.charterList) {
		tableBuilder = tableBuilder.column(charter.getId().toString()).called(charter.getCode()).asNumber(1).withStyle("text-align:right;width:80px;")
	}

	return tableBuilder.render()
}
