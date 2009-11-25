<html>
<head>
	<script type="text/javascript" src="js/itao/starlite/widget/tables.js"></script>
	<script>
		YAHOO.util.Event.onContentReady("charterTable", function() {
			var data = [
				{id:1,client:"Client",description:"Description"}
			];
			var dataSource = new YAHOO.util.DataSource(data);
			dataSource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY;
			dataSource.responseSchema = {
				fields:["id","client","description"]
			};
			new ITAO.starlite.widget.CharterTable("charterTable", "http://localhost:8118/starlite/charters", dataSource);
		});
	</script>
</head>
<body>
	<h2>Charters</h2>
	<div id="charterTable"></div>
	<a href="charter.action">[New Charter]</a>
</body>
</html>