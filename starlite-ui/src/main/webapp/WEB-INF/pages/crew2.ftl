<html>

<head>
	<title>Crew Management</title>
	<link rel="stylesheet" type="text/css" href="styles/forms.css">
	
	<script type="text/javascript" src="scripts/yui/build/element/element-beta-min.js"></script>
	<script type="text/javascript" src="scripts/yui/build/dragdrop/dragdrop-min.js"></script>

	<script type="text/javascript" src="scripts/yui/build/datatable/datatable-beta-min.js"></script>
	<script type="text/javascript" src="scripts/yui/build/container/container-min.js"></script>
	
	<script>
		var removeChildren = function(e) {	
			if (e && e.hasChildNodes()) {
				var c = e.childNodes[0];
				while (c) {
					e.removeChild(c);
					if (e.hasChildNodes())
						c = e.childNodes[0];
					else
						c = null;
				}
			}
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
	
		function createDocumentDiv(data) {
			var docObj = data.docObj;
			var label = data.label;
			var name = data.name;
			var templateUrl = data.templateUrl;
			
			if (!label)
				label = name;
			
			var d = document.createElement("div");
			YAHOO.util.Dom.addClass(d, "fm-opt");
			var lbl = document.createElement("label");
			lbl.setAttribute("for", name);
			lbl.appendChild(document.createTextNode(label));
			YAHOO.util.Dom.addClass(lbl, "thin");
			d.appendChild(lbl);
						
			if (docObj && docObj.url) {
				var link = document.createElement("a");
				link.setAttribute("href", docObj.url);
				link.setAttribute("target", "_blank");
				link.appendChild(document.createTextNode("view"));
				d.appendChild(link);
				if (docObj.isApproved) {
					var approved = document.createElement("img");
					approved.setAttribute("src", "images/icons/tick.png");
					YAHOO.util.Dom.setStyle(approved, "margin-left", "108px");
					d.appendChild(approved);
				} else {
					var check = document.createElement("input");
					check.setAttribute("type", "checkbox");
					YAHOO.util.Dom.setStyle(check, "width", "30px");
					YAHOO.util.Dom.setStyle(check, "margin-left", "100px");
					d.appendChild(check);
					d.appendChild(document.createTextNode("approve?"));
				}
			} else {
				var input = document.createElement("input");
				input.setAttribute("name", name);
				input.setAttribute("type", "file");
				YAHOO.util.Dom.setStyle(input, "width", "200px");
				d.appendChild(input);
				
				if (templateUrl) {
					var link = document.createElement("a");
					link.setAttribute("href", templateUrl);
					link.setAttribute("target", "_blank");
					var span = document.createElement("span");
					span.appendChild(document.createTextNode("[template]"));
					YAHOO.util.Dom.addClass(span, "small");
					link.appendChild(span);
					d.appendChild(link);
				}
			}
			return d;
		}
		
		var crewTable;
	
		YAHOO.util.Event.onContentReady("crewTable", function() {
			var docSchema = [
				"url", "isApproved"
			];
			
			var schema = [
				"id", "firstName", "lastName", "contactNumber", 
				"personalInformationComplete", "credentialsComplete", "documentsComplete",
				"hoursComplete", "otherComplete",
				{key:"technicalQuiz", vals:docSchema},
				{key:"cv", vals:docSchema},
				{key:"contract", vals:docSchema},
				{key:"part127", vals:docSchema}
			];
		
			var dataSource = new ITAO.util.XmlDataSource("http://localhost:8080/starlite-ui/services/crew", {schema:schema, resultNode:"crewmember"});
			
			var crewTableColDefs = [
				{key:"id", label:"Id", formatter:rowFormatter},
				{key:"firstName", label:"First Name"},
				{key:"lastName", label:"Last Name"},
				{key:"contactNumber", label:"Contact Number"},
				{key:"personalInformationComplete", label:"Personal Info Complete", formatter:boolFormatter},
				{key:"credentialsComplete", label:"Credentials Complete", formatter:boolFormatter},
				{key:"documentsComplete", label:"Documents Complete", formatter:boolFormatter},
				{key:"hoursComplete", label:"Hours Complete", formatter:boolFormatter},
				{key:"otherComplete", label:"Other Complete", formatter:boolFormatter}
			];
			
			crewTable = new YAHOO.widget.DataTable("crewTable", crewTableColDefs, dataSource, {
				selectionMode:"single"
			});
			
			crewTable.subscribe("rowMouseoverEvent", crewTable.onEventHighlightRow);
        	crewTable.subscribe("rowMouseoutEvent", crewTable.onEventUnhighlightRow);
        	crewTable.subscribe("rowClickEvent", crewTable.onEventSelectRow);
		});
		
		var docDialog;
		function handleSubmit() {
			alert("Uploading");
		}
		function handleCancel() {
		
		}
		
		YAHOO.util.Event.onContentReady("documents", function() {
			docDialog = new YAHOO.widget.Dialog("documents", {
				visible:false,
				fixedcenter: true,
				modal:true
			});
			var buttons = [ {text:"Save", handler: handleSubmit, isDefault:true},
							{text:"Cancel", handler:handleCancel}];
			docDialog.cfg.queueProperty("buttons", buttons);
			docDialog.render();
		});
		
		function showDocuments(crewMember) {
			var docBody = document.getElementById("docBody");
			removeChildren(docBody);
			
			var legend = document.createElement("legend");
			legend.appendChild(document.createTextNode(crewMember.firstName + " " + crewMember.lastName + "'s Documents"));
			docBody.appendChild(legend);
			docBody.appendChild(createDocumentDiv({label:"Technical Quiz", docObj:crewMember.technicalQuiz, name:"technicalQuiz"}));
			docBody.appendChild(createDocumentDiv({label:"CV", docObj:crewMember.cv, name:"cv"}));
			docBody.appendChild(createDocumentDiv({label:"Contract", docObj:crewMember.contract, name:"contract"}));
			docBody.appendChild(createDocumentDiv({label:"Part 127", docObj:crewMember.part127, name:"part127", templateUrl:"part127.doc"}));
			docDialog.show();		
		};
		
		YAHOO.util.Event.addListener("documentsButton", "click", function() {
				var sris = crewTable.getSelectedRows();
				if (!sris || sris.length == 0)
					return;
				
				var crewMember = crewTable.getRecord(sris[0])._oData;
				showDocuments(crewMember);
			});
	</script>
</head>

<body>
	<div class="buttons" id="buttons" style="float:left;">
		<button id="addButton" style="float:left;"><img src="images/icons/add.png"/>Add</button>
		<button id="editButton" style="float:left;"><img src="images/icons/pencil.png"/>Edit</button>
		<button id="deleteButton" style="float:left;"><img src="images/icons/delete.png"/>Delete</button>
		<button id="refreshButton" style="float:left;"><img src="images/icons/arrow_refresh_small.png"/>Refresh</button>
		
		<button id="documentsButton" style="float:left;"><img src="images/icons/folder_table.png"/>Show Documents</button>
	</div>
	<hr style="clear:left; display:none;"/>
	<br/><br/>
	<div id="crewTable"></div>
	
	<div id="documents">
		<div class="hd">Documents</div>
		<div class="bd">
		 <form style="width:500px">
		 	<fieldset id="docBody"></fieldset>
		 </form>
		</div>
	</div>
</body>