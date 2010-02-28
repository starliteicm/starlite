<#setting number_format = "######.##" />
<#include "/starlite.ftl">
<html>
<head>
  <@enableJQuery/>
  <@enableDatePickers/>
<style>
table tr td {
border: 1px solid black;
padding:2px;
}
table tr th {
border: 1px solid black;
padding:2px;
font-weight:bold;
}
table.jCalendar td {
border:none;
padding:3px 5px;
font-weight:normal;
}
</style>
</head>
<body>

<script>

  function validateDays(){
    var err = 0;
    
    $("#dateFromMsgDays").html("");
    $("#dateToMsgDays").html("");
    
    if($("#dateFromDays").val() == ""){
      err = 1;
      $("#dateFromMsgDays").html("Required");
    }
    
    if($("#dateToDays").val() == ""){
      err = 1;
      $("#dateToMsgDays").html("Required");
    }
    
    if(err == 0){
      document.forms.days.submit();
    }
    return false;
  }


  function validateOnContract(){
    var err = 0;
    
    $("#dateFromMsg").html("");
    $("#dateToMsg").html("");
    
    if($("#dateFrom").val() == ""){
      err = 1;
      $("#dateFromMsg").html("Required");
    }
    
    if($("#dateTo").val() == ""){
      err = 1;
      $("#dateToMsg").html("Required");
    }
    
    if(err == 0){
      document.forms.oncontract.submit();
    }
    return false;
  }
  
  function validateHours(){
    var err = 0;
    
    $("#dateFromHoursMsg").html("");
    $("#dateToHoursMsg").html("");
    
    if($("#dateFromHours").val() == ""){
      err = 1;
      $("#dateFromHoursMsg").html("Required");
    }
    
    if($("#dateToHours").val() == ""){
      err = 1;
      $("#dateToHoursMsg").html("Required");
    }
    
    if(err == 0){
      document.forms.hours.submit();
    }
    return false;
  }
  
</script>

<#if enginetypes?exists >

<li style="width:200px;height:30px;float:left;"><A href="reports.action" style="width:300px;height:20px;" >Back</A></li>
<br/><br/>

<table style="width:900px;">
<tr>
<td colspan="9" style="background:silver;font-weight:bold;width:70%;text-align:center;">
<div >Summary of Hours Flown - ${crewMember.personal.fullName!} (${crewMember.code!})</div>
</td>
<td colspan="3" style="background:silver;font-weight:bold;width:30%;text-align:center;">
<div >${dateFrom} - ${dateTo}</div>
</td>
</tr>

<#assign singledual  = 0 />
<#assign multidual   = 0 />
<#assign totaldual   = 0 />
<#assign singlecapt  = 0 />
<#assign multicapt   = 0 />
<#assign totalcapt   = 0 />
<#assign singleco    = 0 />
<#assign multico     = 0 />
<#assign totalco     = 0 />
<#assign multi       = 0 />
<#assign single      = 0 />
<#assign grandtotal  = 0 />

<#assign alldaydual   = 0 />
<#assign alldaycapt   = 0 />
<#assign alldayco     = 0 />
<#assign allnightdual = 0 />
<#assign allnightcapt = 0 />
<#assign allnightco   = 0 />
<#assign alltotal     = 0 />
<#assign allsim       = 0 />
<#assign allact       = 0 />
<#assign allinstrum   = 0 />
<#assign allinstruct  = 0 />

<#list enginetypes.keySet()! as enginetypekey>
<#assign enginetype = enginetypes.get(enginetypekey)/>
<tr><td colspan="12" style="border:none;">&nbsp;</td></tr>

<tr style="text-align:center;">
<td width="10%" style="border:none;">&nbsp;</td>
<td style="border-right:2px solid black;" width="24%" colspan="3">DAY</td>
<td style="border-right:2px solid black;" width="24%" colspan="3">NIGHT</td>
<td width="8%" style="border:none;">&nbsp;</td>
<td style="border-right:2px solid black;" width="24%" colspan="3">INSTRUMENT</td>
<td width="10%" rowspan="2">INSTRUCTION</td>
</tr>

