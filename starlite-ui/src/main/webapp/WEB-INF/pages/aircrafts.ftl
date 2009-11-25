<html>
<head>
	<script type="text/javascript" src="js/itao/starlite/widget/tables.js"></script>
	<script>
		YAHOO.util.Event.onContentReady("aircraftTable", function() {
			new ITAO.starlite.widget.AircraftTable("aircraftTable", "http://localhost:8118/starlite/aircraft");
		});
	</script>
</head>
<body>
	<h2>Aircraft</h2>
	<div id="aircraftTable"></div>
</body>
</html>