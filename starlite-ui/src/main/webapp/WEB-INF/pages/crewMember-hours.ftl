<#setting number_format = "######" />
<#assign visible = "visibility:hidden" />
<#include "/starlite.ftl">
<html>
<head>
  <title>${crewMember.personal.firstName!} ${crewMember.personal.lastName!}</title>
  <link rel="stylesheet" type="text/css" href="styles/forms.css">
  <@enableDatePickers/>
  <@enableTimePickers/>
  
  <script>
    function checkNum(obj){
      if(isNaN(obj.value)){
        obj.value="";
        return false;
      }
      else if(obj.value*1 > 24 ){
        obj.value="";
        return false;
      }
      else if(obj.value*1 < 0 ){
        obj.value="";
        return false;
      }
      return true;
    }
    
    function changeActivity(sel,day){
      var selected = sel.options[sel.selectedIndex].value;
      
      if((selected == "W")||(selected == "D")){
        //alert(selected+" visible ."+day+"_WorkDiv");
        $("."+day+"_WorkDiv").css("visibility","visible");
      }
      else{
        //alert(selected+" hidden ."+day+"_WorkDiv");
        $("."+day+"_WorkDiv").css("visibility","hidden");    
      }
      
    }
    
    function changeType(sel,day){
      var selected = sel.options[sel.selectedIndex].value;
      if(selected == "t"){
        $("#"+day+"instruments").css("display","none");
        $("#"+day+"position").css("display","none");
      }
      else if(selected == "i"){
        $("#"+day+"instruments").css("display","inline");
        $("#"+day+"position").css("display","none");
      }
      else{
        $("#"+day+"instruments").css("display","none");
        $("#"+day+"position").css("display","inline");
      }
    }

    function changeMonth(sel){
      var month = sel.options[sel.selectedIndex].value;
      $(".months").css("display","none")
      $("#"+month).css("display","");
    }
    
    function sumTotals(month){
      var sum = 0;
      for(var count =1; count < 32; count++){
      
        var date= month + "-";
        if(count < 10){
          date = date + "0"+count;
        }
        else{
          date = date+count;
        }
        
        //alert(date);
        
        var flown = $("#"+date+"_flown").val()
        if(flown != null) { 
          if(flown != "") { 
            if(!isNaN(flown*1)) { 
              sum += (flown*1);
            }
          }
        }
        $("#"+date+"_flowntotal").val(sum);
      }
    
    
    }
    
    function validate(){
      
      var message = "";
      var dateFrom = document.forms.setForm.dateFrom.value;
      var dateTo   = document.forms.setForm.dateTo.value;
      
      if(dateFrom == ""){message = message+"Please Enter a From date<br/>";}
      if(dateTo == ""){message = message+"Please Enter a To date<br/>";}
       
      $("#message").html(message+"<br/>");
        
      if(message == ""){        
      return confirm("This will override any Crew on Contract Information already set over this time period, are you sure you wish to continue?");
      }
      
      
      return false;
      
      }
    
   </script>
  
</head>

