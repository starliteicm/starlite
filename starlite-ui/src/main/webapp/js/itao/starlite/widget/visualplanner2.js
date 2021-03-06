ITAO.namespace("ITAO.widget.planner");
ITAO.namespace("ITAO.util");

function instanceOf(object, constructorFunction) {
  while (object != null) {
    if (object == constructorFunction.prototype)
     {return true}
	 object = object.__proto__;
  }
  return false;
}

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

ITAO.widget.planner.Span = function(type, range, class) {
	var type = type;
	var range = range;
	var class = class;
	
	return {
		getType: function() { return type; },
		getRange: function() { return range; },
		getClass: function() { return class; },
		setType: function(newType) {
			var oldType = type; 
			type = newType;
			this.typeChange.fire(oldType, newType); 
		},
		setRange: function(newRange) {
			var oldRange = range;
			if (!this.beforeRangeChange.fire(oldRange, newRange))
				return;
			range = newRange;
			this.rangeChange.fire(oldRange, newRange);
		},
		setClass: function(newClass) {
			var oldClass = class; 
			class = newClass;
			this.classChange.fire(oldClass, newClass); 
		},
		nudgeRight: function() {
			this.setRange(this.getRange().shiftedRight());
		},
		nudgeLeft: function() {
			this.setRange(this.getRange().shiftedLeft());
		},
		shift: function(amount) {
			this.setRange(this.getRange().shiftedRight(amount));
		},
		moveEnd: function(amount) {
			this.setRange(this.getRange().endShifted(amount));
		},
		moveStart: function(amount) {
			this.setRange(this.getRange().startShifted(amount));
		},
		appendXML: function(doc, el) {
			var sp = doc.createElement("span");
			var t = doc.createElement("type");
			t.appendChild(doc.createTextNode(type));
			sp.appendChild(t);
			
			var r = doc.createElement("range");
			var sd = doc.createElement("start");
			var ed = doc.createElement("end");
			
			r.appendChild(sd);
			r.appendChild(ed);
			
			sd.appendChild(doc.createTextNode(range.getStartDate().formatDate("Y/m/d")));
			ed.appendChild(doc.createTextNode(range.getEndDate().formatDate("Y/m/d")));
			
			sp.appendChild(r);
			
			var c = doc.createElement("class");
			c.appendChild(doc.createTextNode(class));
			sp.appendChild(c);
			
			el.appendChild(sp);
		},
		beforeRangeChange: new YAHOO.util.CustomEvent("BeforeRangeChange"),
		rangeChange: new YAHOO.util.CustomEvent("RangeChange"),
		typeChange: new YAHOO.util.CustomEvent("TypeChange"),
		classChange: new YAHOO.util.CustomEvent("ClassChange")
	}
};

ITAO.widget.planner.Row = function(id, label, spans) {
	var id = id;
	var label = label;
	var spans = [];
	
	var checkSpanRangeChange = function (type, args, me) {
		var spanChanging = me;
		var newStartDate = args[1].getStartDate();
		var newEndDate = args[1].getEndDate();
		for (var i=0; i<spans.length; i++) {
			if (spans[i] == spanChanging) {
				//If it doesn't overlap anything, it's ok
				return !overlapsSurrounding(args[1], i);
			}
		}
	};
	
	var overlapsSurrounding = function(range, i) {
		if (i > 0) {
			//check if left boundary is acceptable
			//If the new start date is less than or equal
			//to the end date of the span to the left
			//then veto this change.
			var spanToLeft = spans[i-1];
			if (spanToLeft.getRange().overlaps(range))
				return true;
		}
		if (i < spans.length-1) {
			//now check the right boundary
			var spanToRight = spans[i+1];
			if (spanToRight.getRange().overlaps(range))
				return true;
		}
		return false;
	};
	
	var canInsert = function(span, i) {
		var range = span.getRange();
		if (i > 0) {
			//check if left boundary is acceptable
			//If the new start date is less than or equal
			//to the end date of the span to the left
			//then veto this change.
			var spanToLeft = spans[i-1];
			if (spanToLeft.getRange().overlaps(range))
				return false;
		}
		if (i < spans.length) {
			//now check the potential right boundary
			var spanToRight = spans[i];
			if (spanToRight.getRange().overlaps(range))
				return false;
		}
		return true;
	};
	
	var doAdd = function(spanToAdd, position) {
		if (canInsert(spanToAdd, position)) {
			spans.splice(position, 0, spanToAdd);
			spanToAdd.beforeRangeChange.subscribe(checkSpanRangeChange, spanToAdd);
			return true;
		}
		return false;
	};
	
	var doRemove = function(spanToRemove) {
		for (var i=0; i<spans.length; i++) {
			var currentSpan = spans[i];
			if (currentSpan == spanToRemove) {
				spans.splice(i, 1);
				return true;
			}
		}
		return false;
	};
	
	var thisObj = {
		getLabel: function() { return label; },
		getSpans: function() {
			//Returns a copy of spans so that the spans that appear
			//in this row cannot be added to or removed from without
			//going through this classes accessor methods.
			var copy = spans.slice();
			return copy;
		},
		setLabel: function(newLabel) {
			var oldLabel = label;
			label = newLabel;
			this.labelChange.fire(oldLabel, newLabel);
		},
		addSpan: function(span) {
			var startDate = span.getRange().getStartDate();
			var endDate = span.getRange().getEndDate();
			for (var i=0; i<spans.length; i++) {
				var currentSpan = spans[i];
				if (currentSpan.getRange().getStartDate() >= startDate) {
					if (doAdd(span, i)) {
						this.spanAdded.fire(span, thisObj);
						return span;
					}
					return;
				}
			}
			if (doAdd(span, spans.length)) {
				this.spanAdded.fire(span, thisObj);
				return span;
			}
			return;
		},
		removeSpan: function(span) {
			if (doRemove(span))
				this.spanRemoved.fire(span, thisObj);
		},
		appendXML: function(doc, el) {
			var r = doc.createElement("row");
			var idEl = doc.createElement("id");
			idEl.appendChild(doc.createTextNode(id));
			r.appendChild(idEl);
			
			var labelEl = doc.createElement("label");
			labelEl.appendChild(doc.createTextNode(label));
			r.appendChild(labelEl);
			
			var spansEl = doc.createElement("spans");
			for (var i=0; i<spans.length; i++) {
				var s = spans[i];
				s.appendXML(doc, spansEl);
			}
			r.appendChild(spansEl);
			el.appendChild(r);
		},
		labelChange: new YAHOO.util.CustomEvent("LabelChange"),
		spanAdded: new YAHOO.util.CustomEvent("SpanAdded"),
		spanRemoved: new YAHOO.util.CustomEvent("SpanRemoved")
	};
	return thisObj;
};

