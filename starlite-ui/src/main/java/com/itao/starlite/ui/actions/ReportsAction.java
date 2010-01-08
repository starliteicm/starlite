package com.itao.starlite.ui.actions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.joda.time.DateMidnight;

import com.google.inject.Inject;
import com.itao.starlite.auth.User;
import com.itao.starlite.auth.UserAware;
import com.itao.starlite.model.AircraftType;
import com.itao.starlite.model.Charter;
import com.itao.starlite.model.CharterList;
import com.itao.starlite.model.CrewDay;
import com.itao.starlite.model.CrewMember;
import com.opensymphony.xwork2.ActionSupport;

import com.itao.starlite.manager.StarliteCoreManager;


@SuppressWarnings("serial")
public class ReportsAction extends ActionSupport implements UserAware {
	public int month, year;

	public boolean notAuthorised = false;

	public String current="reports";

	private User user;
	
	public List<CrewMember> crewMembers;
	public List<CrewDay> crewDays;
	
	@SuppressWarnings("unchecked")
	public TreeMap charterMap;
	
	@SuppressWarnings("unchecked")
	public TreeMap sumCrewDays;
	
	@SuppressWarnings("unchecked")
	public Map enginetypes;
	
	@Inject
	private StarliteCoreManager manager;

	public CrewMember crewMember;
	public String id;	
	
	public String dateFrom;
	public String dateTo;
	public SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat dayformat = new SimpleDateFormat("dd");
	public SimpleDateFormat monthformat = new SimpleDateFormat("MMMM");
	public SimpleDateFormat yearformat = new SimpleDateFormat("yyyy");
	public SimpleDateFormat fullformat = new SimpleDateFormat("dd MMMM yyyy");
	public SimpleDateFormat mysqlFormat = new SimpleDateFormat("yyyy-MM-dd");
	@SuppressWarnings("unchecked")
	public LinkedHashMap<String,LinkedHashMap<String,TreeMap>> years;
	public int daycount;
	public String[] colors = new String[]{"6495ED","7B68EE","EEE8AA","90EE90","F4A460","CD5C5C","8FBC8F"};
	
	@Override
	public String execute() throws Exception {

		if (!user.hasPermission("ManagerEdit"))
			notAuthorised = true;

		DateMidnight dm = new DateMidnight();
		month = dm.getMonthOfYear();
		year = dm.getYear();
		crewMembers = manager.getAllCrew();
		/*for(CrewMember c : crewMembers){
			LOG.info(c.getPersonal().getFirstName());
		}*/
		return SUCCESS;
	}
	
	public HashMap<String, Double> processTypes(CrewDay cd,HashMap<String,Double> typeMap){
		if("d".equals(cd.getType())){
			//DAY_DUAL
			if("Dual".equals(cd.getPosition())){
				double flown = cd.getFlown();
				if(typeMap.containsKey("daydual")){
					flown = flown + typeMap.get("daydual");
				}
				typeMap.put("daydual",flown);
			}
			//DAY_CAPT
			else if("Capt".equals(cd.getPosition())){
				double flown = cd.getFlown();
				if(typeMap.containsKey("daycapt")){
					flown = flown + typeMap.get("daycapt");
				}
				typeMap.put("daycapt",flown);
			}
			//DAY_CO
			else if("Co".equals(cd.getPosition())){
				double flown = cd.getFlown();
				if(typeMap.containsKey("dayco")){
					flown = flown + typeMap.get("dayco");
				}
				typeMap.put("dayco",flown);
			}
			
			//TOTAL
			double flown = cd.getFlown();
			if(typeMap.containsKey("total")){
				flown = flown + typeMap.get("total");
			}
			typeMap.put("total",flown);
		}
		else if("n".equals(cd.getType())){
		    //NIGHT_DUAL
			if("Dual".equals(cd.getPosition())){
				double flown = cd.getFlown();
				if(typeMap.containsKey("nightdual")){
					flown = flown + typeMap.get("nightdual");
				}
				typeMap.put("nightdual",flown);
			}
			//NIGHT_CAPT
			else if("Capt".equals(cd.getPosition())){
				double flown = cd.getFlown();
				if(typeMap.containsKey("nightcapt")){
					flown = flown + typeMap.get("nightcapt");
				}
				typeMap.put("nightcapt",flown);
			}
			//NIGHT_CO
			else if("Co".equals(cd.getPosition())){
				double flown = cd.getFlown();
				if(typeMap.containsKey("nightco")){
					flown = flown + typeMap.get("nightco");
				}
				typeMap.put("nightco",flown);
			}
		
		    //TOTAL
			double flown = cd.getFlown();
			if(typeMap.containsKey("total")){
				flown = flown + typeMap.get("total");
			}
			typeMap.put("total",flown);
		}
		else if("i".equals(cd.getType())){
		    //INST_SIM
			if("sim".equals(cd.getInstruments())){
				double flown = cd.getFlown();
				if(typeMap.containsKey("sim")){
					flown = flown + typeMap.get("sim");
				}
				typeMap.put("sim",flown);
			}
			//INST_ACT
			else if("act".equals(cd.getInstruments())){
				double flown = cd.getFlown();
				if(typeMap.containsKey("act")){
					flown = flown + typeMap.get("act");
				}
				typeMap.put("act",flown);
			}
			
		    //INST_TOTAL
			double flown = cd.getFlown();
			if(typeMap.containsKey("inst_total")){
				flown = flown + typeMap.get("inst_total");
			}
			typeMap.put("inst_total",flown);
		}
		else if("t".equals(cd.getType())){
		    //INSTRUCT
			double flown = cd.getFlown();
			if(typeMap.containsKey("instruct")){
				flown = flown + typeMap.get("instruct");
			}
			typeMap.put("instruct",flown);
		}
		return typeMap;
	}
	
