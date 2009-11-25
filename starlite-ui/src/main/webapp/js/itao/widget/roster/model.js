ITAO.namespace("ITAO.widget.roster");

var xmlHelper = function(schema) {
	var _schema = schema;
	
	var thisObj = {
		parseNode: function(node, oDocument, target) {
			if (!target)
				target = {};
			if (!oDocument)
				oDocument = document;
			for (var i=0; i<_schema.length; i++) {
				var prop = _schema[i];
				var key = "";
				var propName;
				var setter;
				var parser;
				if (typeof prop == 'string') {
					key = prop;
					propName = prop;
				} else {
					key = prop.key;
					propName = prop.name;
					if (!propName)
						propName = key;
					setter = prop.setter;
					parser = prop.parser;
				}
				
				key = key.toLowerCase();
				
				var value = oDocument.evaluate(key, node, null, XPathResult.STRING_TYPE, null).stringValue;
				if (parser) {
					value = parser(value);
				}
				if (setter) {
					target[setter](value);
				} else {
					target[propName] = value;
				}
			}
			return target;
		}
	};
	return thisObj;
};

var linkedList = function() {
	var _first;
	var _last;
	
	this.first = function() { return _first; };
	this.add = function(e) {
			var node = {val:e};
			if (_last) {
				_last.next = node;
				node.prev = _last;
				_last = node;
			} else {
				_first = node;
				_last = node;
			}
	};
	this.remove = function(e) {
		var current = _first;
		while (current) {
			if (current.val == e) {
				that.removeNode(current);
				break;
			}
			current = current.next;
		}
	};
	this.insertBeforeNode = function(next, e) {
		var newNode = {val:e};
		newNode.next = next;
		var p = next.prev;
		next.prev = newNode;
		if (p) {
			newNode.prev = p;
			p.next = newNode;
		} else {
			_first = newNode;
		}
	};
	this.removeNode = function(node) {
		var p = node.prev;
		var n = node.next;
				
		if (p) {
			p.next = n;
		} else {
			_first = n;
		}
		
		if (n) {
			n.prev = p;
		} else {
			_last = p;
		}
	};
	this.iterator = function() {
		return new listIterator(this);
	};
	this.clone = function() {
		var i = this.iterator();
		var newList = new linkedList();
		while (i.hasNext()) {
			newList.add(i.next());
		}
		return newList;
	};

	var that = this;
};

var listIterator = function(list) {
	var _list = list;
	
	var _next = list.first();
	var _previous;
	var _last;
	
	var _i=0;
	
	var thisObj = {
		add: function(e) {
			if (_next) {
				_list.insertBeforeNode(_next, e);
				_next = _next.prev; 
			} else {
				_list.add(e);
				_next = _list.first();
			}
		},
		hasNext: function () { 
			return _next;
		},
		hasPrevious: function() {
			return _previous;
		},
		next: function() {
			_previous = _next;
			_last = _next;
			_next = _next.next;
			_i++;
			return _last.val; 
		},
		nextIndex: function() {
			return i;
		},
		previous: function() {
			_next = _previous;
			_last = _previous;
			_previous = _previous.prev;
			_i--;
			return _last.val;
		},
		previousIndex: function() {
			return _i-1;
		},
		remove: function() {
			if (_last)
				_list.removeNode(_last);
		},
		set: function(e) {
			if (_last)
				_list.replaceNode(_last, e);
		},
		getNode: function() {
			return _current;
		}
	};
	return thisObj;
};

/**
 * @constructor
 * @param {bool} readOnly Optional - if specified indicates whether this model accepts updates
 */
