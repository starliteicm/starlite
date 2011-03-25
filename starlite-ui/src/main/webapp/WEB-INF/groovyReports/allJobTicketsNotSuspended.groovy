import org.jmesa.limit.Limit;
import org.jmesa.facade.TableFacade;
import org.jmesa.facade.TableFacadeImpl;
import org.jmesa.view.component.Column;
import org.jmesa.view.component.Table;
import org.jmesa.view.editor.BasicCellEditor;
import org.jmesa.view.editor.CellEditor;
import org.jmesa.view.html.HtmlBuilder;
import org.jmesa.view.html.component.HtmlTable;

import com.itao.starlite.ui.jmesa.ExpirableDateEditor;

import com.itao.jmesa.dsl.entities.Table;

def generate(manager, pageContext) {
	
	pageContext["title"] = "All Tickets Report"
	
	def month = pageContext["request"].getParameter("month")
	if (month.length() == 1)
		month = "0"+month
	def year = pageContext["request"].getParameter("year")
	def period = year + month
	println period
	
	def tickets = manager.getAllJobTicketsNotSuspended()
	
	def report = []
    pageContext["title"] = "Reporting for Hanger Management"
    
    for (cm in tickets) 
    {
      def reportRow = [:];
      if(cm.jobTicketID != null)
      {
      def fullname = cm.assignedTo.getPersonal().getFullName() 
      def link = "<a href='hangerHistory.action?id="+cm.jobTicketID+"'>View:&nbsp;"+cm.jobTicketID+"</a>"
      
      reportRow["Job Ticket ID"]    = link
      reportRow["aircraft"]         = cm.aircraft.ref
      reportRow["Job"]     			= cm.jobTask.jobTaskValue
      reportRow["assignedTo"]       = cm.assignedTo.code
       reportRow["fullname"]        = fullname
      reportRow["Task"]    			= cm.jobSubTask2.JobSubTaskCode
      reportRow["TaskDesc"]    			= cm.jobSubTask2.JobSubTaskDesc
      reportRow["Ticket Status"]	= cm.jobTicketStatus.jobstatus_value;
      reportRow["Total Ticket Hrs"] = cm.totalTicketHours;
      report.add(reportRow)
      }
    }
    
    def caption = "All tickets not closed the day before"
    
	
	return Table.create("tickets", pageContext["VIEW"])
		.of(report)
		.captioned(caption)
		.withColumns()
			.column("Job Ticket ID").called("Job Ticket ID")
			.column("aircraft").called("Tail #")
			.column("Job").called("Job")
			.column("Task").called("Task")
			.column("TaskDesc").called("Task Description")
			.column("assignedTo").called("Assigned To")
			.column("fullname").called("Name")
			.column("Ticket Status").called("Ticket Status")
			.column("Total Ticket Hrs").called("Total Ticket Hrs").withStyle("text-align:right;width:80px;")
		.render();
}