	@SuppressWarnings("unchecked")
	public String hours() throws Exception {
		
		LOG.info("Running Hours for "+id+" from "+dateFrom+" to "+dateTo);
		
		if (id != null) {
			crewMember = manager.getCrewMemberByCode(id);
			Date start = df.parse(dateFrom);
			Date end = df.parse(dateTo);		
			crewDays = manager.getCrewDayByCrewMemberBetween(new Integer(crewMember.getId()),start,end);
			if(crewDays == null){crewDays = new ArrayList<CrewDay>();}
			
			TreeMap<String,HashMap<String,Double>> single = new TreeMap<String,HashMap<String,Double>>();
			TreeMap<String,HashMap<String,Double>> multi  = new TreeMap<String,HashMap<String,Double>>();

			for(CrewDay cd : crewDays){
				if(cd.getAircraft() != null){
					if(cd.getAircraft().getEngines() > 0){
						if(multi.containsKey(cd.getAircraft().getRef())){
							HashMap<String,Double> typeMap = multi.get(cd.getAircraft().getRef());
							processTypes(cd,typeMap);
							multi.put(cd.getAircraft().getRef(), typeMap);
						}
						else {
							HashMap<String,Double> typeMap = new HashMap<String,Double>();
							processTypes(cd,typeMap);
							multi.put(cd.getAircraft().getRef(), typeMap);
						}
					}
					else{
						if(single.containsKey(cd.getAircraft().getRef())){
							HashMap<String,Double> typeMap = single.get(cd.getAircraft().getRef());
							processTypes(cd,typeMap);
							single.put(cd.getAircraft().getRef(), typeMap);
						}
						else {
							HashMap<String,Double> typeMap = new HashMap<String,Double>();
							processTypes(cd,typeMap);
							single.put(cd.getAircraft().getRef(), typeMap);
						}
					}
				}
			}
			
			
			enginetypes = new LinkedHashMap();
			enginetypes.put("Single Engine Type", single);
			enginetypes.put("Multi Engine Type", multi);
			
			LOG.info(enginetypes);
			
			return SUCCESS;
		}
		return execute();
	}
	
