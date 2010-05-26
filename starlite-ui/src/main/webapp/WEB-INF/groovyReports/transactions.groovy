import com.itao.starlite.model.Component;
import com.itao.starlite.model.Component.ComponentHistory;
import com.itao.jmesa.dsl.entities.Table;

def generate(manager, pageContext) {
	def components = manager.getComponents()
	pageContext["title"] = "Component Transactions"
	
	def month = pageContext["request"].getParameter("month")
	if (month.length() == 1)
		month = "0"+month
	def year = pageContext["request"].getParameter("year")
	def period = year + month
	def dateFrom = new org.joda.time.DateMidnight(Integer.valueOf(year), Integer.valueOf(month), 1).toDate()
    def dateTo = new org.joda.time.DateMidnight(Integer.valueOf(year), (Integer.valueOf(month)+1) %12, 1).toDate()
    
    def dateFormat = new java.text.SimpleDateFormat("MMM yy")
    
	def report = []
	
	for (Component c in components) {
	    System.out.println(period);
	    System.out.println(c);
		for (ComponentHistory hist in c.getHistory()) {			
			def reportRow = [:];
			if(hist.type == "transaction"){
			    if((hist.getDate() > dateFrom)&&(hist.getDate() < dateTo)){
    			reportRow["id"] = hist.getId()
                reportRow["date"] = hist.getDate()
                reportRow["time"] = hist.getTime()
                reportRow["component"] = c.getNumber()
                reportRow["serial"] = c.getSerial()              
                reportRow["action"] = hist.getField()
                reportRow["location"] = hist.getLocation()
                //if(hist.getFromVal() != null){
                reportRow["previous"] = hist.getFromVal()
                //}
                //else {
                //    reportRow["previous"] = new Integer(0)
                //}
                //if(hist.getToVal() != null){
                reportRow["current"] = hist.getToVal()
                //}
                //else {
                //    reportRow["current"] = new Integer(0)
                //}
                
                reportRow["user"] = hist.getUser()
                reportRow["description"] = hist.getDescription()
    			report.add(reportRow)
    			}
    			else{
    			 break;
    			}
			}
		}
	}
	

	
    def tableBuilder =  Table.create("deductions", pageContext["VIEW"])
		.of(report)
		.captioned("Crew Deductions " + dateFormat.format(dateFrom))
		.withColumns()
			.column("id").sort(0).reversed()
			.column("date")
			.column("time")
			.column("component")
			.column("serial")
			.column("action")
			.column("location")
			.column("previous")
			.column("current")
			.column("user")
			.column("description")
			
			
	return tableBuilder.render();
        
}   