<tr style="text-align:center;">
<td width="10%" >TYPE</td>
<td width="8%" >DUAL</td>
<td width="8%" >CAPT</td>
<td style="border-right:2px solid black;" width="8%" >CO</td>
<td width="8%" >DUAL</td>
<td width="8%" >CAPT</td>
<td style="border-right:2px solid black;" width="8%" >CO</td>
<td style="border-right:2px solid black;" width="8%">TOTAL</td>
<td width="8%">SIM</td>
<td width="8%">ACT</td>
<td style="border-right:2px solid black;" width="8%">TOTAL</td>
</tr>

<tr><td colspan="12" style="background-color:silver">${enginetypekey}</td></tr>

<#assign daydual   = 0 />
<#assign daycapt   = 0 />
<#assign dayco     = 0 />
<#assign nightdual = 0 />
<#assign nightcapt = 0 />
<#assign nightco   = 0 />
<#assign total     = 0 />
<#assign sim       = 0 />
<#assign act       = 0 />
<#assign instrum   = 0 />
<#assign instruct  = 0 />

<#list enginetype.keySet()! as aircraftKey>
<#assign aircraft = enginetype.get(aircraftKey) >

<#if aircraft.daydual?exists    ><#assign daydual   = daydual   + aircraft.daydual    /></#if>
<#if aircraft.daycapt?exists    ><#assign daycapt   = daycapt   + aircraft.daycapt    /></#if>
<#if aircraft.dayco?exists      ><#assign dayco     = dayco     + aircraft.dayco      /></#if>
<#if aircraft.nightdual?exists  ><#assign nightdual = nightdual + aircraft.nightdual  /></#if>
<#if aircraft.nightcapt?exists  ><#assign nightcapt = nightcapt + aircraft.nightcapt  /></#if>
<#if aircraft.nightco?exists    ><#assign nightco   = nightco   + aircraft.nightco    /></#if>
<#if aircraft.total?exists      ><#assign total     = total     + aircraft.total      /></#if>
<#if aircraft.sim?exists        ><#assign sim       = sim       + aircraft.sim        /></#if>
<#if aircraft.act?exists        ><#assign act       = act       + aircraft.act        /></#if>
<#if aircraft.inst_total?exists ><#assign instrum   = instrum   + aircraft.inst_total /></#if>
<#if aircraft.instruct?exists   ><#assign instruct  = instruct  + aircraft.instruct   /></#if>

<tr style="text-align:center;">
<td width="10%" >${aircraftKey!}</td>
<td width="8%" >${aircraft.daydual!}</td>
<td width="8%" >${aircraft.daycapt!}</td>
<td style="border-right:2px solid black;" width="8%" >${aircraft.dayco!}</td>
<td width="8%" >${aircraft.nightdual!}</td>
<td width="8%" >${aircraft.nightcapt!}</td>
<td style="border-right:2px solid black;" width="8%" >${aircraft.nightco!}</td>
<td style="border-right:2px solid black;" width="8%" >${aircraft.total!}</td>
<td width="8%" >${aircraft.sim!}</td>
<td width="8%" >${aircraft.act!}</td>
<td style="border-right:2px solid black;" width="8%" >${aircraft.inst_total!}</td>
<td width="10%" >${aircraft.instruct!}</td>
</tr>
</#list>

<#if enginetypekey == "Single Engine Type" >
  <#assign singledual  = singledual + daydual + nightdual />
  <#assign totaldual   = totaldual + singledual />
  <#assign singlecapt  = singlecapt + daycapt + nightcapt />
  <#assign totalcapt   = totalcapt + singlecapt />
  <#assign singleco    = singleco + dayco + nightco />
  <#assign totalco     = totalco + singleco />
  <#assign single      = total />
  <#assign grandtotal  = grandtotal + total />
