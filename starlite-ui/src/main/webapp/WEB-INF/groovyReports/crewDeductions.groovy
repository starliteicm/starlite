import com.itao.starlite.model.CrewMember;
import com.itao.jmesa.dsl.entities.Table;

def generate(manager, pageContext) {
	def crew = manager.getAllCrew()
	pageContext["title"] = "Crew Salary Deductions"
	
	def month = pageContext["request"].getParameter("month")
	if (month.length() == 1)
		month = "0"+month
	def year = pageContext["request"].getParameter("year")
	def period = year + month
	
	def report = []
	
	for (CrewMember cm in crew) {
        CrewMember.FlightAndDutyActuals actuals = cm.getFlightAndDutyActualsForMonth(period);
		for (a in actuals) {
			for (d in a.deductions) {
				def reportRow = [:];
				reportRow["lastName"] = cm.personal.lastName
				reportRow["firstName"] = cm.personal.firstName
				reportRow["date"] = a.date
				reportRow["type"] = d.key
				reportRow["amount"] = d.value.USD
				report.add(reportRow)
			}
		}
	}
	
	def datePeriod = new org.joda.time.DateMidnight(Integer.valueOf(year), Integer.valueOf(month), 1).toDate()
	def dateFormat = new java.text.SimpleDateFormat("MMM yy")
	
    def tableBuilder =  Table.create("deductions", pageContext["VIEW"])
		.of(report)
		.captioned("Crew Deductions " + dateFormat.format(datePeriod))
		.withColumns()
			.column("lastName").sort(0)
			.column("firstName").sort(1)
			.column("date")
			.column("type")
			.column("amount").withStyle("text-align:right;width:80px;")
			
			
	return tableBuilder.render();
        
}   