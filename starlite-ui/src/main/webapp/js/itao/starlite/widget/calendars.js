ITAO.namespace("ITAO.starlite.widget");

ITAO.starlite.widget.RosterView = function(containingDiv) {
	
};

ITAO.starlite.widget.DateRangePicker = function(containingDiv) {
	this.calendar = new YAHOO.widget.CalendarGroup("calendar",containingDiv, 
	                                                            { pagedate:"2/2008" });
	this.lowerYear;
	this.lowerMonth;
	this.lowerDay;
	
	this.upperYear;
	this.upperMonth;
	this.upperDay;
	
	this.oldYear = -1;
	this.oldMonth;
	this.oldDay;
	
	this.getStartDate = function() {
		return this.lowerDay+"-"+this.lowerMonth+"-"+this.lowerYear;
	};
	
	this.getEndDate = function() {
		return this.upperDay+"-"+this.upperMonth+"-"+this.upperYear;
	};
	
	this.compareDates = function(y1,m1,d1, y2,m2,d2) {
		if (y1 > y2)
			return 1;
		if (y1 < y2)
			return -1;
		if (m1 > m2)
			return 1;
		if (m1 < m2)
			return -1;
		if (d1 > d2)
			return 1;
		if (d1 < d2)
			return -1;
	};
	
	this.handleSelect = function(type,args,obj) {
		var dates = args[0];
		var date = dates[0];
		var year = date[0], month = date[1], day = date[2];

		var d = month + "/" + day + "/" + year;
		
		if (this.oldYear == -1) {
			this.lowerYear = year; this.upperYear = year;
			this.lowerMonth = month; this.upperMonth = month;
			this.lowerDay = day; this.upperDay = day;
		} else {
			var compared = this.compareDates(this.oldYear, this.oldMonth, this.oldDay,
											 year, month, day);
			if (compared < 0) {
				this.lowerYear = this.oldYear;
				this.lowerMonth = this.oldMonth;
				this.lowerDay = this.oldDay;
				this.upperYear = year;
				this.upperMonth = month;
				this.upperDay = day;
			} else {
				this.lowerYear = year;
				this.lowerMonth = month;
				this.lowerDay = day;
				this.upperYear = this.oldYear;
				this.upperMonth = this.oldMonth;
				this.upperDay = this.oldDay;
			}
		}
		
		var df = this.lowerMonth+"/"+this.lowerDay+"/"+this.lowerYear;
		var dt = this.upperMonth+"/"+this.upperDay+"/"+this.upperYear;
		
		var range = df+"-"+dt;
		
		this.oldYear = year;
		this.oldMonth = month;
		this.oldDay = day;
		this.calendar.removeRenderers();
		this.calendar.addRenderer(range, this.calendar.renderCellStyleHighlight1);
		this.calendar.render();
	};

	this.calendar.selectEvent.subscribe(this.handleSelect, this, true);
	this.calendar.render();
};