<#else>
  <#assign multidual  = multidual + daydual + nightdual />
  <#assign totaldual   = totaldual + multidual />
  <#assign multicapt  = multicapt + daycapt + nightcapt />
  <#assign totalcapt   = totalcapt + multicapt />
  <#assign multico    = multico + dayco + nightco />
  <#assign totalco     = totalco + multico />
  <#assign multi      = total />
  <#assign grandtotal  = grandtotal + total />
</#if>

<#assign alldaydual   = alldaydual   + daydual />
<#assign alldaycapt   = alldaycapt   + daycapt />
<#assign alldayco     = alldayco     + dayco />
<#assign allnightdual = allnightdual + nightdual />
<#assign allnightcapt = allnightcapt + nightcapt />
<#assign allnightco   = allnightco   + nightco />
<#assign alltotal     = alltotal     + total />
<#assign allsim       = allsim       + sim />
<#assign allact       = allact       + act />
<#assign allinstrum   = allinstrum   + instrum />
<#assign allinstruct  = allinstruct  + instruct />

<tr style="text-align:center;border-top:2px solid black;">
<td width="10%" >Total:</td>
<td width="8%" >${daydual!}</td>
<td width="8%" >${daycapt!}</td>
<td style="border-right:2px solid black;" width="8%" >${dayco!}</td>
<td width="8%" >${nightdual!}</td>
<td width="8%" >${nightcapt!}</td>
<td style="border-right:2px solid black;" width="8%" >${nightco!}</td>
<td style="border-right:2px solid black;" width="8%" >${total!}</td>
<td width="8%" >${sim!}</td>
<td width="8%" >${act!}</td>
<td style="border-right:2px solid black;" width="8%" >${instrum!}</td>
<td width="10%" >${instruct!}</td>
</tr>

</#list>

<tr style="text-align:center;border-top:2px solid black;">
<td width="10%" >Grand Total:</td>
<td width="8%" >${alldaydual!}</td>
<td width="8%" >${alldaycapt!}</td>
<td style="border-right:2px solid black;" width="8%" >${alldayco!}</td>
<td width="8%" >${allnightdual!}</td>
<td width="8%" >${allnightcapt!}</td>
<td style="border-right:2px solid black;" width="8%" >${allnightco!}</td>
<td style="border-right:2px solid black;" width="8%" >${alltotal!}</td>
<td width="8%" >${allsim!}</td>
<td width="8%" >${allact!}</td>
<td style="border-right:2px solid black;" width="8%" >${allinstrum!}</td>
<td width="10%" >${allinstruct!}</td>
</tr>

<tr><td colspan="12" style="border:none;">&nbsp;</td></tr>

<tr style="text-align:center;">
<td colspan="2" width="18%" >Single Dual</td>
<td width="8%" >${singledual}</td>
<td colspan="2" width="16%" >Multi Dual</td>
<td width="8%" >${multidual}</td>
<td colspan="2" width="16%" >Total Dual</td>
<td width="8%" >${totaldual}</td>
<td colspan="3" style="border:none;" width="26%" >&nbsp;</td>
</tr>

<tr style="text-align:center;">
<td colspan="2" width="18%" >Single Capt</td>
<td width="8%" >${singlecapt}</td>
<td colspan="2" width="16%" >Multi Capt</td>
<td width="8%" >${multicapt}</td>
<td colspan="2" width="16%" >Total Capt</td>
<td width="8%" >${totalcapt}</td>
<td colspan="3" style="border:none;" width="26%" >&nbsp;</td>
</tr>

<tr style="text-align:center;">
<td colspan="2" width="18%" >Single Co</td>
<td width="8%" >${singleco}</td>
<td colspan="2" width="16%" >Multi Co</td>
<td width="8%" >${multico}</td>
<td colspan="2" width="16%" >Total Co</td>
<td width="8%" >${totalco}</td>
<td colspan="3" style="border:none;" width="26%" >&nbsp;</td>
</tr>

