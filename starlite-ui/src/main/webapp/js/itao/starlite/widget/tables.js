ITAO.namespace("ITAO.starlite.widget");

ITAO.starlite.widget.LinkFormatter = function(hrefProp, labelProp) {
	this.hrefProp = hrefProp;
	this.labelProp = labelProp;
	
	this.formatter = function(elCell, oRecord, oColumn, oData) {
		var href = "charter.action?id="+oRecord._oData["id"];
		var label = oRecord._oData[labelProp];
		elCell.innerHTML = "<a href='" + href + "'>" + label + "</a>";
	};
};

ITAO.starlite.widget.CharterTable = function(containingDiv, baseUrl, dataSource) {
	this.baseUrl = baseUrl;
	if (dataSource == null) {
		this.dataSource = new YAHOO.util.DataSource(baseUrl);
		this.dataSource.responseType = YAHOO.util.DataSource.TYPE_XML;
		this.dataSource.responseSchema = {
			resultNode:"charter",
			fields:["id","client","description"]
		};
	}
	else {
		this.dataSource = dataSource
	}
	
	this.cols = [
		{key:"id",label:"Id", formatter:new ITAO.starlite.widget.LinkFormatter("id", "id").formatter},
		{key:"client",label:"Client"},
		{key:"description",label:"Description"}
	];
	this.dataTable = new YAHOO.widget.DataTable(containingDiv, this.cols, this.dataSource);
};

ITAO.starlite.widget.AircraftTable = function(containingDiv, baseUrl) {
	this.baseUrl = baseUrl;
	this.dataSource = new YAHOO.util.DataSource(baseUrl);
	this.dataSource.responseType = YAHOO.util.DataSource.TYPE_XML;
	this.dataSource.responseSchema = {
		resultNode:"aircraft",
		fields:["id","name","description"]
	};
	this.cols = [
		{key:"id",label:"Id"},
		{key:"name",label:"Name"},
		{key:"description",label:"Description"}
	];
	this.dataTable = new YAHOO.widget.DataTable(containingDiv, this.cols, this.dataSource);
};

ITAO.starlite.widget.CrewTable = function(containingDiv, baseUrl) {
	this.baseUrl = baseUrl;
	this.dataSource = new YAHOO.util.DataSource(baseUrl);
	this.dataSource.responseType = YAHOO.util.DataSource.TYPE_XML;
	this.dataSource.responseSchema = {
		resultNode:"crewMember",
		fields:["id","firstName","lastName"]
	};
	this.cols = [
		{key:"id",label:"Id"},
		{key:"firstName",label:"First Name"},
		{key:"lastName",label:"Last Name"}
	];
	this.dataTable = new YAHOO.widget.DataTable(containingDiv, this.cols, this.dataSource);
};