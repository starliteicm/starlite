<#include "/starlite.ftl">
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
		
  	<@enableDatePickers/>
  	<link rel="stylesheet" type="text/css" href="${request.contextPath}/styles/jquery.autocomplete.css">
  	<script type="text/javascript" src="${request.contextPath}/js/jquery.autocomplete.js"></script>
		<title>Schedule</title>
		<script>
			var model = new ITAO.widget.roster.Model("services/schedule/crewMember/${crewMember.id}");
		
			YAHOO.util.Event.onContentReady("rosterDiv", function() {
				var d = new Date();				
				var roster = new ITAO.widget.roster.Roster("rosterDiv", model, d, 'week');
				var controller = new ITAO.widget.roster.Controller(roster, model);
				
				YAHOO.util.Event.addListener("zoomInButton", "click", roster.zoomIn);
				YAHOO.util.Event.addListener("zoomOutButton", "click", roster.zoomOut);
				YAHOO.util.Event.addListener("nextButton", "click", roster.showNext);
				YAHOO.util.Event.addListener("previousButton", "click", roster.showPrevious);
				YAHOO.util.Event.addListener("todayButton", "click", roster.today);
				
				YAHOO.util.Event.addListener("selectButton", "click", controller.selectMode);
				YAHOO.util.Event.addListener("addButton", "click", controller.insertMode);
				YAHOO.util.Event.addListener("deleteButton", "click", function() {
					var selectedBar = controller.getSelected();
					if (selectedBar) {
						var ud = selectedBar.userData;
						if (instanceOf(ud, ITAO.widget.roster.Allocation)) {
							//TODO FIX. Should be just ud.getId(), but there is an error somewhere. Temp Fix
							var allocationId = ud.getId().id;
							model.removeAllocation(ud);
							YAHOO.util.Connect.asyncRequest("DELETE", "services/schedule/allocation/"+allocationId);
						}
					}
				});
			});
		</script>
	</head>
	
	<body>
		<div id="toolbar">
			<button id="zoomInButton" style="float: left;"><img src="images/icons/zoom_in.png"/>Zoom In</button>
			<button id="zoomOutButton" style="float: left;"><img src="images/icons/zoom_out.png"/>Zoom Out</button>

			<div class="separator"> </div>

			<button id="todayButton" style="float: left;"><img src="images/icons/date.png"/>Today</button>
			<button id="previousButton" style="float: left;"><img src="images/icons/resultset_previous.png"/>Previous</button>
			<button id="nextButton" style="float: left;"><img src="images/icons/resultset_next.png"/>Next</button>
			
			<div class="separator"> </div>
			
			<button id="deleteButton" style="float: left;"><img src="images/icons/delete.png"/>Delete</button>
			
			<hr class="clear"/>
		</div>
		<br/>
		<@subTabs/>
		<div style="clear:left; border:1px solid silver; padding:10px;">
		<div id="rosterDiv"></div>
		<br/>
		</div>
	</body>
</html>
