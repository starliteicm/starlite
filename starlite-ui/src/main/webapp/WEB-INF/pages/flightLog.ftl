<#include "/starlite.ftl">
<html>
<head>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/styles/jmesa.css">
<script type="text/javascript" src="${request.contextPath}/js/jmesa.js"></script>
<script type="text/javascript">
        $(document).ready(function() {
           addDropShadow('images/table/');
        });
        
        function onSubmitWsColumn() {
			document.getElementById("saveButton").disabled = false;
		}
</script>
<title>Aircraft</title>
</head>
<body>

  <@enableDatePickers/>
  <@enableHelp/>
  
<#if user.hasPermission("UserAdmin")>
<div id="toolbar">
	
	
	<div style="float:left;padding:10px;margin:10px;border:1px solid silver;width:720px;height:370px">
	  <div style="float:left; width: 700px;">
         
		<fieldset>
		<legend>Actuals</legend>
		
		<#assign block = 12>
		<span>
		        
		       <input type="text" value="Flight Date"  readonly="readonly"  STYLE=" background-color: #A9D0F5;"  size="${block}" />
		       <input type="text" value="A/C REG"  readonly="readonly"  STYLE=" background-color: #A9D0F5;"  size="${block}" />
		       <input type="text" value="From Destination"  readonly="readonly"  STYLE=" background-color: #A9D0F5;"  size="${block}" />
		       <input type="text" value="To Destination"  readonly="readonly"  STYLE=" background-color: #A9D0F5;"  size="${block}" />
		       <input type="text" value="Distance"  readonly="readonly"  STYLE=" background-color: #A9D0F5;"  size="${block}" />
		       <input type="text" value="Captain"  readonly="readonly"  STYLE=" background-color: #A9D0F5;"  size="${block}" />
		       
		       
	    </span>
	     <br/>
	    <span>
				<input name="flightLog.flightDateField" id="flightLog.flightDateField"  type="text" value="${flightLog.getFlightDateField()}"  readonly="readonly"  STYLE=" background-color: #F2F2F2;"  size="${block}" />
				<input name="flightLog.registrationField" id="flightLog.registrationField" type="text" value="${flightLog.getRegistrationField()}"  readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				<input name="flightLog.start_LocationField" type="text" value="${flightLog.getStart_LocationField()}"  readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				<input name="flightLog.end_LocationField" type="text" value="${flightLog.getEnd_LocationField()}" readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				<input name="flightLog.GPS_DistanceField" type="text" value="${flightLog.getGPS_DistanceField()}" readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				<input name="flightLog.captainField" id="flightLog.captainField" type="text" value="${flightLog.getCaptainField()}"  readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="${block}"/>
						</span>
		<br />
		<br />
		
		<span>
		       <input type="text" value="Co-Pilot"  readonly="readonly"  STYLE=" background-color: #A9D0F5;"  size="${block}" />
		       <input type="text" value="Flight Engineer"  readonly="readonly"  STYLE=" background-color: #A9D0F5;"  size="${block}" />
		       <input type="text" value="Start Fuel"  readonly="readonly"  STYLE=" background-color: #A9D0F5;"  size="${block}" />
		       <input type="text" value="Take Off"  readonly="readonly"  STYLE=" background-color: #A9D0F5;"  size="${block}" />
		       <input type="text" value="Landing"  readonly="readonly"  STYLE=" background-color: #A9D0F5;"  size="${block}" />
		       <input type="text" value="Generated Date"  readonly="readonly"  STYLE=" background-color: #A9D0F5;"  size="${block}" />
		       
	    </span>
	     <br/>
	    <span>
	            <input name="flightLog.first_OfficerField" id="flightLog.first_OfficerField" type="text" value="${flightLog.getFirst_OfficerField()}"  readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				<input name="flightLog.cabin_CrewField" id="flightLog.cabin_CrewField" type="text" value="${flightLog.getCabin_CrewField()}"  readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				<input name="flightLog.starting_FuelField" type="text" value="${flightLog.getStarting_FuelField()}" readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				<input name="flightLog.takeoffField" type="text" value="${flightLog.getTakeoffField()}" readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				<input name="flightLog.landingField" type="text" value="${flightLog.getLandingField()}" readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				<input name="flightLog.generatedDateField" id="flightLog.generatedDateField"  type="text" value="${flightLog.getGeneratedDateField()}"  readonly="readonly"  STYLE=" background-color: #F2F2F2;"  size="${block}" />
				
		</span>
		<br />
		<br/>
		<span>
		       <input type="text" value="Flight Type"  readonly="readonly"  STYLE=" background-color: #A9D0F5;"  size="${block}" />
		       <input type="text" value="Ref. Number"  readonly="readonly"  STYLE=" background-color: #A9D0F5;"  size="${block}" />
		       <input type="text" value="Passengers"  readonly="readonly"  STYLE=" background-color: #A9D0F5;"  size="${block}" />
		       <input type="text" value="Fuel Uplift"  readonly="readonly"  STYLE=" background-color: #A9D0F5;"  size="${block}" />
		       <input type="text" value="Receipt No"  readonly="readonly"  STYLE=" background-color: #A9D0F5;"  size="${block}" />
		       <input type="text" value="Price"  readonly="readonly"  STYLE=" background-color: #A9D0F5;"  size="${block}" />
		       
		       
		       
	    </span>
	     <br/>
	    <span>
	            <input name="flightLog.flight_TypeField" id="flightLog.flight_TypeField" type="text" value="${flightLog.getFlight_TypeField()}"  readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				<input name="flightLog.reference_NumberField" type="text" value="${flightLog.getReference_NumberField()}"  readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				<input name="flightLog.passengersField" type="text" value="${flightLog.getPassengersField()}" readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				<input name="flightLog.fuel_UpliftField" type="text" value="${flightLog.getFuel_UpliftField()}" readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				<input name="flightLog.receipt_NumberField" id="flightLog.receipt_NumberField" type="text" value="${flightLog.getReceipt_NumberField()}"  readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				<input name="flightLog.priceField" id="flightLog.priceField" type="text" value="${flightLog.getPriceField()}"  readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				
		</span>
		<br />
		<br />
		<span>
		       <input type="text" value="Flight Report"  readonly="readonly"  STYLE=" background-color: #A9D0F5;"  size="${block}" />
		       <input type="text" value="Power Start"  readonly="readonly"  STYLE=" background-color: #A9D0F5;"  size="${block}" />
		       <input type="text" value="Doors Closed"  readonly="readonly"  STYLE=" background-color: #A9D0F5;"  size="${block}" />
		       <input type="text" value="Engine On"  readonly="readonly"  STYLE=" background-color: #A9D0F5;"  size="${block}" />
		       <input type="text" value="Block Start"  readonly="readonly"  STYLE=" background-color: #A9D0F5;"  size="${block}" />
		       <input type="text" value="Block End"  readonly="readonly"  STYLE=" background-color: #A9D0F5;"  size="${block}" />
		       
		       
		       
	    </span>
	     <br/>
	    <span>
				<input name="flightLog.flight_ReportField" id="flightLog.flight_ReportField" type="text" value="${flightLog.getFlight_ReportField()}"  readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				<input name="flightLog.power_StartField" type="text" value="${flightLog.getPower_StartField()}" readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				<input name="flightLog.doors_ClosedField" type="text" value="${flightLog.getDoors_ClosedField()}" readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				<input name="flightLog.engine_OnField" type="text" value="${flightLog.getEngine_OnField()}" readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				<input name="flightLog.block_StartField" id="flightLog.block_StartField"  type="text" value="${flightLog.getBlock_StartField()}"  readonly="readonly"  STYLE=" background-color: #F2F2F2;"  size="${block}" />
				<input name="flightLog.block_EndField" id="flightLog.block_EndField" type="text" value="${flightLog.getBlock_EndField()}"  readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				
		</span>
		<br />
		<br />
		<span>
		       <input type="text" value="Engine Off"  readonly="readonly"  STYLE=" background-color: #A9D0F5;"  size="${block}" />
		       <input type="text" value="Doors Opened"  readonly="readonly"  STYLE=" background-color: #A9D0F5;"  size="${block}" />
		       <input type="text" value="Power End"  readonly="readonly"  STYLE=" background-color: #A9D0F5;"  size="${block}" />
		       <input type="text" value="Airframe Cycles"  readonly="readonly"  STYLE=" background-color: #A9D0F5;"  size="${block}" />
		       <input type="text" value="Engine Cycles"  readonly="readonly"  STYLE=" background-color: #A9D0F5;"  size="${block}" />
		        
		       
		       
		       
	    </span>
	     <br/>
	    <span>
				<input name="flightLog.engine_OffField" type="text" value="${flightLog.getEngine_OffField()}"  readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				<input name="flightLog.doors_OpenedField" type="text" value="${flightLog.getDoors_OpenedField()}" readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				<input name="flightLog.power_EndField" type="text" value="${flightLog.getPower_EndField()}" readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				<input name="flightLog.airframe_CyclesField" id="flightLog.airframe_CyclesField" type="text" value="${flightLog.getAirframe_CyclesField()}"  readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				<input name="flightLog.engine_CyclesField" id="flightLog.engine_CyclesField" type="text" value="${flightLog.getEngine_CyclesField()}"  readonly="readonly" STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				
		</span>
		<br />
		<br />
		
		
		
        </fieldset>
     </div>   
        
	    
	    
     
</div>
<br/>
</#if>

</body>
</html>
