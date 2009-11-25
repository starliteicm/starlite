<html>
<head>
	<title>Schedule Planner</title>
	<!--CSS file (default YUI Sam Skin) -->
	<link rel="stylesheet" type="text/css" href="styles/planner2.css">

	<!-- Dependencies -->
	<script type="text/javascript" src="scripts/yui/build/utilities/utilities.js"></script>

	<!-- Drag and Drop source file --> 
	<script type="text/javascript" src="scripts/yui/build/dragdrop/dragdrop-min.js" ></script>
	
	<script type="text/javascript" src="js/itao/itao.js"></script>
	<script type="text/javascript" src="js/itao/util/date.js"></script>
	
	
	<script type="text/javascript" src="js/itao/widget/visualplanner2.js"></script>
	<script type="text/javascript" src="scripts/yui/build/container/container-min.js"></script> 
	<script>
		if( !window.XMLHttpRequest ) XMLHttpRequest = function()
		{
  			try{ return new ActiveXObject("MSXML3.XMLHTTP") }catch(e){}
  			try{ return new ActiveXObject("MSXML2.XMLHTTP.3.0") }catch(e){}
  			try{ return new ActiveXObject("Msxml2.XMLHTTP") }catch(e){}
  			try{ return new ActiveXObject("Microsoft.XMLHTTP") }catch(e){}
  			throw new Error("Could not find an XMLHttpRequest alternative.")
		};
	
		var planner;
		var m;
	
		var wait;
		YAHOO.util.Event.onContentReady("wait", function() {
				// Initialize the temporary Panel to display while waiting for external content to load
				wait = 
					new YAHOO.widget.Panel("wait",  
					{ width:"240px", 
			  			fixedcenter:true, 
			  			close:false, 
			  			draggable:false, 
			  			zindex:4,
			  			modal:true,
			  			visible:false
					}
				);

				wait.setHeader("Saving, please wait...");
				wait.setBody('<img src="http://us.i1.yimg.com/us.yimg.com/i/us/per/gr/gp/rel_interstitial_loading.gif" />');
				wait.render(document.body);
		});
		
		function calculateZoomIn(units) {
			if (units == "month")
				return "week";
			return "day";
		}
		
		function calculateZoomOut(units) {
			if (units == "day")
				return "week";
			return "month";
		}
	
		YAHOO.util.Event.onContentReady("plannerTable", function() {
			m = new ITAO.widget.planner.PlannerModel();
			
			var startDate = new Date();
			planner = new ITAO.widget.planner.VisualPlanner("plannerTable", startDate, "day");
			planner.setModel(m);
			
			var sd = new ITAO.widget.planner.ScheduleDownloader(m);
			sd.refreshScheduleComplete();
			
			YAHOO.util.Event.addListener("addButton", "click", function() {
				planner.getController().setMode(new ITAO.widget.planner.InsertMode(planner));
			});
			
			YAHOO.util.Event.addListener("deleteButton", "click", function() {
				planner.getController().setMode(new ITAO.widget.planner.DeleteMode(planner));
			});
			
			YAHOO.util.Event.addListener("saveButton", "click", function() {
				wait.show();
				
				var rows = m.getRows();
				
				var doc = document.implementation.createDocument("", "schedule", null);
				var root = doc.firstChild;
				for (var i=0; i < rows.length; i++) {
					var r = rows[i];
					if (!r.isSeparator) {
						
						var spans = r.getSpans();
						for (var j=0; j<spans.length; j++) {
							var s = spans[j];
							if (s.getType() == "allocation") {
								var aEl = doc.createElement("allocation");
								var data = s.getUserData();
								
								var idEl = doc.createElement("id");
								if (data.allocationId) {
									idEl.appendChild(doc.createTextNode(data.allocationId));
								 	aEl.appendChild(idEl);
								}
								
								var fromEl = doc.createElement("from");
								fromEl.appendChild(doc.createTextNode(s.getRange().getStartDate().formatDate("c")));
								aEl.appendChild(fromEl);
								
								var toEl = doc.createElement("to");
								toEl.appendChild(doc.createTextNode(s.getRange().getEndDate().formatDate("c")));
								aEl.appendChild(toEl);
								
								var assignableTypeEl = doc.createElement("assignableType");
								assignableTypeEl.appendChild(doc.createTextNode(data.assignableType));
								aEl.appendChild(assignableTypeEl);
								
								var assignableIdEl = doc.createElement("assignableId");
								assignableIdEl.appendChild(doc.createTextNode(data.assignableId));
								aEl.appendChild(assignableIdEl);
								
								var assignmentTypeEl = doc.createElement("assignmentType");
								assignmentTypeEl.appendChild(doc.createTextNode(data.assignmentType));
								aEl.appendChild(assignmentTypeEl);
								
								var assignmentIdEl = doc.createElement("assignmentId");
								assignmentIdEl.appendChild(doc.createTextNode(data.assignmentId));
								aEl.appendChild(assignmentIdEl);
								
								root.appendChild(aEl);
							}
						}
					}
				}
				
				s = new XMLSerializer().serializeToString(doc);
				
				var callback = {
					success: function(o) {
						wait.hide();
					},
					failure: function(o) {
						wait.hide();
						alert("Failure");
					},
					argument: []
				};
				YAHOO.util.Connect.initHeader("Content-Type", "text/xml", true);
				YAHOO.util.Connect.asyncRequest("PUT", "services/schedule", callback, s);
			});
			
			YAHOO.util.Event.addListener("zoomInButton", "click", function() {
				var controller = planner.getController();
				var selected = planner.getSelected();
				var currentStartDate = planner.getInitialStartDate();
				
				var newZoom = calculateZoomIn(planner.getUnits());
				planner = new ITAO.widget.planner.VisualPlanner("plannerTable", currentStartDate, newZoom, null, null, controller);
				planner.setModel(m);
				planner.setSelected(selected);
			});
			
			YAHOO.util.Event.addListener("zoomOutButton", "click", function() {
				var controller = planner.getController();
				var selected = planner.getSelected();
				var currentStartDate = planner.getInitialStartDate();
				
				var newZoom = calculateZoomOut(planner.getUnits());
				planner = new ITAO.widget.planner.VisualPlanner("plannerTable", currentStartDate, newZoom, null, null, controller);
				planner.setModel(m);
				planner.setSelected(selected);
			});
			
			YAHOO.util.Event.addListener("todayButton", "click", function() {
				var controller = planner.getController();
				var selected = planner.getSelected();
				var units = planner.getUnits();
				
				planner = new ITAO.widget.planner.VisualPlanner("plannerTable", new Date(), units, null, null, controller);
				planner.setModel(m);
				planner.setSelected(selected);
			});
			
			
			YAHOO.util.Event.addListener("nextButton", "click", function() {
				var controller = planner.getController();
				var selected = planner.getSelected();
				var currentStartDate = planner.getInitialStartDate();
				var newStartDate = new Date();
				newStartDate.setTime(currentStartDate.getTime());
				var units = planner.getUnits();
				if (units == "day") {
					newStartDate.setDate(currentStartDate.getDate() + 7);
				} else if (units == "week") {
					newStartDate.setDate(currentStartDate.getDate() + 14);
				} else if (units == "month") {
					newStartDate.setMonth(currentStartDate.getMonth() + 1);
				}
				planner = new ITAO.widget.planner.VisualPlanner("plannerTable", newStartDate, planner.getUnits(), null, null, controller);
				planner.setModel(m);
				planner.setSelected(selected);
			});
			
			YAHOO.util.Event.addListener("previousButton", "click", function() {
				var controller = planner.getController();
				var selected = planner.getSelected();
				var currentStartDate = planner.getInitialStartDate();
				var newStartDate = new Date();
				newStartDate.setTime(currentStartDate.getTime());
				var units = planner.getUnits();
				if (units == "day") {
					newStartDate.setDate(currentStartDate.getDate() - 7);
				} else if (units == "week") {
					newStartDate.setDate(currentStartDate.getDate() - 14);
				} else if (units == "month") {
					newStartDate.setMonth(currentStartDate.getMonth() - 1);
				}
				planner = new ITAO.widget.planner.VisualPlanner("plannerTable", newStartDate, planner.getUnits(), null, null, controller);
				planner.setModel(m);
				planner.setSelected(selected);
			});
			
			planner.getController().modeChange.subscribe(function(type, args, me) {
				var m = args[0];
				if (m && m.name == "insert") {
					YAHOO.util.Dom.addClass("addButton", "active");
					YAHOO.util.Dom.removeClass("deleteButton", "active");
				} else if (m && m.name == "delete") {
					YAHOO.util.Dom.removeClass("addButton", "active");
					YAHOO.util.Dom.addClass("deleteButton", "active");
				} else {
					YAHOO.util.Dom.removeClass("deleteButton", "active");
					YAHOO.util.Dom.removeClass("addButton", "active");
				}
			}, planner.getController());
		});
		
		YAHOO.util.Event.onContentReady("allocationDiv", function() {
			ad = new YAHOO.widget.Dialog("allocationDiv",
     			{   
					visible : false,
					modal: true,
					constraintoviewport : true
    			}
			);
			
			ad.classMap = {};
			
			m.rowAdded.subscribe(function(type, args, me) {
				var row = args[0];
				
				if (!row.getUserData() || !row.getUserData().assignmentType)
					return;
			
				var assignmentType = row.getUserData().assignmentType;
				var assignmentId = row.getUserData().assignmentId;
					
				var selectBox = document.getElementById("allocationSelection");
				var opt = document.createElement("option");
				
				opt.appendChild(document.createTextNode(row.getLabel()));
				opt.setAttribute("value", assignmentType+"-"+assignmentId);
				ad.classMap[assignmentType+"-"+assignmentId] = row.getUserData().class;
				selectBox.appendChild(opt);
			});
			
			ad.render();
			
			
		});
		
		var charterChanged = function() {
			var selectBox = document.getElementById("allocationSelection");
			var val = selectBox.options[selectBox.selectedIndex].value;
			var classVal = ad.classMap[val];
			var selected = planner.getSelected();
			if (selected) {
				selected.getSpan().setClass(classVal);
				var parts = val.split("-");
				var assignmentType = parts[0];
				var assignmentId;
				if (parts.length > 1)
					assignmentId = parts[1];
				selected.getSpan().getUserData().assignmentType = assignmentType;
				selected.getSpan().getUserData().assignmentId = assignmentId;
			}
		};
	</script>

