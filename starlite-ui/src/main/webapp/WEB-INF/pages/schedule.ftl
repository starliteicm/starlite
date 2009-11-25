<html>
	<head>
		<link rel="stylesheet" type="text/css" href="https://secure.i-tao.com/scripts/yui/build/assets/skins/sam/skin.css">
		<script type="text/javascript" src="https://secure.i-tao.com/scripts/yui/build/utilities/utilities.js"></script>
		<script type="text/javascript" src="https://secure.i-tao.com/scripts/yui/build/container/container_core-min.js"></script>
		<script type="text/javascript" src="https://secure.i-tao.com/scripts/yui/build/event/event-min.js"></script>
		<script type="text/javascript" src="https://secure.i-tao.com/scripts/yui/build/connection/connection-min.js"></script>  
		<script type="text/javascript" src="https://secure.i-tao.com/scripts/yui/build/menu/menu-min.js"></script>
		<script type="text/javascript" src="https://secure.i-tao.com/scripts/yui/build/datasource/datasource-beta-min.js"></script>
		<script type="text/javascript" src="https://secure.i-tao.com/scripts/yui/build/container/container-min.js"></script>
		
		<script type="text/javascript" src="js/itao/itao.js"></script> 	
		<script type="text/javascript" src="js/itao/util/date.js"></script>
		<script type="text/javascript" src="scripts/itao/util/xmlhelper.js"></script>
	
		<link rel="stylesheet" type="text/css" href="styles/roster.css">
		<script type="text/javascript" src="js/itao/widget/roster/model.js"></script>
		<script type="text/javascript" src="js/itao/widget/roster/view.js"></script>
		<script type="text/javascript" src="js/itao/widget/roster/controller.js"></script>
		<title>Schedule</title>
		<script>
			var model = new ITAO.widget.roster.Model("services/schedule");
		
			YAHOO.util.Event.onContentReady("rosterDiv", function() {
				var d = new Date();
				//d.setTime(Date.parse("2008/03/17"));
				var roster = new ITAO.widget.roster.Roster("rosterDiv", model, d, 'week');
				var controller = new ITAO.widget.roster.Controller(roster, model);
				
				YAHOO.util.Event.addListener("zoomInButton", "click", roster.zoomIn);
				YAHOO.util.Event.addListener("zoomOutButton", "click", roster.zoomOut);
				YAHOO.util.Event.addListener("nextButton", "click", roster.showNext);
				YAHOO.util.Event.addListener("previousButton", "click", roster.showPrevious);
				YAHOO.util.Event.addListener("todayButton", "click", roster.today);
				
				YAHOO.util.Event.addListener("selectButton", "click", controller.selectMode);
				YAHOO.util.Event.addListener("addButton", "click", controller.insertMode);
				YAHOO.util.Event.addListener("deleteButton", "click", controller.deleteMode);
			});
		</script>
	</head>
	
	<body>
		<div id="toolbar">
		<!--button id="selectButton" style="float: left;"><img src="images/icons/mouse.png"/>Select</button-->
<!--button id="addButton" style="float: left;"><img src="images/icons/add.png"/>Add</button>
<button id="deleteButton" style="float: left;"><img src="images/icons/delete.png"/>Delete</button>
<button id="saveButton" style="float: left;"><img src="images/icons/tick.png"/>Save</button>

<div style="float:left;width:4px;height:27px;margin-right:7px;background-color:#dedede;">&nbsp;</div-->

<button id="zoomInButton" style="float: left;"><img src="images/icons/zoom_in.png"/>Zoom In</button>
<button id="zoomOutButton" style="float: left;"><img src="images/icons/zoom_out.png"/>Zoom Out</button>

<div class="separator"> </div>

<button id="todayButton" style="float: left;"><img src="images/icons/date.png"/>Today</button>
<button id="previousButton" style="float: left;"><img src="images/icons/resultset_previous.png"/>Previous</button>
<button id="nextButton" style="float: left;"><img src="images/icons/resultset_next.png"/>Next</button>
<!--img style="float: right; cursor:pointer;" src="images/icons/resultset_next.png"/>
<img style="float: right; cursor:pointer;" src="images/icons/resultset_previous.png"/-->
<hr class="clear"/>
</div>
<br/>

		<div id="rosterDiv"></div>
	</body>

</html>
