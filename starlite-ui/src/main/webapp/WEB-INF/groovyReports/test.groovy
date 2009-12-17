
import com.itao.jmesa.dsl.entities.Table;

def generate(manager, pageContext) {
	def crew = manager.getAllCrewReadOnly()
	pageContext["title"] = "Test Report"
	def user = pageContext["request"].getAttribute("user")
	def docManager = pageContext["request"].getAttribute("docManager") 

    tcm = []
	for (cm in crew) {
		def reportRow = [:];
		def code = cm.code
		println code
		folder = docManager.getFolderByPath("/crew/"+code, user);
		
		reportRow["code"] = code
		reportRow["personal.lastName"] = cm.personal.lastName
		reportRow["personal.firstName"] = cm.personal.firstName
		reportRow["personal.passportExpiryDate"] = cm.personal.passportExpiryDate							
		reportRow["role.r1.expiryDate"] = cm.role.r1.expiryDate
		reportRow["role.expiryDate"] = cm.role.expiryDate									
		reportRow["role.crm.expiryDate"] = cm.role.crm.expiryDate
		reportRow["role.dg.expiryDate"] = cm.role.dg.expiryDate
		reportRow["role.huet.expiryDate"] = cm.role.huet.expiryDate
		
//		tcm.add(code)
		
//		folder = docManager.getFolderByPath("/crew/"+code, user);
	}
	
//	tcm = ["Tri01","Ern01","Cha02","Xan01","Chr01","Fra01"]
//	
//	println "================="
//	for (cm in tcm) {
//		println cm
//		folder = docManager.getFolderByPath("/crew/"+cm, user);
//	}
	return "Done";
}
