<html>

<head>
	<title>Aircraft Management</title>
	<link rel="stylesheet" type="text/css" href="styles/forms.css">
	<script type="text/javascript" src="http://yui.yahooapis.com/2.4.1/build/utilities/utilities.js"></script>
	
	<script type="text/javascript" src="http://yui.yahooapis.com/2.4.1/build/element/element-beta-min.js"></script>
	<script type="text/javascript" src="http://yui.yahooapis.com/2.4.1/build/datasource/datasource-beta-min.js"></script>
	<script type="text/javascript" src="http://yui.yahooapis.com/2.4.1/build/connection/connection-min.js"></script>
	<script type="text/javascript" src="http://yui.yahooapis.com/2.4.1/build/dragdrop/dragdrop-min.js"></script>

	<script type="text/javascript" src="http://yui.yahooapis.com/2.4.1/build/datatable/datatable-beta-min.js"></script>
	<script type="text/javascript" src="http://yui.yahooapis.com/2.4.1/build/container/container-min.js"></script>
	
	<script>
		var aircraftDataSource = new YAHOO.util.DataSource("http://localhost:8080/starlite-ui/services/aircraft");
		aircraftDataSource.responseType = YAHOO.util.DataSource.TYPE_XML;
		aircraftDataSource.responseSchema = {
			resultNode: "aircraft",
			fields: ["id", "ref", "cargoSize", "licences", "performanceCounter"]
		};
		
		var rowFormatter = function(elCell, oRecord, oColumn, oData) {
			if (oRecord.getData("processing")) {
				elCell.innerHTML = "<img src='images/icons/loader.gif'/>";
			} else {
				elCell.innerHTML = oData;
			}
		};
		
		var aircraftTableColDefs = [
			{key:"id", label:"Id", formatter:rowFormatter},
			{key:"ref", label:"Reference"},
			{key:"cargoSize", label:"Cargo Size"},
			{key:"licences", label:"Licences"},
			{key:"performanceCounter", label:"Performance Counter"}
		];
		var aircraftTable;
		
		YAHOO.util.Event.onContentReady("aircraftTable", function() {
			aircraftTable = new YAHOO.widget.DataTable("aircraftTable", aircraftTableColDefs, aircraftDataSource, {
				selectionMode:"single"
			});
			
			aircraftTable.subscribe("rowMouseoverEvent", aircraftTable.onEventHighlightRow);
        	aircraftTable.subscribe("rowMouseoutEvent", aircraftTable.onEventUnhighlightRow);
        	aircraftTable.subscribe("rowClickEvent", aircraftTable.onEventSelectRow);
		});
		
		var addEditAircraftDialog;
		
		var successfullyProcessed = function(o) {
			var rec = o.argument[0];
			
			if (o.argument.length > 1 && o.argument[1] == "delete") {
				aircraftTable.deleteRow(rec);
			} else {
			
				var id = rec._oData.id;
				if (!id) {
					id = o.responseXML.firstChild.firstChild.firstChild.nodeValue;
				}
				var cargoSize = rec._oData.cargoSize;
				var licences = rec._oData.licences;
				var performanceCounter = rec._oData.performanceCounter;
				var ref = rec._oData.ref;
				aircraftTable.updateRow(rec, {id:id, ref:ref, cargoSize:cargoSize, licences:licences, performanceCounter:performanceCounter, processing:false});
			}
		};
		
		var unsuccessfullyProcessed = function(o) {
		
		};
		
		YAHOO.util.Event.onContentReady("addEditAircraft", function() {
			var handleCancel = function() {
				this.cancel();
			};
			
			var handleSubmit = function() {
				var id = document.getElementById("id").value;
				var ref = document.getElementById("ref").value;
				var cargoSize = document.getElementById("cargoSize").value;
				var licences = document.getElementById("licences").value;
				var performanceCounter = document.getElementById("performanceCounter").value;
				
				var doc = document.implementation.createDocument("", "aircraft", null);
				var aircraftEl = doc.firstChild;
				
				var idEl = doc.createElement("id");
				idEl.appendChild(doc.createTextNode(id));
				aircraftEl.appendChild(idEl);
				
				var refEl = doc.createElement("ref");
				refEl.appendChild(doc.createTextNode(ref));
				aircraftEl.appendChild(refEl);
				
				var cargoSizeEl = doc.createElement("cargoSize");
				cargoSizeEl.appendChild(doc.createTextNode(cargoSize));
				aircraftEl.appendChild(cargoSizeEl);
				
				var licencesEl = doc.createElement("licences");
				licencesEl.appendChild(doc.createTextNode(licences));
				aircraftEl.appendChild(licencesEl);
				
				var performanceCounterEl = doc.createElement("performanceCounter");
				performanceCounterEl.appendChild(doc.createTextNode(performanceCounter));
				aircraftEl.appendChild(performanceCounterEl);
				
				var s = new XMLSerializer().serializeToString(doc);
				var method;
				YAHOO.util.Connect.initHeader("Content-Type", "text/xml", true);
				if (id && id != -1) {
					var recId = aircraftTable.getSelectedRows()[0];
					var rec = aircraftTable.getRecord(recId);
					
					aircraftTable.updateRow(rec, {id:id, ref:ref, processing:true, cargoSize:cargoSize, licences:licences, performanceCounter:performanceCounter});
					YAHOO.util.Connect.asyncRequest("PUT", "services/aircraft/"+id, {success:successfullyProcessed, failure:unsuccessfullyProcessed, argument:[rec]}, s);
				} else {
					aircraftTable.addRow({ref:ref, cargoSize:cargoSize, licences:licences, performanceCounter:performanceCounter, processing:true});
					var rs = aircraftTable.getRecordSet();
					var rec = rs.getRecord(rs.getLength()-1);
					YAHOO.util.Connect.asyncRequest("POST", "services/aircraft", {success:successfullyProcessed, failure:unsuccessfullyProcessed, argument:[rec]}, s);
				}
				addEditAircraftDialog.hide();
			};
			
			var buttons = [ {text:"Save", handler: handleSubmit, isDefault:true},
							{text:"Cancel", handler:handleCancel}];
		
			addEditAircraftDialog = new YAHOO.widget.Dialog("addEditAircraft", {
				visible:false,
				fixedcenter: true,
				modal:true
			});
			addEditAircraftDialog.cfg.queueProperty("buttons", buttons);
			addEditAircraftDialog.render();
		});
		
		YAHOO.util.Event.onContentReady("buttons", function() {
			YAHOO.util.Event.addListener("addButton", "click", function() {
				document.getElementById("id").value="-1";
				document.getElementById("ref").value="";
				document.getElementById("cargoSize").value="";
				document.getElementById("licences").value="";
				document.getElementById("performanceCounter").value="";
				addEditAircraftDialog.show();
			});
			
			YAHOO.util.Event.addListener("editButton", "click", function() {
				var sris = aircraftTable.getSelectedRows();
				if (!sris || sris.length == 0)
					return;
				
				var record = aircraftTable.getRecord(sris[0]);
					
				document.getElementById("id").value=record._oData.id;
				document.getElementById("ref").value=record._oData.ref;
				document.getElementById("cargoSize").value=record._oData.cargoSize;
				document.getElementById("licences").value=record._oData.licences;
				document.getElementById("performanceCounter").value=record._oData.performanceCounter;
				addEditAircraftDialog.show();
			});
			
			YAHOO.util.Event.addListener("deleteButton", "click", function() {
				var sris = aircraftTable.getSelectedRows();
				if (!sris || sris.length == 0)
					return;
				
				var record = aircraftTable.getRecord(sris[0]);
				var id = record._oData.id;
				var ref = record._oData.ref;
				var cargoSize = record._oData.cargoSize;
				var licences = record._oData.licences;
				var performanceCounter = record._oData.performanceCounter;
				aircraftTable.updateRow(record, {id:id, ref:ref, cargoSize:cargoSize, licences:licences, performanceCounter:performanceCounter, processing:true});
				YAHOO.util.Connect.asyncRequest("DELETE", "services/aircraft/"+id, {success:successfullyProcessed, failure:unsuccessfullyProcessed, argument:[record,"delete"]});
			});
		});
	</script> 
