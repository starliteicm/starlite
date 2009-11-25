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
	def crew = manager.getAllCrew()
	pageContext["title"] = "Licences Report"
	
	return Table.create("expiry", pageContext["VIEW"])
		.of(crew)
		.captioned("Expiry Report")
		.withColumns()
			.column("code").link('crewMember!assignments.action?id=${code}&fromPage='+pageContext["thisUrl"])
			.column("personal.lastName").called("Last Name")
			.column("personal.firstName").called("First Name")
			.column("personal.passportExpiryDate").called("Passport").asDate()
			.column("role.expiryDate").called("Medical").asDate()
			.column("role.r1.expiryDate").called("r1").asDate()
			.column("role.r2.expiryDate").called("r2").asDate()
			.column("role.crm.expiryDate").called("crm").asDate()
			.column("role.dg.expiryDate").called("dg").asDate()
			.column("role.ifr.expiryDate").called("ifr").asDate()
		.render();
}
