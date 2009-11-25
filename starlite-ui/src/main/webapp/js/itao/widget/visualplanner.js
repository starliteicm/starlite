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
		setType: function(newClass) {
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
		beforeRangeChange: new YAHOO.util.CustomEvent("BeforeRangeChange"),
		rangeChange: new YAHOO.util.CustomEvent("RangeChange"),
		typeChange: new YAHOO.util.CustomEvent("TypeChange"),
		classChange: new YAHOO.util.CustomEvent("ClassChange")
	}
};

ITAO.widget.planner.Row = function(label, spans) {
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
	
	return {
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
					if (doAdd(span, i))
						this.spanAdded.fire(span);
					return;
				}
			}
			if (doAdd(span, spans.length)) {
				this.spanAdded.fire(span);
			}
		},
		removeSpan: function(span) {
			if (doRemove(span))
				this.spanRemoved.fire(span);
		},
		labelChange: new YAHOO.util.CustomEvent("LabelChange"),
		spanAdded: new YAHOO.util.CustomEvent("SpanAdded"),
		spanRemoved: new YAHOO.util.CustomEvent("SpanRemoved")
	}
};

ITAO.widget.planner.PlannerModel = function() {
	var rows = [];
	
	return {
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
		rowAdded: new YAHOO.util.CustomEvent("RowAdded"),
		rowRemoved: new YAHOO.util.CustomEvent("RowRemoved")
	}
};

var nextId=1;
function getNextId() {
	return nextId++;
}

ITAO.widget.planner.Bar = function(span, row, planner, tableRow) {
	var span = span;
	var row = row;
	var planner = planner;
	var tableRow = tableRow;
	
	var leftHandleVisible = false;
	var rightHandleVisible = false;
	
	var firstEl;
	var lastEl;
	
	var rangeChanged = function(type, args, me) {
		clear();
		display();
	};
	
	var createBarSegment = function(class) {
		var barSegment = document.createElement("div");
		YAHOO.util.Dom.addClass(barSegment, "barSegment");
		if (class) {
			YAHOO.util.Dom.addClass(barSegment, class);
		}
		return barSegment;
	};
	
	var createLeftBarSegment = function(class) {
		var handle = document.createElement("div");
		YAHOO.util.Dom.addClass(handle, "leftHandle");
		if (class) {
			YAHOO.util.Dom.addClass(handle, class);
		}
		var barSegment = document.createElement("div");
		// \u00a0 is nbsp
		barSegment.appendChild(document.createTextNode("\u00a0"));
		YAHOO.util.Dom.addClass(barSegment, "barSegment");
		if (class) {
			YAHOO.util.Dom.addClass(barSegment, class);
		}
		handle.appendChild(barSegment);
		return handle;
	};
	
	var createRightBarSegment = function(class) {
		var handle = document.createElement("div");
		YAHOO.util.Dom.addClass(handle, "rightHandle");
		if (class) {
			YAHOO.util.Dom.addClass(handle, class);
		}
		var barSegment = document.createElement("div");
		barSegment.appendChild(document.createTextNode("\u00a0"));
		YAHOO.util.Dom.addClass(barSegment, "barSegment");
		if (class) {
			YAHOO.util.Dom.addClass(barSegment, class);
		}
		handle.appendChild(barSegment);
		return handle
	};
	
	var createSingleBarSegment = function(class) {
		var handle = document.createElement("div");
		YAHOO.util.Dom.addClass(handle, "leftHandle");
		if (class) {
			YAHOO.util.Dom.addClass(handle, class);
		}
		var barSegment = document.createElement("div");
		// \u00a0 is nbsp
		barSegment.appendChild(document.createTextNode("\u00a0"));
		YAHOO.util.Dom.addClass(barSegment, "rightHandle");
		handle.appendChild(barSegment);
		return handle;
	};
	
	span.rangeChange.subscribe(rangeChanged, span); 
	
	var display = function() {
		var startCol = planner.convertDateToCol(span.getRange().getStartDate());
		var endCol = planner.convertDateToCol(span.getRange().getEndDate());
		
		var totalCols = planner.getNumCols();
		
		if (startCol >= totalCols || endCol < 0) {
			leftHandleVisible = false;
			rightHandleVisible = false;
			firstEl = null;
			lastEl = null;
			return;
		}
		
		if (startCol < 0) {
			leftHandleVisible = false;
		} else {
			leftHandleVisible = true;
		}
		
		if (endCol >= totalCols) {
			rightHandleVisible = false;
		} else {
			rightHandleVisible = true;
		}
		
		var el = tableRow.firstChild.nextSibling;
		var c = 0;
		while (c < startCol) {
			el = el.nextSibling;
			c++;
		}
		
		if (startCol == endCol) {
			firstEl = el;
			lastEl = el;
			var singleCell = createSingleBarSegment(span.getClass());
			
			el.appendChild(singleCell);
			return;
		}
		
		if (endCol == 0) {
			firstEl = el;
			lastEl = el;
			var rh = createRightBarSegment(span.getClass());
			el.appendChild(rh);
			return;
		}
		
		firstEl = el;
		if (leftHandleVisible) {
			var lh = createLeftBarSegment(span.getClass());
			el.appendChild(lh);
		} else {
			var bs = createBarSegment(span.getClass());
			el.appendChild(bs);
		}
		
		el = el.nextSibling;
		c++;
		
		while (el && c < endCol) {
			lastEl = el;
			var segment = createBarSegment(span.getClass());
			segment.id = "b"+getNextId();
			var ddbar = new ITAO.widget.planner.DDBar(segment.id, "planner");
			ddbar.span = span;
			el.appendChild(segment);
			el = el.nextSibling;
			c++;
		}
		
		if (el && rightHandleVisible) {
			lastEl = el;
			var rh = createRightBarSegment(span.getClass());
			el.appendChild(rh);
		} else if (el) {
			lastEl = el;
			el.appendChild(createBarSegment(span.getClass()));
		}
	};
	
	display();
	
	var clear = function() {
		if (firstEl) {
			var el = firstEl;
			while (el) {
				removeChildren(el);
				if (el == lastEl) {
					return;
				}
				el = el.nextSibling;
			}
		}
	};
	
	return {
	
	}
};

