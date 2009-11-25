<html>
<head>
	<script type="text/javascript" src="js/itao/itao.js"></script>
	<script type="text/javascript" src="js/yui_2.4.1/yui/build/utilities/utilities.js"></script> 
	<link rel="stylesheet" type="text/css" href="js/yui_2.4.1/yui/build/reset-fonts-grids/reset-fonts-grids.css">
	
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

	<script>
		var dataSource = new YAHOO.util.DataSource("http://localhost:8080/starlite-ui/services/starlite-core/charters");
		dataSource.responseType = YAHOO.util.DataSource.TYPE_XML;
		dataSource.responseSchema = {
			resultNode:"charter",
			fields:["id","code","startDate","endDate","client","description","location","status"]
		};
	
		var roster;
	
		var rosterCharterRenderer = function(workingDate, cell) {
			var cellDay = workingDate.getDate() +"";
			if (cellDay.length == 1) {
				cellDay = "0"+cellDay;
			}
			var cellMonth = workingDate.getMonth();
			cellMonth++;
			cellMonth = cellMonth +"";
			if (cellMonth.length == 1) {
				cellMonth = "0"+cellMonth;
			}
			var cellYear = workingDate.getFullYear();
			var cellDate = cellYear+"-"+cellMonth+"-"+cellDay;
			
			var dayEvents = this.charterEvents[cellDate];
			if (dayEvents) {
				var ih = '<span style="height:100%;"><a href="#" class="' + this.Style.CSS_CELL_SELECTOR + '">' + this.buildDayLabel(workingDate) + "</a>";
				for (var i=0; i<dayEvents.length; i++) {
					ih = ih + '<br/>';
					ih = ih + dayEvents[i];
				}
				ih = ih + "</span>";
				cell.innerHTML = ih;
			} else {
				cell.innerHTML = '<a href="#" class="' + this.Style.CSS_CELL_SELECTOR + '">' + this.buildDayLabel(workingDate) + '</a>';
			}
			YAHOO.util.Dom.addClass(cell, this.Style.CSS_CELL_SELECTABLE);
			//this.renderCellStyleHighlight1(workingDate, cell);
			return YAHOO.widget.Calendar.STOP_RENDER; 
		};
	
		
		var dataHandler = function(sRequest, oResponse, bError) {
			if (oResponse && !oResponse.error && YAHOO.lang.isArray(oResponse.results)) {
				var charters = oResponse.results;
				
				for (var i=0; i<charters.length; i++) {
					var charter = charters[i];
					var start = charter.startDate;
					var end = charter.endDate;
					var startDayEvents = this.charterEvents[start];
					if (!startDayEvents) {
						startDayEvents = [];
					}
					startDayEvents[startDayEvents.length] = charter.code + " start";
					this.charterEvents[start] = startDayEvents;
					var endDayEvents = this.charterEvents[end];
					if (!endDayEvents) {
						endDayEvents = [];
					}
					endDayEvents[endDayEvents.length] = charter.code + " end";
					this.charterEvents[end] = endDayEvents;
				}
				this.render();
			} else {
				//alert("Error " + bError);
			}
		};
		
		var changePageHandler = function(type, args, obj) {
			var cellDates = obj.cellDates;
			for (var i=0; i<cellDates.length; i++) {
				var cd = cellDates[i];
				if (cd[2] == 1) {
					var year = cd[0]+"";
					var month = cd[1];
					var monthDone = this.monthsDone[month-1];
					if (!monthDone) {
						var yearMonth = year+"-"+month;
						dataSource.sendRequest("/"+yearMonth, this.dataHandler, this);
						this.monthsDone[month-1] = true;
					}
					return;
				}
			}
		};
		
		var mySelectHandler = function(type,args,obj) { 
			var selected = args[0];
			var workingDate = this._toDate(selected[0]);
			var cellDay = workingDate.getDate() +"";
			if (cellDay.length == 1) {
				cellDay = "0"+cellDay;
			}
			var cellMonth = workingDate.getMonth();
			cellMonth++;
			cellMonth = cellMonth +"";
			if (cellMonth.length == 1) {
				cellMonth = "0"+cellMonth;
			}
			var cellYear = workingDate.getFullYear();
			var cellDate = cellYear+"-"+cellMonth+"-"+cellDay;
			
		}; 
	
		YAHOO.util.Event.onContentReady("roster", function() {
			roster = new YAHOO.widget.Calendar("roster");
			roster.removeRenderers();
			roster.renderCellStyleToday = roster.renderCellDefault;
			roster.renderCellStyleSelected = roster.renderCellDefault;
			roster.selectEvent.subscribe(function() {
				if (this.mode == "aircraft") {
					if (this.selectedAircraft)
						location.href="dailyInfo.action?aircraftRef="+this.selectedAircraft;
				}
			}, roster, true);
			roster.render();
		});
		
		var setRosterHighlighted = function(range, status) {
			var style;
			if (status == 'Operate') {
				style = roster.renderCellStyleHighlight1;
			} else if (status == 'Unavailable') {
				style = roster.renderCellStyleHighlight2;
			} else if (status == 'Prepare') {
				style = roster.renderCellStyleHighlight3;
			} else if (status == 'Approved') {
				style = roster.renderCellStyleHighlight4;
			}
			if (range.substring(0,2) == "01") {
				roster.setMonth(0);
			} else {
				roster.setMonth(1);
			}
			roster.removeRenderers();
			roster.addRenderer(range, style);
			roster.render();			
		};
		
		var switchToCharters = function() {
			YAHOO.util.Dom.setStyle(['crew', 'aircraft'], 'display', 'none'); 
			YAHOO.util.Dom.setStyle('charters', 'display', 'block');
			roster.removeRenderers();
			roster.render();
			setDetailPane();
			roster.mode = "charter";
			roster.selectedAircraft = null;
		};
		
		var switchToCrew = function() {
			YAHOO.util.Dom.setStyle(['charters', 'aircraft'], 'display', 'none');
			YAHOO.util.Dom.setStyle('crew', 'display', 'block'); 
			roster.removeRenderers();
			roster.setMonth(0);
			roster.render();
			setDetailPane();
			roster.mode="crew";
			roster.selectedAircraft = null;
		};
		
		var switchToAircraft = function() {
			YAHOO.util.Dom.setStyle(['charters', 'crew'], 'display', 'none');
			YAHOO.util.Dom.setStyle('aircraft', 'display', 'block'); 
			roster.removeRenderers();
			roster.setMonth(0);
			roster.render();
			setDetailPane();
			roster.mode="aircraft";
			roster.selectedAircraft = null;
		};
		
		var showRonsSchedule = function() {
			roster.removeRenderers();
			roster.addRenderer('02/16/2008-02/28/2008', roster.renderCellStyleHighlight3);
			roster.render();
		};
		
		var showJasonsSchedule = function() {
			roster.removeRenderers();
			roster.addRenderer('01/07/2008-01/28/2008', roster.renderCellStyleHighlight1);
			roster.render();
		};
		
		var showJonathonsSchedule = function() {
			roster.removeRenderers();
			roster.addRenderer('01/10/2008-01/29/2008', roster.renderCellStyleHighlight1);
			roster.render();
		};
		
		var showChrisSchedule = function() {
			roster.removeRenderers();
			roster.addRenderer('01/24/2008-02/11/2008', roster.renderCellStyleHighlight2);
			roster.render();
		};
		
		var showDavidSchedule = function() {
			roster.removeRenderers();			
			roster.render();
		};
		
		var currentDetail;
		
		var setDetailPane = function(containerId) {
			if (currentDetail) {
				YAHOO.util.Dom.setStyle(currentDetail, 'display', 'none');
			}
			currentDetail = containerId;
			if (containerId)
				YAHOO.util.Dom.setStyle(containerId, 'display', 'block');
		};
		
	</script>
	
	<style type="text/css">
		.yui-skin-sam .yui-calendar { 
			width: 100%;
			height: 600px;
		}
		
		.yui-skin-sam .yui-calendar .calweekdayrow { 
			height: 25px;
		}
		
		.yui-skin-sam .yui-calendar thead { 
			height: 35px;
		}
		
		.yui-skin-sam .yui-calendar td.calcell.today {
			background-color:#fff;
		}
	</style>
