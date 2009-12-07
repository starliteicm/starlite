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
	pageContext["title"] = "Crew Member Profile"
	
	return Table.create("Profile", pageContext["VIEW"])
		.of(crewProfile)
		.captioned("Member Profile")
		.withColumns()
			.column("personal.firstName").called("First Name")
			.column("personal.lastName").called("Last Name")
			.column("personal.gender").called("Gender")
			.column("personal.dateOfBirth").called("DOB")
			.column("personal.nationality").called("Nationality")			
		.render();
}