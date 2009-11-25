ITAO.namespace("ITAO.widget.roster");

ITAO.widget.roster.Controller = function(rosterView, rosterModel) {
	var _view = rosterView;
	var _model = rosterModel;
	
	var _mode;
	var _filter;
	
	var _selected;
	
	function handleBarMouseEvent(type, args, me) {
		var e = args[0];
		var bar = args[1];
		
		var handler;
		if (e.type == 'click')
			handler = _mode.handleBarClick;
		else if (e.type == 'mousedown')
			handler = _mode.handleBarMouseDown;
		else if (e.type == 'mouseup')
			handler = _mode.handleBarMouseUp;
		else if (e.type == 'mouseover')
			handler = _mode.handleBarMouseOver;
		else if (e.type == 'mouseout')
			handler = _mode.handleBarMouseOut;
		
		if (handler) {
			handler(e, bar);
		}
		YAHOO.util.Event.stopEvent(e);
	}
	
	function handleBarAreaMouseEvent(type, args, me) {
		var e = args[0];
		var rowObj = args[1];
		
		var handler;
		if (e.type == 'click')
			handler = _mode.handleBarAreaClick;
		else if (e.type == 'mousedown')
			handler = _mode.handleBarAreaMouseDown;
		else if (e.type == 'mouseup')
			handler = _mode.handleBarAreaMouseUp;
		else if (e.type == 'mouseover')
			handler = _mode.handleBarAreaMouseOver;
		else if (e.type == 'mouseout')
			handler = _mode.handleBarAreaMouseOut;
		
		if (handler) {
			handler(e, rowObj);
		}
		YAHOO.util.Event.stopEvent(e);
	}
	
	function handleAssignmentLabelMouseEvent(type, args, me) {
		var e = args[0];
		var assignment = args[1];
		
		var handler;
		if (e.type == 'click')
			handler = _mode.handleAssignmentLabelClick;
		else if (e.type == 'mousedown')
			handler = _mode.handleAssignmentLabelMouseDown;
		else if (e.type == 'mouseup')
			handler = _mode.handleAssignmentLabelMouseUp;
		else if (e.type == 'mouseover')
			handler = _mode.handleAssignmentLabelMouseOver;
		else if (e.type == 'mouseout')
			handler = _mode.handleAssignmentLabelMouseOut;
		
		if (handler) {
			handler(e, assignment);
		}
		YAHOO.util.Event.stopEvent(e);
	}
	
	this.setSelected = function(bar) {
		if (_selected) {
			YAHOO.util.Dom.removeClass(_selected.barDiv, "selected");
		}
		_selected = bar;
		if (_selected) {
			YAHOO.util.Dom.addClass(_selected.barDiv, "selected");
		}
	};
	
	this.getSelected = function() {
		return _selected;
	};
	
	this.setMode = function(newMode) {
		if (_mode) {
			var f = _mode.deactivate;
			if (f)
				f();
		}
		_mode = newMode;
		if (_mode) {
			var f = _mode.activate;
			if (f)
				f();
		}
	}
	
	this.getModel = function() {
		return _model;
	}
	
	this.getView = function() {
		return _view;
	}
	
	this.setFilter = function(filter) {
		_filter = filter;
		_view.setFilter(_filter);
	}
	
	this.getFilter = function() {
		return _filter;
	}
	this.selectMode = function() {
		that.setMode(new ITAO.widget.roster.SelectMode(that));
	}
	
	this.insertMode = function() {
		that.setMode(new ITAO.widget.roster.InsertMode(that));
	}
	
	this.deleteMode = function() {
		that.setMode(new ITAO.widget.roster.DeleteMode(that));
	}
	
	var that = this;
	
	{
		_view.barMouseEvent.subscribe(handleBarMouseEvent, _view);
		_view.barAreaMouseEvent.subscribe(handleBarAreaMouseEvent, _view);
		_view.assignmentLabelMouseEvent.subscribe(handleAssignmentLabelMouseEvent, _view);
		this.setMode(new ITAO.widget.roster.SelectMode(this));
	}
	
};