</head>
<body class="yui-skin-sam">
	<div id="doc3" class="yui-t3">
	   <div id="hd"><h1>Starlite</h1></div>
	   <div id="bd">
		<div id="yui-main">
		<div class="yui-b"><div class="yui-g">
			<div id="roster"></div>
		</div>
	</div>
		</div>
		<div class="yui-b">
			<div id="leftList" style="height:300px; overflow:auto;">
				<div id="charters">
					<div id="charterInfo1" class="odd operate" onclick="setRosterHighlighted('01/07/2008-01/28/2008', 'Operate'); setDetailPane('SU001Details');" onmouseover="YAHOO.util.Dom.addClass('charterInfo1', 'hover');" onmouseout="YAHOO.util.Dom.removeClass('charterInfo1', 'hover');">
						<h3>SU001 - Sudan</h3>
						<p>7th Jan 2008 - 28th Jan 2008</p>
						<p>Status: Operate</p>
					</div>
					
					<div id="charterInfo2" class="odd operate" onclick="setRosterHighlighted('01/10/2008-01/29/2008', 'Operate'); setDetailPane('SP001Details');" onmouseover="YAHOO.util.Dom.addClass('charterInfo2', 'hover');" onmouseout="YAHOO.util.Dom.removeClass('charterInfo2', 'hover');">
						<h3>SP001 - Spain</h3>
						<p>10th Jan 2008 - 29th Jan 2008</p>
						<p>Status: Operate</p>
					</div>
					
					<div id="charterInfo3" class="odd prepare" onclick="setRosterHighlighted('02/16/2008-02/28/2008', 'Prepare'); setDetailPane('SU002Details');" onmouseover="YAHOO.util.Dom.addClass('charterInfo3', 'hover');" onmouseout="YAHOO.util.Dom.removeClass('charterInfo3', 'hover');">
						<h3>SU002 - Sudan</h3>
						<p>16th Feb 2008 - 28th Feb 2008</p>
						<p>Status: Prepare</p>
					</div>
					
					<div id="charterInfo4" class="odd approved" onclick="setRosterHighlighted('02/24/2008-03/7/2008', 'Approved'); setDetailPane('SP002Details');" onmouseover="YAHOO.util.Dom.addClass('charterInfo4', 'hover');" onmouseout="YAHOO.util.Dom.removeClass('charterInfo4', 'hover');">
						<h3>SP002 - Spain</h3>
						<p>24th Feb 2008 - 7th Mar 2008</p>
						<p>Status: Approved</p>
					</div>
				</div>
				<div id="crew" style="display:none;">
					<div id="crew1" class="odd available" onclick="showRonsSchedule();" onmouseover="YAHOO.util.Dom.addClass('crew1', 'hover');" onmouseout="YAHOO.util.Dom.removeClass('crew1', 'hover');">
						<h3>Capt Leon Kieser</h3>
						<p>Available</p>
					</div>
					
					<div id="crew2" class="odd" onclick="showJasonsSchedule();" onmouseover="YAHOO.util.Dom.addClass('crew2', 'hover');" onmouseout="YAHOO.util.Dom.removeClass('crew2', 'hover');">
						<h3>Capt Eugene Viljoen</h3>
						<p>On Charter (SU001)</p>
					</div>
					
					<div id="crew3" class="odd" onclick="showJonathonsSchedule();" onmouseover="YAHOO.util.Dom.addClass('crew3', 'hover');" onmouseout="YAHOO.util.Dom.removeClass('crew3', 'hover');">
						<h3>Daniel Jansen</h3>
						<p>On Charter (SP001)</p>
					</div>
					
					<div id="crew4" class="odd unavailable" onclick="showChrisSchedule();" onmouseover="YAHOO.util.Dom.addClass('crew4', 'hover');" onmouseout="YAHOO.util.Dom.removeClass('crew4', 'hover');">
						<h3>Adriaan Pienaar</h3>
						<p>On Leave</p>
					</div>
					
					<div id="crew5" class="odd available" onclick="showDavidSchedule();" onmouseover="YAHOO.util.Dom.addClass('crew5', 'hover');" onmouseout="YAHOO.util.Dom.removeClass('crew5', 'hover');">
						<h3>Daniel Erasmus</h3>
						<p>Available</p>
					</div>
					
					<div id="crew6" class="odd available" onmouseover="YAHOO.util.Dom.addClass('crew6', 'hover');" onmouseout="YAHOO.util.Dom.removeClass('crew6', 'hover');">
						<h3>Andries van Coller</h3>
						<p>Available</p>
					</div>
					
					<div id="crew7" class="odd available" onmouseover="YAHOO.util.Dom.addClass('crew7', 'hover');" onmouseout="YAHOO.util.Dom.removeClass('crew7', 'hover');">
						<h3>Wynand Bezuidenhout</h3>
						<p>Available</p>
					</div>
					
					<div id="crew8" class="odd available" onmouseover="YAHOO.util.Dom.addClass('crew8', 'hover');" onmouseout="YAHOO.util.Dom.removeClass('crew8', 'hover');">
						<h3>Deon Pretorius</h3>
						<p>Available</p>
					</div>
					
					<div id="crew9" class="odd available" onmouseover="YAHOO.util.Dom.addClass('crew9', 'hover');" onmouseout="YAHOO.util.Dom.removeClass('crew9', 'hover');">
						<h3>Phillipus van Coller</h3>
						<p>Available</p>
					</div>
					
					<div id="crew10" class="odd available" onmouseover="YAHOO.util.Dom.addClass('crew10', 'hover');" onmouseout="YAHOO.util.Dom.removeClass('crew10', 'hover');">
						<h3>Willem Beaurain</h3>
						<p>Available</p>
					</div>
				</div>
				<div id="aircraft" style="display:none;">
					
					
					<div id="ZS-RUH" class="odd" onclick="roster.selectedAircraft='ZS-RUH'; setRosterHighlighted('02/16/2008-02/28/2008', 'Operate');" onmouseover="YAHOO.util.Dom.addClass('ZS-RUH', 'hover');" onmouseout="YAHOO.util.Dom.removeClass('ZS-RUH', 'hover');">
						<h3>ZS-RUH</h3>
						<p>On Charter (SP001)</p>
					</div>
					
					<div id="ZS-HIZ" class="odd" onclick="roster.selectedAircraft='ZS-HIZ'; setRosterHighlighted('01/10/2008-01/29/2008', 'Operate');" onmouseover="YAHOO.util.Dom.addClass('ZS-HIZ', 'hover');" onmouseout="YAHOO.util.Dom.removeClass('ZS-HIZ', 'hover');">
						<h3>ZS-HIZ</h3>
						<p>On Charter (SU001)</p>
					</div>
					
					<div id="ZS-HJA" class="odd unavailable" onclick="roster.selectedAircraft='ZS-HJA'; setRosterHighlighted('01/03/2008-02/12/2008', 'Unavailable');" onmouseover="YAHOO.util.Dom.addClass('ZS-HJA', 'hover');" onmouseout="YAHOO.util.Dom.removeClass('ZS-HJA', 'hover');">
						<h3>ZS-HJA</h3>
						<p>Maintenance</p>
					</div>
					
					<div id="ZS-RWO" class="odd available" onclick="roster.selectedAircraft='ZS-RWO'; roster.removeRenderers(); roster.render();" onmouseover="YAHOO.util.Dom.addClass('ZS-RWO', 'hover');" onmouseout="YAHOO.util.Dom.removeClass('ZS-RWO', 'hover');">
						<h3>ZS-RWO</h3>
						<p>In Hangar</p>
					</div>
					
					<div id="ZS-RNK" class="odd available" onclick="roster.selectedAircraft='ZS-RNK'; roster.removeRenderers(); roster.render();" onmouseover="YAHOO.util.Dom.addClass('ZS-RNK', 'hover');" onmouseout="YAHOO.util.Dom.removeClass('ZS-RNK', 'hover');">
						<h3>ZS-RNK</h3>
						<p>In Hangar</p>
					</div>
				</div>
			</div>
			<div id="detailsContainer" style="height:300px; overflow:auto; background-color:#e8e8e8; border: 1px solid black; padding:4px; margin-top:12px;">
				<div id="SU001Details" style="display:none;">
					<h3>SU001 - Sudan</h3>
					<p>7th Jan 2008 - 28th Jan 2008</p>
					<br/>
					<p style="font-size: 108%;">Status: Operate</p>
					<p style="font-size: 108%;">Specialist Equipment: None</p>
					<p style="font-size: 108%;">Aircraft: HWT200</p>
					<br/>
					<a style="font-size: 108%;" href="editCharter.action?charterId=SU001&client=Sudan&status=Operate">Plan</a>
				</div>
				<div id="SU002Details" style="display:none;">
					<h3>SU002 - Sudan</h3>
					<p>16th Feb 2008 - 28th Feb 2008</p>
					<br/>
					<p style="font-size: 108%;">Status: Operate</p>
					<p style="font-size: 108%;">Specialist Equipment: None</p>
					<p style="font-size: 108%;">Aircraft: HWT200</p>
					<br/>
					<a style="font-size: 108%;" href="editCharter.action?charterId=SU002&client=Sudan&status=Prepare">Plan</a>
				</div>
				<div id="SP001Details" style="display:none;">
					<h3>SP001 - Spain</h3>
					<p>10th Jan 2008 - 29th Jan 2008</p>
					<br/>
					<p style="font-size: 108%;">Status: Prepare</p>
					<p style="font-size: 108%;">Specialist Equipment: None</p>
					<p style="font-size: 108%;">Aircraft: ABE249</p>
					<br/>
					<a style="font-size: 108%;" href="editCharter.action?charterId=SP001&client=Spain&status=Operate">Plan</a>
				</div>
				<div id="SP002Details" style="display:none;">
					<h3>SP002 - Spain</h3>
					<p>24th Feb 2008 - 7th Mar 2008</p>
					<br/>
					<p style="font-size: 108%;">Status: Approved</p>
					<p style="font-size: 108%;">Specialist Equipment: None</p>
					<p style="font-size: 108%;">Aircraft: HWT200</p>
					<br/>
					<a style="font-size: 108%;" href="editCharter.action?charterId=SP002&client=Spain&status=Approved">Plan</a>
				</div>
			</div>
			
		</div>
	   <div id="ft">i-Tao: Web Based Solutions</div>
	</div>
	<div style="position:absolute; top:5px; left:330px; font-size:122%;">
		<a href="#" onclick="switchToCharters();">Charters</a> 
		<a href="#" onclick="switchToCrew();">Crew</a> 
		<a href="#" onclick="switchToAircraft();">Aircraft</a></div>
</body>
</html>