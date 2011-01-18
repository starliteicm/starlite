<#include "/starlite.ftl">
<#setting number_format = "######.##########"/>

<html>
<head>
<!--Author: Celeste Groenewald -->
<@enableHelp/>
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
            
            var parameterString = createParameterStringForLimit(ticketID);
            location.href = '${request.contextPath}/hangerHistory.action?' + parameterString;
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




table.pretty {
  margin: 1em 1em 1em 2em;
  background: whitesmoke;
  border-collapse: collapse;
  text-align:center;

}
table.pretty2 {
  margin: 0.1em 0.1em 0.1em 0.1em;
  background: whitesmoke;
  border-collapse: collapse;
  text-align:center;
  margin-left:auto; 
  margin-right:auto;

}
table.pretty th {
  border: 2px silver solid;
  padding: 0.1em;
  text-align:center;
  font-weight:bold;
}
table.pretty td {
  border: 2px silver solid;
  padding: 0.1em;
  background: white;
  text-align:center;

    width: 100px;
  height: 30px;
}


table.pretty2 td {
  border: 0px silver solid;
  padding: 0.1em;
  text-align:center;
      width: 100px;
  height: 30px;
}
table.pretty th {
  background: gainsboro;
  text-align:center;
 font-family:"Verdana", Times, serif;
 font-size:1.0em; 
text-align:center;
}
table.pretty caption {
  margin-left: inherit;
  margin-right: inherit;
}

</style>
<title>Hanger Management</title>
</head>
<body>
<#assign domain = "starlite">


<div id="toolbar">   

  
        
   <#if user.hasPermission("hangerRead")>
   
   <#if (isNotACrewMember = false) >
    <div style="margin-left:150px;">
    <form action="hangerHistory.action" enctype="multipart/form-data" method="post">
    <button class="smooth" style="margin-left:10px;padding:2px 10px 2px 7px;" onclick="Test('Button One')" type="submit"><img src="images/icons/view_icon.png"/>View Job History</button>
    </form>
   </#if>
       
    <#if user.hasPermission("hangerWrite")>
    <div style="margin-left:250px;" >
    <form action="hanger!saveTask.action" enctype="multipart/form-data" method="post">
    <input type="text" style="float:left" name="newTask" id="newTask"/>
    <button class="smooth" style="margin-left:10px;padding:2px 10px 2px 7px;" onclick="return confirm('WARNING: Adding a new task cannot be undone - are you sure you want to add a new task?')" type="submit"><img src="images/icons/add.png"/>Add New Task</button>
    </form>
    </#if>
       </div>
    </div>
  
    
    <hr class="clear"/>
    
    <br />


    
        
    <div style="margin-left:0px;">
    <#if (isNotACrewMember = false) >
    <form action="hanger!updateStatus.action" enctype="multipart/form-data" method="post">
    
    <div style="margin-left:133px;">
    <div class="smooth" style="margin-left:0px;padding:1px 1px 1px 1px;">
    
     <div class="dropShadow">

     <table class="pretty" >          
      <tr>
      <td style="text-align:center;">
          <img class="tooltip" title="<b>Hanger Management:</b><br/>To WORK on a job, please select the corresponding <b><i>green</i></b> button.<br/>To SUSPEND a job, please select the corresponding <b><i>orange</i></b> button. <br/>To CLOSE a job, please select the corresponding <b><i>blue</i></b> button. Please note that once a job is closed, it cannot be re-opened."  style="background-color:transparent;cursor:help;"  src="images/icons/info.png"/>
     
       </td>
      <#list jobTasks as x>
             <th width="125">${x.getJobTaskValue()}</th>
      </#list>
      </tr>
          <#assign aircraftCount=0>
	      <#list aircrafts as y>
		            <tr>
		               <th width="100">${y.getRef()}</th>
		               <#assign taskCount=0>
		                <#list jobTasks as xx>
			               <td> 
			                 <table class="pretty2" >
					            <tr>
					              
					                <#list jobTicketMatrix as ticket>
					                    <#list ticket.getJobStatusButtons() as button>
					                     <#if (ticket.getPosX() == taskCount) && (ticket.getPosY() == aircraftCount)>
					                     <td width="30" height="30" >
					                     <button class="buttonStyle" style="background-image:url(images/hanger/${button.getCurBtnImgPath()});width:${button.getCurBtnWidth()+2}px;height:${button.getCurBtnHeight()+2}px" name="selectedValue" value="${ticket.getJobTicketID()},${button.getJobStatusBtnID()}" onclick="return confirm('Are you sure you want to ${button.getCurBtnStatusValue()} this job?')" type="submit"  />
					                    </td>

					                    </#if>
					                    
					                    </#list>
					                 </#list>  
							      
  			                    </tr>				                
			                 </table>	              				           
			               </td>
			              <#assign taskCount = taskCount+1>
			            </#list>
			            
		            </tr>
		  <#assign aircraftCount=aircraftCount+1>          
	      </#list>
    </table>

    </div>
    </div>
    </div>
     </form>
     </#if>
    </div> 
  
  </#if>
</div>
<br/>

 
</body>
</html>