ITAO.widget.planner.PlannerModel = function(xmlDoc) {
	var rows = [];
	
	var thisObj = {
		addRow: function(row) {
			rows[rows.length] = row;
			this.rowAdded.fire(row);
		},
		removeRow: function(row) {
			for (var i=0; i<rows.length; i++) {
				if (rows[i] == row) {
					rows.splice(i, 1);
					this.rowRemoved.fire(row);
					return;
				}
			}
		},
		getRows: function() {
			//return a copy
			return rows.slice();
		},
		toXML: function() {
			var doc = document.implementation.createDocument("", "rows", null);
			var rowsEl = doc.firstChild;
			for (var i=0; i<rows.length; i++) {
				rows[i].appendXML(doc, rowsEl);
			}
			return doc;
		},
		rowAdded: new YAHOO.util.CustomEvent("RowAdded"),
		rowRemoved: new YAHOO.util.CustomEvent("RowRemoved")
	};
	
	if (xmlDoc) {
		var root = xmlDoc.firstChild;
		for (var i=0; i<root.childNodes.length; i++) {
			var rowEl = root.childNode[i];
			
			var rowId = rowEl.firstChild.nodeValue;
			var rowLabel = rowEl.firstChild.nextSibling.nodeValue;
			var rowSpansEl = rowEl.firstChild.nextSibling.nextSibling;
			
			var s = [];
			for (var j=0; j<rowSpansEl.childNodes.length; j++) {
				var spanEl = rowSpansEl.childNodes[j];
				var spanType = spanEl.firstChild.nodeValue;
				var spanFrom = spanEl.firstChild.nextSibling.firstChild.nodeValue;
				var spanTo = spanEl.firstChild.nextSibling.firstChild.nextSibling.nodeValue;
				var spanRange = new ITAO.util.DateRange(spanFrom, spanTo);
				
				var spanClass = spanEl.firstChild.nextSibling.nextSibling.nodeValue;
				
				s[j] = new ITAO.widget.planner.Span(spanType, spanRange, spanClass);
			}
			
			var r = new ITAO.widget.planner.Row(rowId, rowLabel, s);
			thisObj.addRow(r);
		}
	}
	return thisObj;
};

var nextId=1;
function getNextId() {
	return nextId++;
}

