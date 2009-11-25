<html>
<head>
	<script type="text/javascript" src="js/itao/itao.js"></script>
	<script type="text/javascript" src="js/yui_2.4.1/yui/build/utilities/utilities.js"></script> 
	<link rel="stylesheet" type="text/css" href="js/yui_2.4.1/yui/build/reset-fonts-grids/reset-fonts-grids.css">
	<link rel="stylesheet" type="text/css" href="styles/slotFiller.css">
	
	<!--aggregate file path for YUI Sam Skin CSS for UI controls--> 
	<link rel="stylesheet" type="text/css" href="js/yui_2.4.1/yui/build/assets/skins/sam/skin.css">
	<!-- Dependencies --> 
	<script type="text/javascript" src="js/yui_2.4.1/yui/build/container/container_core-min.js"></script>
	<script type="text/javascript" src="js/yui_2.4.1/yui/build/element/element-beta-min.js"></script>
	<script type="text/javascript" src="js/yui_2.4.1/yui/build/datasource/datasource-beta-min.js"></script>

	<!-- Source File -->
	<script type="text/javascript" src="js/yui_2.4.1/yui/build/menu/menu-min.js"></script>
	<script type="text/javascript" src="js/yui_2.4.1/yui/build/datatable/datatable-beta-min.js"></script> 
	<script type="text/javascript" src="js/yui_2.4.1/yui/build/container/container-min.js"></script>
	
	<link rel="stylesheet" type="text/css" href="styles/default.css">
	<script type="text/javascript" src="js/yui_2.4.1/yui/build/calendar/calendar-min.js"></script>
	<script type="text/javascript" src="http://yui.yahooapis.com/2.4.1/build/tabview/tabview-min.js"></script>
	
		<script type="text/javascript" src=" js/itao/starlite/widget/calendars.js"></script>

	<script type="text/javascript" src="js/itao/starlite/widget/dragAndDropList.js"></script>
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
				{label:"Aircraft 2", id:"aircraft2"}
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
	
		
		var cal;
		YAHOO.util.Event.onContentReady("calendarDiv", function() {
			cal = new ITAO.starlite.widget.DateRangePicker("calendarDiv");
		});
		
		YAHOO.util.Event.onContentReady("demo", function() {
			new YAHOO.widget.TabView("demo");
		});
	</script>
	
	
</head>
<body class="yui-skin-sam">
	<div id="doc3" class="yui-t3">
	   <div id="hd"><h1>Starlite</h1></div>
	   <div id="bd">
		<div id="yui-main">
		<div class="yui-b"><div class="yui-g">
			<div id="demo" class="yui-navset">
    			<ul class="yui-nav">
        			<li class="selected"><a href="#tab1"><em>Details</em></a></li>
        			<li><a href="#tab2"><em>Aircraft</em></a></li>
        			<li><a href="#tab3"><em>Crew</em></a></li>
    			</ul>            
    			<div class="yui-content">
        			<div style="height:350px;">
        				<h2>${charterId} - ${client}</h2>
        				<form id="charterForm" action="javascript:submitForm()">
						<table>
							<tr><td class="formLabel">Status</td>
								<td><select>
										<option <#if status=='Prospect'>selected</#if>>Prospect</option>
										<option <#if status=='Approved'>selected</#if>>Approved</option>
										<option <#if status=='Prepare'>selected</#if>>Prepare</option>
										<option <#if status=='Position'>selected</#if>>Position</option>
										<option <#if status=='Operate'>selected</#if>>Operate</option>
										<option <#if status=='Decommission'>selected</#if>>Decommission</option>
									</select>
								</td>
							</tr>
							<tr><td class="formLabel">Dates</td>
								<td id="calendarDiv"></td>
							</tr>
							<tr>
								<td> </td>
								<td><br/><input type="submit" value="Save"/>&nbsp;&nbsp;<input type="submit" value="Cancel"/></td>
							</tr>
						</table>
						<input type="hidden" name="charterId" value="1"/>
						<input type="hidden" name="aircraftId" value="1"/>
						<input type="hidden" name="crew" id="crewHidden"/>
						<input type="hidden" name="dateFrom" id="dateFrom"/>
						<input type="hidden" name="dateTo" id="dateTo"/>
						</form>
        			
        			</div>
        				
        			<div style="height:350px;">
        				<div id="aircraftFiller"></div>						
        				
        			</div>
        			<div>
        				<div id="crewFiller"></div>	
					</div>
    			</div>
			</div>
		</div>
		</div>
		</div>
		<div class="yui-b">
			<div id="leftList" style="height:600px; overflow:auto;">
				<div id="charters">
					<div id="charterInfo1" onclick="location.href='editCharter.action?charterId=SU001&client=Sudan&status=Operate'" class="odd operate" onmouseover="YAHOO.util.Dom.addClass('charterInfo1', 'hover');" onmouseout="YAHOO.util.Dom.removeClass('charterInfo1', 'hover');">
						<h3>SU001 - Sudan</h3>
						<p>7th Jan 2008 - 28th Jan 2008</p>
						<p>Status: Operate</p>
					</div>
					
					<div id="charterInfo2" onclick="location.href='editCharter.action?charterId=SP001&client=Spain&status=Operate'" class="odd operate" onmouseover="YAHOO.util.Dom.addClass('charterInfo2', 'hover');" onmouseout="YAHOO.util.Dom.removeClass('charterInfo2', 'hover');">
						<h3>SP001 - Spain</h3>
						<p>10th Jan 2008 - 29th Jan 2008</p>
						<p>Status: Operate</p>
					</div>
					
					<div id="charterInfo3" class="odd prepare" onclick="location.href='editCharter.action?charterId=SU002&client=Sudan&status=Prepare'"  onmouseover="YAHOO.util.Dom.addClass('charterInfo3', 'hover');" onmouseout="YAHOO.util.Dom.removeClass('charterInfo3', 'hover');">
						<h3>SU002 - Sudan</h3>
						<p>16th Feb 2008 - 28th Feb 2008</p>
						<p>Status: Prepare</p>
					</div>
					
					<div id="charterInfo4" class="odd approved" onclick="location.href='editCharter.action?charterId=SP002&client=Spain&status=Approved'"  onmouseover="YAHOO.util.Dom.addClass('charterInfo4', 'hover');" onmouseout="YAHOO.util.Dom.removeClass('charterInfo4', 'hover');">
						<h3>SP002 - Spain</h3>
						<p>24th Feb 2008 - 7th Mar 2008</p>
						<p>Status: Approved</p>
					</div>
				</div>
			</div>
		</div>
	   <div id="ft">i-Tao: Web Based Solutions</div>
	</div>
	<div style="position:absolute; top:5px; left:330px; font-size:122%;">
		<a href="roster.action">Roster View</a> 
	</div>
</body>
</html>