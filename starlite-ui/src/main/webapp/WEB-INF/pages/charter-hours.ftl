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
</script>
</head>
<body>
	<@subTabs/>
	<div style="clear:left; border: 1px solid silver; padding: 10px;">
	${tableHtml}
	</div>
</body>
</html>