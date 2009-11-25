<html>
<head>
	<script type="text/javascript" src="js/yui_2.4.1/yui/build/calendar/calendar-min.js"></script>
	<script type="text/javascript" src=" js/itao/starlite/widget/calendars.js"></script>

	<script type="text/javascript" src="js/itao/starlite/widget/dragAndDropList.js"></script>

	<script>
		var calendarDialog;
		var cal;
		YAHOO.util.Event.onContentReady("calendarDiv", function() {
			cal = new ITAO.starlite.widget.DateRangePicker("calendarDiv");
		});
		
		YAHOO.util.Event.onContentReady("crewList", function() {
			new YAHOO.util.DDTarget("crewList");
			new YAHOO.util.DDTarget("availableCrewList");
			new ITAO.starlite.widget.DDList("crew1");
			new ITAO.starlite.widget.DDList("crew2");
			new ITAO.starlite.widget.DDList("crew3");
			new ITAO.starlite.widget.DDList("crew4");
			new ITAO.starlite.widget.DDList("crew5");
		});

var callback =
	{
		success: function(o) {
			//wait.hide();
			alert("Success");
		},
		failure: function(o) {
			//wait.hide();
			alert("Failure: " + o.statusText);
		},
		argument: []
	}

		function submitForm() {
			//wait.show();
			
			var ul = document.getElementById("crewList");
			var items = ul.getElementsByTagName("li");
			var i;
			var crew = "";
			for (i=0; i<items.length; i++) {
				var crewId = items[i].id;
				crewId = crewId.substring(4, crewId.length);
				crew = crew+crewId+",";
			}
			if (crew.length > 0) {
				crew = crew.substring(0, crew.length-1);
			}
			var dateFrom = cal.getStartDate();
			var dateTo = cal.getEndDate();
			document.getElementById("dateFrom").value=dateFrom;
			document.getElementById("dateTo").value=dateTo;
			document.getElementById("crewHidden").value=crew;
			var formObject = document.getElementById('charterForm');
			
			
			
			YAHOO.util.Connect.setForm(formObject);
			var cObj = YAHOO.util.Connect.asyncRequest('POST', 'http://localhost:8080/starlite-ui/services/scheduler/charter', callback);
		};
	</script>
	
	<style type="text/css">

div.workarea { padding:10px; margin-right:10px; float:left; background: #eeeeee; border: 1px solid #dddddd; }




</style>
</head>
<body>
	<h2>Schedule Charter</h2>
	<form id="charterForm" action="javascript:submitForm()">
	<table>
		<!--tr><td class="formLabel">Client</td>
			<td><input type="text"/></td>
		</tr>
		<tr><td class="formLabel">Location</td>
			<td><input type="text"/></td>
		</tr>
		<tr><td class="formLabel">Status</td>
			<td><select>
					<option>Prospect</option>
					<option>Approved</option>
					<option>Prepare</option>
					<option>Position</option>
					<option>Operate</option>
					<option>Decommission</option>
				</select>
			</td>
		</tr-->
		<tr><td class="formLabel">CharterId</td>
			<td>1</td>
		</tr>
		<tr><td class="formLabel">AircraftId</td>
			<td>1</td>
		</tr>
		<tr><td class="formLabel">Dates</td>
			<td id="calendarDiv"></td>
		</tr>
		<tr><td></td><td>&nbsp;<br/><br/></td></tr>
		<!--tr><td class="formLabel">Aircraft</td>
			<td><input type="text"/></td>
		</tr-->
		<tr><td class="formLabel">Crew</td>
			<td>
				<div class="workarea">
				<ul id="crewList" class="draglist_alt">
					<li id="crew1">Ron Chan</li>
					<li id="crew2">David Lobo</li>
					<li id="crew3">Jason Choy</li>
				</ul>
				</div>
				<div class="workarea">
				<ul id="availableCrewList" class="draglist_alt">
					<li id="crew4">Jonathon Elliot</li>
					<li id="crew5">Chris Graham</li>
				</ul>
				</div>
			</td>
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
</body>
</html>