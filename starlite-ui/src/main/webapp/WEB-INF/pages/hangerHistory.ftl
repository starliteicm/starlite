<#include "/starlite.ftl">
<#setting number_format = "######.##########"/>

<html>
<head>
<!--Author: Celeste Groenewald -->

<link rel="stylesheet" type="text/css" href="${request.contextPath}/styles/jmesa.css">

<script type="text/javascript" src="${request.contextPath}/js/jmesa.js"></script>
<script type="text/javascript">
        $(document).ready(function() {
           addDropShadow('images/table/');
        });
        
        function onSubmitWsColumn() {
            document.getElementById("saveButton").disabled = false;
        }
        
        function onInvokeExportAction(ticketID) {
            //For the Table Export Buttons to CSV and Excel
            //Currently not working
            
            var parameterString = createParameterStringForLimit(id);
            location.href = '${request.contextPath}/hanger.action?' + parameterString;
        }
</script>
</script>

<style  type="text/css" >

div.container {width:98%; margin:1%;}
table#table1 {text-align:center; margin-left:auto; margin-right:auto; width:100px;}
tr,td {text-align:left;}


.buttonStyle {
display: inline;
background-color: transparent;
background-repeat: no-repeat;
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
<title>Hanger Management</title>
</head>


<@enableHelp/>

 
  
<body>

<div id="toolbar">   

  
        
   <#if user.hasPermission("hangerRead")>
   <#if current="tickets">
   <@subTabs/>
   
    <div style="float:left;padding:5px;margin-left:10px;margin-top:0px;border:1px solid silver;width:1000px;height:300px;valign:left;">
    <div style="margin-left:0px;" >
	
	<form action="hangerHistory!updateTicket.action?id=${id!}" enctype="multipart/form-data" method="post">
     
        <div style="float:left; width: 360px;">
	    <fieldset>
		<legend>General</legend>
		<#assign block = 5>
		<div class="fm-opt">
		    <div style="margin-left:-35px;" >
		    <label style="text" for="jobTicket.jobTicketID">ID:</label>
			<input name="jobTicket.jobTicketID" type="text" value="${jobTicket.jobTicketID}" readonly="readonly"  STYLE=" background-color: #F2F2F2;"  size="${block}"/>
			</div>
	    </div>	
	    <div class="fm-opt">
		    <div style="margin-left:-35px;" >
		    <label style="text" for="jobTicket.assignedTo.personal.fullName">AME:</label>
			<input name="jobTicket.assignedTo.personal.fullName" type="text" value="${jobTicket.assignedTo.personal.fullName}" readonly="readonly"  STYLE=" background-color: #F2F2F2;"  size="${block}"/>
			</div>
	    </div>	
	    <div class="fm-opt">
	       <div style="margin-left:-35px;" >
			<label style="text" for="jobTicket.aircraft.ref">Aircraft:</label>
			<input name="jobTicket.aircraft.ref" type="text" value="${jobTicket.aircraft.ref}" readonly="readonly"  STYLE=" background-color: #F2F2F2;"  size="${block}"/>
		   </div>	
	    </div>
	    <div class="fm-opt">
	       <div style="margin-left:-35px;" >
			<label style="text" for="jobTicket.jobTask.jobTaskValue">Job:</label>
			<input name="jobTicket.jobTask.jobTaskValue" type="text" value="${jobTicket.jobTask.jobTaskValue}" readonly="readonly"  STYLE=" background-color: #F2F2F2;"  size="${block}"/>
		   </div>	
	    </div>
	    <div class="fm-opt">
	       <div style="margin-left:-35px;" >
			<label style="text" for="jobTicket.jobSubTask">Task:</label>
			<input name="jobTicket.jobSubTask" type="text" value="${jobTicket.jobSubTask}" readonly="readonly"  STYLE=" background-color: #F2F2F2;"  size="${block}"/>
		   </div>	
	    </div>
	    
        </fieldset>
        </div>
     
     <!-- ADMINISTRATIVE -->
     
        <div style="float:left; width: 360px;">
        <fieldset>
        <legend>Administrative</legend>
		<#assign block = 5>
		<div class="fm-opt">
	       <div style="margin-left:-35px;" >
			<label style="text" for="jobTicket.jobTicketStatus.jobStatusValue">Current Status:</label>
			<input name="jobTicket.jobTicketStatus.jobStatusValue" type="text" value="${jobTicket.jobTicketStatus.jobStatusValue}"  readonly="readonly"  STYLE=" background-color: #F2F2F2;"   size="${block}"/>
		   </div>	
	    </div>
        <div class="fm-opt">
	       <div style="margin-left:-35px;" >
			<label style="text" for="hours">Total Hours Worked:</label>
			<input name="hours" type="text" value="${time!}" readonly="readonly"  STYLE=" background-color: #F2F2F2;text-align:right;" size="${block}"/>
		   </div>	
	    </div>
	    
	    <#if user.hasPermission("hangerWrite")>
	    <div class="fm-opt">
	       <div style="margin-left:-35px;" >
			<label style="text" for="newHours">New Hours:</label>
			<input name="newHours" type="text" value="${newHours!}"  STYLE=" background-color: #FFFFFF;text-align:right;" size="${block}"/>
		   </div>	
	    </div>
	    <div class="fm-opt">
	       <div style="margin-left:-35px;" >
			<label style="text" for="newMins">New Minutes</label>
			<input name="newMins" type="text" value="${newMins!}"   STYLE=" background-color: #FFFFFF;text-align:right;" size="${block}"/>
		   </div>
		<div class="fm-opt">
	       <div style="margin-left:-35px;" >
			<label style="text" for="reasonForUpdate">Reason for update:</label>
			<input name="reasonForUpdate" type="text" value="${reasonForUpdate!}"  STYLE=" background-color: #FFFFFF;text-align:right;" size="${block}"/>
		   </div>		
	    </div>
	
	    <div style="margin-left:197px;" >
	        <button class="smooth" style="margin-left:10px;padding:2px 10px 2px 7px;" onclick="return confirm('Would you like to save these changes?')" type="submit"><img src="images/icons/add.png"/>Submit</button>
	    </div>
	    </#if>
	        
	    </fieldset>
	     </div>
    
         
    </form>
    
    </div>
    </div>
   
   </#if>
   
   <!-- History TAB -->
    <#if current="history">
     <@jmesa id="JobTicketHistory"/>     
   </#if>
   
    <!-- History EDIT TAB -->
    <#if current="editHistory">
     <@jmesa id="JobTicketHistoryEdit"/>     
   </#if>
   
   </#if>

    <hr class="clear"/>
    <br />

</div> 
<br/>
</body>
</html>