if (YAHOO.env.ua.ie > 0) {
	alert("The scheduler does not support Internet Explorer. Please use Firefox");
} 

ITAO.namespace("ITAO.widget.roster");

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

var defaultFilter = {
	showAssignment: function(assignment, roster) {
		var start = roster.getDisplayedStartDate().getTime();
		var end = roster.getDisplayedEndDate().getTime();
		if (assignment.dateRange.getEndDate().getTime() < start)
			return false;
		if (assignment.dateRange.getStartDate().getTime() > end)
			return false;
		return true;
	},
	showAssignable: function() {
		return true;
	},
	showAllocation: function() {
		return true;
	}
};

/**
 * <p>The 'view' of the Roster component. Responsible for drawing the table, bars etc.
 * Mouse events are forwarded to any listeners (generally a Controller instance).</p>
 * <p>The data is represented by a model instance. When the visible date range changes, model is
 * queried for assignments, assignables and allocations. The entire table is redrawn if the date range changes.
 * The date range may be changed by calling zoomIn, zoomOut, today, showNext, showPrevious or setDate.</p>
 * 
 * @constructor
 */
ITAO.widget.roster.Roster = function(containingDiv, model, startDate, units) {
	var _containingDiv = document.getElementById(containingDiv);
	var _model = model;
	var _startDate = startDate;
	var _units = units;
	
	var _numCols;
	var _numColsDefaults = {'day':42,'week':12,'month':6};
	
	var _colWidth;
	var _colWidthDefaults = {'day':20,'week':70,'month':140};
	
	var _displayedStartDate;
	var _displayedEndDate;
	
	var _tableEl;
	var _tableHeadEl;
	var _tableBodyEl;	
	
	var _assignmentBarsContainerMap = {};
	var _assignableBarsContainerMap = {};
	
	var _dateStartPos = {};
	var _dateEndPos = {};
	
	var _colDate = [];
	var _xPosDate = [];
	var _firstUnitCol;
	
	var _filter = defaultFilter;
	
	var _assignableRowMap = {};
	
	var _classes = [
		"blue", "yellow", "purple", "orange", "pink", "ruby", "black"
	];
	
	var FIRST_COL_WIDTH = 150;
	var TABLE_BORDER_WIDTH = 1;
	
	//initialise defaults
	{
		if (!_startDate) {
			_startDate = new Date();
		}
		_startDate.setHours(0);
		_startDate.setMinutes(0);
		_startDate.setSeconds(0);
		_startDate.setMilliseconds(0);
		
		//one of day/week/month
		//Default: day;
		if (!_units)
			_units = 'day';
		
		//The pixel width to display each unit
		_colWidth = _colWidthDefaults[_units];
		
		//The number of units to display at one time
		_numCols = _numColsDefaults[_units];
	}
	
	var createHead = function() {
		_dateStartPos = {};
		_dateEndPos = {};
		_colDate = [];
		_xPosDate = [];
		
		var thead = document.createElement("thead");
		var topRow = document.createElement("tr");
		var bottomRow = document.createElement("tr");
		
		thead.appendChild(topRow);
		thead.appendChild(bottomRow);
		
		var firstCol = document.createElement("th");
		YAHOO.util.Dom.addClass(firstCol, "firstCol");
		bottomRow.appendChild(firstCol);
		
		firstCol = document.createElement("th");
		YAHOO.util.Dom.addClass(firstCol, "firstCol");
		topRow.appendChild(firstCol);
		
		var labels = createHeadLabels();
		
		var topStartLabel;
		if (_units == 'day') {
			topStartLabel = _displayedStartDate.formatDate("M, Y");
		} else if (_units == 'week') {
			topStartLabel = _displayedStartDate.formatDate("M, Y");
		} else {
			topStartLabel = _displayedStartDate.formatDate("Y");
		}
		
		var currentTopColSpan = 0;
		var minLeftSizeForFirstLabel = 50;
		var leftSorted = false;
		
		var lastLabelAdded;
		
		var bottomRowLabels = labels[1];
		for (var i=0; i<bottomRowLabels.length; i++) {
			var col = document.createElement("th");
			col.appendChild(document.createTextNode(bottomRowLabels[i].label));
			if (bottomRowLabels[i].isToday) {
				YAHOO.util.Dom.addClass(col, "today");
			}
			var w = _colWidth - 1;
			YAHOO.util.Dom.setStyle(col, "width", w+"px");
			YAHOO.util.Dom.setStyle(col, "text-align", "center");
			YAHOO.util.Dom.addClass(col, "col");
			if (i == bottomRowLabels.length - 1) {
				YAHOO.util.Dom.addClass(col, "last");
			}
			bottomRow.appendChild(col);
			if (i == 0)
				_firstUnitCol = col;
			
			var colTopLabel = bottomRowLabels[i].topLabel;	
			if (!leftSorted) {
				var totalWidth = i*_colWidth;
				if (totalWidth >= minLeftSizeForFirstLabel) {
					var lbl = document.createElement("th");
					lbl.appendChild(document.createTextNode(topStartLabel));
					topRow.appendChild(lbl);
					lastLabelAdded = lbl;
					leftSorted=true;
				}
			}
			if (colTopLabel) {
				if (!leftSorted) {
					//Left gap wasn't big enough to fit the first label
					leftSorted=true;
				}
				if (lastLabelAdded) {
					lastLabelAdded.setAttribute("colspan", currentTopColSpan);
					currentTopColSpan = 0;
				}
				var lbl = document.createElement("th");
				lbl.appendChild(document.createTextNode(colTopLabel));
				topRow.appendChild(lbl);
				lastLabelAdded = lbl;
			}
			currentTopColSpan++;
		}
		if (lastLabelAdded) {
			lastLabelAdded.setAttribute("colspan", currentTopColSpan);
			currentTopColSpan = 0;
		}
		return thead;
	};
	
	var createHeadLabels = function() {
		var currentDate = _displayedStartDate;
		var today = new Date();
		today.setHours(0);
		today.setMinutes(0);
		today.setSeconds(0);
		today.setMilliseconds(0);
		
		var dateIncrement;
		var labelFormatter;
		var highlightToday;
		
		if (_units == 'day') {
			dateIncrement = function(d) {
				var newDate = new Date();
				newDate.setTime(d.getTime());
				newDate.setDate(d.getDate()+1);
				return newDate;
			};
			labelFormatter = function(d) {
				return d.getDate();
			};
			highlightToday = function(d) {
				return d.getTime() == today.getTime();
			};
			startOfEraLabel = function(d) {
				if (d.getDate() == 1) {
					return d.formatDate("M, Y");
				}
			};
		} else if (_units == 'week') {
			dateIncrement = function(d) {
				var newDate = new Date();
				newDate.setTime(d.getTime());
				newDate.setDate(d.getDate()+7);
				return newDate;
			};
			labelFormatter = function(d) {
				return d.getDate();
			};
			highlightToday = function(d) {
				var plusOne = new Date();
				plusOne.setTime(d.getTime());
				plusOne.setDate(d.getDate()+7);
				return d.getTime() <= today.getTime()
				  &&   plusOne.getTime() >  today.getTime();
			};
			startOfEraLabel = function(d) {
				if (d.getDate() <= 7) {
					return d.formatDate("M, Y");
				}
			};
		} else if (_units == 'month') {
			dateIncrement = function(d) {
				var newDate = new Date();
				newDate.setTime(d.getTime());
				newDate.setMonth(d.getMonth()+1);
				return newDate;
			};
			labelFormatter = function(d) {
				return d.formatDate("F");
			};
			highlightToday = function(d) {
				return d.getMonth() == today.getMonth() && d.getFullYear() == today.getFullYear();
			};
			startOfEraLabel = function(d) {
				if (d.getMonth() == 0) {
					return d.formatDate("Y");
				}
			};
		}
		
		var bottomRowLabels = [];
		for (var i=0; i<_numCols; i++) {
			var thisCol = {};
			thisCol.label = labelFormatter(currentDate);
			if (highlightToday(currentDate)) {
				thisCol.isToday = true;
			}
			thisCol.topLabel=startOfEraLabel(currentDate);
			bottomRowLabels.push(thisCol);
			var oldDate = currentDate;
			currentDate = dateIncrement(currentDate);
			addColToXPos(oldDate, currentDate);
		}
		
		return [{}, bottomRowLabels];
	};
	
	function addColToXPos(startDate, endDate) {
		_colDate.push(startDate);
		
		var d = new Date();
		var startTime = startDate.getTime();
		d.setTime(startDate.getTime());
		var oldDt;
		var duration = endDate.getTime() - startDate.getTime();
		
		var startPos = (_colDate.length-1)*_colWidth;
		while (d.getTime() < endDate.getTime()) {
			var t = d.getTime();
			var offset = t - startTime;
			var dx = offset/duration*_colWidth;
			
			var pos = startPos+dx;
			_dateStartPos[t] = pos;
			if (oldDt)
				_dateEndPos[oldDt] = pos-1;
			oldDt = t;
			d.setDate(d.getDate()+1);
		}
		
		_dateEndPos[oldDt] = ((_colDate.length)*_colWidth) -1;
		
		/*
		var tempDate = ne Date();
		var startTime = startDate.getTime();
		var endTime = endDate.getTime();
		var duration = endTime - startTime;
		
		//force float
		var inc = duration/(_colWidth*1.0);
		var total = 0;
		var prev = startTime;
		_dateStartPos[prev] = _xPosDate.length;
		
		for (var i=0; i<_colWidth; i++) {
			tempDate.setTime(Math.floor(startTime+total));
			tempDate.setHours(0);
			tempDate.setMinutes(0);
			tempDate.setSeconds(0);
			tempDate.setMilliseconds(0);
			var t = tempDate.getTime();
			if (t > endTime)
				t = endTime;
			_xPosDate.push(t);
			if (t != prev && t < endTime) {
				_dateEndPos[prev] = _xPosDate.length - 2;
				_dateStartPos[t] = _xPosDate.length - 1;
				prev = t;
			}
			total += inc;
		}
		_dateEndPos[prev] = _xPosDate.length-1;
		*/
	}
	
	var resetTableSize = function() {
		var width = _numCols*_colWidth;
		width += FIRST_COL_WIDTH + 2* TABLE_BORDER_WIDTH;
		width++;
		YAHOO.util.Dom.setStyle(_tableEl, "width", width+"px");
	};
	
	//setup table
	var _tableEl;
	var _tableHeadEl;
	var _tableBodyEl;
	var _assignmentDetailsDiv;
	var _assignmentDetailsBody;
	var _assignmentDetailsOverlay;
	
	var setupTable = function() {
		removeChildren(_containingDiv);
		_tableEl = document.createElement("table");
		_tableHeadEl = document.createElement("thead");
		_tableBodyEl = document.createElement("tbody");
		
		_tableEl.appendChild(_tableHeadEl);
		_tableEl.appendChild(_tableBodyEl);
		
		_assignmentDetailsDiv = document.createElement("div");
		_assignmentDetailsDiv.id = "assignmentDetails";
		_assignmentDetailsBody = document.createElement("div");
		YAHOO.util.Dom.addClass(_assignmentDetailsBody, "bd");
		YAHOO.util.Dom.addClass(_assignmentDetailsBody, "assignmentDetailsBody");
		_assignmentDetailsBody.appendChild(document.createTextNode("Hello"));
		_assignmentDetailsDiv.appendChild(_assignmentDetailsBody);
		_assignmentDetailsOverlay = new YAHOO.widget.Overlay(_assignmentDetailsDiv);
		_assignmentDetailsOverlay.cfg.setProperty("constraintoviewport", true);
		//_assignmentDetailsOverlay.cfg.setProperty("effect", {effect:YAHOO.widget.ContainerEffect.FADE,duration:0.25} );
		_assignmentDetailsOverlay.render();
		
		YAHOO.util.Dom.setStyle(_tableEl, "border-width", TABLE_BORDER_WIDTH + "px");
		YAHOO.util.Dom.addClass(_tableEl, "planner");
		
		resetTableSize();
		
		_containingDiv.appendChild(_tableEl);
		_containingDiv.appendChild(_assignmentDetailsDiv);
	}
	
	function reloadFromModel() {
		refreshBody();
	}
	
	function allocationAdded(type, args, me) {
		var allocation = args[0];
		var assignable = allocation.getAssignable();
		var row = _assignableRowMap[assignable.type+assignable.id];
		if (row) {
			row.insertBar(allocation.getDateRange(), allocation.getAssignment().id, allocation);
		}
	}
	
	function allocationRemoved(type, args, me) {
		var allocation = args[0];
		var assignable = allocation.getAssignable();
		var row = _assignableRowMap[assignable.type+assignable.id];
		if (row) {
			row.removeBar(allocation);
		}
	}
	
	var refreshHead = function() {
		var oldHead = _tableHeadEl;
		var newHead = createHead();
		_tableEl.replaceChild(newHead, oldHead);
		_tableHeadEl = newHead;
	};
	
	var refreshBody = function() {
		var newBody = document.createElement("tbody");
		
		_assignmentBarsContainerMap = {};
		_assignableBarsContainerMap = {};
		
		var assignments = _model.getAssignments();
		for (var i=0; i<assignments.length; i++) {
			var assignment = assignments[i];
			if (!_filter || _filter.showAssignment(assignment, that)) {
				var assignmentRow = createAssignmentRow(assignment);
				YAHOO.util.Dom.addClass(assignmentRow, "detailRow");
				newBody.appendChild(assignmentRow);
			}
		}
		
		newBody.appendChild(createSeparator());		
		
		var assignables = _model.getAssignables();
		_assignableRowMap = {};
		for (var i=0; i<assignables.length; i++) {
			var assignable = assignables[i];
			if (!_filter || _filter.showAssignable(assignable, that)) {
				var assignableRow = createAssignableRow(assignable);
				_assignableRowMap[assignable.type+assignable.id] = assignableRow[1];
				YAHOO.util.Dom.addClass(assignableRow[0], "detailRow");
				newBody.appendChild(assignableRow[0]);
			}
		}
		
		var oldBody = _tableBodyEl;
		_tableEl.replaceChild(newBody, oldBody);
		_tableBodyEl = newBody;
	};
	
	var refreshView = function() {
		determineDates();
		setupTable();
		refreshHead();
		refreshBody();
	};
	
	function registerMouseEventHandlers(el, obj, fn) {
		YAHOO.util.Event.addListener(el, "click", fn, obj);
		YAHOO.util.Event.addListener(el, "mousedown", fn, obj);
		YAHOO.util.Event.addListener(el, "mouseup", fn, obj);
		YAHOO.util.Event.addListener(el, "mouseover", fn, obj);
		YAHOO.util.Event.addListener(el, "mouseout", fn, obj);
	}
	
	var createAssignmentRow = function(assignment) {
		var rowEl = document.createElement("tr");
		var tdEl = document.createElement("td");
		YAHOO.util.Dom.addClass(tdEl, "firstCol");
		
		var label = document.createTextNode(assignment.label);
		tdEl.appendChild(label);
		rowEl.appendChild(tdEl);
		registerMouseEventHandlers(tdEl, assignment, handleAssignmentLabelMouseEvent);
		
		var bc = createBarsContainer();
		registerMouseEventHandlers(bc, assignment, handleBarAreaMouseEvent);
		_assignmentBarsContainerMap[assignment] = bc;
		
		var bar = new ITAO.widget.roster.Bar(assignment.dateRange, that, null, assignment.id, assignment);
		registerMouseEventHandlers(bar.barDiv, bar, handleBarMouseEvent);
		bc.appendChild(bar.barDiv);
		
		rowEl.appendChild(bc);
		return rowEl;
	};
	
	var createSeparator = function() {
		var rowEl = document.createElement("tr");
		var tdEl = document.createElement("td");
		YAHOO.util.Dom.addClass(tdEl, "firstCol");
		
		rowEl.appendChild(tdEl);
		
		tdEl = document.createElement("td");
		tdEl.setAttribute("colspan", _numCols);
		YAHOO.util.Dom.addClass(tdEl, "detail"+_colWidth);
		tdEl.appendChild(document.createElement("hr"));
		
		rowEl.appendChild(tdEl);
		
		registerMouseEventHandlers(tdEl, null, handleBarAreaMouseEvent);
		
		YAHOO.util.Dom.addClass(rowEl, "separator");
		return rowEl;
	};
		var createAssignableRow = function(assignable) {
		var rowEl = document.createElement("tr");
		var tdEl = document.createElement("td");
		YAHOO.util.Dom.addClass(tdEl, "firstCol");
		
		var label = document.createTextNode(assignable.label);
		tdEl.appendChild(label);
		rowEl.appendChild(tdEl);
		registerMouseEventHandlers(tdEl, assignable, handleAssignableLabelMouseEvent);
		
		var bc = createBarsContainer();
		var row = new ITAO.widget.roster.Row(that, assignable, bc);
		
		registerMouseEventHandlers(bc, assignable, handleBarAreaMouseEvent);
		_assignableBarsContainerMap[assignable] = bc;
		
		var allocations = _model.getAllocations(assignable, _displayedStartDate, _displayedEndDate);

		var p;
		for (var i=0; i<allocations.length; i++) {
			var a = allocations[i];
			
			if (!_filter || _filter.showAllocation(a, that)) {
				var id = a.getAssignment().id;
				var bar = row.insertBar(a.getDateRange(), id, a);
				registerMouseEventHandlers(bar.barDiv, bar, handleBarMouseEvent);
				//bc.appendChild(bar.barDiv);
				p = bar;
			}
		}
		
		rowEl.appendChild(bc);
		return [rowEl,row];
	};
	
	var createBarsContainer = function() {
		var tdEl = document.createElement("td");
		tdEl.setAttribute("colspan", _numCols);
		YAHOO.util.Dom.addClass(tdEl, "detail"+_colWidth);
		return tdEl;
	};
	
	var createEmptyRow = function() {
		return document.createElement("tr");
	};
	
	var adjustStartDate = function() {
		var newDate = new Date();
		newDate.setTime(_startDate.getTime());
		 
		if (_units == 'day' || _units == 'week') {
			var d = newDate.getDay();
			d--;
			if (d < 0)
				d = 6;
			newDate.setDate(newDate.getDate() - d);
		} else {
			newDate.setDate(1);
		}
		_displayedStartDate = newDate;
	};
	
	var determineEndDate = function() {
		var newDate = new Date();
		newDate.setTime(_displayedStartDate.getTime());
		if (_units == 'day') {
			newDate.setDate(newDate.getDate()+_numCols-1);
		} else if (_units == 'week') {
			newDate.setDate(newDate.getDate()+_numCols*7-1);
		} else if (_units == 'month') {
			newDate.setMonth(newDate.getMonth()+_numCols);
			newDate.setDate(newDate.getDate()-1);
		}
		_displayedEndDate = newDate;
	};
	
	var determineDates = function() {
		adjustStartDate();
		determineEndDate();
	};
	
	function _getBounds(dateRange) {
		var sd = dateRange.getStartDate();
		var ed = dateRange.getEndDate();
		
		return [0,100,true,true];
	}
	
	function handleBarMouseEvent(e, bar) {
		that.barMouseEvent.fire(e, bar);
	}
	
	function handleBarAreaMouseEvent(e, row) {
		that.barAreaMouseEvent.fire(e, row);
	}
	
	function handleAssignmentLabelMouseEvent(e, assignment) {
		that.assignmentLabelMouseEvent.fire(e, assignment);
	}
	
	function handleAssignableLabelMouseEvent(e, assignable) {
		that.assignableLabelMouseEvent.fire(e, assignable);
	}
	
	this.getDisplayedStartDate = function() {
		return _displayedStartDate;
	};
	
	this.getDisplayedEndDate = function() {
		return _displayedEndDate;
	};
	
	this.setUnits = function(units) {
		var oldUnits = _units;
		if (oldUnits == units)
			return;
		_units = units;
		_numCols = _numColsDefaults[_units];
		_colWidth = _colWidthDefaults[_units];
		refreshView();
	};
	
	this.setStartDate = function(newDate) {
		if (newDate.getTime() != _startDate.getTime()) {
			_startDate = newDate;
			refreshView();
		}
	};
	
	this.zoomIn = function() {
		if (_units == 'month')
			that.setUnits('week');
		else if (_units == 'week')
			that.setUnits('day');
	};
	this.zoomOut = function() {
		if (_units == 'week')
			that.setUnits('month');
		if (_units == 'day')
			that.setUnits('week');
	};
	this.showNext = function() {
		var newDate = new Date();
		newDate.setTime(_startDate.getTime());
		if (_units == 'day') {
			newDate.setDate(newDate.getDate() + 7);
		} else if (_units == 'week') {
			newDate.setDate(newDate.getDate() + 14);
		} else if (_units == 'month') {
			newDate.setMonth(newDate.getMonth() + 2);
		}
		that.setStartDate(newDate);
	};
	this.showPrevious = function() {
		var newDate = new Date();
		newDate.setTime(_startDate.getTime());
		if (_units == 'day') {
			newDate.setDate(newDate.getDate() - 7);
		} else if (_units == 'week') {
			newDate.setDate(newDate.getDate() - 14);
		} else if (_units == 'month') {
			newDate.setMonth(newDate.getMonth() - 2);
		}
		that.setStartDate(newDate);
	};
	this.today = function() {
		var newDate = new Date();
		newDate.setHours(0);
		newDate.setMinutes(0);
		newDate.setSeconds(0);
		newDate.setMilliseconds(0);
		that.setStartDate(newDate);
	};
	this.getBounds= function(dateRange) {
		var sd = dateRange.getStartDate();
		var ed = dateRange.getEndDate();
		var startPos, endPos;
		var showLeft, showRight;
		
		if (sd.getTime() > _displayedEndDate.getTime()
		 || ed.getTime() < _displayedStartDate.getTime()
		) {
			return [0,0,false,false];
		}
		
		if (sd.getTime() < _displayedStartDate.getTime()) {
			startPos = 0;
			showLeft = false;
		} else {
			startPos = this.getDateStartPos(sd);
			showLeft = true;
		}
		
		if (ed.getTime() > _displayedEndDate.getTime()) {
			endPos = _numCols * _colWidth-1;
			showRight = false;
		} else {
			endPos = this.getDateEndPos(ed);
			showRight = true;
		}
		return [startPos, endPos, showLeft, showRight];
	};
	this.getDateStartPos = function(date) {
		var t = date.getTime();
		var p = _dateStartPos[t];
		if (!p && p != 0)
			return -1;
		else return p;
	};
	this.getDateEndPos = function(date) {
		var t = date.getTime();
		var p = _dateEndPos[t];
		//p += _colWidth-1;
		if (!p && p != 0)
			return -1;
		else return p;
	};
	this.getDateAtXPos = function(x) {
		var xOffset = YAHOO.util.Dom.getXY(_firstUnitCol)[0];
		x -= xOffset;
		
		var pos = x / _colWidth;
		var col = Math.floor(pos);
		var ratio = pos - col;
		var colStartDate = _colDate[col];
		
		var d = new Date();
			d.setTime(colStartDate.getTime());
		if (_units == 'day') {
			return d;
		} else if (_units == 'week') {
			var extraDays = Math.round(ratio*6);
			d.setDate(d.getDate()+extraDays);
			return d;
		} else if (_units == 'month') {
			var nextMonth = new Date();
			nextMonth.setTime(d.getTime());
			nextMonth.setMonth(nextMonth.getMonth()+1);
			var duration = nextMonth.getTime()-d.getTime();
			var extraMillis = duration*ratio;
			d.setTime(d.getTime()+extraMillis);
			d.setHours(0);
			d.setMinutes(0);
			d.setSeconds(0);
			d.setMilliseconds(0);
			return d;
		}
	};
	this.getClass = function(i) {
		i = i%_classes.length;
		return _classes[i];
	};
	
	this.setFilter = function(filter) {
		if (filter)
			_filter = filter;
		else
			_filter = defaultFilter;
		refreshBody();
	};
	
	this.showAssignmentDetails = function(assignment, x, y, div) {
		_assignmentDetailsOverlay.hide();
		removeChildren(_assignmentDetailsBody);
		_assignmentDetailsBody.appendChild(document.createTextNode(assignment.label));
		_assignmentDetailsOverlay.cfg.setProperty("xy", [x,y+10]);
		_assignmentDetailsOverlay.show();
	};
	
	this.hideAssignmentDetails = function() {
		_assignmentDetailsOverlay.hide();
	}
	
	//EVENTS
	this.barMouseEvent = new YAHOO.util.CustomEvent("BarMouseEvent", this);
	this.barAreaMouseEvent = new YAHOO.util.CustomEvent("BarAreaMouseEvent", this);
	this.assignmentLabelMouseEvent = new YAHOO.util.CustomEvent("AssignmentLabelMouseEvent", this);
	this.assignableLabelMouseEvent = new YAHOO.util.CustomEvent("AssignableLabelMouseEvent", this);
	
	var that = this;
	
	{
		refreshView();
		_model.modelRefreshed.subscribe(reloadFromModel);
		_model.allocationAdded.subscribe(allocationAdded);
		_model.allocationRemoved.subscribe(allocationRemoved);
		_model.prepareData(_displayedStartDate, _displayedEndDate);
	}
};

