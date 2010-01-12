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
	def crewProfile = manager.getAllCrew()
	//get 183 days info
	
	def dateFrom = pageContext["dateFrom"]
    def dateTo = pageContext["dateTo"]
	
    def sumCrewDays = manager.getSumCrewDays(dateFrom,dateTo);
	
	
	
	def report = []
	pageContext["title"] = "183 Days"
	
	for (cm in crewProfile) {
	  def reportRow = [:];
	  if(cm.personal.lastName != ""){
	  reportRow["firstName"] = cm.personal.firstName
	  reportRow["lastName"]  = cm.personal.lastName
	  count = sumCrewDays.get(cm.personal.fullName)
	  if(count == null){count = 0}
	  reportRow["count"]     = count
      report.add(reportRow)
      }
	}
	
	if(pageContext["VIEW"] == Table.Type.EXCEL){
	
	return Table.create("183 Days", pageContext["VIEW"])
        .of(report)
        .captioned("183 Days")
        .withColumns()
            .column("firstName").called("First Name").sort(1)
            .column("lastName").called("Last Name").sort(0)
            .column("count").called("Working Days")         
        .render();
	
	}
	
	return Table.create("183 Days", pageContext["VIEW"])
		.of(report)
		.captioned("183 Days ("+dateFrom.replace("/","-")+" - "+dateTo.replace("/","-")+")")
		.withColumns()
			.column("firstName").called("First Name").sort(1)
			.column("lastName").called("Last Name").sort(0)
			.column("count").called("Working Days")			
		.render();
}