	@SuppressWarnings("unchecked")
	public String days183() throws Exception {
		
		LOG.info("Running 183 from "+dateFrom+" to "+dateTo);
		
		//Setup maps
		charterMap = new TreeMap<String,TreeMap<String,TreeMap>>();
		
		//for each charter
		CharterList charters = manager.getAllCharters();
		
		//get 183 days info
		sumCrewDays = manager.getSumCrewDays();
		
	    Date start = df.parse(dateFrom);
		Date end = df.parse(dateTo);
		
		
		years  = new LinkedHashMap<String,LinkedHashMap<String,TreeMap>>();
		
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(start);

		while(cal.getTime().before(end)){
			String year = yearformat.format(cal.getTime());
			String month = monthformat.format(cal.getTime());
			
			if(years.containsKey(year)){
				LinkedHashMap<String,TreeMap> months = years.get(year);	
				if(months.containsKey(month)){
					TreeMap days = months.get(month);
					String day = dayformat.format(cal.getTime());
					Map dayMap = new HashMap();
					dayMap.put("day", cal.get(Calendar.DAY_OF_WEEK));
					days.put(day,dayMap);
					months.put(month, days);
					years.put(year, months);
					cal.add(Calendar.DAY_OF_MONTH, 1);
				}
				else{
					TreeMap days = new TreeMap();
					String day = dayformat.format(cal.getTime());
					Map dayMap = new HashMap();
					dayMap.put("day", cal.get(Calendar.DAY_OF_WEEK));
					days.put(day,dayMap);
					months.put(month, days);
					years.put(year, months);
					cal.add(Calendar.DAY_OF_MONTH, 1);
				}
			}
			else{
				LinkedHashMap<String,TreeMap> months = new LinkedHashMap<String,TreeMap>();
				TreeMap days = new TreeMap();
				String day = dayformat.format(cal.getTime());
				TreeMap dayMap = new TreeMap();
				dayMap.put("day", cal.get(Calendar.DAY_OF_WEEK));
				days.put(day,dayMap);
				months.put(month, days);
				years.put(year, months);
				cal.add(Calendar.DAY_OF_MONTH, 1);
			}
			daycount ++;
		}

		for(Charter chart : charters.charterList){
			
			//fromdate to todate get all crew days
			List<CrewDay> crewDays = manager.getCrewDayByCharterBetween(chart.getId(),start, end); 
			
			TreeMap<String,TreeMap> aircraftMap = new TreeMap<String,TreeMap>();
			
			for(CrewDay day : crewDays){
				
				if(day.getAircraft() != null){
				  //if they are assigned to an aircraft
				  if(aircraftMap.containsKey(day.getAircraft().getRef())){
					  TreeMap<String,TreeMap<String,TreeMap>> typeMap = aircraftMap.get(day.getAircraft().getRef());
					  LOG.info(day.getCrewMember().getRole().getPosition()+" "+ day.getCrewMember().getId());
					  if((""+day.getCrewMember().getRole().getPosition()).toUpperCase().indexOf("AME") != -1){
						if(typeMap.containsKey("ZAME")){
							TreeMap<String,TreeMap> groupMap = typeMap.get("ZAME");
							TreeMap<String,TreeMap> crewMap = groupMap.get("crew");
							TreeMap<String,Integer> totalMap = groupMap.get("total");
							
							if(crewMap.containsKey(day.getCrewMember().getPersonal().getFullName())){
								TreeMap dayMap = crewMap.get(day.getCrewMember().getPersonal().getFullName());
								dayMap.put(fullformat.format(day.getDate()), day);
								crewMap.put(day.getCrewMember().getPersonal().getFullName(),dayMap);
								groupMap.put("crew",crewMap);
								
								if("W".equals(day.getActivity())){
								if(totalMap.containsKey(fullformat.format(day.getDate()))){
									totalMap.put(fullformat.format(day.getDate()), totalMap.get(fullformat.format(day.getDate())) +1 );
								}
								else {
									totalMap.put(fullformat.format(day.getDate()), 1 );									
								}
								}
								
								groupMap.put("total",totalMap);
								typeMap.put("ZAME",groupMap);
							}
							else{
								TreeMap dayMap = new TreeMap();
								dayMap.put(fullformat.format(day.getDate()), day);
								crewMap.put(day.getCrewMember().getPersonal().getFullName(),dayMap);
								groupMap.put("crew",crewMap);
								
								if("W".equals(day.getActivity())){
								if(totalMap.containsKey(fullformat.format(day.getDate()))){
									totalMap.put(fullformat.format(day.getDate()), totalMap.get(fullformat.format(day.getDate())) +1 );
								}
								else {
									totalMap.put(fullformat.format(day.getDate()), 1 );									
								}
							    }
								
								groupMap.put("total",totalMap);
								typeMap.put("ZAME",groupMap);
							}
						} 
						else{
							TreeMap groupMap = new TreeMap();
							TreeMap crewMap = new TreeMap();
							TreeMap totalMap = new TreeMap();
							TreeMap dayMap = new TreeMap();
							dayMap.put(fullformat.format(day.getDate()), day);
							crewMap.put(day.getCrewMember().getPersonal().getFullName(),dayMap);
							groupMap.put("crew",crewMap);
							
							if("W".equals(day.getActivity())){
							totalMap.put(fullformat.format(day.getDate()), 1 );									
							}
							
							groupMap.put("total",totalMap);
							typeMap.put("ZAME",groupMap);
						}
					  }
					  else{
					    if(typeMap.containsKey("Pilot")){
					    	TreeMap<String,TreeMap> groupMap = typeMap.get("Pilot");
							TreeMap<String,TreeMap> crewMap = groupMap.get("crew");
							TreeMap<String,Integer> totalMap = groupMap.get("total");
					    	
					    	if(crewMap.containsKey(day.getCrewMember().getPersonal().getFullName())){
								TreeMap dayMap = crewMap.get(day.getCrewMember().getPersonal().getFullName());
								dayMap.put(fullformat.format(day.getDate()), day);
								crewMap.put(day.getCrewMember().getPersonal().getFullName(),dayMap);
								groupMap.put("crew",crewMap);
								
								if("W".equals(day.getActivity())){
								if(totalMap.containsKey(fullformat.format(day.getDate()))){
									totalMap.put(fullformat.format(day.getDate()), totalMap.get(fullformat.format(day.getDate())) +1 );
								}
								else {
									totalMap.put(fullformat.format(day.getDate()), 1 );									
								}
								}
								
								groupMap.put("total",totalMap);
								typeMap.put("Pilot",groupMap);
							}
							else{
								TreeMap dayMap = new TreeMap();
								dayMap.put(fullformat.format(day.getDate()), day);
								crewMap.put(day.getCrewMember().getPersonal().getFullName(),dayMap);
								groupMap.put("crew",crewMap);
								
								if("W".equals(day.getActivity())){
								if(totalMap.containsKey(fullformat.format(day.getDate()))){
									totalMap.put(fullformat.format(day.getDate()), totalMap.get(fullformat.format(day.getDate())) +1 );
								}
								else {
									totalMap.put(fullformat.format(day.getDate()), 1 );									
								}
								}
								
								groupMap.put("total",totalMap);
								typeMap.put("Pilot",groupMap);
							}
					    	
					    }
					    else{
					    	TreeMap groupMap = new TreeMap();
							TreeMap crewMap = new TreeMap();
							TreeMap totalMap = new TreeMap();
							TreeMap dayMap = new TreeMap();
							dayMap.put(fullformat.format(day.getDate()), day);
							crewMap.put(day.getCrewMember().getPersonal().getFullName(),dayMap);
							groupMap.put("crew",crewMap);
							
							if("W".equals(day.getActivity())){
							totalMap.put(fullformat.format(day.getDate()), 1 );									
							}
							
							groupMap.put("total",totalMap);
							typeMap.put("Pilot",groupMap);
					    }
				      }
				  }
				  else{
					  //aircraft not found add.
					  TreeMap typeMap = new TreeMap();
					  TreeMap groupMap = new TreeMap();
					  TreeMap crewMap = new TreeMap();
					  TreeMap totalMap = new TreeMap();
					  TreeMap dayMap = new TreeMap();
					  
					  if((""+day.getCrewMember().getRole().getPosition()).toUpperCase().indexOf("AME") != -1){
						  dayMap.put(fullformat.format(day.getDate()), day);
						  crewMap.put(day.getCrewMember().getPersonal().getFullName(),dayMap);
						  groupMap.put("crew",crewMap);
						  if("W".equals(day.getActivity())){
						  totalMap.put(fullformat.format(day.getDate()), 1 );
						  }
						  groupMap.put("total",totalMap);
						  typeMap.put("ZAME",groupMap);
					  }
					  else{
						  dayMap.put(fullformat.format(day.getDate()), day);
						  crewMap.put(day.getCrewMember().getPersonal().getFullName(),dayMap);
						  groupMap.put("crew",crewMap);
						  if("W".equals(day.getActivity())){
						  totalMap.put(fullformat.format(day.getDate()), 1 );	
						  }
						  groupMap.put("total",totalMap);
						  typeMap.put("Pilot",groupMap);
					  }
					  aircraftMap.put(day.getAircraft().getRef(), typeMap);
				  }
			    }	
			}
			
			charterMap.put(chart.getCode(),aircraftMap);
		}
		LOG.info("\n"+charterMap);
		return "days183";
	}

	public void setUser(User arg0) {
		this.user = arg0;
	}

}