ITAO.widget.roster.Model = function(baseUrl, readOnly) {
	var _baseUrl = baseUrl;
	
	var _assignments = [];
	var _assignables = [];
	var _allocations = [];
	
	var _assignmentIdMap = {};
	var _assignableIdMap = {};
	var _allocationIdMap = {};
	
	var _readOnly = readOnly;
	
	var _assignableAllocationMap = {};
	
	var _dataStartDate;
	var _dataEndDate;
	
	var _transactionNumber = 0;
	
	var _changeList;
	
	var dateParser = function(oData) {
		if (typeof oData == 'string') {
			var dateStr = oData.substring(0,10);
			dateStr = dateStr.replace(/-/g, '/');
			var date = new Date();
			date.setTime(Date.parse(dateStr));
			return date;
		}
	};
	
	var assignmentSchema = ["id", "type", "label", {key:"from", parser:dateParser}, {key:"to", parser:dateParser}];
	var assignableSchema = ["id", "type", "label"];
	var allocationSchema = ["id", "assignmentType", "assignmentId", "assignableType", "assignableId", {key:"from", parser:dateParser}, {key:"to", parser:dateParser}];
	
	var assignmentNodeParser = new xmlHelper(assignmentSchema);
	var assignableNodeParser = new xmlHelper(assignableSchema);
	var allocationNodeParser = new xmlHelper(allocationSchema);
	
	
	//creates the allocation as long as it doesn't overlap with any other allocation for the given assignable.
	//returns the created allocation, or nothing if not created.
	var insertIfAble = function(allocation, list, announceChanges) {
		if (_readOnly)
			return;
		
		var dateRange = allocation.getDateRange();
		var iterator = list.iterator();
		
		var a, p;
		while (iterator.hasNext()) {
			a = iterator.next();
			if (dateRange.getStartDate().getTime() < a.getDateRange().getStartDate().getTime()) {
				//Check start date is greater than previous end date
				if (p) {
					if (dateRange.getStartDate().getTime() <= p.getDateRange().getEndDate().getTime())
						return;
				}
				//Check end date is less than next start date
				if (dateRange.getEndDate().getTime() >= a.getDateRange().getStartDate().getTime())
					return;
				//list.insertBeforeNode(iterator.getNode(), allocation);
				iterator.previous();
				iterator.add(allocation);
				if (announceChanges) {
					if (_changeList)
						_changeList.allocationToAdd(allocation);
					that.allocationAdded.fire(allocation);
				}
				return allocation;
			} else if (dateRange.getStartDate().getTime() == a.getDateRange().getStartDate().getTime()) {
				return;
			}
			p = a;
		}
		
		list.add(allocation);
		if (announceChanges) {
			if (_changeList)
				_changeList.allocationToAdd(allocation);
			that.allocationAdded.fire(allocation);
		}
		return allocation;
	};
	
	//Deletes the allocation if it exists in the model.
	//Returns the deleted allocation
	var doDelete = function(allocation, announceChanges) {
		var assignable = allocation.getAssignable();
		var allocations = _assignableAllocationMap[assignable.type+assignable.id];
		
		var iterator = allocations.iterator();
		while (iterator.hasNext()) {
			var a = iterator.next();
			if (a == allocation) {
				iterator.remove();
				if (announceChanges) {
					if (_changeList)
						_changeList.allocationToRemove(a);
					that.allocationRemoved.fire(a);
				}
				return allocation;
			}
		}
	};
	
	function doMove(allocation) {
		var assignable = allocation.getAssignable();
		var allocations = _assignableAllocationMap[assignable.type+assignable.id];
		var announceChanges = !allocation.getId();
		
		doDelete(allocation, announceChanges);
		insertIfAble(allocation, allocations, announceChanges);
		if (!announceChanges) {
			_changeList.allocationToModify(allocation);
			that.allocationChanged(allocation);
		}
	}
	
	var doRefreshModel = function(params) {
		YAHOO.util.Connect.asyncRequest('GET', baseUrl+params, 
			{success:handleModelData, failure:refreshFailed}
		);
	};
	
	var handleModelData = function(o) {
		_changeList = null;
		var assignments = parseNodes("assignment", o.responseXML, assignmentNodeParser);
		var assignables = parseNodes("assignable", o.responseXML, assignableNodeParser);
		var allocations = parseNodes("allocation", o.responseXML, allocationNodeParser);
		
		
		_assignments = [];
		for (var i=0; i<assignments.length; i++) {
			var a = assignments[i];
			var ass = new ITAO.widget.roster.Assignment(a.type, a.id, a.label, new ITAO.util.DateRange(a.from, a.to));
			_assignments.push(ass);
			_assignmentIdMap[ass.type+ass.id] = ass;
		}
		
		_assignables = [];
		for (var i=0; i<assignables.length; i++) {
			var a = assignables[i];
			var ass = new ITAO.widget.roster.Assignable(a.type, a.id, a.label);
			_assignables.push(ass);
			_assignableIdMap[ass.type+ass.id] = ass;
			_assignableAllocationMap[ass.type+ass.id] = new linkedList();
		}
		
		_allocations = [];
		for (var i=0; i<allocations.length; i++) {
			var a = allocations[i];
			var assignment = _assignmentIdMap[a.assignmentType+a.assignmentId];
			var assignable = _assignableIdMap[a.assignableType+a.assignableId];
			if (assignment && assignable) {
				var all = new ITAO.widget.roster.Allocation(a, assignment, assignable, new ITAO.util.DateRange(a.from, a.to));
				_allocations.push();
				insertIfAble(all, _assignableAllocationMap[assignable.type+assignable.id]);
				all.beforeChangeDateRange.subscribe(checkCanChange);
				all.dateRangeChange.subscribe(updateAllocationPosition);
			}
		}
		_changeList = new ITAO.widget.roster.ModelChangeList();
		that.modelRefreshed.fire();
	};
	
	function checkCanChange(type, args, me) {
		return canMove(args[0], args[1]);
	}
	
	var canMove = function(allocation, newDateRange) {
		var assignable = allocation.getAssignable();
		var allocations = _assignableAllocationMap[assignable.type+assignable.id];
		var clone = allocations.clone();
		var newAllocation = new ITAO.widget.roster.Allocation(null, null, null, newDateRange);
		clone.remove(allocation);
		return insertIfAble(newAllocation, clone);
	};
	
	
	var updateAllocationPosition = function(type, args, me) {
		doMove(args[2]);
	};
	
	var parseNodes = function(nodeName, document, nodeParser) {
		var iterator = document.evaluate("//"+nodeName, document, null, XPathResult.ORDERED_NODE_ITERATOR_TYPE, null);
		
		var vals = [];
		var current = iterator.iterateNext();
		while (current) {
			vals.push(nodeParser.parseNode(current, document));
			current = iterator.iterateNext();
		}
		return vals;
	};
	
	var refreshFailed = function(o) {
		
	};
	
	/**
	 * @param {ITAO.widget.roster.Assignable} assignable
	 * @param {ITAO.widget.roster.Assignment} assignment
	 * @param {ITAO.util.DateRange} dateRange
	 */
	this.addAllocation = function(assignable, assignment, dateRange) {
		var newAllocation = new ITAO.widget.roster.Allocation(null, assignment, assignable, dateRange);
		newAllocation = insertIfAble(newAllocation, _assignableAllocationMap[assignable.type+assignable.id]);
		if (newAllocation) {
			newAllocation.beforeChangeDateRange.subscribe(checkCanChange);
			newAllocation.dateRangeChange.subscribe(updateAllocationPosition);
			that.allocationAdded.fire(newAllocation);
			return newAllocation;
		}
	}
	
	this.moveAllocation = function(allocation, newDateRange) {
		var oldStartTime = allocation.getDateRange().getStartDate().getTime();
		var oldEndTime = allocation.getDateRange().getEndDate().getTime();
		
		var newStartTime = newDateRange.getStartDate().getTime();
		var newEndTime = newDateRange.getEndDate().getTime();
		
		//Nothing to do
		if (oldStartTime == newStartTime && oldEndTime == newEndTime)
			return;
			
		allocation.setDateRange(newDateRange);
	}
		
	this.removeAllocation = function(allocation) {
		if (!_readOnly) {
			var deleted = doDelete(allocation);
			if (deleted) {
				this.allocationRemoved.fire(deleted);
			}
		}
	}
		
	this.getAssignments = function() {
		return _assignments.slice(0);
	};
		
	this.getAssignables = function() {
		return _assignables.slice(0);
	};
	
	this.getAssignmentClosestStartDate = function(d) {
		var lastA;
		var dTime = d.getTime();
		for (var i=0; i<_assignments.length; i++) {
			var a = _assignments[i];
			if (a.dateRange.getStartDate().getTime() > dTime)
				break;
			lastA = a;
		}
		return lastA;
	};
	
	this.guessAssignmentByStartDate = function(d) {
		var lastA;
		var lastA;
		var dTime = d.getTime();
		for (var i=0; i<_assignments.length; i++) {
			var a = _assignments[i];
			if (a.dateRange.getStartDate().getTime() > dTime)
				break;
			if (a.dateRange.getEndDate().getTime() >= dTime)
				lastA = a;
		}
		return lastA;
	};
	
	this.guessAssignmentByDateRange = function(dateRange) {
		var startTime = dateRange.getStartDate().getTime();
		var endTime = dateRange.getEndDate().getTime();
		
		var lastA;
		var lastA;
		var dTime = d.getTime();
		for (var i=0; i<_assignments.length; i++) {
			var a = _assignments[i];
			if (a.dateRange.getStartDate().getTime() > startTime)
				break;
			if (a.dateRange.getEndDate().getTime() >= endTime)
				lastA = a;
		}
		return lastA;
	};;
	
	this.getAllocations = function(assignable, from, to) {
		var allocations = _assignableAllocationMap[assignable.type+assignable.id];
		var it = allocations.iterator();
		var as = [];
		while (it.hasNext()) {
			var a = it.next();
			var start = a.getDateRange().getStartDate();
			var end = a.getDateRange().getEndDate();
			if (end.getTime() >= from.getTime()
			 && start.getTime() <= to.getTime()
			)
				as.push(a);
		}
		return as;
	};
		
	this.rereshModel = function(params) {
		if (!params)
			params = "";
		doRefreshModel(params);
	};
	
	this.prepareData = function(from, to) {
		_transactionNumber++;
		var callback = {
			success: function(o) {
				if (o.argument[0] == _transactionNumber)
					handleModelData(o);
			},
			failure: function(o) {
				if (o.argument[0] == _transactionNumber)
					refreshFailed(o);
			},
			argument:[_transactionNumber] 
		};
		YAHOO.util.Connect.asyncRequest('GET', _baseUrl+"/"+from.formatDate("Y-m-d")+"/"+to.formatDate("Y-m-d"), callback);
	};
		
	this.modelRefreshed = new YAHOO.util.CustomEvent("modelRefreshedEvent", this);
		
	this.allocationAdded = new YAHOO.util.CustomEvent("allocationAddedEvent", this);
	this.allocationRemoved = new YAHOO.util.CustomEvent("allocationRemovedEvent", this);
	this.allocationChanged = new YAHOO.util.CustomEvent("allocationChangedEvent", this);
	this.getBaseUrl = function() {
		return _baseUrl;
	};
	
	var that = this;
};

