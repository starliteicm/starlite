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
<style type="text/css">

.buttonStyle {
display: inline;
background-color: transparent;
background-repeat: no-repeat;
border: 0;
margin: 0px auto;
padding: 0px 0px 0 0;
text-align: center;
font-family: Helvetica, Calibri, Arial, sans-serif;
font-size: 100%;
font-weight: bold;
text-decoration: none;
}

.buttonStyle:hover{
background-position: 0 -1px;
color: #FFFFFF;
}
</style>
<body>

  <@enableDatePickers/>
  <@enableHelp/>
  
<#if user.hasPermission("UserAdmin")>
<div id="toolbar">

       

	
        <div style="float:left;padding:10px;margin:10px;border:1px solid silver;width:1100px;height:550px">
        
         <div style="float:left; width: 370px;">
         <#if edited = true>
            <form autocomplete="off"  action="flightOFP!editedSave.action?id=${id}&edited=true&typeId=${typeId}&approval=${approval}" enctype="multipart/form-data" method="post">
         </#if>
         <#if edited = false>
	        <form autocomplete="off"  action="flightOFP!save.action" enctype="multipart/form-data" method="post">
	     </#if> 
	     
	     
	          
		<fieldset>
		<legend>OFP</legend>
		<div class="fm-opt">
				<label for="flightOFP.id">Internal ID:</label>
				<input name="flightOFP.id" readonly="readonly" STYLE=" background-color: #F2F2F2;" type="text" id="ofpID" value="${flightOFP.id!}" />
		</div>
		<div class="fm-opt">
				<label for="flightOFP.flightActualStatus.flightActualStatusValue">Approval Status:</label>
				<input name="flightOFP.flightActualStatus.flightActualStatusValue" readonly="readonly" STYLE=" background-color: #F2F2F2;" type="text" id="ofpStatus" value="${flightOFP.flightActualStatus.flightActualStatusValue!}" />
		</div>
		<div class="fm-opt">
				<label for="ofpDate">Flight Date:</label>
				<input name="ofpDate"  type="text" class="date-pick" id="ofpDate" value="<#if flightOFP.OFPDate??>${flightOFP.OFPDate?string('dd/MM/yyyy')}<#else>${ofpDate!}</#if>" />
		</div>
		
		<div class="fm-opt">
				<label for="flightOFP.contract">Contract:</label>
	    		<select name="flightOFP.contract">
	    		   <#list contracts as ac> 	
	    		   <#if ac = flightOFP.contract>	  
	    		   	<option SELECTED>${ac}
	    		   </#if>
	    		   <#if ac != flightOFP.contract>
	    		    <option>${ac}
	    		    </#if>
	    		   </#list>
	    		</select>
	    		<br/>	
		</div>
		<div class="fm-opt">
                <label for="aircraftRef">A/C REG:</label>
	    		<select name="aircraftRef">
	    		   <#list aircrafts as ac> 	
	    		   <#if ac = aircraftRef>	  
	    		   	<option SELECTED>${ac}
	    		   </#if>
	    		   <#if ac != aircraftRef>
	    		    <option>${ac}
	    		    </#if>
	    		   </#list>
	    		</select>
	    		<br/>
        </div>
        		<div class="fm-opt">
				<label for="flightOFP.invoiceNo">Invoice No:</label>
				<input name="flightOFP.invoiceNo" type="text" value="${flightOFP.invoiceNo}"/>	
		</div>
		<div class="fm-opt">
				<label for="flightOFP.flightType">Flight Type:</label>
				<input name="flightOFP.flightType" type="text" value="${flightOFP.flightType}"/>	
		</div>
		<div class="fm-opt">
                <label for="gCatagory">G Category:</label>
	    		<select name="gCategory">
	    		   <#list gCategoryList as ac> 	
	    		   <#if ac = gCategory>	  
	    		   	<option SELECTED>${ac}
	    		   </#if>
	    		   <#if ac != gCategory>
	    		    <option>${ac}
	    		    </#if>
	    		   </#list>
	    		</select>
	    		<br/>
        </div>		
		<div class="fm-opt">
			    <div class="fm-opt">
				<div class="fm-opt">
				<label for="flightOFP.pilot">Captain:</label>
	    		<select name="flightOFP.pilot">
	    		   <#list pilots as person >
  	    			<#if person = flightOFP.pilot>	  
	    		   	<option SELECTED>${person}
	    		    </#if>
	    		    <#if person != flightOFP.pilot>
	    		    <option>${person}
	    		    </#if>
	    		   </#list>
	    		</select>
	            </div>
			</div>
		</div>
		<div class="fm-opt">
			    <div class="fm-opt">
				<div class="fm-opt">
				<label for="flightOFP.coPilot">Co-Pilot:</label>
	    		<select name="flightOFP.coPilot">
	    		   <#list pilots as person >
	    			<#if person = flightOFP.coPilot>	  
	    		   	<option SELECTED>${person}
	    		    </#if>
	    		    <#if person != flightOFP.coPilot>
	    		    <option>${person}
	    		    </#if>
	    		   </#list>
	    		</select>
	            </div>
			</div>
		</div>
		<div class="fm-opt">
			    <div class="fm-opt">
				<div class="fm-opt">
				<label for="flightOFP.AME">AME:</label>
	    		<select name="flightOFP.AME">
	    		   <#list engineers as person >
	    			<#if person = flightOFP.AME>	  
	    		   	<option SELECTED>${person}
	    		    </#if>
	    		    <#if person != flightOFP.AME>
	    		    <option>${person}
	    		    </#if>
	    		   </#list>
	    		</select>
	            </div>
			</div>
		</div>
		<div class="fm-opt">
                <label for="flightOFP.PAX">PAX (KG):</label>
				<input name="flightOFP.PAX" type="text" value="${flightOFP.PAX!}"/>
            </div>
            <div class="fm-opt">
                <label for="flightOFP.freight">Freight (KG):</label>
				<input name="flightOFP.freight" type="text" value="${flightOFP.freight}"/>
            </div>
            <div class="fm-opt">
                <label for="flightOFP.totalLoad">Total Load (KG):</label>
				<input name="flightOFP.totalLoad" type="text" value="${flightOFP.totalLoad}"/>
            </div>
			<div class="fm-opt">
                <label for="flightOFP.ACServiceType">Type of Service</label>
	    		<select name="flightOFP.ACServiceType">
	    		   <#list typeOfService as service>
	    			<#if service = flightOFP.ACServiceType>	  
	    		   	<option SELECTED>${service}
	    		    </#if>
	    		    <#if service != flightOFP.ACServiceType>
	    		    <option>${service}
	    		    </#if>
	    		   </#list>
	    		</select>	
            </div>
            <div class="fm-opt">
                <label for="flightOFP.ACHrsToNextService">HRS to next Service:</label>
				<input name="flightOFP.ACHrsToNextService" type="text" value="${flightOFP.ACHrsToNextService}"/>
            </div>
            
          
            		
		</fieldset>       
		</div>
	
        
         <div style="float:left; width: 670px;">
		
		<fieldset>
			<legend>Landings</legend>
			<#assign block = 13>
		<span>
		       &nbsp;&nbsp;&nbsp;
		       <input type="text" value="From"  readonly="readonly"  STYLE=" background-color: #A9D0F5;"  size="${block}" />
		       <input type="text" value="To"  readonly="readonly"  STYLE=" background-color: #A9D0F5;"  size="${block}" />
		       <input type="text" value="Time"  readonly="readonly"  STYLE=" background-color: #A9D0F5;"  size="${block}" />
		       <input type="text" value="Hobbs On"  readonly="readonly"  STYLE=" background-color: #A9D0F5;"  size="${block}" />
		       <input type="text" value="Hobbs Off"  readonly="readonly"  STYLE=" background-color: #A9D0F5;"  size="${block}" />
		       <input type="text" value="Hobbs Difference"  readonly="readonly"  STYLE=" background-color: #A9D0F5;"  size="${block}" />
		       
		       
		       
	    </span>
	     <br/>
	    <span>
	            <#if (status > 3)>
	            1:
	            <input readonly="readonly" name="from1" id="from1"  type="text" value="${from1!}"    STYLE=" background-color: #F2F2F2;"  size="${block}" />
				<input readonly="readonly" name="to1" id="to1" type="text" value="${to1!}"   STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				<input readonly="readonly" name="time1" type="text" value="${time1!}"   STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				<input readonly="readonly" name="hobbsOn1" type="text" value="${hobbsOn1!}"  STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				<input readonly="readonly" name="hobbsOff1" type="text" value="${hobbsOff1!}"  STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				<input name="hobbsDiff1" id="hobbsDiff1" type="text" value="${hobbsDiff1!}"   readonly="readonly"  STYLE=" background-color: #F2F2F2;"  size="${block}"/>
	            <#else>
	            1:
				<input name="from1" id="from1"  type="text" value="${from1!}"    STYLE=" background-color: #FFFFFF;"  size="${block}" />
				<input name="to1" id="to1" type="text" value="${to1!}"   STYLE=" background-color: #FFFFFF;"  size="${block}"/>
				<input name="time1" type="text" value="${time1!}"   STYLE=" background-color: #FFFFFF;"  size="${block}"/>
				<input name="hobbsOn1" type="text" value="${hobbsOn1!}"  STYLE=" background-color: #FFFFFF;"  size="${block}"/>
				<input name="hobbsOff1" type="text" value="${hobbsOff1!}"  STYLE=" background-color: #FFFFFF;"  size="${block}"/>
				<input name="hobbsDiff1" id="hobbsDiff1" type="text" value="${hobbsDiff1!}"   readonly="readonly"  STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				</#if>
		</span>
		<br/>
	    <span>
	           <#if (status > 3)>
	           2:
				<input readonly="readonly" name="from2" id="from2"  type="text" value="${from2!}"    STYLE=" background-color: #F2F2F2;"  size="${block}" />
				<input readonly="readonly" name="to2" id="to2" type="text" value="${to2!}"   STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				<input readonly="readonly" name="time2" type="text" value="${time2!}"   STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				<input readonly="readonly" name="hobbsOn2" type="text" value="${hobbsOn2!}"  STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				<input readonly="readonly" name="hobbsOff2" type="text" value="${hobbsOff2!}"  STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				<input readonly="readonly" name="hobbsDiff2" id="hobbsDiff1" type="text" value="${hobbsDiff2!}"   readonly="readonly"  STYLE=" background-color: #F2F2F2;"  size="${block}"/>
	           <#else>
	            2:
				<input name="from2" id="from2"  type="text" value="${from2!}"    STYLE=" background-color: #FFFFFF;"  size="${block}" />
				<input name="to2" id="to2" type="text" value="${to2!}"   STYLE=" background-color: #FFFFFF;"  size="${block}"/>
				<input name="time2" type="text" value="${time2!}"   STYLE=" background-color: #FFFFFF;"  size="${block}"/>
				<input name="hobbsOn2" type="text" value="${hobbsOn2!}"  STYLE=" background-color: #FFFFFF;"  size="${block}"/>
				<input name="hobbsOff2" type="text" value="${hobbsOff2!}"  STYLE=" background-color: #FFFFFF;"  size="${block}"/>
				<input name="hobbsDiff2" id="hobbsDiff1" type="text" value="${hobbsDiff2!}"   readonly="readonly"  STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				</#if>
		</span>
		<br/>
		<span>
		        <#if (status > 3)>
		        3:
		        <input readonly="readonly" name="from3" id="from3"  type="text" value="${from3!}"    STYLE=" background-color: #F2F2F2;"  size="${block}" />
				<input readonly="readonly" name="to3" id="to3" type="text" value="${to3!}"   STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				<input readonly="readonly" name="time3" type="text" value="${time3!}"   STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				<input readonly="readonly" name="hobbsOn3" type="text" value="${hobbsOn3!}"  STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				<input readonly="readonly" name="hobbsOff3" type="text" value="${hobbsOff3!}"  STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				<input readonly="readonly" name="hobbsDiff3" id="hobbsDiff1" type="text" value="${hobbsDiff3!}"  readonly="readonly"  STYLE=" background-color: #F2F2F2;" size="${block}"/>
		        <#else>
	            3:
				<input name="from3" id="from3"  type="text" value="${from3!}"    STYLE=" background-color: #FFFFFF;"  size="${block}" />
				<input name="to3" id="to3" type="text" value="${to3!}"   STYLE=" background-color: #FFFFFF;"  size="${block}"/>
				<input name="time3" type="text" value="${time3!}"   STYLE=" background-color: #FFFFFF;"  size="${block}"/>
				<input name="hobbsOn3" type="text" value="${hobbsOn3!}"  STYLE=" background-color: #FFFFFF;"  size="${block}"/>
				<input name="hobbsOff3" type="text" value="${hobbsOff3!}"  STYLE=" background-color: #FFFFFF;"  size="${block}"/>
				<input name="hobbsDiff3" id="hobbsDiff1" type="text" value="${hobbsDiff3!}"  readonly="readonly"  STYLE=" background-color: #F2F2F2;" size="${block}"/>
				</#if>
		</span>
		<br/>
		<span>
		        <#if (status > 3)>
		         4:
		        <input readonly="readonly" name="from4" id="from4"  type="text" value="${from4!}"    STYLE=" background-color: #F2F2F2;"  size="${block}" />
				<input readonly="readonly" name="to4" id="to4" type="text" value="${to4!}"   STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				<input readonly="readonly" name="time4" type="text" value="${time4!}"   STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				<input readonly="readonly" name="hobbsOn4" type="text" value="${hobbsOn4!}"  STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				<input readonly="readonly" name="hobbsOff4" type="text" value="${hobbsOff4!}"  STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				<input name="hobbsDiff4" id="hobbsDiff1" type="text" value="${hobbsDiff4!}" readonly="readonly"  STYLE=" background-color: #F2F2F2;"  size="${block}"/>
		        
		        <#else>
	            4:
				<input name="from4" id="from4"  type="text" value="${from4!}"    STYLE=" background-color: #FFFFFF;"  size="${block}" />
				<input name="to4" id="to4" type="text" value="${to4!}"   STYLE=" background-color: #FFFFFF;"  size="${block}"/>
				<input name="time4" type="text" value="${time4!}"   STYLE=" background-color: #FFFFFF;"  size="${block}"/>
				<input name="hobbsOn4" type="text" value="${hobbsOn4!}"  STYLE=" background-color: #FFFFFF;"  size="${block}"/>
				<input name="hobbsOff4" type="text" value="${hobbsOff4!}"  STYLE=" background-color: #FFFFFF;"  size="${block}"/>
				<input name="hobbsDiff4" id="hobbsDiff1" type="text" value="${hobbsDiff4!}" readonly="readonly"  STYLE=" background-color: #F2F2F2;"  size="${block}"/>
				</#if>
		</span>
		<br/>
		<br/>
		   
		</fieldset>
		<div style="margin-top:240px;margin-left:550px;">
	 
	  <#if edited = true>
	     <button class="smooth" style="margin-left:0;padding:2px 10px 2px 7px;" onclick="return confirm('Are you sure you want to save this OFP?')" type="submit"><img src="images/icons/add.png"/>Update</button>

      </#if>
      <#if edited = false>
       <button class="smooth" style="margin-left:0;padding:2px 10px 2px 7px;" onclick="return confirm('Are you sure you want to save this OFP?')" type="submit"><img src="images/icons/add.png"/>Save</button>
	    
	  </#if>
     </div>
	</div>

		<hr class="clear"/>  
         
       </form>
       
  
    </div>
     
</div>
<br/>
</#if>

</body>
</html>
