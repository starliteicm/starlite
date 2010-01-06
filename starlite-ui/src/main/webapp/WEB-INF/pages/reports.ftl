<#setting number_format = "######" />
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
  
</script>

<#if charterMap?exists >

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
                <td title="${date}"  <#if cd.day==1> style="border-right:2px solid black;" </#if>  > </td>
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
  <ul>
	<li style="width:200px;height:30px;"><a href="script.action?scriptName=aircraftCharterMatrix.groovy&month=${month}&year=${year?c}">Aircraft/Charter Matrix</a>
	<li style="width:200px;height:30px;"><a href="script.action?scriptName=certificate.groovy">Certificates</a>
	<li style="width:200px;height:30px;"><a href="script.action?scriptName=crewMemberProfiles.groovy">Crew Member Profiles</a>
	<li style="width:200px;height:30px;"><a href="script.action?scriptName=crewLicences.groovy">Crew Licences</a>
	<#if notAuthorised>
	<#else>
	<li style="width:200px;height:30px;"><a href="script.action?scriptName=crewDayByCharters.groovy&month=${month}&year=${year?c}">Crew Days On Contract</a>
	<li style="width:200px;height:30px;"><a href="script.action?scriptName=crewDeductions.groovy&month=${month}&year=${year?c}">Crew Deductions</a>
	<li style="width:200px;height:30px;"><a href="script.action?scriptName=crewPayments.groovy&month=${month}&year=${year?c}">Crew Payments</a>
	<li style="width:200px;height:30px;"><a href="script.action?scriptName=crewPaymentAnalysis.groovy&month=${month}&year=${year?c}">Crew Payment Analysis</a>
	</#if>
  </ul>

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
<form name="profile" id="profile" action="crewMember!profile.action">
<li style="width:200px;height:30px;float:left;"><A href="#" style="width:300px;height:20px;" onclick="document.forms.profile.submit();" >View Profile</A></li>
<select style="float:left" name="id">
<#list crewMembers! as crew>
<option value="${crew.code?if_exists}" > ${crew.personal.fullName?if_exists}
</#list>
</select>
</form>
<br/><br/>
<form name="oncontract" id="oncontract" action="reports!days183.action">
<li style="width:200px;height:30px;float:left;"><A href="#" style="width:300px;height:20px;" onclick="validateOnContract();" >View On Contract</A></li>
  <div class="fm-opt">
            <span style="color:red">*</span><input id="dateFrom" onfocus="this.blur();" name="dateFrom" type="text" class="date-pick" value=""/><span style="color:red" id="dateFromMsg"></span>
            <span style="color:red">*</span><input id="dateTo"   onfocus="this.blur();" name="dateTo"   type="text" class="date-pick" value=""/><span style="color:red" id="dateToMsg"></span>
  </div>
</form>
</#if>
<div style="height:120px;">&nbsp;</div>
</body>
</html>