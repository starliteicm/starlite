<#include "/starlite.ftl">
<#setting number_format = "######.##########"/>

<html>
<head>
<!--Author: Celeste Groenewald -->

<link rel="stylesheet" type="text/css" href="${request.contextPath}/styles/jmesa.css">

<script type="text/javascript" src="${request.contextPath}/js/jmesa.js"></script>
<script type="text/javascript">
      function onInvokeExportAction(id) 
         {
           <#if tab = "suspendedTickets">
            var parameterString = createParameterStringForLimit(id);
            location.href = '${request.contextPath}/hanger!editSuspend.action?id=${id!}&' + parameterString;
            </#if>
            <#if tab = "closedTickets">
            var parameterString = createParameterStringForLimit(id);
            location.href = '${request.contextPath}/hanger!editClosed.action?id=${id!}&' + parameterString;
            </#if>
    }
</script>


<style  type="text/css" >

div.container {width:98%; margin:1%;}
table#table1 {text-align:center; margin-left:auto; margin-right:auto; width:100px;}
tr,td {text-align:left;}


.buttonStyle {
display: inline;
background-color: #FFFFFF;
background-repeat: no-repeat;
margin: 0px auto;
padding: 0px 0px 0 0;
text-align: center;
font-family: Calibri, Arial, Helvetica, sans-serif;
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
   <#if current="newJob">
   <@subTabs/>
   
    <div style="float:left;padding:5px;margin-left:10px;margin-top:0px;border:1px solid silver;width:1000px;height:180px;valign:left;">
    <div style="margin-left:0px;" >
	
	<form action="hanger!saveTicket.action" enctype="multipart/form-data" method="post">
	<input type="hidden" name="id" value="${id!}"/>
	<input type="hidden" name="ticket.jobTicketID" value="${id!}"/>
     
	    <fieldset>
		<legend>New Job Ticket</legend>
		<div class="fm-opt">
		    <div style="margin-left:-35px;" >
		    <label style="text" for="aircraft.ref">A/C REG:</label>
			<select name="aircraft.ref">
			   <#if aircrafts.isEmpty() = false>
			   <#list aircrafts as ac> 	
			       <option>${ac.ref}
			   </#list>
			   </#if>
			</select>
			</div>
	    </div>	
	    <div class="fm-opt">
		    <div style="margin-left:-35px;" >
		    <label style="text" for="jobTask.jobTaskValue">Job Type:</label>
			<select name="jobTask.jobTaskValue">
			   <#if jobTasks.isEmpty() = false>
			   <#list jobTasks as jt> 	
			       <option>${jt.jobTaskValue}
			   </#list>
			   </#if>
			</select>
			</div>
	    </div>
	    <div class="fm-opt">
		    <div style="margin-left:-35px;" >
		    <label style="text" for="newSubTaskCode">Job Task:</label>
			<select name="newSubTaskCode">
			   <#if jobSubTasks.isEmpty() = false>
			   <#list jobSubTasks as jt> 	
			       <option>${jt.jobSubTaskCode}
    		   </#list>
    		   </#if>
			</select>
			</div>
			<#if jobSubTasks.isEmpty() = false>
			<#list jobSubTasks as jt>
			   <#if jt.jobSubTaskCode == newSubTaskDesc>
			     <input name="jobSubTaskDesc" type="text" value="${jt.jobSubTaskDesc}"/>
			   </#if>
			</#list>
    		</#if>
	    </div>		
	    <div class="fm-opt">
	       <div style="margin-left:193px;" >
			   <button class="smooth" style="margin-left:10px;padding:2px 10px 2px 7px;" onclick="return confirm('Would you like to create a ticket for this task?')" type="submit"><img src="images/icons/add.png"/>Submit</button>
		   </div>	
	    </div>
        </fieldset>
    </form>
    
    </div>
    </div>
   
   </#if>
   
   <!-- WIP TAB -->
    <#if current="WIPTickets">
   
    <@jmesa id="JobTicketWIP"/>     
   </#if>
   
   <!-- SUSPENDED TAB -->
   <#if current="suspendedTickets">
      <@jmesa id="suspendedTickets"/>   
     
   </#if>
   
   <!-- CLOSED TAB -->
   <#if current="closedTickets">
       <@jmesa id="closedTickets"/>
      
   </#if>
   
   </#if>
   
   <!-- ADMINISTRATION TAB -->
   
   <#if user.hasPermission("hangerWrite")>
   <#if current="admin">
   <@subTabs/>
    <div style="float:left;padding:5px;margin-left:10px;margin-top:0px;border:1px solid silver;width:1000px;height:300px;valign:left;">
    <div style="margin-left:0px;" >		    
	    <form action="hanger!saveTask.action" enctype="multipart/form-data" method="post">
	        <fieldset>
		        <legend>Create New Job Type</legend>
		           <div class="fm-opt">
		           <div style="margin-left:-35px;" >
		               <label style="text" for="aircraft.ref">Job Type:</label>
	                   <input type="text" style="float:left" name="newTask" id="newTask"/>
	                   <img class="tooltip" title="<b>Job Type</b><br /><br />The Job Type is the high-level description i.e. 'P4-Servicing'."  style="background-color:transparent;cursor:help;"  src="images/icons/info.png"/>
	               </div>
	               </div>
	               <br/>
	               <div class="fm-opt">
	               <div style="margin-left:197px;" >
	                   <button class="smooth" style="margin-left:10px;margin-top:-25px;padding:2px 10px 2px 7px;" onclick="return confirm('WARNING: Creating a new job cannot be undone - are you sure you want to create a new job?')" type="submit"><img src="images/icons/add.png"/>Submit</button>
	               </div>
	               </div>
	         </fieldset>          
	    </form>
	    <form action="hanger!saveSubTask.action" enctype="multipart/form-data" method="post">
	        <fieldset>
		        <legend>Create New Job Task</legend>
		           <div class="fm-opt">
		           <div style="margin-left:-35px;" >
		               <label style="text" for="newSubTaskCode">Job Task Code:</label>
	                   <input type="text" style="float:left" name="newSubTaskCode" id="newSubTaskCode"/>
	                   <img class="tooltip" title="<b>Job Task</b><br /><br />A Job Task consists out of a code (i.e. '330-P4-PRE-001'), a description and an optional reference number.."  style="background-color:transparent;cursor:help;"  src="images/icons/info.png"/>
	               </div>
	               </div>
	               <br />
	               <div class="fm-opt">
		           <div style="margin-left:-35px;margin-top:-27px;" >
		               <label style="text" for="newSubTaskDesc">Job Task Description:</label>
	                   <input type="text" style="float:left" name="newSubTaskDesc" id="newSubTaskDesc"/>
	               </div>
	               </div>
	                <br />
	               <div class="fm-opt">
		           <div style="margin-left:-35px;" >
		               <label style="text" for="newSubTaskRef">Job Task Reference:</label>
	                   <input type="text" style="float:left" name="newSubTaskRef" id="newSubTaskRef"/>
	               </div>
	               </div>
	               <br/>
	               <div class="fm-opt">
	               <div style="margin-left:197px;" >
	                   <button class="smooth" style="margin-left:10px;padding:2px 10px 2px 7px;" onclick="return confirm('Are you sure you want to create a new task?')" type="submit"><img src="images/icons/add.png"/>Submit</button>
	               </div>
	               </div>
	         </fieldset>          
	    </form>
    </div>
    </div>

   </#if>
   </#if>

    <hr class="clear"/>
    <br />

</div> 
<br/>
</body>
</html>