ITAO.widget.planner.Bar = function(span, planner, leftBar, row) {
	var span = span;
	var planner = planner;
	var _row = row;
	
	var leftHandleVisible = false;
	var rightHandleVisible = false;
	
	var leftHandleSpan;
	var barSpan;
	var rightHandleSpan;
	var entireBarDiv;
	
	var previousBar = leftBar;
	var nextBar;
	
	var startPos;
	var size;
	var leftMargin;
	var changed = true;
	var currentClass;
	
	var inView;
	
	var rangeChanged = function(type, args, me) {
		redraw();
	};
	
	var clicked = function(e, obj) {
		thisObj.clickEvent.fire(obj.bar, e);
		YAHOO.util.Event.stopEvent(e);
	};
	
	var mouseover = function(e, obj) {
		thisObj.mouseoverEvent.fire(obj.bar, e);
		YAHOO.util.Event.stopEvent(e);
	};
	
	var mouseout = function(e, obj) {
		thisObj.mouseoutEvent.fire(obj.bar, e);
		YAHOO.util.Event.stopEvent(e);
	};
	
	var createBar = function(class) {
		currentClass = class;
		var entireBar = document.createElement("div");
		YAHOO.util.Dom.addClass(entireBar, "entireBar");
		if (class) {
			YAHOO.util.Dom.addClass(entireBar, class);
		}
		YAHOO.util.Dom.setStyle(entireBar, "float", "left");
		
		var leftHandle = document.createElement("span");
		YAHOO.util.Dom.addClass(leftHandle, "leftHandle");
		YAHOO.util.Dom.setStyle(leftHandle, "float", "left");
		YAHOO.util.Dom.setStyle(leftHandle, "display", "block");
		
		var bar = document.createElement("span");
		YAHOO.util.Dom.addClass(bar, "barSegment");
		YAHOO.util.Dom.setStyle(bar, "float", "left");
		YAHOO.util.Dom.setStyle(bar, "display", "block");
		
		var rightHandle = document.createElement("span");
		YAHOO.util.Dom.addClass(rightHandle, "rightHandle");
		YAHOO.util.Dom.setStyle(rightHandle, "float", "left");
		YAHOO.util.Dom.setStyle(rightHandle, "display", "block");
		
		entireBar.appendChild(leftHandle);
		entireBar.appendChild(bar);
		entireBar.appendChild(rightHandle);
		
		leftHandleSpan = leftHandle;
		barSpan = bar;
		entireBarDiv = entireBar;
		
		YAHOO.util.Event.addListener(entireBarDiv, "click", clicked, {bar:thisObj}, true);
		YAHOO.util.Event.addListener(entireBarDiv, "mouseover", mouseover, {bar:thisObj}, true);
		YAHOO.util.Event.addListener(entireBarDiv, "mouseout", mouseout, {bar:thisObj}, true);
		
		rightHandleSpan = rightHandle;
		return entireBar;
	};
	
	var classChanged= function(type, args, me) {
		var newClass = me.getClass();
		if (currentClass) {
			YAHOO.util.Dom.removeClass(entireBarDiv, currentClass);
		}
		if (newClass) {
			YAHOO.util.Dom.addClass(entireBarDiv, newClass);
		}
		
		currentClass = newClass;
	};
	
	span.rangeChange.subscribe(rangeChanged, span); 
	span.classChange.subscribe(classChanged, span);
	
	var redraw = function() {
		calculateSizeAndPos();
		calculateLeftMargin();
		if (changed) {
			setLeftMargin();
			setSize();
			changed = false;
			thisObj.sizeOrLocationChange.fire();
		}
	};
	
	var display = function() {
		var b = createBar(span.getClass());
		redraw();
	};
	
	var calculateSizeAndPos = function() {
		var oldStartPos = startPos;
		var oldSize = size;
		
		startPos = planner.convertDateToXPosRel(span.getRange().getStartDate());
		var endDatePlusOne = new Date();
		endDatePlusOne.setTime(span.getRange().getEndDate());
		endDatePlusOne.setDate(endDatePlusOne.getDate()+1);
		var endPos = planner.convertDateToXPosRel(endDatePlusOne);
		
		var totalWidth = planner.getScale() * planner.getNumCols();
		
		if (endPos < 0 || startPos > totalWidth-1) {
			inView = false;
			startPos = -1;
			size = -1;
			
		} else {
		
			inView = true;
		
			leftHandleVisible = startPos >= 0;
			rightHandleVisible = endPos <= totalWidth;
		
			if (!leftHandleVisible) {
				startPos = 0;
			}
		
			if (!rightHandleVisible) {
				endPos = totalWidth;
			}

			size = endPos - startPos;
		}
			
		if (oldSize != size || oldStartPos != startPos) {
			changed = true;
		}
	};
	
	var calculateLeftMargin = function() {
		var oldLeftMargin = leftMargin;
		var leftEndPos;
		
		if (previousBar) {
			leftEndPos = previousBar.getEndPos();
		} else {
			leftEndPos = 0;
		}
		
		var sp = startPos;
		
		leftMargin = startPos - leftEndPos;
		
		var lm = leftMargin;
		
		var plannerWidth = planner.getScale()*planner.getNumCols(); 
		if (startPos+size >= plannerWidth) {
			size = plannerWidth - startPos - 1;
		}
		if (oldLeftMargin != leftMargin)
			changed = true;
	};
	
	var previousChanged = function(type, args, me) {
		redraw();
	};
	
	if (previousBar)
		previousBar.sizeOrLocationChange.subscribe(previousChanged, previousBar);
	
	var setLeftMargin = function() {
		YAHOO.util.Dom.setStyle(entireBarDiv, "margin-left", leftMargin);
	};
	
	var setSize = function() {
		if (!inView) {
			YAHOO.util.Dom.setStyle(leftHandleSpan, "display", "none");
			YAHOO.util.Dom.setStyle(rightHandleSpan, "display", "none");
			YAHOO.util.Dom.setStyle(barSpan, "display", "none");
			return;
		}
		
		if (size < 16) {
			YAHOO.util.Dom.setStyle(leftHandleSpan, "display", "none");
			YAHOO.util.Dom.setStyle(rightHandleSpan, "display", "none");
			YAHOO.util.Dom.setStyle(barSpan, "display", "none");
			YAHOO.util.Dom.setStyle(entireBarDiv, "width", 0);
			size = 0;
			return;
		} else if (size < 20) {
			size = 20;
		}
		
			YAHOO.util.Dom.removeClass(entireBarDiv, "small");
			var sizeLeft = size;
			if (leftHandleVisible) {
				YAHOO.util.Dom.setStyle(leftHandleSpan, "display", "block");
				YAHOO.util.Dom.setStyle(leftHandleSpan, "width", "10px");
				sizeLeft -= 10;
			} else {
				YAHOO.util.Dom.setStyle(leftHandleSpan, "display", "none");
			}
			
			if (rightHandleVisible) {
				YAHOO.util.Dom.setStyle(rightHandleSpan, "display", "block");
				YAHOO.util.Dom.setStyle(rightHandleSpan, "width", "10px");
				sizeLeft -= 10;
			} else {
				YAHOO.util.Dom.setStyle(rightHandleSpan, "display", "none");
			}
			
			YAHOO.util.Dom.setStyle(barSpan, "display", "block");
			YAHOO.util.Dom.setStyle(barSpan, "width", sizeLeft+"px");
			
			YAHOO.util.Dom.setStyle(entireBarDiv, "width", size+"px");
		
	};
	
	var thisObj = {
		getNext: function() { return nextBar; },
		getPrevious: function() { return previousBar; },
		setNext: function(newNext) { nextBar = newNext; },
		setPrevious: function(newPrevious) {
			var oldPrevious = previousBar; 
			previousBar = newPrevious;
			if (oldPrevious) {
				oldPrevious.sizeOrLocationChange.unsubscribe(previousChanged);
			}
			if (newPrevious)
				newPrevious.sizeOrLocationChange.subscribe(previousChanged, newPrevious);
			redraw();
		},
		getDiv: function() { return entireBarDiv; },
		getStartPos: function() { return startPos; },
		getEndPos: function() { return startPos+size; },
		getSpan: function() { return span; },
		getRow: function() { return _row; },
		sizeOrLocationChange: new YAHOO.util.CustomEvent("sizeOrLocationChange"),
		clickEvent: new YAHOO.util.CustomEvent("barClicked"),
		mouseoverEvent: new YAHOO.util.CustomEvent("mouseoverBar"),
		mouseoutEvent: new YAHOO.util.CustomEvent("mouseoutBar")
	};
	display();
	return thisObj;
};

