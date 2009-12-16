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

import com.itao.starlite.model.CrewMember;
import com.itao.starlite.auth.User;
import com.itao.starlite.auth.manager.AuthManager;
import com.itao.starlite.docs.manager.DocumentManager;

def user = pageContext["request"].getAttribute("user")
def docManager = pageContext["request"].getAttribute("docManager") 
def docFilename = pageContext["request"].getAttribute("user")

def generate(manager, pageContext) {
	def crew    = manager.getAllCrew()
	
	for (CrewMember cm in crew) {
    	def reportRow = [:];
        reportRow["crm"] = cm.getCrm().getExpiryDate();
        reportRow["dg"]  = cm.getDg().getExpiryDate();
        reportRow["huet"]= cm.getHuet().getExpiryDate();        	
	    reportRow["r1"]  = cm.getR1().getExpiryDate();
        reportRow["passport"] = cm.getPassportNumber().getExpiryDate();        
    }
    
    
        
    def doctype = pageContext["request"].getParameter("documenttype")
    def passportstyle = ""
    def licencestyle  = ""
    def medicalstyle  = ""
    def crmstyle      = ""
    def dgstyle       = ""
    def huetstyle     = ""
    
    if(doctype=="passport"){
       passportstyle="background:yellow"
    }
    if(doctype=="licence"){
       licencestyle="background:yellow"
    }
    if(doctype=="medical"){
       medicalstyle="background:yellow"
    }
    if(doctype=="crm"){
       crmstyle="background:yellow"
    }
    if(doctype=="dg"){
       dgstyle="background:yellow"
    }
    if(doctype=="huet"){
       huetstyle="background:yellow"
    }

	pageContext["title"] = "Crew Document Analysis"	
	return Table.create("Crew Document Analysis", pageContext["VIEW"])
		.of(crew)
		.captioned("Crew Document Analysis")
		.withColumns()
			.column("code").link('crewMember!assignments.action?id=${code}&fromPage='+pageContext["thisUrl"])
			.column("personal.lastName").called("Last Name")
			.column("personal.firstName").called("First Name")
			.column("personal.passportExpiryDate").called("Passport").as("com.itao.starlite.ui.jmesa.ExpirableDateEditor").withStyle(passportstyle)							
			.column("role.r1.expiryDate").called("Licence").as("com.itao.starlite.ui.jmesa.ExpirableDateEditor").withStyle(licencestyle)
			.column("role.expiryDate").called("Medical").as("com.itao.starlite.ui.jmesa.ExpirableDateEditor").withStyle(medicalstyle)									
			.column("role.crm.expiryDate").called("CRM").as("com.itao.starlite.ui.jmesa.ExpirableDateEditor").withStyle(crmstyle).link('documents/crew/${code}/${role.crm.bookmark.url}')
			.column("role.dg.expiryDate").called("DG").as("com.itao.starlite.ui.jmesa.ExpirableDateEditor").withStyle(dgstyle)
			.column("role.huet.expiryDate").called("Huet").as("com.itao.starlite.ui.jmesa.ExpirableDateEditor").withStyle(huetstyle)
		.render();
}