<tr style="text-align:center;">
<td colspan="2" width="18%" >Total Single</td>
<td width="8%" >${single}</td>
<td colspan="2" width="16%" >Total Multi</td>
<td width="8%" >${multi}</td>
<td colspan="6" style="border:none;" width="50%" >&nbsp;</td>
</tr>

<tr style="text-align:center;">
<td colspan="2" width="18%" >Total</td>
<td width="8%" >${grandtotal}</td>
<td colspan="9" style="border:none;" width="74%" >&nbsp;</td>
</tr>


</table>


<#elseif charterMap?exists >

<li style="width:200px;height:30px;float:left;"><A href="reports.action" style="width:300px;height:20px;" >Back</A></li>
<br/><br/>

<#list charterMap.keySet()! as chartKey>
  <#assign chart=charterMap.get(chartKey) />
  <#if chart.size() != 0 >
  
    <#list chart.keySet()! as aircraftKey>
      <#assign aircraft=chart.get(aircraftKey) />
  
      <table> 
  
       <#assign count=0 />
  
       <tr>
       <th style="width:150px;"><div style="width:150px;">${chartKey}</div></th>
       <#list years.keySet()! as year>
         <#assign months= years.get(year) />
         <#list months.keySet()! as month>
           <#assign color=colors[count % 5] />
           <#assign count=count+1 />
           <#assign days=months.get(month) />
           <td style="background-color:#${color};text-align:center;font-weight:bold;" colspan="${days.size()!}">${month}</td>
         </#list>
       </#list>
       <td>&nbsp;</td>
       <td>&nbsp;</td>
       </tr>
       
       <tr>
       <th style="width:150px;"><div style="width:150px;">${aircraftKey}</div></th>
       <#list years.keySet()! as year>
         <#assign months= years.get(year) />
         <#list months.keySet()! as month>
           <#assign days= months.get(month) />
           <#list days.keySet()! as day>
             <td>${day}</td>
           </#list>
         </#list>
       </#list>
       <td>Total</td>
       <td>Excess</td>
       </tr>
  
  
      <#list aircraft.keySet()! as typeKey>
        <#assign type=aircraft.get(typeKey) />
        <#if typeKey=="ZAME">
          <#assign typeColor="orange"/>
        <#else>
          <#assign typeColor="silver"/>
        </#if>
        
        
        <#list type.crew.keySet()! as crewKey>
          <#assign crew=type.crew.get(crewKey) />
          <#assign totalW=0 />
          <#assign totalT=0 />
          <#assign counter=0 />
          
          <!--<tr>
          <td style="width:150px;background-color:${typeColor};"><div style="width:150px;">${crewKey}</div></td>
          <td colspan="${daycount}">${crew}</td>
          </tr>-->
          
          <tr>
          <td style="width:150px;background-color:${typeColor};"><div style="width:150px;">${crewKey}</div></td>
          <#list years.keySet()! as year>
            <#assign months= years.get(year) />
            <#list months.keySet()! as month>
              <#assign days= months.get(month) />
              <#list days.keySet()! as day>
                <#assign cd= days.get(day) />
                <#assign date= ""+day+" "+month+" "+year />
                <#if crew.get(date)?exists >
                <#assign crewDay=crew.get(date) />
                
                <#if crewDay.activity == "W">
                <#assign totalW=totalW+1 />
                <td title="${date}"  <#if cd.day==1> style="border-right:2px solid black;" </#if>  > ${crewDay.activity!} </td>
                <#elseif crewDay.activity == "T">
                <#assign totalT=totalT+1 />
                <td title="${date}"  <#if cd.day==1> style="border-right:2px solid black;" </#if>  > ${crewDay.activity!} </td>
                <#else>
                <td title="${date}"  <#if cd.day==1> style="border-right:2px solid black;" </#if>  > ${crewDay.activity!} </td>
                </#if>
                
                <#else>
                <td title="${date}"  <#if cd.day==1> style="border-right:2px solid black;" </#if>  > </td>
                </#if>
              </#list>
            </#list>
          </#list>
          
          <td colspan="2" style="border:2px solid black">${totalW}W, ${totalT}T</td>
    
          </tr>
        </#list>
        
        <tr style="background-color:${typeColor};">
        <td style="width:150px;background-color:${typeColor};"><div style="width:150px;">&nbsp;</div></td>
        
        <#assign over=0 />
        <#assign totaltype=0 />
        <#list years.keySet()! as year>
            <#assign months= years.get(year) />
            <#list months.keySet()! as month>
              <#assign days= months.get(month) />
              <#list days.keySet()! as day>
                <#assign cd= days.get(day) />
                <#assign total= type.total />
                <#assign date= ""+day+" "+month+" "+year />
                <#if total.get(date)?exists >
                
                <#assign totalday=total.get(date) />
                <#if totalday gt 2>
                <#assign over=over + (totalday - 2) />
                </#if>
                <#assign totaltype=totaltype+totalday />
                
                <td title="${date}"  <#if cd.day==1> style="border-right:2px solid black;" </#if>  > ${totalday} </td>
                <#else>
                <td title="${date}"  <#if cd.day==1> style="border-right:2px solid black;" </#if>  > </td>
                </#if>
              </#list>
            </#list>
          </#list>
          <td>${totaltype}</td>
          <td>${over}</td>
        </tr>
      </#list>
      
      </table>
      <br/>
    </#list>
  </#if>