</head>
<body class="yui-skin-sam">
	<div class="buttons" id="buttons" style="float:left;">
		<button id="addButton" style="float:left;"><img src="images/icons/add.png"/>Add</button>
		<button id="editButton" style="float:left;"><img src="images/icons/pencil.png"/>Edit</button>
		<button id="deleteButton" style="float:left;"><img src="images/icons/delete.png"/>Delete</button>
	</div>
	<hr style="clear:left; display:none;"/>
	<br/><br/>
	<div id="aircraftTable"> </div>
	
	<div id="addEditAircraft">
		<div class="hd">Enter Aircraft Details</div>
		<div class="bd">
			<form action="javascript:doSaveAircraft" class="thin">
				<input type="hidden" name="id" id="id"/>
				<fieldset>
				<legend>Aircraft Info</legend>
				<div class="fm-opt">
					<label for="ref">Reference:</label>
					<input name="ref" id="ref" type="text"/>
				</div>
				<div class="fm-opt">
					<label for="cargoSize">Cargo Size:</label>
					<input name="cargoSize" id="cargoSize" type="text"/>
				</div>
				<div class="fm-opt">
					<label for="licences">Licences:</label>
					<input name="licences" id="licences" type="text"/>
				</div>
				<div class="fm-opt">
					<label for="performanceCounter">Performance Counter:</label>
					<input name="performanceCounter" id="performanceCounter" type="text"/>
				</div>
				</fieldset>
			</form>
		</div>
	</div>
</body>

</html>