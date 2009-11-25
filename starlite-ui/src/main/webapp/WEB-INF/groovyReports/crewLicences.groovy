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
	
	return Table.create("licences", pageContext["VIEW"])
		.of(crew)
		.captioned("Crew Licences")
		.withColumns()
			.column("personal.firstName").called("First Name")
			.column("personal.lastName").called("Last Name")
			.column("role.r1.number").called("r1")
			.column("role.r1.expiryDate").called("Expiry").asDate()
			.column("role.r2.number").called("r2")
			.column("role.r2.expiryDate").called("Expiry").asDate()
		.render();
}