ITAO.widget.roster.Row = function(roster, rowData, tdEl) {
	var _bars = [];
	var _roster = roster;
	var _rowData = rowData;
	
	var _tdEl = tdEl
	
	this.insertBar = function(dateRange, id, barData) {
		var startTime = dateRange.getStartDate().getTime();
		var endTime = dateRange.getEndDate().getTime();
		var lastB;
		var nextB;
		var i;
		for (i=0; i<_bars.length; i++) {
			nextB = _bars[i];
			if (nextB.dateRange.getStartDate().getTime() >= startTime)
				break;
			lastB = nextB;
			nextB = null;
		}
		//Do these two checks to make sure the dates do not overlap with the
		//previous and next bars. If the view is consistent with the model and
		//bars are only added through the event handlers handling Model events
		//then they should never overlap.
		if (lastB) {
			if (lastB.userData.getDateRange().getEndDate().getTime() >= startTime)
				return;
		}
		if (nextB) {
			if (nextB.userData.getDateRange().getStartDate().getTime() <= endTime)
				return;
		}
		var newBar = new ITAO.widget.roster.Bar(dateRange, _roster, lastB, id, barData);
		if (nextB) {
			nextB.setPrevious(newBar);
			_tdEl.insertBefore(newBar.barDiv, nextB.barDiv);
		} else {
			_tdEl.appendChild(newBar.barDiv);
		}
		_bars.splice(i,0,newBar);
		return newBar;
	}
	
	this.removeBar = function(barData) {
		for (var i=0; i<_bars.length; i++) {
			var bar = _bars[i];
			if (bar.userData == barData) {
				var p,n;
				if (i >0)
					p = _bars[i];
				if (i+1<_bars.length)
					n = _bars[i+1];
				_bars.splice(i,1);
				_tdEl.removeChild(bar.barDiv);
				if (n) {
					n.setPrevious(p);
				}
			}
		}
	}
}

