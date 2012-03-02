<#include "/starlite.ftl">
<html>
<head>
<link rel="stylesheet" type="text/css" href="styles/jmesa.css">
<script type="text/javascript" src="js/jmesa.js"></script>
<script type="text/javascript">
        $(document).ready(function() {
           addDropShadow('images/table/');
        });
</script>
<style>
.jmesa tr td {
    text-align:right;
}
.jmesa tr.header td {
    text-align:left;
}
.jmesa tr td a {
    float:left;
}
</style>
</head>
<body>
	<@subTabs/>
	<div style="clear:left; border: 1px solid silver; padding: 10px;">
	${tableHtml}
	</div>
</body>
</html>