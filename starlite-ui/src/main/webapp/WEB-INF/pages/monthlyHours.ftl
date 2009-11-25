<#include "/starlite.ftl">
<html>
<head>
	<link rel="stylesheet" type="text/css" href="styles/jmesa.css">
	<script type="text/javascript" src="js/jquery-1.2.3.min.js"></script>
	<script type="text/javascript" src="js/jmesa.js"></script>
	<script type="text/javascript">
	        $(document).ready(function() {
	           addDropShadow('images/table/');
	        });
	        function onSubmitWsColumn() {
				document.getElementById("saveButton").disabled = false;
			}
			var saving = false;
			
			function checkSaveBeforeUnload(oEvent) {
				if (!saving && !document.getElementById('saveButton').disabled) {
					return "Changes have not been saved.";
				}
			}
			
			window.onbeforeunload = checkSaveBeforeUnload;
	</script>
	<title>Monthly Hours - ${monthName}, ${year?string("####")}</title>
</head>

<body>
	<#if approvalGroup?? && approvalGroup.approvalStatus.toString() == "UNDER_REVIEW">
		<h3>Locked</h3>
	</#if>
	
	<#if approvalGroup?? && approvalGroup.approvalStatus.toString() == "APPROVED">
		<h3>Approved</h3>
	</#if>
	
	<#if user.hasPermission("Approve") && approvalGroup??>
	<div id="toolbar">
		<#if approvalGroup.approvalStatus.toString() == "OPEN_FOR_EDITING">
		<a href="${request.contextPath}/monthlyHours!review.action?aircraftId=${aircraftId}&year=${year}&month=${month}"><img src="${request.contextPath}/images/icons/lock.png"/>Begin Approval</a>
		</#if>
		<#if approvalGroup.approvalStatus.toString() == "UNDER_REVIEW">
		<a href="${request.contextPath}/monthlyHours!approve.action?aircraftId=${aircraftId}&year=${year}&month=${month}"><img src="${request.contextPath}/images/icons/accept.png"/>Approve</a>
		<a href="${request.contextPath}/monthlyHours!open.action?aircraftId=${aircraftId}&year=${year}&month=${month}"><img src="${request.contextPath}/images/icons/lock_open.png"/>Re-Open</a>
		</#if>
		<#if approvalGroup.approvalStatus.toString() == "APPROVED">
		<a href="${request.contextPath}/monthlyHours!open.action?aircraftId=${aircraftId}&year=${year}&month=${month}"><img src="${request.contextPath}/images/icons/lock_open.png"/>Re-Open</a>
		</#if>
		<hr class="clear"/>
	</div>
	<br/>
	</#if>

	<#assign id="monthlyHours"+aircraft.ref+monthName+year?string('####')/>
	<div id="toolbar">
		<!--button id="addButton"><img src="images/icons/add.png"/>Add</button-->
		<button id="saveButton" onclick="javascript:saving=true;setSaveToWorksheet('${id}Table');onInvokeAction('${id}Table','save_worksheet')" ${saveDisabled!}><img src="images/icons/pencil.png"/>Save</button>
		<div class="separator"> </div>
		<a href="${previousLink}"><img src="images/icons/resultset_previous.png"/>Previous</a>
		<a href="${nextLink}"><img src="images/icons/resultset_next.png"/>Next</a>
		<hr class="clear"/>
	</div>
	<!--a href="${previousLink}">Previous</a> <a href="${nextLink}">Next</a-->
	<h1>${monthName}, ${year?string("####")}</h1>
	<h2>${aircraftCode}</h2>
	
	<form name="${id}Form" action="${request.requestURL}${params!}">
		<div id="${id}">
			${tableHtml}
		</div>
		<input type="hidden" name="aircraftId" value="${aircraftId}"/>
		<input type="hidden" name="month" value="${month}"/>
		<input type="hidden" name="year" value="${year?string('####')}"/>
	</form>

	<script type="text/javascript">
	function onInvokeAction(id) {
		setExportToLimit(id, '');
	    createHiddenInputFieldsForLimitAndSubmit(id);
	}
	</script>
</body>
</html>
