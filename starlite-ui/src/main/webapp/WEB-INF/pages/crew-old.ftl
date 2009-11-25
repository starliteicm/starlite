<html>

<head>
	<title>Crew Management</title>
	<link rel="stylesheet" type="text/css" href="styles/forms.css">
	<script type="text/javascript" src="http://yui.yahooapis.com/2.4.1/build/utilities/utilities.js"></script>
	
	<script type="text/javascript" src="http://yui.yahooapis.com/2.4.1/build/element/element-beta-min.js"></script>
	<script type="text/javascript" src="http://yui.yahooapis.com/2.4.1/build/datasource/datasource-beta-min.js"></script>
	<script type="text/javascript" src="http://yui.yahooapis.com/2.4.1/build/connection/connection-min.js"></script>
	<script type="text/javascript" src="http://yui.yahooapis.com/2.4.1/build/dragdrop/dragdrop-min.js"></script>

	<script type="text/javascript" src="http://yui.yahooapis.com/2.4.1/build/datatable/datatable-beta-min.js"></script>
	<script type="text/javascript" src="http://yui.yahooapis.com/2.4.1/build/container/container-min.js"></script>

	<script>
		var crewDataSource = new YAHOO.util.DataSource("http://localhost:8080/starlite-ui/services/crew");
		crewDataSource.responseType = YAHOO.util.DataSource.TYPE_XML;
		crewDataSource.responseSchema = {
			resultNode: "crewmember",
			fields: ["id", "firstname", "lastname", "contactnumber", "personalinformationcomplete", "credentialscomplete", "documentscomplete", "hourscomplete", "othercomplete"]
		};
		
		var rowFormatter = function(elCell, oRecord, oColumn, oData) {
			if (oRecord.getData("processing")) {
				elCell.innerHTML = "<img src='images/icons/loader.gif'/>";
			} else {
				elCell.innerHTML = oData;
			}
		};
		
		var boolFormatter = function(elCell, oRecord, oColumn, oData) {
			if (oData && oData == "true") {
				elCell.innerHTML = "<img src='images/icons/tick.png'/>";
			} else {
				elCell.innerHTML = "<img src='images/icons/cross.png'/>";
			}
			YAHOO.util.Dom.setStyle(elCell, "text-align", "center");
		};
		
		var crewTableColDefs = [
			{key:"id", label:"Id", formatter:rowFormatter},
			{key:"firstname", label:"First Name"},
			{key:"lastname", label:"Last Name"},
			{key:"contactnumber", label:"Contact Number"},
			{key:"personalInformationcomplete", label:"Personal Info Complete", formatter:boolFormatter},
			{key:"credentialscomplete", label:"Credentials Complete", formatter:boolFormatter},
			{key:"documentscomplete", label:"Documents Complete", formatter:boolFormatter},
			{key:"hourscomplete", label:"Hours Complete", formatter:boolFormatter},
			{key:"othercomplete", label:"Other Complete", formatter:boolFormatter}
		];
		var crewTable;
		
		YAHOO.util.Event.onContentReady("crewTable", function() {
			crewTable = new YAHOO.widget.DataTable("crewTable", crewTableColDefs, crewDataSource, {
				selectionMode:"single"
			});
			
			crewTable.subscribe("rowMouseoverEvent", crewTable.onEventHighlightRow);
        	crewTable.subscribe("rowMouseoutEvent", crewTable.onEventUnhighlightRow);
        	crewTable.subscribe("rowClickEvent", crewTable.onEventSelectRow);
		});
		
		var addEditCrewDialog;
		
		var successfullyProcessed = function(o) {
			var rec = o.argument[0];
			
			if (o.argument.length > 1 && o.argument[1] == "delete") {
				crewTable.deleteRow(rec);
			} else {
			
				var id = rec._oData.id;
				if (!id) {
					id = o.responseXML.firstChild.firstChild.firstChild.nodeValue;
				}
				var firstname = rec._oData.firstname;
				var lastname = rec._oData.lastname;
				var contactnumber = rec._oData.contactnumber;
				crewTable.updateRow(rec, {id:id, firstname:firstname, lastname:lastname, contactnumber:contactnumber, processing:false});
			}
		};
		
		var unsuccessfullyProcessed = function(o) {
		
		};
		
		YAHOO.util.Event.onContentReady("addEditCrew", function() {
			var handleCancel = function() {
				this.cancel();
			};
			
			var handleSubmit = function() {
				var id = document.getElementById("id").value;
				var firstname = document.getElementById("firstname").value;
				var lastname = document.getElementById("lastname").value;
				var contactnumber = document.getElementById("contactnumber").value;
				
				var doc = document.implementation.createDocument("", "crewMember", null);
				var crewMemberEl = doc.firstChild;
				
				var idEl = doc.createElement("id");
				idEl.appendChild(doc.createTextNode(id));
				crewMemberEl.appendChild(idEl);
				
				var firstnameEl = doc.createElement("firstname");
				firstnameEl.appendChild(doc.createTextNode(firstname));
				crewMemberEl.appendChild(firstnameEl);
				
				var lastnameEl = doc.createElement("lastname");
				lastnameEl.appendChild(doc.createTextNode(lastname));
				crewMemberEl.appendChild(lastnameEl);
				
				var contactnumberEl = doc.createElement("contactnumber");
				contactnumberEl.appendChild(doc.createTextNode(contactnumber));
				crewMemberEl.appendChild(contactnumberEl);
				
				var s = new XMLSerializer().serializeToString(doc);
				var method;
				YAHOO.util.Connect.initHeader("Content-Type", "text/xml", true);
				if (id && id != -1) {
					var recId = crewTable.getSelectedRows()[0];
					var rec = crewTable.getRecord(recId);
					crewTable.updateRow(rec, {id:id, firstname:firstname, lastname:lastname, contactnumber:contactnumber, processing:true});
					YAHOO.util.Connect.asyncRequest("PUT", "services/crew/"+id, {success:successfullyProcessed, failure:unsuccessfullyProcessed, argument:[rec]}, s);
				} else {
					crewTable.addRow({firstname:firstname, lastname:lastname, contactnumber:contactnumber, processing:true});
					var rs = crewTable.getRecordSet();
					var rec = rs.getRecord(rs.getLength()-1);
					YAHOO.util.Connect.asyncRequest("POST", "services/crew", {success:successfullyProcessed, failure:unsuccessfullyProcessed, argument:[rec]}, s);
				}
				addEditCrewDialog.hide();
			};
			
			var buttons = [ {text:"Save", handler: handleSubmit, isDefault:true},
							{text:"Cancel", handler:handleCancel}];
		
			addEditCrewDialog = new YAHOO.widget.Dialog("addEditCrew", {
				visible:false,
				fixedcenter: true,
				modal:true
			});
			addEditCrewDialog.cfg.queueProperty("buttons", buttons);
			addEditCrewDialog.render();
		});
		
		YAHOO.util.Event.onContentReady("buttons", function() {
			YAHOO.util.Event.addListener("addButton", "click", function() {
				document.getElementById("id").value="-1";
				document.getElementById("firstname").value="";
				document.getElementById("lastname").value="";
				document.getElementById("contactnumber").value="";
				addEditCrewDialog.show();
			});
			
			YAHOO.util.Event.addListener("editButton", "click", function() {
				var sris = crewTable.getSelectedRows();
				if (!sris || sris.length == 0)
					return;
				
				var record = crewTable.getRecord(sris[0]);
					
				document.getElementById("id").value=record._oData.id;
				document.getElementById("firstname").value=record._oData.firstname;
				document.getElementById("lastname").value=record._oData.lastname;
				document.getElementById("contactnumber").value=record._oData.contactnumber;
				addEditCrewDialog.show();
			});
			
			YAHOO.util.Event.addListener("deleteButton", "click", function() {
				var sris = crewTable.getSelectedRows();
				if (!sris || sris.length == 0)
					return;
				
				var record = crewTable.getRecord(sris[0]);
				var id = record._oData.id;
				var firstname = record._oData.firstname;
				var lastname = record._oData.lastname;
				var contactnumber = record._oData.contactnumber;
				crewTable.updateRow(record, {id:id, firstname:firstname, lastname:lastname, contactnumber:contactnumber, processing:true});
				YAHOO.util.Connect.asyncRequest("DELETE", "services/crew/"+id, {success:successfullyProcessed, failure:unsuccessfullyProcessed, argument:[record,"delete"]});
			});
			
			YAHOO.util.Event.addListener("refreshButton", "click", function() {
				crewTable.deleteRows(0, crewTable.getRecordSet().getLength());
				crewTable.showTableMessage(crewTable.MSG_LOADING);
				crewDataSource.sendRequest("", crewTable.onDataReturnInitializeTable, crewTable);
			});
		});
	</script> 
