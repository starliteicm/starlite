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



def generate(manager, pageContext) {
	def user = pageContext["request"].getAttribute("user")
	def docManager = pageContext["request"].getAttribute("docManager") 
	
	def crew = manager.getAllCrew()
    def report = []
	for (CrewMember cm in crew) {
    	def reportRow = [:];
		
		reportRow["code"] = cm.code
		reportRow["personal.lastName"] = cm.personal.lastName
		reportRow["personal.firstName"] = cm.personal.firstName
		reportRow["personal.passportExpiryDate"] = cm.personal.passportExpiryDate							
		reportRow["role.r1.expiryDate"] = cm.role.r1.expiryDate
		reportRow["role.expiryDate"] = cm.role.expiryDate									
		reportRow["role.crm.expiryDate"] = cm.role.crm.expiryDate
		reportRow["role.dg.expiryDate"] = cm.role.dg.expiryDate
		reportRow["role.huet.expiryDate"] = cm.role.huet.expiryDate
		
		try{
		code = cm.code;		
		path = "/crew/"+code;
		System.out.println("path is :"+path)
		folder = docManager.getFolderByPath(path, user);
		reportRow["passport"] = folder.getDocumentByTag("passport0") ? "documents/crew/"+cm.code+"/"+folder.getDocumentByTag("passport0").name : "#";
		reportRow["licence"]  = folder.getDocumentByTag("licence") ? "documents/crew/"+cm.code+"/"+folder.getDocumentByTag("licence").name : "#";
		reportRow["medical"]  = folder.getDocumentByTag("medical") ? "documents/crew/"+cm.code+"/"+folder.getDocumentByTag("medical").name : "#" ;
		reportRow["crm"]      = folder.getDocumentByTag("CRM") ? "documents/crew/"+cm.code+"/"+folder.getDocumentByTag("CRM").name : "#";
		reportRow["dg"]       = folder.getDocumentByTag("DG") ? "documents/crew/"+cm.code+"/"+folder.getDocumentByTag("DG").name : "#";
		reportRow["huet"]     = folder.getDocumentByTag("HUET") ? "documents/crew/"+cm.code+"/"+folder.getDocumentByTag("HUET").name : "#";
		}
		catch(Exception e){//e.printStackTrace();
		}
		report.add(reportRow)
    }
    
    def doctype = pageContext["request"].getParameter("documenttype")
    def passportstyle = ""
    def licencestyle  = ""
    def medicalstyle  = ""
    def crmstyle      = ""
    def dgstyle       = ""
    def huetstyle     = ""
    
    if(doctype=="passport"){
       passportstyle="background:#d0f0c0;"
    }
	else if(doctype=="licence"){
       licencestyle="background:#d0f0c0"
    }
	else if(doctype=="medical"){
       medicalstyle="background:#d0f0c0"
    }
	else if(doctype=="crm"){
       crmstyle="background:#d0f0c0"
    }
	else if(doctype=="dg"){
       dgstyle="background:#d0f0c0"
    }
	else if(doctype=="huet"){
       huetstyle="background:#d0f0c0"
    }

	pageContext["title"] = "Crew Document Analysis"	
	return Table.create("Crew Document Analysis", pageContext["VIEW"])
		.of(report)
		.captioned("Crew Document Analysis")
		.withColumns()
			.column("code").link('crewMember!assignments.action?id=${code}&fromPage='+pageContext["thisUrl"])
			.column("personal.lastName").called("Last Name")
			.column("personal.firstName").called("First Name")
			.column("personal.passportExpiryDate").called("Passport").as("com.itao.starlite.ui.jmesa.ExpirableDateEditor").withStyle(passportstyle).link('${passport}')						
			.column("role.r1.expiryDate").called("Licence").as("com.itao.starlite.ui.jmesa.ExpirableDateEditor").withStyle(licencestyle).link('${licence}')
			.column("role.expiryDate").called("Medical").as("com.itao.starlite.ui.jmesa.ExpirableDateEditor").withStyle(medicalstyle).link('${medical}')									
			.column("role.crm.expiryDate").called("CRM").as("com.itao.starlite.ui.jmesa.ExpirableDateEditor").withStyle(crmstyle).link('${crm}')
			.column("role.dg.expiryDate").called("DG").as("com.itao.starlite.ui.jmesa.ExpirableDateEditor").withStyle(dgstyle).link('${dg}')
			.column("role.huet.expiryDate").called("Huet").as("com.itao.starlite.ui.jmesa.ExpirableDateEditor").withStyle(huetstyle).link('${huet}')
		.render();
}