ITAO.widget.planner.BarsContainer = function(row, rowIndex, detailRow, planner) {
	var _row = row;
	var _rowIndex = rowIndex;
	var _detailRow = detailRow;
	var _planner = planner;
	
	var _firstBar;
	
	var doInsert = function(b, prev, next) {
		if (!prev)
			_firstBar = b;
		else
			prev.setNext(b);
			
		if (next) {
			b.setNext(next);
			next.setPrevious(b);
		}
	};
	
	var doAdd = function(span) {
		var nextBar = _firstBar;
		var previousBar;
		
		while (nextBar && nextBar.getSpan().getRange().getStartDate() < span.getRange().getStartDate()) {
			previousBar = nextBar;
			nextBar = nextBar.getNext();
		}
		
		var b = new ITAO.widget.planner.Bar(span, planner, previousBar, _row);
		
		doInsert(b, previousBar, nextBar);
		
		if (nextBar) {
			var d = nextBar.getDiv();
			_detailRow.insertBefore(b.getDiv(), d);
		} else {
			_detailRow.appendChild(b.getDiv());
		}
		_planner.barAdded(b);
		return b;
	};
	
	var doRemove = function(span) {
		var cb = _firstBar;
		
		while (cb && cb.getSpan() != span) {
			cb = cb.getNext();
		}
		
		if (!cb) {
			//this row didn't even contain the span
			return;
		}
		
		_detailRow.removeChild(cb.getDiv());
		var nextBar = cb.getNext();
		var previousBar = cb.getPrevious();
		
		if (previousBar) {
			previousBar.setNext(nextBar);
		} else {
			_firstBar = nextBar;
		}
		if (nextBar) {
			nextBar.setPrevious(previousBar);
		}
	};
	
	var spanAdded = function(type, args, me) {
		doAdd(args[0]);
	};
	
	var spanRemoved = function(type, args, me) {
		doRemove(args[0]);
	};
	
	_row.spanAdded.subscribe(spanAdded, _row);
	_row.spanRemoved.subscribe(spanRemoved, _row);
	
	var thisObj = {
		addBar: function(span) {
			return doAdd(span);
		}
	};
	
	return thisObj;
};

