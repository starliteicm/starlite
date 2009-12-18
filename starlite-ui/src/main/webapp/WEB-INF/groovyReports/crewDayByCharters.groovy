import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.text.SimpleDateFormat;

import com.itao.starlite.model.Aircraft;
import com.itao.starlite.model.CrewDay;
import com.itao.starlite.model.CrewMember;

import com.itao.jmesa.dsl.entities.Table;

def contract(manager, crew, allAircraft, charter, dateFrom, dateTo) {
	
	TreeMap<String,CrewMember> ordered = new TreeMap<String,CrewMember>();
	for(CrewMember cm : crew){
		if(cm.getCode() != null){
			ordered.put(cm.getCode(), cm);
		}
	}
	allCrew = new ArrayList<CrewMember>(ordered.values());
	
	crewDays = manager.getCrewDayByCharterBetween(charter.getId(), dateFrom, dateTo); 
	
	crewDayAircraft =  new TreeMap<String,Map>();
	
	for(CrewDay cd : crewDays){
		Aircraft a = cd.getAircraft();
		CrewMember c = cd.getCrewMember();
		
		if((a != null)&&(c != null)){
			if(!crewDayAircraft.containsKey(""+a.getRef())){
				//add aircraft
				HashMap aircraft = new HashMap();
				aircraft.put("aircraft", a);
				aircraft.put("crewMap", new TreeMap());
				crewDayAircraft.put(""+a.getRef(), aircraft);
			}
			Map crewMap =  (Map) crewDayAircraft.get(""+a.getRef()).get("crewMap");
			if(!crewMap.containsKey(""+c.getCode())){
				//crew member already added
				Map crewMember = new HashMap();
				crewMember.put("crewMember", c);
				crewMember.put("crewDayMap", new HashMap());
				crewMap.put(""+c.getCode(), crewMember);
			}
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			Map crewDayMap = (Map) ((Map) crewMap.get(""+c.getCode())).get("crewDayMap");
			Calendar cal = Calendar.getInstance();
			cal.setTime(cd.getDate());
			cal.add(Calendar.DAY_OF_MONTH, -1);
			if(crewDayMap.containsKey(df.format(cal.getTime()))){
				Map crewDay = (Map) crewDayMap.get(df.format(cal.getTime()));
				crewDay.put("end",cd.getDate());
				crewDayMap.remove(df.format(cal.getTime()));
				crewDayMap.put(df.format(cd.getDate()),crewDay);
			}
			else{
				Map crewDay = (Map) new HashMap();
				crewDay.put("start",cd.getDate());
				crewDay.put("end",cd.getDate());
				crewDayMap.put(df.format(cd.getDate()),crewDay);
			}
		}	
	}
	return crewDayAircraft
}

def generate(manager, pageContext) {
	def charters = manager.getAllCharters().charterList
	pageContext["title"] = "Crew Charter Days"
	
	def month = pageContext["request"].getParameter("month")
	if (month.length() == 1)
		month = "0"+month
	def year = pageContext["request"].getParameter("year")
	def period = year + month
	
	def report = []
	
	def crew = manager.getAllCrew();
	
	def allAircraft = manager.getAllAircraft().aircraftList;
	
	def dateFrom = new org.joda.time.DateMidnight(new Integer(year), new Integer(month), 1).toDate()
    def dateTo = new org.joda.time.DateMidnight(new Integer(year),new Integer(month), 30).toDate()
	
	for (charter in charters) {
		crewDayAircraft = contract(manager, crew, allAircraft, charter, dateFrom, dateTo)
		def reportRow = [:];
		println charter.id+", "+crewDayAircraft
		reportRow["lastName"] = charter.id
		reportRow["firstName"] = crewDayAircraft
//		reportRow["date"] = a.date
//		reportRow["type"] = d.key
//		reportRow["amount"] = d.value.USD
		report.add(reportRow)
		
	}
	
	def datePeriod = new org.joda.time.DateMidnight(Integer.valueOf(year), Integer.valueOf(month), 1).toDate()
	def dateFormat = new java.text.SimpleDateFormat("MMM yy")
	
    def tableBuilder =  Table.create("deductions", pageContext["VIEW"])
		.of(report)
		.captioned("Crew Charter Time " + dateFormat.format(datePeriod))
		.withColumns()
			.column("lastName").sort(0)
			.column("firstName").sort(1)
//			.column("date")
//			.column("type")
//			.column("amount").withStyle("text-align:right;width:80px;")
			
			
	return tableBuilder.render();
        
}   