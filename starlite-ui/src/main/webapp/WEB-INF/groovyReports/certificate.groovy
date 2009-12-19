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

	if(pageContext["VIEW"] == Table.Type.HTML){	
	return Table.create("expiry", pageContext["VIEW"]) 
		.of(crew)
		.captioned("Expiry Report")
		.withColumns()
			.column("code").link('crewMember!assignments.action?id=${code}&fromPage='+pageContext["thisUrl"])
			.column("personal.lastName").called("Last Name")
			.column("personal.firstName").called("First Name")
			.column("role.crm.number").called("crm")
			.column("role.crm.expiryDate").called("crm exp").asDate()
			.column("role.dg.number").called("dg")
			.column("role.dg.expiryDate").called("dg exp").asDate()
			.column("role.ifr.number").called("ifr")
			.column("role.ifr.expiryDate").called("ifr exp").asDate()
		.render();
	}
	else{
	return Table.create("expiry", pageContext["VIEW"]) 
        .of(crew)
        .captioned("Expiry Report")
        .withColumns()
            .column("code")
            .column("personal.lastName").called("Last Name")
            .column("personal.firstName").called("First Name")
            .column("role.crm.number").called("crm")
            .column("role.crm.expiryDate").called("crm exp").asDate()
            .column("role.dg.number").called("dg")
            .column("role.dg.expiryDate").called("dg exp").asDate()
            .column("role.ifr.number").called("ifr")
            .column("role.ifr.expiryDate").called("ifr exp").asDate()
        .render();	
	}
}