ITAO.widget.planner.VisualPlanner = function(containingDiv, startDate, units, numCols, scale) {
	var rows = [];
	var bars = [];
	var containingDiv = containingDiv;
	var _startDate = new Date();
	if (typeof startDate == 'string')
		_startDate.setTime(Date.parse(startDate));
	else {
		_startDate.setTime(startDate.getTime());
	}
	
	var _controller;
	var _selected;
	
	var _endDate = new Date();
	
	var mode = "insert";
	var subMode = "awaitingFirst";
	var _tmpObj;
	
	_startDate.setHours(0);
	_startDate.setMinutes(0);
	_startDate.setSeconds(0);
	_startDate.setMilliseconds(0);
	
	var numWeeks = numWeeks;
	var table;
	var tableBody;
	var firstUnitCol;
	
	var _actionMap = {};
	
	//one of day/week/month
	//Default: day;
	var _units = 'day';
	if (units)
		_units = units;
	
	//The pixel width to display each unit
	var _scale;
	if (scale) {
		_scale = scale;
	} else {
		if (_units == 'day')
			_scale = 20;
		else if (_units == 'week')
			_scale = 70;
		else if (_units == 'month')
			_scale = 140;
	}
	
	//The number of units to display at one time
	var _numCols;
	if (numCols) {
		_numCols = numCols;
	} else {
		if (_units == 'day')
			//show 6 weeks worth of days
			_numCols = 6*7;
		else if (_units == 'week')
			//show 12 weeks - roughly 3 months
			_numCols = 12;
		else if (_units == 'month')
			//show 6 months
			_numCols = 6;
	}
	
	{
		_endDate.setTime(_startDate.getTime());
		if (_units == 'day') {
			_endDate.setDate(_endDate.getDate() + _numCols);
		} else if (_units == 'week') {
			_endDate.setDate(_endDate.getDate() + _numCols*7);
		} else if (_units == 'month') {
			_endDate.setMonth(_endDate.getMonth() + _numCols);
		}
	}
	
	var _adjustedStartDate = new Date();
	//adjust start date
	{
		_adjustedStartDate.setTime(_startDate.getTime());
		if (_units == 'month') {
			_adjustedStartDate.setDate(1);
		}
		_startDate = _adjustedStartDate;
	}
	
	//Construct basic table
	var constructBasicTable = function() {
		var cd = new YAHOO.util.Element(containingDiv);
		removeChildren(cd);
	
		var tbl = document.createElement("table");
		table = tbl;
		YAHOO.util.Dom.setStyle(tbl, "width", _numCols*_scale+152+"px");
		YAHOO.util.Dom.setStyle(tbl, "border", "1px solid black");
		//YAHOO.util.Dom.addClass(tbl, "planner");
		
		//Build head
		var thead;
		
		if (_units == 'day')
			thead = buildHeaderForDayUnits();
		else if (_units == 'week')
			thead = buildHeaderForWeekUnits();
		else if (_units == 'month')
			thead = buildHeaderForMonthUnits();	
		tbl.appendChild(thead);
		
		var tbody = document.createElement("tbody");
		tbl.appendChild(tbody);
		tableBody = tbody;
		
		cd.appendChild(tbl);
	};
	
	var clicked = function(e, obj) {
		var x = e.clientX;
		var p = obj.planner;
		
		var date = obj.planner.convertXPosToDate(x);
		var row = obj.row;
		
		p.clickEvent.fire(date, row, e);
		//alert("clicked empty cell\ndate: " + date +"\nrow: "+row.getLabel());
		
		/*
		if (p.getMode() == "insert") {
			
			if (p.getSubMode() == "awaitingFirst") {
				var s = obj.row.addSpan(new ITAO.widget.planner.Span("new",
					new ITAO.util.DateRange(date, date)));
				if (s) {
					p.setTmpObj(s);
					p.setSubMode("awaitingSecond");
				}
			} else if (p.getSubMode() == "awaitingSecond") {
				var s = p.getTmpObj();
				p.setTmpObj(null);
				p.setSubMode("awaitingFirst");
				var oldRange = s.getRange();
				var newRange = new ITAO.util.DateRange(oldRange.getStartDate(), date);
				s.setRange(newRange);
			}
		}
		*/
	};
	
	var labelClicked = function(e, obj) {
		YAHOO.util.Event.stopEvent(e);
		var p = obj.planner;
		var l = obj.label;
		
		var a = p.getActionForId(l);
		if (a) {
			a.execute();
		}
	};
	
	var addRowToTable = function(row, i) {
		var detailRow = document.createElement("tr");
		YAHOO.util.Dom.addClass(detailRow, "detailRow");
		
		var detailLabel = document.createElement("td");
		var l = document.createTextNode(row.getLabel());
		detailLabel.appendChild(l);
		detailRow.appendChild(detailLabel);
		YAHOO.util.Dom.addClass(detailLabel, "firstCol");
		
		YAHOO.util.Event.addListener(detailLabel, "click", labelClicked, {planner:planner, label:row.getLabel()}, true);
			
		var barCell = document.createElement("td");
		barCell.setAttribute("colspan", _numCols);
		YAHOO.util.Dom.addClass(barCell, "detail"+_scale);
		
		detailRow.appendChild(barCell);
		
		YAHOO.util.Event.addListener(detailRow, "click", clicked, {planner:planner, row:row}, true);
		
		tableBody.appendChild(detailRow);
		return barCell;
	};
	
	var buildHeaderForDayUnits = function() {
		var thead = document.createElement("thead");
		var topRow = document.createElement("tr");
		var bottomRow = document.createElement("tr");
		
		thead.appendChild(bottomRow);
		
		var currentDate = new Date();
		currentDate.setTime(_adjustedStartDate);
		
		var firstCol = document.createElement("th");
		YAHOO.util.Dom.addClass(firstCol, "firstCol");
		bottomRow.appendChild(firstCol);
		for (var i=0; i<_numCols; i++) {
			var th = document.createElement("th");
			if (i==0)
				firstUnitCol = th;
			YAHOO.util.Dom.addClass(th, "col");
			var width = _scale;
			if (i == _numCols-1)
				width--;
			YAHOO.util.Dom.setStyle(th, "width", width+"px");
			var text = document.createTextNode(currentDate.getDate().toString());
			th.appendChild(text);
			bottomRow.appendChild(th);
			currentDate.setDate(currentDate.getDate()+1);
		}
		return thead;
	};
	
	var buildHeaderForWeekUnits = function() {
		var thead = document.createElement("thead");
		var topRow = document.createElement("tr");
		var bottomRow = document.createElement("tr");
		
		thead.appendChild(bottomRow);
		
		var currentDate = new Date();
		currentDate.setTime(_adjustedStartDate);
		
		var firstCol = document.createElement("th");
		YAHOO.util.Dom.addClass(firstCol, "firstCol");
		bottomRow.appendChild(firstCol);
		for (var i=0; i<_numCols; i++) {
			var th = document.createElement("th");
			if (i==0)
				firstUnitCol = th;
			YAHOO.util.Dom.addClass(th, "col");
			var width = _scale;
			if (i == _numCols-1)
				width--;
			YAHOO.util.Dom.setStyle(th, "width", width+"px");
			var text = document.createTextNode(currentDate.getDate().toString());
			th.appendChild(text);
			bottomRow.appendChild(th);
			currentDate.setDate(currentDate.getDate()+7);
		}
		return thead;
	};
	
	var buildHeaderForMonthUnits = function() {
		var thead = document.createElement("thead");
		var topRow = document.createElement("tr");
		var bottomRow = document.createElement("tr");
		
		thead.appendChild(bottomRow);
		
		var currentDate = new Date();
		currentDate.setTime(_adjustedStartDate);
		
		var firstCol = document.createElement("th");
		YAHOO.util.Dom.addClass(firstCol, "firstCol");
		bottomRow.appendChild(firstCol);
		for (var i=0; i<_numCols; i++) {
			var th = document.createElement("th");
			if (i==0)
				firstUnitCol = th;
			YAHOO.util.Dom.addClass(th, "col");
			var width = _scale;
			if (i == _numCols-1)
				width--;
			YAHOO.util.Dom.setStyle(th, "width", width+"px");
			var text = document.createTextNode(currentDate.getMonth() + 1);
			th.appendChild(text);
			bottomRow.appendChild(th);
			currentDate.setMonth(currentDate.getMonth()+1);
		}
		return thead;
	};
	
	constructBasicTable();
	
	var model;

	var addRow = function(row) {
		var index = rows.length;
		rows[index] = row;
		var spansInRow = row.getSpans();
		var detailRow = addRowToTable(row, index);
		createBarsForRow(row, index, detailRow);
	};
	
	var barClicked = function(type, args, me) {
		planner.barClickEvent.fire(args[0], args[1]);
	};
	
	var mouseoverBar = function(type, args, me) {
		planner.mouseoverBarEvent.fire(args[0], args[1]);
	};
	
	var mouseoutBar = function(type, args, me) {
		planner.mouseoutBarEvent.fire(args[0], args[1]);
	};
	
	var createBarsForRow = function(row, rowIndex, detailRow) {
		var spansInRow = row.getSpans();
		var barsInRow = [];
		var previousBar;
		var barsContainer = new ITAO.widget.planner.BarsContainer(row, rowIndex, detailRow, planner);
		for (var i=0; i<spansInRow.length; i++) {
			var span = spansInRow[i];
			var b = barsContainer.addBar(span);
		}
		rows[rowIndex] = barsContainer;
	};
	
	var rowAdded = function(type, args, me) {
		var row = args[0];
		addRow(row);
	};
	
	var rowRemoved = function(type, args, me) {
		
	};
	
	var addRows = function(rows) {
		for (var i=0; i<rows.length; i++) {
			addRow(rows[i]);
		}
	}
	
	var planner = {
		setModel: function(pModel) {
			model = pModel;
			var rows = model.getRows();
			if (rows) {
				addRows(rows);
			}
			model.rowAdded.subscribe(rowAdded, model);
			model.rowRemoved.subscribe(rowRemoved, model);
		},
		convertDateToCol: function(date) {
			if (date < _startDate)
				return -1;
  			var sDate = _startDate;
  			var eDate = date;
  		
  			var daysApart = Math.abs(Math.round((sDate-eDate)/86400000));
  			var col = daysApart;
  			if (_units == 'day') {
  				return daysApart;
  			}
  			return 1;
		},
		convertDateToXPosRel: function(date) {
			if (date < _startDate)
				return -1;
			var totalDuration = _endDate - _startDate;
			var givenDuration = date - _startDate;
			
			var frac = givenDuration / totalDuration;
			var pos = frac * _scale * _numCols;
			var pos = Math.floor(pos);
			return pos;
		},
		convertColToXPos: function(col) {
			return YAHOO.util.Dom.getXY(firstUnitCol)[0] + col*_scale; 
		},
		convertXPosToCol: function(x) {
			var xOffset = YAHOO.util.Dom.getXY(firstUnitCol)[0];
			x -= xOffset;
			return Math.floor(x / _scale); 
		},
		convertXPosToDate: function(x) {
			var xOffset = YAHOO.util.Dom.getXY(firstUnitCol)[0];
			x -= xOffset;
			var frac = x/(_numCols*_scale);
			
			var totalDuration = _endDate - _startDate;
			var d = totalDuration*frac;
			var date = new Date();
			date.setTime(_startDate.getTime()+d);
			date.setHours(0);
			date.setMinutes(0);
			date.setSeconds(0);
			date.setMilliseconds(0);
			return date;
		},
		getNumCols: function() {
			return _numCols;
		},
		getScale: function() {
			return _scale;
		},
		getMode: function() {
			return mode;
		},
		getSubMode: function() {
			return subMode;
		},
		setSubMode: function(newSubMode) {
			subMode = newSubMode;
		},
		setMode: function(newMode) {
			mode = newMode;
		},
		setTmpObj: function(tmpObj) {
			_tmpObj = tmpObj;
		},
		getTmpObj: function() {
			return _tmpObj;
		},
		getController: function() {
			return controller;
		},
		setController: function(newController) {
			if (controller)
				controller.deactivate();
			controller = newController;
		},
		setSelected: function(bar) {
			if (bar != _selected) {
				if (_selected)
					YAHOO.util.Dom.removeClass(_selected.getDiv(), "selected");
					
				_selected = bar;
				if (_selected)
					YAHOO.util.Dom.addClass(_selected.getDiv(), "selected");
			}
		},
		getSelected: function() {
			return _selected;
		},
		getActionForId: function(id) {
			return _actionMap[id];
		},
		putActionForId: function(id, action) {
			_actionMap[id] = action;
		},
		barAdded: function(bar) {
			bar.clickEvent.subscribe(barClicked, bar);
			bar.mouseoverEvent.subscribe(mouseoverBar, bar);
			bar.mouseoutEvent.subscribe(mouseoutBar, bar);
		},
		barRemoved: function(bar) {
			bar.clickEvent.unsubscribe(barClicked);
		},
		clickEvent: new YAHOO.util.CustomEvent("plannerClickEvent"),
		barClickEvent: new YAHOO.util.CustomEvent("barClickEvent"),
		mouseoverBarEvent: new YAHOO.util.CustomEvent("mouseoverBarEvent"),
		mouseoutBarEvent: new YAHOO.util.CustomEvent("mouseoutBarEvent")
	};
	controller = new ITAO.widget.planner.PlannerController(planner);
	return planner;
};

