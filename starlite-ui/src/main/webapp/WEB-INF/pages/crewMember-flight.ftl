<#include "/starlite.ftl">
<html>
<head>
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/styles/jmesa.css">
	<script type="text/javascript" src="${request.contextPath}/js/jquery-1.2.3.min.js"></script>
	<script type="text/javascript" src="${request.contextPath}/js/jmesa.js"></script>
	<script type="text/javascript" src="${request.contextPath}/js/tooltip.js"></script>
	<script type="text/javascript" src="${request.contextPath}/js/jquery.dimensions.js"></script>
	
	
	<script type="text/javascript">
        $(document).ready(function() {
        	$('.tooltip').tooltip({ 
    			track: true, 
    			delay: 10, 
    			showURL: false, 
    			showBody: " - ", 
    			extraClass: "pretty", 
    			fixPNG: true, 
    			opacity: 0.95,
    			top: 100,
    			left: 30
			});
           addDropShadow('images/table/');
        });
	</script>
    <@enableDatePickers/>
    
    <style type="text/css">
    #tooltip.pretty {
    position:absolute;
    margin-top:80px;
	font-family: Arial;
	border: none;
	width: 210px;
	padding:20px;
	height: 135px;
	opacity: 0.9;
	background: url('images/shadow.png');
}
#tooltip.pretty h3 {
	margin-bottom: 0.75em;
	font-size: 9pt;
	width: 210px;
	text-align: center;
}
#tooltip.pretty div { width: 210px; text-align: left; }
    </style>
    
	<title>${crewMember.personal.firstName!} ${crewMember.personal.lastName!}</title>
</head>

<body>
	<@subTabs/>

	<#if notAuthorised>
	<form action="#" method="POST" style="padding:20px;" class="smart readonly">
	<fieldset>
		<legend>Not Authorised</legend>
		<br/>
	</fieldset>
	</form>
	<#else>

	<div style="border: 1px solid silver; padding: 10px; clear:left;">
	<#if user.hasPermission("ManagerEdit")>
		<a href="${request.contextPath}/crewMember!addFlightActuals.action?tab=flight&id=${id}">Add Entry</a>
		<br/><br/>
	</#if>
		<form name="crewFlightHoursForm" action="${request.requestURL}${params!}">
		<input type="hidden" name="tab" value="flight"/>
		<input type="hidden" name="id" value="${id}"/>
			<div id="crewFlightHours">
				${tableHtml}
			</div>
		</form>
		
		<div style="width:500px; border: 1px solid silver; padding: 10px; clear:left;">
		<form action="crew!payPDF.action" method="POST" target="_blank">
		<input name="ids" type="hidden" value="${crewMember.id}"/>
		  <img class="tooltip" title="<br/> If you are getting a blank page when exporting you may be selecting a time period for which there are no entries"  style="cursor:help;position:static;float:right;" style="cursor:help;position:relative;float:right;" src="images/icons/info.png"/><button type="submit" class="smooth" style="position:relative;float:right;top:-4px;"><img src="images/icons/page_white_acrobat.png"/>Export</button>
		  <label for="dateFrom">Export Pay Advice:</label>
		  <input name="dateFrom" type="text" class="date-pick" value=""/>
    	  <input name="dateTo" type="text" class="date-pick" value=""/>
    	  
    	</form>
    	</div>
    	
		<br/><br/>
		
		<div style="width:500px; border: 1px solid silver; padding: 10px; clear:left;">
		<form action="crew!payPDF.action" method="POST" target="_blank">
		 <input name="ids" type="hidden" value="${crewMember.id}"/>
		  <img class="tooltip" title="If you are getting a blank page when exporting you may be either selecting a month where there is not an entry <br/>  or <br/>   You may be selecting a month in which the entry has not yet had a date & amount paid set." style="cursor:help;position:static;float:right;" src="images/icons/info.png"/><button type="submit" class="smooth" style="position:relative;float:right;top:-4px;"><img src="images/icons/page_white_acrobat.png"/>Export</button>
		  <label for="date">Export Pay Slip:</label>
		  <input name="date" type="text" class="date-pick" value=""/>
		</form>
	    </div>
	    
	    <br/><br/>

		<script type="text/javascript">
			function onInvokeAction(id) {
			setExportToLimit(id, '');
    		createHiddenInputFieldsForLimitAndSubmit(id);
		}
		</script>
	</div>


	</#if>
</body>
</html>