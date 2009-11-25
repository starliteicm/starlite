ITAO.namespace("ITAO.widget");
ITAO.namespace("ITAO.data");
ITAO.namespace("ITAO.util");

function instanceOf(object, constructorFunction) {
  while (object != null) {
    if (object == constructorFunction.prototype)
     {return true}
	 object = object.__proto__;
  }
  return false;
}

Date.prototype.setTimeFromString = function(dateString) {
	var time = Date.parse(dateString);
	this.setTime(time);
};

ITAO.util.DateRange = function(date1, date2) {
	this.startDate = new Date();
	this.endDate = new Date();
	
	if (typeof date1 == 'string' || instanceOf(date1, String)) {
		this.startDate.setTime(Date.parse(date1));
	} else if (instanceOf(date1, Date)) {
		this.startDate.setTime(date1.getTime());
	}
	
	if (typeof date2 == 'string' || instanceOf(date2, String)) {
		this.endDate.setTime(Date.parse(date2));
	} else if (instanceOf(date2, Date)) {
		this.endDate.setTime(date2.getTime());
	}
	
	if (this.startDate > this.endDate) {
		var tmp = this.endDate;
		this.endDate = this.startDate;
		this.startDate = tmp;
	}
	
	this.overlaps = function(otherRange) {
		if (instanceOf(otherRange, ITAO.util.DateRange)) {
			if (otherRange.startDate > this.endDate) {
				return false;
			}
			if (otherRange.endDate < this.startDate) {
				return false;
			}
			return true;
		}
		return false;
	};
	
	this.prototypetoString = function() {
		return this.startDate.toString() + " - " + this.endDate.toString();
	};
};

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



ITAO.data.Period = function(startDate, endDate) {
	this.startDate = parseDate(startDate);
	this.endDate = parseDate(endDate);
	this.listeners = [];
	
	this.setDates = function(newStartDate, newEndDate) {
		var nds = parseDate(newStartDate);
		var ned = parseDate(newEndDate);
		
	};
	
	this.addListener = function(l) {
		for (var i=0; i<this.listeners.length; i++) {
			if (this.listeners[i] == l)
				return;
		}
		this.listeners[this.listeners.length] = l;
	};
	
	this.fireChangeEvent = function() {
		for (var i=0; i<this.listeners.length; i++) {
			var d1 = new Date();
			d1.setTime(this.startDate.getTime());
			var d2 = new Date();
			d2.setTime(this.endDate.getTime());
			listeners[i].datesChanged(d1, d2);
		}
	};
};


ITAO.widget.PlannerBar = function(period, planner, row) {
	this.period = period;
	this.planner = planner;
	this.row = row;
	
	this.startingElement=null;
	this.endingElement=null;
	
	this.leftHandleShown=false;
	this.rightHandleShown=false;
	
	this.renderBar = function() {
		this.removeBar();
		var startCol = this.planner.convertDateToCol(this.period.startDate);
		var endCol = this.planner.convertDateToCol(this.period.endDate);
		
	};
	
	this.removeBar = function() {
		var el = this.startingElement;
		while(el) {
			removeChildren(el);
			if (el == this.endingElement)
				el = null;
			else
				el = el.nextSibling;
		}
	};
};