/**
 * Represents an assignment. Assignments must have a type. the id, label and dateRange properties are optional.
 * Assignments without an id are usually singleton assignments, such as Leave.
 * 
 * Non-Singleton Assignment types such as Charter should have an Id to identify which Charter this is associated with.
 * 
 * If excluded, the label property is defaulted to "{type}[ - {id}]" - e.g. "Charter - SU001", or "Leave"
 * @constructor
 */
ITAO.widget.roster.Assignment = function(type, id, label, dateRange) {
	this.type = type;
	this.id = id;
	if (label) {
		this.label = label;
	} else {
		this.label = type;
		if (id)
			this.label += " - " + id;
	}
	this.dateRange = dateRange;
};

/**
 * Represents an assignable. Assignables must have a type. Id and label are optional. If exclueded the label property
 * is defaulted to "{type}[ - {id}]" - e.g. "CrewMember - 1"
 * @constructor
 */
ITAO.widget.roster.Assignable = function(type, id, label) {
	this.type = type;
	this.id = id;
	if (label) {
		this.label = label;
	} else {
		this.label = type;
		if (id)
			this.label += " - " + id;
	}
};

/**
 * Represents an allocation of an Assignable to an Assignment for a specified duration.
 * @constructor
 */
ITAO.widget.roster.Allocation = function(id, assignment, assignable, dateRange) {
	var _id = id;
	var _assignment = assignment;
	var _assignable = assignable;
	var _dateRange = dateRange;
	
	/**
	 * @returns the date range of this allocation
	 */
	this.getDateRange = function() {
			return _dateRange;
	};

	this.setDateRange = function(newDateRange) {
			var canChange = that.beforeChangeDateRange.fire(that, newDateRange);
			if (!canChange)
				return;
			var oldDateRange = _dateRange;
			_dateRange = newDateRange;
			that.dateRangeChange.fire(oldDateRange, newDateRange, that);
	};

	/**
	 * @returns the assignable associated with this allocation
	 */
	this.getAssignable = function() {
			return _assignable;
	};
	
	/**
	 * @returns the assignment associated with this allocation
	 */
	this.getAssignment = function() {
			return _assignment;
	};
	
	this.getId = function() {
		return _id;
	};
	
	this.beforeChangeDateRange = new YAHOO.util.CustomEvent("beforeChangeDateRangeEvent", this);
	this.dateRangeChange = new YAHOO.util.CustomEvent("dateRangeChange", this);
	
	var that = this;
};