ITAO.widget.planner.PlannerController = function(planner) {
	var _planner = planner;
	var _mode = new ITAO.widget.planner.SelectMode(planner);
		
	var handlePlannerClick = function(type, args, me) {
		var m = _mode.handle(type, args, me);
		if (m)
			thisObj.setMode(m);
	};
	
	var handleBarClick = function(type, args, me) {
		var m = _mode.handleBarClick(type, args, me);
		if (m)
			thisObj.setMode(m);
	};
	
	var handleMouseOverBar = function(type, args, me) {
		if (_mode && _mode.handleMouseOverBar) {
			var m = _mode.handleMouseOverBar(type, args, me);
			if (m)
				thisObj.setMode(m);
		}
	};
	
	var handleMouseOutBar = function(type, args, me) {
		if (_mode && _mode.handleMouseOutBar) {
			var m = _mode.handleMouseOutBar(type, args, me);
			if (m)
				thisObj.setMode(m);
		}
	};
	
	_planner.clickEvent.subscribe(handlePlannerClick, _planner);
	_planner.barClickEvent.subscribe(handleBarClick, _planner);
	_planner.mouseoverBarEvent.subscribe(handleMouseOverBar, _planner);
	_planner.mouseoutBarEvent.subscribe(handleMouseOutBar, _planner);
	
	thisObj = {
		deactivate: function() {
			_mode = new ITAO.widget.planner.DoNothingMode();
		},
		setMode: function(newMode) {
			if (_mode != newMode) {
				if (_mode) {
					if (_mode.deactivate) {
						_mode.deactivate();
					}
					
				}
				_mode = newMode;
				if (_mode) {
					if (_mode.activate) {
						_mode.activate();
					}
				}
				
				this.modeChange.fire(_mode);
			}
		},
		modeChange: new YAHOO.util.CustomEvent("modeChange")
	};
	return thisObj;
};