ITAO.widget.Planner = function(containingDiv, rows, startDate, numWeeks) {
	this.startDate = parseDate(startDate);
	this.numWeeks = numWeeks;
	this.data = rows;

	this.createLine = function(startCol, endCol, class) {
		this.removeChildren(startCol);
		var startSegment = this.createLeftBarSegment(class);
		startCol.appendChild(startSegment[0]);
		startCol.appendChild(startSegment[1]);
		var next = startCol.nextSibling;
		while (next && next != endCol) {
			this.removeChildren (next);
			next.appendChild(this.createBarSegment(class));
			next = next.nextSibling;
		}
		if (next == endCol) {
			var endSegment = this.createRightBarSegment(class);
			next.appendChild(endSegment[0]);
			next.appendChild(endSegment[1]);
		}
	};
	
	this.removeChildren = function(e) {
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
	
	this.createBarSegment = function(class) {
		var barSegment = document.createElement("div");
		YAHOO.util.Dom.addClass(barSegment, "barSegment");
		if (class) {
			YAHOO.util.Dom.addClass(barSegment, class);
		}
		return barSegment;
	};
	
	this.createLeftBarSegment = function(class) {
		var lbs = document.createElement("div");
		YAHOO.util.Dom.addClass(lbs, "leftHandle");
		new YAHOO.util.DD(lbs);
		var bar = document.createElement("div");
		YAHOO.util.Dom.addClass(bar, "endBarSegment");
		if (class) {
			YAHOO.util.Dom.addClass(lbs, class);
			YAHOO.util.Dom.addClass(bar, class);
		}
		return [lbs, bar];
	};
	
	this.createRightBarSegment = function(class) {
		var bar = document.createElement("div");
		YAHOO.util.Dom.addClass(bar, "endBarSegment");
		
		var rbs = document.createElement("div");
		YAHOO.util.Dom.addClass(rbs, "rightHandle");
		new YAHOO.util.DD(rbs);
		if (class) {
			YAHOO.util.Dom.addClass(rbs, class);
			YAHOO.util.Dom.addClass(bar, class);
		}
		return [bar, rbs];
	};
	
	this.convertDateToCol = function(date) {
		if (date < this.startDate)
			return -1;
  		var sDate = this.startDate;
  		var eDate = date;
  		
  		var daysApart = Math.abs(Math.round((sDate-eDate)/86400000));
  		var col = daysApart;
  		return daysApart;
	};
	
	this.getElForDateAndRow = function(date, row) {
		var col = this.convertDateToCol(date);
		if (col < 0) {
			return col;
		}
		
	};
	
	this.refreshBars = function() {
		if (!this.data)
			return;
		
		this.clearBars();
		
		for (var i=0; i<this.data.length; i++) {
			var d = this.data[i];
			var periods = d.periods;
			if (periods) {
				for (var j=0; j<periods.length; j++) {
					var p = periods[j];
					var c1 = this.convertDateToCol(p.startDate);
					var c2 = this.convertDateToCol(p.endDate);
					var e1 = document.getElementById("e"+i+"c"+c1);
					var e2 = document.getElementById("e"+i+"c"+c2);
					this.createLine(e1, e2, "blue");
				}
			}
		}
	};
	
	this.clearBars = function() {
		for (var i=0; i<this.data.length; i++) {
			this.clearRow(i);
		}
	};
	
	this.clearRow = function(i) {
		var el = document.getElementById("e"+i+"c0");
		while (el) {
			this.removeChildren(el);
			el = el.nextSibling;
		}
	}
	
	//Construct basic table
	{
		var cd = new YAHOO.util.Element(containingDiv);
		this.removeChildren(cd);
	
		var tbl = document.createElement("table");
		YAHOO.util.Dom.addClass(tbl, "planner");
		
		//Build header row
		var headerRow = document.createElement("tr");
		YAHOO.util.Dom.addClass(headerRow, "weekRow");
		//Build first col
		var firstCol = document.createElement("td");
		YAHOO.util.Dom.addClass(firstCol, "firstCol");
		
		var currentDate = new Date();
		currentDate.setTime(this.startDate.getTime());
		
		
		headerRow.appendChild(firstCol);
		for (var i=0; i<numWeeks; i++) {
			var col = document.createElement("th");
			var text = document.createTextNode(currentDate.formatDate("jS M Y"));
			col.appendChild(text);
			col.setAttribute("colspan", 7);
			YAHOO.util.Dom.addClass(col, "weekStart");
			headerRow.appendChild(col);
			currentDate.setDate(currentDate.getDate()+7);			
		}
		
		tbl.appendChild(headerRow);
		
		
		
		var numCols = numWeeks * 7;
		if (rows) {
			for (var i=0; i<rows.length; i++) {
				var r = rows[i];
			
				var detailRow = document.createElement("tr");
				YAHOO.util.Dom.addClass(detailRow, "detailRow");
				
				var detailLabel = document.createElement("td");
				var l = document.createTextNode(r.label);
				detailLabel.appendChild(l);
				detailRow.appendChild(detailLabel);
				
				var even = false;
				for (var j=0; j<numCols; j++) {
					var c = document.createElement("td");
					c.id="e"+i+"c"+j;
					YAHOO.util.Dom.addClass(c, "timebox");
					if (even) {
						YAHOO.util.Dom.addClass(c, "even");
					} else {
						YAHOO.util.Dom.addClass(c, "odd");
					}
					even = !even;
					if (j % 7 == 0) {
						YAHOO.util.Dom.addClass(c, "weekStart");
					}
					detailRow.appendChild(c);
				}
				tbl.appendChild(detailRow);
			}
		}
		
		cd.appendChild(tbl);
	}
};