ITAO.widget.roster.ModelChange = function(type, allocation) {
	this.type = type;
	this.allocation = allocation;
};

/**
 * Represents all the changes that need to be sent to the server when committing.
 * Changes are atomic at a per-row level.
 */
ITAO.widget.roster.ModelChangeList = function() {
	var _changes = [];
	
	this.allocationToAdd = function(allocation) {
		//Cannot add an allocation that already exists!
		if (allocation.id)
			return;
		for (var i=0; i<_changes.length; i++) {
			var c = _changes[i];
			if (c.allocation == allocation)
				return;
		}
		var change = new ITAO.widget.roster.ModelChange('add', allocation);
		_changes.push(change);
	};
	
	this.allocationToModify = function(allocation) {
		//Check if the allocation is already in the change list
		for (var i=0; i<_changes.length; i++) {
			var c = _changes[i];
			//If so, we don't need to add it.
			if (c.allocation == allocation)
				return;
		}
		//If not then it must be an existing allocation
		if (allocation.id) {
			var change = new ITAO.widget.roster.ModelChange('update', allocation);
			_changes.push(change);
		}
	};
	
	this.allocationToRemove = function(allocation) {
		for (var i=0; i<_changes.length; i++) {
			var c = _changes[i];
			//If there is already a change entry for this allocation
			if (c.allocation == allocation) {
				//If this allocation is new since the last commit then
				//just remove the 'add' from the list
				if (!allocation.id) {
					_changes.splice(i,1);
				} 
				//Otherwise change the ModelChange type to 'remove'
				else {
					c.type = 'remove'; 
				}
				return;
			}
		}
		//We only need to add a change entry if this allocation already exists
		if (allocation.id) {
			var change = new ITAO.widget.roster.ModelChange('remove', allocation);
			_changes.push(change);
		}
	};
	
	this.getChanges = function() {
		return _changes.slice();
	};
	
	this.hasChanges = function() {
		return _changes.length > 0;
	};
};
