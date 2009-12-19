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
	
	def crew = manager.getAllCrewReadOnly()
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
		reportRow["passport"] = folder.getDocumentByTag("passport0") ? "documents/crew/"+cm.code+"/"+folder.getDocumentByTag("passport0").name : "";
		if(!"".equals(reportRow["passport"])){reportRow["ptick"] = "Y";}
		else{reportRow["ptick"] = "N";}
		reportRow["licence"]  = folder.getDocumentByTag("licence") ? "documents/crew/"+cm.code+"/"+folder.getDocumentByTag("licence").name : "";
		if(!"".equals(reportRow["licence"])){reportRow["ltick"] = "Y";}
        else{reportRow["ltick"] = "N";}
		reportRow["medical"]  = folder.getDocumentByTag("medical") ? "documents/crew/"+cm.code+"/"+folder.getDocumentByTag("medical").name : "" ;
		if(!"".equals(reportRow["medical"])){reportRow["mtick"] = "Y";}
        else{reportRow["mtick"] = "N";}
		reportRow["crm"]      = folder.getDocumentByTag("CRM") ? "documents/crew/"+cm.code+"/"+folder.getDocumentByTag("CRM").name : "";
		if(!"".equals(reportRow["crm"])){reportRow["ctick"] = "Y";}
        else{reportRow["ctick"] = "N";}
		reportRow["dg"]       = folder.getDocumentByTag("DG") ? "documents/crew/"+cm.code+"/"+folder.getDocumentByTag("DG").name : "";
		if(!"".equals(reportRow["dg"])){reportRow["dtick"] = "Y";}
        else{reportRow["dtick"] = "N";}
		reportRow["huet"]     = folder.getDocumentByTag("HUET") ? "documents/crew/"+cm.code+"/"+folder.getDocumentByTag("HUET").name : "";
		if(!"".equals(reportRow["medical"])){reportRow["htick"] = "Y";}
        else{reportRow["htick"] = "N";}
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
	
	if(pageContext["VIEW"] == Table.Type.HTML){
	return Table.create("Crew Document Analysis", pageContext["VIEW"])
		.of(report)
		.captioned("Crew Document Analysis")
		.withColumns()
			.column("code").link('crewMember!assignments.action?id=${code}&fromPage='+pageContext["thisUrl"])
			.column("personal.lastName").called("Last Name")
			.column("personal.firstName").called("First Name")
			.column("personal.passportExpiryDate").called("Passport Exp").as("com.itao.starlite.ui.jmesa.ExpirableDateEditor").withStyle(passportstyle)				
			.column("passport").called("Doc").as("com.itao.starlite.ui.jmesa.DocumentCellEditor").withStyle(passportstyle)
			.column("role.r1.expiryDate").called("Licence Exp").as("com.itao.starlite.ui.jmesa.ExpirableDateEditor").withStyle(licencestyle)
			.column("licence").called("Doc").as("com.itao.starlite.ui.jmesa.DocumentCellEditor").withStyle(licencestyle)
			.column("role.expiryDate").called("Medical Exp").as("com.itao.starlite.ui.jmesa.ExpirableDateEditor").withStyle(medicalstyle)									
			.column("medical").called("Doc").as("com.itao.starlite.ui.jmesa.DocumentCellEditor").withStyle(medicalstyle)
			.column("role.crm.expiryDate").called("CRM Exp").as("com.itao.starlite.ui.jmesa.ExpirableDateEditor").withStyle(crmstyle)
			.column("crm").called("Doc").as("com.itao.starlite.ui.jmesa.DocumentCellEditor").withStyle(crmstyle)
			.column("role.dg.expiryDate").called("DG Exp").as("com.itao.starlite.ui.jmesa.ExpirableDateEditor").withStyle(dgstyle)
			.column("dg").called("Doc").as("com.itao.starlite.ui.jmesa.DocumentCellEditor").withStyle(dgstyle)
			.column("role.huet.expiryDate").called("Huet Exp").as("com.itao.starlite.ui.jmesa.ExpirableDateEditor").withStyle(huetstyle)
			.column("huet").called("Doc").as("com.itao.starlite.ui.jmesa.DocumentCellEditor").withStyle(huetstyle)
		.render();
	}
	else{
	return Table.create("Crew Document Analysis", pageContext["VIEW"])
        .of(report)
        .captioned("Crew Document Analysis")
        .withColumns()
            .column("code")
            .column("personal.lastName").called("Last Name")
            .column("personal.firstName").called("First Name")
            .column("personal.passportExpiryDate").called("Passport Exp")             
            .column("ptick").called("Doc")
            .column("role.r1.expiryDate").called("Licence Exp")
            .column("ltick").called("Doc")
            .column("role.expiryDate").called("Medical Exp")                               
            .column("mtick").called("Doc")
            .column("role.crm.expiryDate").called("CRM Exp")
            .column("ctick").called("Doc")
            .column("role.dg.expiryDate").called("DG Exp")
            .column("dtick").called("Doc")
            .column("role.huet.expiryDate").called("Huet Exp")
            .column("htick").called("Doc")
        .render();
	}
}