</#list>


<#else>

<#assign currentUser = Session.userObj>

  <ul>
	<#if currentUser.hasRead("reportMatrix")><li style="width:200px;height:30px;"><a href="script.action?scriptName=aircraftCharterMatrix.groovy&month=${month}&year=${year?c}">Aircraft/Charter Matrix</a></#if>
	<!--<#if currentUser.hasRead("reportCert")><li style="width:200px;height:30px;"><a href="script.action?scriptName=certificate.groovy">Certificates</a></#if>
	<#if currentUser.hasRead("reportMemberProfile")><li style="width:200px;height:30px;"><a href="script.action?scriptName=crewMemberProfiles.groovy">Crew Member Profiles</a></#if>
	<#if currentUser.hasRead("reportLicence")><li style="width:200px;height:30px;"><a href="script.action?scriptName=crewLicences.groovy">Crew Licences</a></#if>-->
	<#if notAuthorised>
	<#else>
	<!--<#if currentUser.hasRead("reportWorked")><li style="width:200px;height:30px;"><a href="script.action?scriptName=crewDayByCharters.groovy&month=${month}&year=${year?c}">Crew Days Worked</a></#if>-->
	<#if currentUser.hasRead("reportDeduction")><li style="width:200px;height:30px;"><a href="script.action?scriptName=crewDeductions.groovy&month=${month}&year=${year?c}">Crew Deductions</a></#if>
	<#if currentUser.hasRead("reportPayments")><li style="width:200px;height:30px;"><a href="script.action?scriptName=crewPayments.groovy&month=${month}&year=${year?c}">Crew Payments</a></#if>
	<#if currentUser.hasRead("reportPaymentsAnalysis")><li style="width:200px;height:30px;"><a href="script.action?scriptName=crewPaymentAnalysis.groovy&month=${month}&year=${year?c}">Crew Payment Analysis</a></#if>
	<#if currentUser.hasRead("reportHours")><li style="width:200px;height:30px;"><a href="script.action?scriptName=hours.groovy">Crew Hours</a></#if> 	
	</#if>
  </ul>

<#if currentUser.hasRead("report183")>
<form name="days" id="days" action="script.action">
<input type="hidden" name="scriptName" value="183days.groovy"/>
<li style="width:200px;height:30px;float:left;"><A href="#" style="width:300px;height:20px;" onclick="validateDays();" >View 183 Days</A></li>
  <div class="fm-opt">
            <span style="color:red">*</span><input id="dateFromDays" onfocus="this.blur();" name="dateFrom" type="text" class="date-pick" value=""/><span style="color:red" id="dateFromMsgDays"></span>
            <span style="color:red">*</span><input id="dateToDays"   onfocus="this.blur();" name="dateTo"   type="text" class="date-pick" value=""/><span style="color:red" id="dateToMsgDays"></span>
  </div>