</head>
<body class="yui-skin-sam">
	<div class="buttons" id="buttons" style="float:left;">
		<button id="addButton" style="float:left;"><img src="images/icons/add.png"/>Add</button>
		<button id="editButton" style="float:left;"><img src="images/icons/pencil.png"/>Edit</button>
		<button id="deleteButton" style="float:left;"><img src="images/icons/delete.png"/>Delete</button>
		<button id="refreshButton" style="float:left;"><img src="images/icons/arrow_refresh_small.png"/>Refresh</button>
		
		<button id="showDocsButton" style="float:left;"><img src="images/icons/folder_table.png"/>Show Documents</button>
	</div>
	<hr style="clear:left; display:none;"/>
	<br/><br/>
	<div id="crewTable" style="clear:left; margin-top:10px;"> </div>
	
	<div id="addEditCrew">
		<div class="hd">Enter Crew Member Details</div>
		<div class="bd">
			<!--form action="javascript:doSaveCrew">
				<input type="hidden" name="id" id="id"/>
				<label for="firstname">First Name</label><input type="text" name="firstname" id="firstname"/><br/>
				<label for="lastname">Last Name</label><input type="text" name="lastname" id="lastname"/>
			</form-->
			<div style="height:610px; overflow:auto;">
				<form>
				<div style="float:left; width: 500px;">
				<input type="hidden" name="id" id="id"/>
		<fieldset>
			<legend>Personal Information</legend>
			<div class="fm-opt">
				<label for="firstname">First Name:</label>
				<input name="firstname" id="firstname" type="text"/>
			</div>
			<div class="fm-opt">
				<label for="lastname">Last Name:</label>
				<input name="lastname" id="lastname" type="text"/>
			</div>
			<div class="fm-opt">
				<label for="contactnumber">Contact Number:</label>
				<input name="contactnumber" id="contactnumber" type="text"/>
			</div>
		</fieldset>
		
		<fieldset>
			<legend>Credentials</legend>
			<div class="fm-opt">
				<label for="licenceNumber">Licence Number:</label>
				<input name="licenceNumber" id="licenceNumber" type="text"/>
			</div>
			<div class="fm-opt">
				<label for="licenceNumber">Licence Type:</label>
				<select name="licenceNumber" id="licenceNumber">
					<option value="type1">Licence Type 1</option>
					<option value="type2">Licence Type 2</option>
					<option value="type3">Licence Type 3</option>
				</select>
			</div>
			<div class="fm-opt">
				<label for="licenceExpiryDate">Licence Expiry Date:</label>
				<input name="licenceExpiryDate" id="licenceExpiryDate" type="text" style="width:78px;"/>
				<button id="showLicenceDatePicker">Change</button>
			</div>
			<div class="fm-opt">
				<label for="ratings">Ratings:</label>
				<input name="ratings" id="ratings" type="text"/>
			</div>
			<div class="fm-opt">
				<label for="medicalExpiry">Medical Expiry:</label>
				<input name="medicalExpiryDate" id="medicalExpiryDate" type="text" style="width:78px;"/>
				<button id="showMedicalDatePicker">Change</button>
			</div>
			<div class="fm-opt">
				<label for="ifExpiry">IF Expiry:</label>
				<input name="ifExpiryDate" id="ifExpiryDate" type="text" style="width:78px;"/>
				<button id="showIfDatePicker">Change</button>
			</div>
			<div class="fm-opt">
				<label for="instructorExpiry">Instructor Expiry:</label>
				<input name="instructorExpiry" id="instructorExpiry" type="text" style="width:78px;"/>
				<button id="showInstructorDatePicker">Change</button>
			</div>
			<div class="fm-opt">
				<label for="dgExpiry">DG Expiry:</label>
				<input name="dgExpiry" id="dgExpiry" type="text" style="width:78px;"/>
				<button id="showDgDatePicker">Change</button>
			</div>
			<div class="fm-opt">
				<label for="crmExpiry">CRM Expiry:</label>
				<input name="crmExpiry" id="crmExpiry" type="text" style="width:78px;"/>
				<button id="showCrmDatePicker">Change</button>
			</div>
			<div class="fm-opt">
				<label for="baseCheckDone">Base Check - Date Done:</label>
				<input name="baseCheckDone" id="baseCheckDone" type="text" style="width:78px;"/>
				<button id="showBaseCheckDoneDatePicker">Change</button>
			</div>
			<div class="fm-opt">
				<label for="typeDoneOn">Type Done On:</label>
				<select name="typeDoneOn" id="typeDoneOn">
					<option value="type1">Type 1</option>
					<option value="type2">Type 2</option>
					<option value="type3">Type 3</option>
				</select>
			</div>
			<div class="fm-opt">
				<label for="nvgTraining">NVG Training:</label>
				<input name="nvgTraining" id="nvgTraining" type="text"/>
			</div>
			<div class="fm-opt">
				<label for="underslingRating">Undersling Rating:</label>
				<input name="underslingRating" id="underslingRating" type="text"/>
			</div>
		</fieldset>
		</div>
	<div style="float:left; width: 500px; margin-left:50px;">
		<fieldset>
			<legend>Documents</legend>
			<div class="fm-opt">
				<label for="technicalQuizOnFile">Technical Quiz On File:</label>
				<input name="technicalQuizOnFile" id="technicalQuizOnFile" type="checkbox"/>
			</div>
			<div class="fm-opt">
				<label for="cvOnFile">CV On File:</label>
				<input name="cvOnFile" id="cvOnFile" type="checkbox"/>
			</div>
			<div class="fm-opt">
				<label for="contractOnFile">Contract On File:</label>
				<input name="contractOnFile" id="contractOnFile" type="checkbox"/>
			</div>
			<div class="fm-opt">
				<label for="part127Complete">Part 127 Read And Signed:</label>
				<input name="part127Complete" id="part127Complete" type="checkbox"/>
			</div>
			<div class="fm-opt">
				<label for="updatedHoursOnFile">Updated Hours On File:</label>
				<input name="updatedHoursOnFile" id="updatedHoursOnFile" type="checkbox"/>
			</div>
		</fieldset>
		
		<fieldset>
			<legend>Hours</legend>
			<div class="fm-opt">
				<label for="r22Hours">R22 Hours:</label>
				<input name="r22Hours" id="r22Hours" type="text"/>
			</div>
			<div class="fm-opt">
				<label for="r44Hours">R44 Hours:</label>
				<input name="r44Hours" id="r44Hours" type="text"/>
			</div>
			<div class="fm-opt">
				<label for="b206Hours">B206 Hours:</label>
				<input name="b206Hours" id="b206Hours" type="text"/>
			</div>
			<div class="fm-opt">
				<label for="ec120Hours">EC120 Hours:</label>
				<input name="ec120Hours" id="ec120Hours" type="text"/>
			</div>
			<div class="fm-opt">
				<label for="b2Hours">B2 Hours:</label>
				<input name="b2Hours" id="b2Hours" type="text"/>
			</div>
			<div class="fm-opt">
				<label for="b4Hours">B4 Hours:</label>
				<input name="b4Hours" id="b4Hours" type="text"/>
			</div>
			<div class="fm-opt">
				<label for="totalHeliHours">Total Heli Hours:</label>
				<input name="totalHeliHours" id="totalHeliHours" type="text"/>
			</div>
		</fieldset>
		
		<fieldset>
			<legend>Other</legend>
			<div class="fm-opt">
				<label for="330onLicence">330 On Licence:</label>
				<input name="330onLicence" id="330onLicence" type="text"/>
			</div>
			<div class="fm-opt">
				<label for="330hours">330 Hours:</label>
				<input name="330hours" id="330hours" type="text"/>
			</div>
			<div class="fm-opt">
				<label for="instructorOn330">Instructor On 330:</label>
				<input name="instructorOn330" id="instructorOn330" type="text"/>
			</div>
			<div class="fm-opt">
				<label for="passportExpires">Passport Expires:</label>
				<input name="passportExpires" id="passportExpires" type="text" style="width:78px;"/>
				<button id="showPassportExpiresDatePicker">Change</button>
			</div>
			<div class="fm-opt">
				<label for="Position">Position:</label>
				<input name="Position" id="Position" type="text"/>
			</div>
		</fieldset>
		</div>
	</form>
	</div>
		</div>
	</div>
	
	<div id="showDocuments">
		
	</div>
</body>

</html>