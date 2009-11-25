import com.itao.starlite.ui.jmesa.ExpirableDateEditor;
import com.itao.starlite.model.CrewMember;
import com.itao.starlite.model.Money;

import com.itao.jmesa.dsl.entities.Table;

def generate(manager, pageContext) {
	def crew = manager.getAllCrew()
	pageContext["title"] = "Crew Payment Analysis"
	
	def month = pageContext["request"].getParameter("month")
	if (month.length() == 1)
		month = "0"+month
	def year = pageContext["request"].getParameter("year")
	def period = year + month
	println period
	
	def cl = manager.getAllCharters()
	def al = manager.getAllAircraft()
	
	def report = []
	def dueTotal = 0
	def added = 0
	
	def charterTotals = [:]
	//	 create total collectors for each charter
	for (c in cl.charterList) {
		charterTotals[c.getId().toString()] = 0 
	}
	
	for (CrewMember cm in crew) {
    	
    	
        
        CrewMember.FlightAndDutyActuals actuals = cm.getFlightAndDutyActualsForMonth(period);
        
        //do for aircraft
		for (a in al.aircraftList) {
		    def airReportRow = [:];
		    airReportRow["lastName"] = cm.getPersonal().getLastName();
            airReportRow["firstName"] = cm.getPersonal().getFirstName();
			def airDueTotal = 0
			def airAdd = 0
			def airTotal = actuals.getTotal()
            
        	airReportRow["aircraft"] = a.getRef();
        
			for (c in cl.charterList) {
			    
			    def airCharterEntry = actuals.getEntries().get( c.getCode() +"_" + a.getRef() );			
			    if ( airCharterEntry == null ) 
				    charterValue = 0;
			    else {
				    def chargeDays = actuals.getAreaDays() + actuals.getDailyDays();
				    if ( chargeDays == 0 ) chargeDays = 1;
				    def charterApportion = (airCharterEntry.getAreaDays() + airCharterEntry.getDailyDays()) /  new Double(chargeDays) ;
				    charterValue = actuals.getMonthlyRate().getAmountAsDouble() * charterApportion;
				    charterValue += actuals.getDailyRate().getAmountAsDouble() * (airCharterEntry.getDailyDays());
				    charterValue += actuals.getFlightRate().getAmountAsDouble() * (airCharterEntry.getFlightDays());
				    charterValue += actuals.getAreaRate().getAmountAsDouble() * (airCharterEntry.getAreaDays());
				    charterValue += actuals.getInstructorRate().getAmountAsDouble() * (airCharterEntry.getInstructorDays());
				    //charterValue = ctotal.getAmountAsDouble()
				    if(charterValue > 0.0){
				       airAdd = 1;
				       airDueTotal += charterValue;
				       dueTotal += charterValue;
				    }
				    
			    }
			
			    airReportRow[c.getId().toString()] = charterValue;
			
		        def airCharterTotal = charterTotals.get( c.getId().toString() );
		        airCharterTotal += charterValue;
		        charterTotals[c.getId().toString()] = airCharterTotal;
		    }
		    //airDueTotal += airTotal.getAmount()		    
		    if(airDueTotal > 0.0){
		    	if(airAdd == 1){
		    	    airReportRow["total"] = new Money(" "+airDueTotal)
            		report.add(airReportRow)
            		added = 1;
            	}
            }
		}
        
        def reportRow = [:];
        reportRow["lastName"] = cm.getPersonal().getLastName();
        reportRow["firstName"] = cm.getPersonal().getFirstName();
        def total = actuals.getTotal()
        reportRow["total"] = total
        def nonDueTotal = 0.0
        
        // create columns for each charter
		for (c in cl.charterList) {
		
		    reportRow["aircraft"] = "";
		    //do for no aircraft
			def charterEntry = actuals.getEntries().get( c.getCode() );			
			    if ( charterEntry == null ) 
				    charterValue = 0;
			    else {
				    def chargeDays = actuals.getAreaDays() + actuals.getDailyDays();
				    if ( chargeDays == 0 ) chargeDays = 1;
				    def charterApportion = (charterEntry.getAreaDays() + charterEntry.getDailyDays()) /  new Double(chargeDays) ;
				    charterValue = actuals.getMonthlyRate().getAmountAsDouble() * charterApportion;
				    charterValue += actuals.getDailyRate().getAmountAsDouble() * (charterEntry.getDailyDays());
				    charterValue += actuals.getFlightRate().getAmountAsDouble() * (charterEntry.getFlightDays());
				    charterValue += actuals.getAreaRate().getAmountAsDouble() * (charterEntry.getAreaDays());
				    charterValue += actuals.getInstructorRate().getAmountAsDouble() * (charterEntry.getInstructorDays());
				    //charterValue = ctotal.getAmountAsDouble()
				    if(charterValue > 0.0){
				       nonDueTotal += charterValue
				       dueTotal += charterValue
				    }
			    }
			
			    reportRow[c.getId().toString()] = charterValue;
			
		        def charterTotal = charterTotals.get( c.getId().toString() );
		        charterTotal += charterValue;
		        charterTotals[c.getId().toString()] = charterTotal;
		    
		}
		
		if(nonDueTotal > 0.0){
		    reportRow["total"] = new Money(" "+nonDueTotal);
        	report.add(reportRow)
        	added = 1;
        }
        
	}
	
	def totalRow =[:];
    totalRow["lastName"] = "zz Total"
    totalRow["firstName"] = ""
    totalRow["total"]  = new Money(" "+ dueTotal)
    
    //  create totals for each charter
	for (c in cl.charterList) {
		def charterTotal = charterTotals.get( c.getId().toString() );
		if ( charterTotal==null ) charterTotal = 0;
		totalRow[c.getId().toString()] = charterTotal 
	}
    report.add(totalRow)
	
	def datePeriod = new org.joda.time.DateMidnight(Integer.valueOf(year), Integer.valueOf(month), 1).toDate()
	def dateFormat = new java.text.SimpleDateFormat("MMM yy")
	
    def tableBuilder =  Table.create("payments", pageContext["VIEW"])
		.of(report)
		.captioned("Crew Payments " + dateFormat.format(datePeriod))
		.withColumns()
			.column("lastName").sort(0)
			.column("firstName").sort(1)
			.column("total").called("Total Due").withStyle("text-align:right;width:80px;")
			.column("aircraft").called("Aircraft Reg")
			for (charter in cl.charterList) {
				tableBuilder = tableBuilder.column(charter.getId().toString()).called(charter.getCode()).asNumber(2).withStyle("text-align:right;width:80px;")
			}
			
	return tableBuilder.render();
        
}   