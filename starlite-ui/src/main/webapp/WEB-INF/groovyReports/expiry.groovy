import org.jmesa.limit.Limit;
import org.jmesa.facade.TableFacade;
import org.jmesa.facade.TableFacadeImpl;
import org.jmesa.view.component.Column;
import org.jmesa.view.component.Table;
import org.jmesa.view.editor.BasicCellEditor;
import org.jmesa.view.editor.CellEditor;
import org.jmesa.view.html.HtmlBuilder;
import org.jmesa.view.html.component.HtmlTable;
import org.jmesa.view.html.renderer.HtmlCellRenderer;
import com.itao.starlite.ui.jmesa.ExpirableDateEditor;

import com.itao.jmesa.dsl.entities.Table;

def generate(manager, pageContext) {
	def crew = manager.getAllCrew()
		
	pageContext["title"] = "Crew Document Analysis"	
	return Table.create("Crew Document Analysis", pageContext["VIEW"])
		.of(crew)
		.captioned("Crew Document Analysis")
		.withColumns()
			.column("code").link('crewMember!assignments.action?id=${code}&fromPage='+pageContext["thisUrl"])
			.column("personal.lastName").called("Last Name")
			.column("personal.firstName").called("First Name")			
			.column("personal.passportExpiryDate").called("Passport").asDate()		
			.column("role.expiryDateWithColor").called("Medical")									
			.column("role.crmExpiryDateWithColor").called("crm")
			.column("role.dgExpiryDateWithColor").called("dg")
			.column("role.ifrExpiryDateWithColor").called("ifr")
		.render();
}