</form>
<br/>
</#if>

<#if currentUser.hasRead("reportDoc")>
<form name="crewDoc" id="crewDoc" action="script.action">
    <input type="hidden" name="scriptName" value="crewDocumentAnalysis.groovy" />
<li style="width:200px;height:30px;float:left;" ><A href="#" onclick="document.forms.crewDoc.submit();" >View Crew Document Report</A></li>
    <select style="float:left" name="documenttype">
        <option value="">-</option>
        <option value="passport">Passport</option>
        <option value="licence">Licence</option>
        <option value="medical">Medical</option>
        <option value="crm">CRM</option>
        <option value="dg">DG</option>
        <option value="huet">Huet</option>
    </select>
</form>
<br/><br/>
</#if>

<#if currentUser.hasRead("reportProfile")>
<form name="profile" id="profile" action="crewMember!profile.action">
<li style="width:200px;height:30px;float:left;"><A href="#" style="width:300px;height:20px;" onclick="document.forms.profile.submit();" >View Profile</A></li>
<select style="float:left" name="id">
<#list crewMembers! as crew>
<#if crew.code?exists >
<option value="${crew.code!}" > ${crew.personal.fullName?if_exists} (${crew.code!})
</#if>
</#list>
</select>
</form>
<br/><br/>
</#if>

<#if currentUser.hasRead("reportRequired")>
<form name="required" id="required" action="crewMember!required.action">
<li style="width:200px;height:30px;float:left;"><A href="#" style="width:300px;height:20px;" onclick="document.forms.required.submit();" >View Required</A></li>
<select style="float:left" name="id">
<#list crewMembers! as crew>
<#if crew.code?exists >
<option value="${crew.code!}" > ${crew.personal.fullName?if_exists} (${crew.code!})
</#if>
</#list>
</select>
</form>
<br/><br/>
</#if>

<#if currentUser.hasRead("reportOnContract")>
<form name="oncontract" id="oncontract" action="reports!days183.action">
<li style="width:200px;height:30px;float:left;"><A href="#" style="width:300px;height:20px;" onclick="validateOnContract();" >View Days On Contract</A></li>
  <div class="fm-opt">
            <span style="color:red">*</span><input id="dateFrom" onfocus="this.blur();" name="dateFrom" type="text" class="date-pick" value=""/><span style="color:red" id="dateFromMsg"></span>
            <span style="color:red">*</span><input id="dateTo"   onfocus="this.blur();" name="dateTo"   type="text" class="date-pick" value=""/><span style="color:red" id="dateToMsg"></span>
  </div>
</form>
<br/>
</#if>

<#if currentUser.hasRead("reportHoursFlown")>
<form name="hours" id="hours" action="reports!hours.action">
<li style="width:200px;height:30px;float:left;"><A href="#" style="width:300px;height:20px;" onclick="validateHours();" >View Hours Flown</A></li>
  <div class="fm-opt">
  <select style="float:left" name="id">
  <#list crewMembers! as crew>
  <#if crew.code?exists >
    <option value="${crew.code!}" > ${crew.personal.fullName?if_exists} (${crew.code!})
  </#if>
  </#list>
  </select>
            <span style="color:red">*</span><input id="dateFromHours" onfocus="this.blur();" name="dateFrom" type="text" class="date-pick" value=""/><span style="color:red" id="dateFromHoursMsg"></span>
            <span style="color:red">*</span><input id="dateToHours"   onfocus="this.blur();" name="dateTo"   type="text" class="date-pick" value=""/><span style="color:red" id="dateToHoursMsg"></span>
  </div>
</form>
</#if>


</#if>
<div style="height:120px;">&nbsp;</div>
</body>
</html>