var assignmentFilter = function(assignment) {
	var _assignment = assignment;
	
	this.showAssignment = function(assignment) {
		if (assignment.type == _assignment.type) {
			return assignment.id == _assignment.id;
		}
		return false;
	}
	
	this.showAssignable = function(assignable) {
		return true;
	}
	
	this.showAllocation = function(allocation) {
		var assignment = allocation.getAssignment();
		if (assignment.type == _assignment.type) {
			return assignment.id == _assignment.id;
		}
		return false;
	}
};

ITAO.widget.roster.SelectMode = function(controller) {
	var _controller = controller;
	
	this.handleBarMouseDown = function(e, bar) {
		_controller.setSelected(bar);
	};
	
	this.handleBarAreaMouseDown = function(e, rowObj) {
		_controller.setSelected(null);
	};
	
	this.handleAssignmentLabelMouseDown = function(e, assignment) {
		if (_controller.getFilter())
			_controller.setFilter(null);
		else
			_controller.setFilter(new assignmentFilter(assignment));
	};
	
	var _mouseOverTimer;
	var _assignment;
	var _x;
	var _y;
	var _div;
	
	function showAssignmentDetails() {
		_controller.getView().showAssignmentDetails(_assignment, _x, _y, _div);
	}
	
	function hideAssignmentDetails() {
		_controller.getView().hideAssignmentDetails();
	}
	
	this.handleBarMouseOver = function(e, bar) {
		var ud = bar.userData;
		
		_assignment = null;
		if (instanceOf(ud, ITAO.widget.roster.Allocation)) {
			_assignment = ud.getAssignment();
		} else if (instanceOf(ud, ITAO.widget.roster.Assignment)) {
			_assignment = ud;
		}
		if (_assignment) {
			if (_mouseOverTimer) {
				clearTimeout(_mouseOverTimer);
			}
		
			_x = e.pageX;
			_y = e.pageY;
			_div = bar.barDiv;
			_mouseOverTimer = setTimeout(showAssignmentDetails, 500);
		}
	};
	
	this.handleBarMouseOut = function(e, bar) {
		if (_mouseOverTimer)
			clearTimeout(_mouseOverTimer);
		_mouseOverTimer = setTimeout(hideAssignmentDetails, 250);
	};
	
};

ITAO.widget.roster.InsertMode = function(controller) {
	var _controller = controller;
	var _allocation;
	
	var _firstClick = true;
	var _firstDate;
	
	this.handleBarAreaMouseDown = function(e, rowObj) {
		if (instanceOf(rowObj, ITAO.widget.roster.Assignable)) {
			var date = _controller.getView().getDateAtXPos(e.pageX);
			var assignable = rowObj;
			if (_firstClick) {
				var closestAssignment = _controller.getModel().guessAssignmentByStartDate(date);
				if (closestAssignment) {
					_allocation = _controller.getModel().addAllocation(assignable, closestAssignment, new ITAO.util.DateRange(date, date));
					_firstClick = false;
					_firstDate = date;
				}
			} else {
				var date = _controller.getView().getDateAtXPos(e.pageX);
				_allocation.setDateRange(new ITAO.util.DateRange(_firstDate, date));
				_firstClick = true;
			}
		}
	};
	
	this.handleAssignmentLabelMouseDown = function(e, assignment) {
		if (_controller.getFilter())
			_controller.setFilter(null);
		else
			_controller.setFilter(new assignmentFilter(assignment));
	};
};

/*
ITAO.widget.roster.DeleteMode = function(controller) {
	_controller = controller;
	
	this.activate = function() {
		var bar = _controller.get
		_controller.getModel().removeAllocation(ud);
	}
	
	this.handleBarMouseDown = function(e, bar) {
		var ud = bar.userData;
		if (instanceOf(ud, ITAO.widget.roster.Allocation)) {
			_controller.getModel().removeAllocation(ud);
		}
	}
	
};
*/