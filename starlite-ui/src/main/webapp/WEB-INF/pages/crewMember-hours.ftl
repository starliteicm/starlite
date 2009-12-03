<#include "/starlite.ftl">
<html>
<head>
  <title>${crewMember.personal.firstName!} ${crewMember.personal.lastName!}</title>
  <link rel="stylesheet" type="text/css" href="styles/forms.css">
  <@enableJQuery/>
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
      if(selected == "i"){
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
		<div style="padding:20px;">
		
		Select Month: 
		<select name="month" onchange="changeMonth(this);">		
		<option></option>
		<#list months.keySet() as month>
           <option>${month}</option>		
		</#list>
		</select>
		<br/><br/>
				
		<#list months.keySet() as month>
		
		<#if readOnly>
        <form action="#" method="POST" autocomplete="off" class="smart readonly">
        <#else>
        <form action="crewMember!saveHours.action" autocomplete="off" method="POST" class="smart">
        </#if>
        
        <input type="hidden" name="id" value="${id}">
        <input type="hidden" name="month" value="${month}">
		
		<div class="months" id="${month}" style="padding:5px;border:1px solid silver;margin:20px;display:none;">
		<h3 style="height:20px;width:70px;background-color:white;color:silver;position:relative;top:-22px;left:30px;padding:5px;">${month}</h3>
		<table style="width:100%">
		<tr>
		<th>&nbsp;</th>
		<th>Activity / Comments</th>
		<th>Type</th>
		<th>Tail#</th>
		<th>Charter</th>
		<th>Hours Flow</th>
		<th>Total Flow</th>
		<th>Time In</th>
		<th>Time Out</th>
		<th>Hours</th>
		</tr>
		
		<#list months.get(month).keySet() as day>
		<#if months.get(month).get(day) == 7>
		<tr style="background-color:#DDD;border-bottom:1px dotted silver;height:20px;padding:2px;">
		<#elseif months.get(month).get(day) == 1>
		<tr style="background-color:#DDD;border-bottom:1px dotted silver;height:20px;padding:2px;">
		<#else>
		<tr style="border-bottom:1px dotted silver;height:20px;padding:2px;">
		</#if>
		<td>${day}</td>
		<td>
		<select name="${month}-${day}_activity" onchange="changeActivity(this,'${month}-${day}');">
		<option></option>
		<option value="W">Work [W]</option>
		<option value="T">Travel [T]</option>
		<option value="D">Training [D]</option>
		<option value="L">Leave [L]</option>
		<option value="R">Rest [R]</option>
		<option value="U">Unavailable [U]</option>
		</select>
		<input style="width:80px;">
		</td>
		
		<td>
		
		<div class="${month}-${day}_WorkDiv" style="visibility:hidden;">
		
        <select id="${month}-${day}_type" onchange="changeType(this,'${month}-${day}_');">
          <option></option>
          <option value="n">Night</option>
          <option value="d">Day</option>
          <option value="i">Instruments</option>
        </select>
        
        <select id="${month}-${day}_position" style="display:inline;width:50px">
          <option></option>
          <option>Dual</option>
          <option>Capt</option>
          <option>Co</option>
        </select>
        
        <select id="${month}-${day}_instruments" style="display:none;width:50px">
          <option></option>
          <option>Sim</option>
          <option>Act</option>
        </select>
        </div>
        
        </td>
        
        <td>
        
        <div class="${month}-${day}_WorkDiv" style="visibility:hidden;">
        <select id="${month}-${day}_tail">
          <option></option>
        <#list allAircraft as a>
          <option>${a.ref}</option>
        </#list>
        </select>
        </div>
        
        </td>
        
        <td>
        
        <div class="${month}-${day}_WorkDiv" style="visibility:hidden;">
        <select id="${month}-${day}_charter">
        <option></option>
        <#list allCharters as c>
            <option>${c.code}</option>
        </#list>
        </select>
        </div>
        
        </td>
		
		<td>
          <input style="width:45px;" name="${month}-${day}_flown" id="${month}-${day}_flown" onchange='checkNum(this);sumTotals("${month}");'>
        </td>
		<td>
          <input style="width:45px;" name="${month}-${day}_flowntotal" id="${month}-${day}_flowntotal" readonly value="0">
        </td>
		
		<td>
          <input style="width:45px;" class="time-pick" id="${month}-${day}_timein" onblur='timeBetween("#${month}-${day}_timein","#${month}-${day}_timeout","#${month}-${day}_hours");' name="${month}-${day}_timein">
        </td>
        <td>
          <input style="width:45px;" class="time-pick" id="${month}-${day}_timeout" onblur='timeBetween("#${month}-${day}_timein","#${month}-${day}_timeout","#${month}-${day}_hours");' name="${month}-${day}_timeout">
        </td>
        <td>
          <input style="width:45px;" name="${month}-${day}_hours" value="00:00" readonly id="${month}-${day}_hours" />
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