ITAO.widget.planner.VisualPlanner = function(containingDiv, start, numWeeks) {
	var rows = [];
	var bars = [];
	var containingDiv = containingDiv;
	var startDate = new Date();
	startDate.setTime(Date.parse(start));
	var numWeeks = numWeeks;
	var table;
	
	var createBarsForRow = function(row, rowIndex, detailRow) {
		var spansInRow = row.getSpans();
		var barsInRow = [];
		for (var i=0; i<spansInRow.length; i++) {
			var span = spansInRow[i];
			var bar = new ITAO.widget.planner.Bar(span, row, planner, detailRow);
			barsInRow[barsInRow.length] = bar;
		}
		rows[rowIndex] = barsInRow;
	};
	
	//Construct basic table
	var constructBasicTable = function() {
		var cd = new YAHOO.util.Element(containingDiv);
		removeChildren(cd);		
	
		var tbl = document.createElement("table");
		table = tbl;
		YAHOO.util.Dom.addClass(tbl, "planner");
		
		//Build header row
		var headerRow = document.createElement("tr");
		YAHOO.util.Dom.addClass(headerRow, "weekRow");
		//Build first col
		var firstCol = document.createElement("td");
		YAHOO.util.Dom.addClass(firstCol, "firstCol");
		
		var currentDate = new Date();
		currentDate.setTime(startDate.getTime());
		
		
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
		
		
		
		cd.appendChild(tbl);
	};
	
	var addRowToTable = function(row, i) {
		var numCols = numWeeks * 7;
		var detailRow = document.createElement("tr");
		YAHOO.util.Dom.addClass(detailRow, "detailRow");
		
		var detailLabel = document.createElement("td");
		var l = document.createTextNode(row.getLabel());
		detailLabel.appendChild(l);
		detailRow.appendChild(detailLabel);
		YAHOO.util.Dom.addClass(detailLabel, "firstCol");
			
		var even = false;
		for (var j=0; j<numCols; j++) {
			var c = document.createElement("td");
			c.id="e"+i+"c"+j;
			new YAHOO.util.DDTarget(c.id, "planner");
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
		table.appendChild(detailRow);
		return detailRow;
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
			if (date < startDate)
				return -1;
  			var sDate = startDate;
  			var eDate = date;
  		
  			var daysApart = Math.abs(Math.round((sDate-eDate)/86400000));
  			var col = daysApart;
  			return daysApart;
		},
		getNumCols: function() {
			return numWeeks * 7;
		}
	};
	return planner;
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