ITAO.widget.roster.Bar = function(dateRange, roster, previous, i, userData) {
	this.dateRange = dateRange;
	this.userData = userData;
	this.endPosChanged = new YAHOO.util.CustomEvent("EndPosChanged", this);
	var _margin;
	
	var _tooltipText;
	if (instanceOf(userData, ITAO.widget.roster.Allocation)) {
		_tooltipText = userData.getAssignment().label;
	} else {
		_tooltipText = userData.label;
	}
	
	function resetMargin() {
		var oldMargin = _margin;
		_margin = that.startPos;
		if (that.previous)
			_margin -= that.previous.endPos;
		
		if (_margin != oldMargin) {
			YAHOO.util.Dom.setStyle(that.barDiv, "margin-left", _margin+"px");
			that.endPosChanged.fire();
		}
	}
	
	function previousEndPosChangedHandler(type, args, me) {
		resetMargin();
	}
	
	function setPrevious(prev) {
		if (that.previous)
			that.previous.endPosChanged.unsubscribe(previousEndPosChangedHandler);
		that.previous = prev;
		if (prev) {
			that.previous.endPosChanged.subscribe(previousEndPosChangedHandler, that);
		}
		resetMargin();
	}
	
	var that = this;
	
	//setup bar element
	{
		setPrevious(previous);
		var bounds = roster.getBounds(this.dateRange);
		
		this.startPos = bounds[0];
		this.endPos = bounds[1];
		this.leftHandleVisible = bounds[2];
		this.rightHandleVisible = bounds[3];
		this.setPrevious = setPrevious;
		
		var length = this.endPos - this.startPos;
		var lengthRemaining = length;
		
		var entireBar = document.createElement("div");
		
		YAHOO.util.Dom.addClass(entireBar, "entireBar");
		YAHOO.util.Dom.setStyle(entireBar, "float", "left");
		YAHOO.util.Dom.setStyle(entireBar, "width", length+"px");
		
		if (!i) {
			i=0;
		}
		
		that.myclass = roster.getClass(i);
		YAHOO.util.Dom.addClass(entireBar, that.myclass);
		
		if (length < 19) {
			YAHOO.util.Dom.addClass(entireBar, "small");
			entireBar.appendChild(document.createElement("span"));
		} else {
			
			if (this.leftHandleVisible)
				lengthRemaining -= 10;
			if (this.rightHandleVisible)
				lengthRemaining -= 10;
			
			if (this.leftHandleVisible) {
				var leftHandle = document.createElement("span");
				YAHOO.util.Dom.addClass(leftHandle, "leftHandle");
				YAHOO.util.Dom.setStyle(leftHandle, "float", "left");
				YAHOO.util.Dom.setStyle(leftHandle, "display", "block");
				if (length == 19) {
					YAHOO.util.Dom.setStyle(leftHandle, "width", "9px");
				}
				entireBar.appendChild(leftHandle);
			}
			
			var bar = document.createElement("span");
			YAHOO.util.Dom.addClass(bar, "barSegment");
			YAHOO.util.Dom.setStyle(bar, "float", "left");
			YAHOO.util.Dom.setStyle(bar, "display", "block");
			entireBar.appendChild(bar);
			
			if (this.rightHandleVisible) {
				var rightHandle = document.createElement("span");
				YAHOO.util.Dom.addClass(rightHandle, "rightHandle");
				YAHOO.util.Dom.setStyle(rightHandle, "float", "left");
				YAHOO.util.Dom.setStyle(rightHandle, "display", "block");
				entireBar.appendChild(rightHandle);
			}
			
			YAHOO.util.Dom.setStyle(bar, "width", lengthRemaining+"px");
		}
		this.barDiv = entireBar;
		resetMargin();
	}
};