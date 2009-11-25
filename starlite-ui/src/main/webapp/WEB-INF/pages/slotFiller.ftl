<html>
<head>
	<link rel="stylesheet" type="text/css" href="js/yui_2.4.1/yui/build/reset-fonts-grids/reset-fonts-grids.css">
	<link rel="stylesheet" type="text/css" href="js/yui_2.4.1/yui/build/assets/skins/sam/skin.css">
	<link rel="stylesheet" type="text/css" href="styles/slotFiller.css">
	
	<script type="text/javascript" src="js/yui_2.4.1/yui/build/utilities/utilities.js"></script> 
	<!-- Dependencies --> 
	<script type="text/javascript" src="js/yui_2.4.1/yui/build/container/container_core-min.js"></script>
	<script type="text/javascript" src="js/yui_2.4.1/yui/build/element/element-beta-min.js"></script>
	<script type="text/javascript" src="js/yui_2.4.1/yui/build/datasource/datasource-beta-min.js"></script>

	<!-- Source File -->
	<script type="text/javascript" src="js/yui_2.4.1/yui/build/menu/menu-min.js"></script> 
	<script type="text/javascript" src="js/yui_2.4.1/yui/build/container/container-min.js"></script>
	

	
	<script type="text/javascript" src="js/itao/itao.js"></script>
	<script type="text/javascript" src="js/itao/starlite/widget/dd.js"></script>
	
	<script>
		YAHOO.util.Event.onContentReady("crewFiller", function() {
			var slots = [
				{label:"Captain", id:"captain"},
				{label:"Co-Pilot", id:"co-pilot"},
				{label:"Engineer", id:"engineer"}
			];
			
			var availableElements = [
				{label:"Capt Leon Kieser", id:1},
				{label:"Capt Eugene Viljoen", id:2},
				{label:"Daniel Jansen", id:3},
				{label:"Adriaan Pienaar", id:4},
				{label:"Daniel Erasmus", id:5},
				{label:"Andries van Coller", id:6},
				{label:"Wynand Bezuidenhout", id:7},
				{label:"Deon Pretorius", id:8},
				{label:"Phillipus van Coller", id:9},
				{label:"Willem Beaurain", id:10}
			];
			new ITAO.starlite.widget.dd.SlotFiller("crewFiller", slots, "Available Crew", availableElements);
		});
		
		YAHOO.util.Event.onContentReady("aircraftFiller", function() {
			var slots = [
				{label:"Aircraft 1", id:"aircraft1"},
				{label:"Aircraft 2", id:"aircraft2"},
			];
			
			var availableElements = [
				{label:"AGB154", id:1},
				{label:"FLO294", id:2},
				{label:"DKO200", id:3},
				{label:"DKK203", id:4},
				{label:"PSL203", id:5}
			];
			new ITAO.starlite.widget.dd.SlotFiller("aircraftFiller", slots, "Available Aircraft", availableElements);
		});
	</script>
</head>

<body class="yui-skin-sam">
	<div id="doc3">
		<div id="bd">
			<div id="crewFiller"></div>
			<div id="aircraftFiller"></div>			
		</div>
	</div>
</body>

</html>