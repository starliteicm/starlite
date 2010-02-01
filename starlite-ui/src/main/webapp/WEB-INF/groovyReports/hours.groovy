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
	
	def report = []
	pageContext["title"] = "Hours"
	
	for (cm in crewProfile) {
	  def reportRow = [:];
	  if(cm.personal.lastName != ""){
	  reportRow["firstName"] = cm.personal.firstName
	  reportRow["lastName"]  = cm.personal.lastName
      report.add(reportRow)
      }
	}
	
	if(pageContext["VIEW"] == Table.Type.EXCEL){
	
	return Table.create("Hours", pageContext["VIEW"])
        .of(report)
        .captioned("Hours")
        .withColumns()
            .column("firstName").called("First Name").sort(1)
            .column("lastName").called("Last Name").sort(0)       
        .render();
	
	}
	
	return Table.create("Hours", pageContext["VIEW"])
		.of(report)
		.captioned("Hours")
		.withColumns()
			.column("firstName").called("First Name").sort(1)
			.column("lastName").called("Last Name").sort(0)
		.render();
}