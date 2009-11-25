<#include "/starlite.ftl">
<html>
<head>
  <title>Crew Reports</title>
  <link rel="stylesheet" type="text/css" href="${request.contextPath}/styles/muffin.css">
</head>

<body>
	
	<div style="float:left; width: 700px;">
		<fieldset>
			<legend>Reports</legend>
            <table>
            <tr><td><a href="crew!expiryReport.action">Expiry Report</a></td></tr>
            <tr><td><a href="crew!licenceReport.action">Licence Report</a></td></tr>
            <tr><td><a href="crew!certificateReport.action">Certificate Report</a></td></tr>
            <tr><td><a href="crew!hoursReport.action">Crew Payments Report</a></td></tr>
            </table>
        </fieldset>
    </div>

</body>
</html>