</head>

<body class="yui-skin-sam">

<div style="text-align:left; padding:5px; width:800px;" class="buttons">
<!--button id="addButton" style="float: left;"><img src="images/icons/add.png"/>Add</button>
<button id="deleteButton" style="float: left;"><img src="images/icons/delete.png"/>Delete</button>
<button id="saveButton" style="float: left;"><img src="images/icons/tick.png"/>Save</button>

<div style="float:left;width:4px;height:27px;margin-right:7px;background-color:#dedede;">&nbsp;</div-->

<button id="zoomInButton" style="float: left;"><img src="images/icons/zoom_in.png"/>Zoom In</button>
<button id="zoomOutButton" style="float: left;"><img src="images/icons/zoom_out.png"/>Zoom Out</button>

<div style="float:left;width:4px;height:27px;margin-right:7px;background-color:#dedede;">&nbsp;</div>

<button id="todayButton" style="float: left;"><img src="images/icons/date.png"/>Today</button>
<button id="previousButton" style="float: left;"><img src="images/icons/resultset_previous.png"/>Previous</button>
<button id="nextButton" style="float: left;"><img src="images/icons/resultset_next.png"/>Next</button>
<!--img style="float: right; cursor:pointer;" src="images/icons/resultset_next.png"/>
<img style="float: right; cursor:pointer;" src="images/icons/resultset_previous.png"/-->
<hr style="clear: both; border:0; background-color:#ffffff;"/>
</div>
<div id="plannerTable"></div>

<div id="allocationDiv">
	<div class="bd">
		<select id="allocationSelection" onChange="charterChanged();">
			
		</select>
	</div>
</div>

<div id="wait"> </div>

</body>
</html>