ITAO.widget.planner.DoNothingMode = function(planner, class) {
	return {
	
	}
};

ITAO.widget.planner.InsertMode = function(planner, class) {
	var _planner = planner;
	var _class = class;
	var _subMode = "awaitingFirst";
	
	var _newSpan;
	var _firstRow;
	
	var reset = function() {
		_newSpan = null;
		_firstRow = null;
		_subMode = "awaitingFirst";
	};
	
	return {
		name: "insert",
		activate: function() {
			
		},
		deactivate: function() {
			
		},
		handle: function(type, args, me) {
			var date = args[0];
			var row = args[1];
			
			if (row.getLabel() == "" || row.getLabel() == "SU001" || row.getLabel() == "SP003" || row.getLabel() == "FR002") {
				return new ITAO.widget.planner.SelectMode(planner);
			}
			
			if (_subMode == "awaitingFirst") {
				_newSpan = row.addSpan(new ITAO.widget.planner.Span("new",
					new ITAO.util.DateRange(date, date), _class));
				if (_newSpan) {
					_subMode = "awaitingSecond";
					_firstRow = row;
				}
			} else if (_subMode == "awaitingSecond") {
				//if the user has clicked a different row
				//switch the state to awaitingFirst and re-handle
				if (_firstRow != row) {
					return new ITAO.widget.planner.SelectMode(planner);
				}
				var s = _newSpan;
				_newSpan = null;
				_subMode = "awaitingFirst";
				var oldRange = s.getRange();
				var newRange = new ITAO.util.DateRange(oldRange.getStartDate(), date);
				s.setRange(newRange);
				return new ITAO.widget.planner.SelectMode(planner);
			}
		},
		handleBarClick: function(type, args, me) {
			return new ITAO.widget.planner.SelectMode(planner);
		}
	}
};