<body>
	<#if crewMember.approvalGroup.approvalStatus.toString() == "UNDER_REVIEW">
		<h3>Locked</h3>
	</#if>
	
	<#if crewMember.approvalGroup.approvalStatus.toString() == "APPROVED">
		<h3>Approved</h3>
	</#if>


	<#if user.hasPermission("Approve")>
	<div id="toolbar">
		<#if crewMember.approvalGroup.approvalStatus.toString() == "OPEN_FOR_EDITING">
		<a href="${request.contextPath}/crewMember!review.action?id=${id}&tab=${tab}"><img src="${request.contextPath}/images/icons/lock.png"/>Begin Approval</a>
		</#if>
		<#if crewMember.approvalGroup.approvalStatus.toString() == "UNDER_REVIEW">
		<a href="${request.contextPath}/crewMember!approve.action?id=${id}&tab=${tab}"><img src="${request.contextPath}/images/icons/accept.png"/>Approve</a>
		<a href="${request.contextPath}/crewMember!open.action?id=${id}&tab=${tab}"><img src="${request.contextPath}/images/icons/lock_open.png"/>Re-Open</a>
		</#if>
		<#if crewMember.approvalGroup.approvalStatus.toString() == "APPROVED">
		<a href="${request.contextPath}/crewMember!open.action?id=${id}&tab=${tab}"><img src="${request.contextPath}/images/icons/lock_open.png"/>Re-Open</a>
		</#if>
		<hr class="clear"/>
	</div>
	<br/>
	</#if>
	<@subTabs/>
	
	    <div style="display:block;padding:10px;width:100%;height:auto;margin-top:35px;border:1px solid silver;position:relative">
		
		
		<div id="loading" style="color:silver;">Loading... Please Wait</div>
		
		<div style="padding:20px;padding-bottom:0px;width:450px;">
		  <form name="setForm" action="crewMember!saveRange.action" onsubmit="return validate();" method="GET">
		    <input type="hidden" name="id" value="${id}">
		    <fieldset>
            <legend>Set Range</legend>
		    
		    <div id="message" style="color:red"></div>
		    
		    <div class="fm-opt">
		    <label for="dateFrom">Date Range:</label>
		    <input name="dateFrom" type="text" class="date-pick" value="${dateFrom!}"/>
            <input name="dateTo" type="text" class="date-pick" value="${dateTo!}"/>
            </div>
            
            <div class="fm-opt">
            <label for="activity">Activity:</label>
            <select name="activity">
                <option SELECTED value="W">Work [W]</option>
                <option value="T">Travel [T]</option>
                <option value="D">Training [D]</option>
                <option value="L">Leave [L]</option>
                <option value="R">Rest [R]</option>
                <option value="U">Unavailable [U]</option>
            </select>
            </div>
            
            <div class="fm-opt">
            <label for="tail">Aircraft:</label>
            <select name="tail" id="tail">
              <option></option>
              <#list allAircraft as a>
                  <option SELECTED value="${a.id}">${a.ref}</option>
              </#list>
            </select>
            </div>
            
            <div class="fm-opt">
            <label for="chart">Contract:</label>
            <select name="chart" id="chart">
              <#list allCharters as c>
                <option SELECTED value="${c.id}">${c.code}</option>
              </#list>
            </select>
            </div>
            <br/>
            <button type="submit" class="smooth" style="float:right; margin-right:10px; margin-bottom: 4px;"><img src="images/icons/pencil.png"/>Set</button>
            
            
            </fieldset>
            
          </form>
		</div>
		
		<div style="padding:20px;padding-top:0px;">
		
		  <form action="crewMember.action" autocomplete="off" >
		  <input type="hidden" name="tab" value="hours">
          <input type="hidden" name="id" value="${id}">
            <fieldset>
            <legend>Days on Contract</legend> 
            <div style="float:left;padding-right:20px;" class="fm-opt">
            <label for="hoursMonth">Select Month:</label>
            <select id="hoursMonth" name="hoursMonth" >      
            <option></option>
            <#list months.keySet() as month>
              <option>${month}</option>        
            </#list>
            </select>
            </div>
            <button type="submit" class="smooth" style="float:left;margin-right:10px; margin-bottom: 4px;"><img src="images/icons/zoom_in.png"/>Select</button>
            <br/><br/>
            </fieldset>    
          </form>
		
				
		<#list months.keySet() as month>
		
		  <#if readOnly>
            <form action="#" method="POST" autocomplete="off" class="smart readonly">
          <#else>
            <form action="crewMember!saveHours.action" autocomplete="off" method="POST" class="smart">
          </#if>
        
          <input type="hidden" name="id" value="${id}">
          <input type="hidden" name="hoursMonth" value="${month}">
		  <#if hoursMonth! == month>
		  <div class="months" id="${month}" style="padding:5px;border:1px solid silver;margin:20px;">
		  <#else>
		  <div class="months" id="${month}" style="padding:5px;border:1px solid silver;margin:20px;display:none;">
		  </#if>
		  <h3 style="height:20px;width:70px;background-color:white;color:silver;position:relative;top:-22px;left:30px;padding:5px;">${month}</h3>
		  <table style="width:60%">
		  <tr>
		    <th>&nbsp;</th>
		    <th><b>Activity / Comments</b></th>
		   <!-- <th><b>Type</b></th> -->
		    <th><b>Registration</b></th>
		    <th><b>Contract</b></th>
		    
		  </tr>
		
		<#assign flownTotal = 0  />
		
		<#list months.get(month).keySet() as day>
		  <#if months.get(month).get(day).get("day") == 7>
		    <tr style="background-color:#DDD;border-bottom:1px dotted silver;height:20px;padding:2px;">
		  <#elseif months.get(month).get(day).get("day") == 1>
		    <tr style="background-color:#DDD;border-bottom:1px dotted silver;height:20px;padding:2px;">
		  <#else>
		    <tr style="border-bottom:1px dotted silver;height:20px;padding:2px;">
		  </#if>
		
		  <#assign crewDay = months.get(month).get(day) />
		  
		  <#assign visible = "visibility:visible" />
		  
		  <#if crewDay.get("crewDay")?? >
		  <input type="hidden" name="${month}-${day}_id" value='${crewDay.get("crewDay").id}'>
		  </#if>
		
		  <td>${day}</td>
		  <td>
	  	  <select id="${month}-${day}_activity" name="${month}-${day}_activity" > <!-- onchange="changeActivity(this,'${month}-${day}');"> -->
	 	    <option></option>
		    <#if crewDay.get("crewDay")?? >
		      <#if crewDay.get("crewDay").activity == "W">
		        <#assign visible = "visibility:visible" />
		        <option SELECTED value="W">Work [W]</option>
		        <option value="T">Travel [T]</option>
		        <option value="D">Training [D]</option>
		        <option value="L">Leave [L]</option>
		        <option value="R">Rest [R]</option>
		        <option value="U">Unavailable [U]</option>
		      <#elseif crewDay.get("crewDay").activity == "T">
		        <option value="W">Work [W]</option>
                <option SELECTED value="T">Travel [T]</option>
                <option value="D">Training [D]</option>
                <option value="L">Leave [L]</option>
                <option value="R">Rest [R]</option>
                <option value="U">Unavailable [U]</option>
		      <#elseif crewDay.get("crewDay").activity == "D">
		        <#assign visible = "visibility:visible" />
		        <option value="W">Work [W]</option>
                <option value="T">Travel [T]</option>
                <option SELECTED value="D">Training [D]</option>
                <option value="L">Leave [L]</option>
                <option value="R">Rest [R]</option>
                <option value="U">Unavailable [U]</option>
		      <#elseif crewDay.get("crewDay").activity == "L">
		        <option value="W">Work [W]</option>
                <option value="T">Travel [T]</option>
                <option value="D">Training [D]</option>
                <option SELECTED value="L">Leave [L]</option>
                <option value="R">Rest [R]</option>
                <option value="U">Unavailable [U]</option>
		      <#elseif crewDay.get("crewDay").activity == "R">
		        <option value="W">Work [W]</option>
                <option value="T">Travel [T]</option>
                <option value="D">Training [D]</option>
                <option value="L">Leave [L]</option>
                <option SELECTED value="R">Rest [R]</option>
                <option value="U">Unavailable [U]</option>
		      <#elseif crewDay.get("crewDay").activity == "U">
		        <option value="W">Work [W]</option>
                <option value="T">Travel [T]</option>
                <option value="D">Training [D]</option>
                <option value="L">Leave [L]</option>
                <option value="R">Rest [R]</option>
                <option SELECTED value="U">Unavailable [U]</option>
		      <#else>
		        <option value="W">Work [W]</option>
                <option value="T">Travel [T]</option>
                <option value="D">Training [D]</option>
                <option value="L">Leave [L]</option>
                <option value="R">Rest [R]</option>
                <option value="U">Unavailable [U]</option>
		      </#if>
		    <#else>
		      <option value="W">Work [W]</option>
              <option value="T">Travel [T]</option>
              <option value="D">Training [D]</option>
              <option value="L">Leave [L]</option>
              <option value="R">Rest [R]</option>
              <option value="U">Unavailable [U]</option>
		    </#if>
		</select>
		
		
		
		<#if crewDay.get("crewDay")?? >
		  <input name="${month}-${day}_comment" value="${crewDay.get("crewDay").comments!}" style="width:80px;">
		<#else>
		  <input name="${month}-${day}_comment" value="" style="width:80px;">
		</#if>
		</td>
		
		<td>
		
		<div class="${month}-${day}_WorkDiv" style="${visible}">
		
        <select name="${month}-${day}_type" id="${month}-${day}_type" onchange="changeType(this,'${month}-${day}_');">
          <option></option>
          <#if crewDay.get("crewDay")??>
            <#if crewDay.get("crewDay").type! == "n">
              <option SELECTED value="n">Night</option>
              <option value="d">Day</option>
              <option value="i">Instruments</option>
              <option value="t">Instructor</option>
            <#elseif crewDay.get("crewDay").type! == "d" >
              <option value="n">Night</option>
              <option SELECTED value="d">Day</option>
              <option value="i">Instruments</option>
              <option value="t">Instructor</option>
            <#elseif crewDay.get("crewDay").type! == "i" >
              <option value="n">Night</option>
              <option value="d">Day</option>
              <option SELECTED value="i">Instruments</option>
              <option value="t">Instructor</option>
            <#elseif crewDay.get("crewDay").type! == "t" >
              <option value="n">Night</option>
              <option value="d">Day</option>
              <option  value="i">Instruments</option>
              <option SELECTED value="t">Instructor</option>
            <#else>
              <option value="n">Night</option>
              <option value="d">Day</option>
              <option value="i">Instruments</option>
              <option value="t">Instructor</option>
            </#if>
          <#else>
            <option value="n">Night</option>
            <option value="d">Day</option>
            <option value="i">Instruments</option>
            <option value="t">Instructor</option>
          </#if>
          
          
        </select>
        
        <select name="${month}-${day}_position" id="${month}-${day}_position"
        <#if crewDay.get("crewDay")?? >
        <#if crewDay.get("crewDay").type! != "i" && crewDay.get("crewDay").type! != "t">
        style="display:inline;width:50px">
        <#else>
         style="display:none;width:50px">
         </#if>
         <#else>
                style="display:inline;width:50px">
        </#if>
          <option></option>
          <#if crewDay.get("crewDay")??>
            <#if crewDay.get("crewDay").position! == "Dual" >
              <option SELECTED>Dual</option>
              <option>Capt</option>
              <option>Co</option>
            <#elseif crewDay.get("crewDay").position! == "Capt" >
              <option>Dual</option>
              <option SELECTED>Capt</option>
              <option>Co</option>
            <#elseif crewDay.get("crewDay").position! == "Co" >
              <option>Dual</option>
              <option>Capt</option>
              <option SELECTED>Co</option>
            <#else>
              <option>Dual</option>
              <option>Capt</option>
              <option>Co</option>
            </#if>
          <#else>
            <option>Dual</option>
            <option>Capt</option>
            <option>Co</option>
          </#if>
          
        </select>
        
        <select name="${month}-${day}_instruments" id="${month}-${day}_instruments" 
         <#if crewDay.get("crewDay")??>
         <#if crewDay.get("crewDay").type! == "i" >
        style="display:inline;width:50px">
        <#else>
        style="display:none;width:50px">
        </#if>
        <#else>
                style="display:none;width:50px">
        </#if>
        
          <option></option>
          <#if crewDay.get("crewDay")??>
            <#if crewDay.get("crewDay").instruments! == "Sim" >
              <option SELECTED>Sim</option>
              <option>Act</option>
            <#elseif crewDay.get("crewDay").instruments! == "Act" >
              <option>Sim</option>
              <option SELECTED>Act</option>
            <#else>
              <option>Sim</option>
              <option>Act</option>
            </#if>
          <#else>
            <option>Sim</option>
            <option>Act</option>
          </#if>
        </select>
        </div>
        
        </td>
        
        <td>
        
        <div class="${month}-${day}_WorkDiv" style="${visible}">
        <select name="${month}-${day}_tail" id="${month}-${day}_tail">
          <option></option>
        <#list allAircraft as a>
        
          <#if crewDay.get("crewDay")??>
            <#if crewDay.get("crewDay").aircraft??>
              <#if crewDay.get("crewDay").aircraft.id == a.id >
                <option SELECTED value="${a.id}">${a.ref}</option>
              <#else>
                <option value="${a.id}">${a.ref}</option>
              </#if>
            <#else>
              <option value="${a.id}">${a.ref}</option>
            </#if>
          <#else>
            <option value="${a.id}">${a.ref}</option>
          </#if>
          
        </#list>
        </select>
        </div>
        
        </td>
        
        <td>
        
        <div class="${month}-${day}_WorkDiv" style="${visible}">
        <select name="${month}-${day}_charter" id="${month}-${day}_charter">
        <option></option>
        <#list allCharters as c>
         
          <#if crewDay.get("crewDay")??>
            <#if crewDay.get("crewDay").charter??>
              <#if crewDay.get("crewDay").charter.id == c.id >
                <option SELECTED value="${c.id}">${c.code}</option>
              <#else>
                <option value="${c.id}">${c.code}</option>
              </#if>
            <#else>
              <option value="${c.id}">${c.code}</option>
            </#if>
          <#else>
            <option value="${c.id}">${c.code}</option>
          </#if>
            
        </#list>
        </select>
        </div>
        
        </td>
		
		
		
		</tr>
		</#list>
		
		</table>
		<br/><br/>
		<hr class="clear"/>
		<#if !readOnly>
        <button type="submit" class="smooth" style="float:right; margin-right:10px; margin-bottom: 4px;"><img src="images/icons/pencil.png"/>Save</button>
        <hr class="clear"/>
        </#if>
		
		</div>
		</form>
		
		</#list>
		
		<hr class="clear"/>
		</div>
		</div>
	
</body>
</html>