ITAO.widget.planner.DeleteMode = function(planner) {
	var _planner = planner;
	
	return {
		name: "delete",
		activate: function() {
			
		},
		deactivate: function() {
			
		},
		handle: function(type, args, me) {
			return new ITAO.widget.planner.SelectMode(planner);
		},
		handleBarClick: function(type, args, me) {
			var b = args[0];
			var rl = b.getRow().getLabel();
			if (rl == "SU001" || rl == "SP003" || rl == "FR002")
				return new ITAO.widget.planner.SelectMode(planner);
			var s = b.getSpan();
			b.getRow().removeSpan(s);
			return new ITAO.widget.planner.SelectMode(planner);
		},
		handleMouseOverBar: function(type, args, e) {
			var b = args[0];
			var rl = b.getRow().getLabel();
			if (rl == "SU001" || rl == "SP003" || rl == "FR002")
				return;
			YAHOO.util.Dom.addClass(b.getDiv(), "deleteable");
		},
		handleMouseOutBar: function(type, args, e) {
			var b = args[0];
			var rl = b.getRow().getLabel();
			if (rl == "SU001" || rl == "SP003" || rl == "FR002")
				return;
			YAHOO.util.Dom.removeClass(b.getDiv(), "deleteable");
		}
	}
};

ITAO.widget.planner.SelectMode = function(planner) {
	var _planner = planner;
	var _lastClick;
	
	return {
		deactivate: function() {
			planner.setSelected(null);
		},
		handleBarClick: function(type, args, e) {
			var b = args[0];
			var thisClick = new Date();
			if (_lastClick) {
				var elapsed = thisClick - _lastClick;
				if (elapsed < 300) {
					var rl = b.getRow().getLabel();
					if (!(rl == "SU001" || rl == "SP003" || rl == "FR002")) {
						var x = e.clientX;
						var y = e.clientY;
						ad.cfg.setProperty("context", [b.getDiv(), "tl", "bl"]);
						ad.show();
					}
				}
			} 			
			
			planner.setSelected(b);
			
			_lastClick = thisClick;
		},
		handle: function() {
			planner.setSelected(null);
		},
		handleMouseOverBar: function(type, args, e) {
			var b = args[0];
			
			return;
		},
		handleMouseOutBar: function(type, args, e) {
			ci.hide();
			return;
		}
	}
};


ITAO.widget.planner.DDBar = function(id, sGroup, config) {
	ITAO.widget.planner.DDBar.superclass.constructor.call(this, id, sGroup, config);
	this.lastColumn;
	
	this.getColumnNoForCell = function(cell) {
		var id = cell.id;
		var parts = id.split("c");
		return parts[parts.length-1];
	};
	
	this.span;
};

YAHOO.extend(ITAO.widget.planner.DDBar, YAHOO.util.DDProxy, {
	onMouseDown: function(x, y) {
		var dragEl = this.getEl();
		var initialCell = dragEl.parentNode;
		this.lastColumn = this.getColumnNoForCell(initialCell);
	},
	onMouseUp: function(x,y) {
		this.lastColumn = null;
	},
	onDragOver: function(e, id) {
		var srcEl = this.getEl();
		var destEl = YAHOO.util.Dom.get(id);
		
		while (!destEl.nodeName == 'td') {
			
			destEl = destEl.parentNode;
			if (!destEl)
				return;
		}
		var colOver = this.getColumnNoForCell(destEl);
		var diff = colOver - this.lastColumn;
		if (diff != 0) {
			this.span.shift(diff);
		}
		
		this.lastColumn = colOver;
	}
});

YAHOO.namespace("YAHOO.example");

YAHOO.example.DDResize = function(panelElId, barSegmentId, sGroup, config) {
    YAHOO.example.DDResize.superclass.constructor.call(this, panelElId, sGroup, config);
    //this.setHandleElId(panelElId);
    this.barSegment = YAHOO.util.Element(barSegmentId);
};

YAHOO.extend(YAHOO.example.DDResize, YAHOO.util.DragDrop, {

    onMouseDown: function(e) {
        this.startWidth = 0;

        this.startPos = [YAHOO.util.Event.getPageX(e),
                         YAHOO.util.Event.getPageY(e)];
    },

    onDrag: function(e) {
        var newPos = [YAHOO.util.Event.getPageX(e),
                      YAHOO.util.Event.getPageY(e)];

        var offsetX = newPos[0] - this.startPos[0];

        var newWidth = Math.max(this.startWidth + offsetX, 10);

		
        this.barSegment.style.width = newWidth + "px";
    }
});



/*

ITAO.widget.planner.DDHandle = function() {
	var lastColumn;
	
};

*/