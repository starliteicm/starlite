ITAO.namespace("ITAO.data");

ITAO.data.Event = function(date, title, description, class) {
	this.date = date;
	this.title = title;
	this.description = description;
	this.class = class;
};

ITAO.data.CalendarModel = function() {
	this.events = {};
	
	this.addEvent = function(event) {
		var i;
		var dayEvents = this.events[event.date];
		if (!dayEvents) {
			dayEvents = [];
			this.events[event.date] = dayEvents;
		}
		for (i=0; i<dayEvents.length; i++) {
			var e = dayEvents[i];
			if (e == event)
				return;
		}
		dayEvents[i] = event;
	};
	
	this.removeEvent = function(event) {
		var i;
		var dayEvents = this.events[event.date];
		if (!dayEvents)
			return;
		for (i=0; i<dayEvents.length; i++) {
			var e = dayEvents[i];
			if (e == event) {
				var newDayEvents = dayEvents.slice(0, i-1).concat(dayEvents.slice(i+1));
				this.events[event.date] = newDayEvents;
				return;
			}
		}
	}; 
};

ITAO.namespace("ITAO.widget");

ITAO.widget.Calendar = function(id, calendarModel) {
	ITAO.widget.Calendar.superclass.constructor.call(this, id);
	this.calendarModel = calendarModel;
};

YAHOO.extend(ITAO.widget.Calendar, YAHOO.widget.Calendar, {
	renderCellDefault : function(workingDate, cell) {
		cell.innerHTML = '<a href="#" class="' + this.Style.CSS_CELL_SELECTOR + '">' + this.buildDayLabel(workingDate) + "</a>";
		var d = workingDate.formatDate("Y-m-d");
		if (!this.calendarModel)
			return;
		var dayEvents = this.calendarModel.events[d];
		if (!dayEvents)
			return;
		cell.innerHTML += '<ul>';
		for (var i=0; i<dayEvents.length; i++) {
			var e = dayEvents[i];
			cell.innerHTML += '<li class='+e.class+'>'+e.title+'</li>';
		}
		cell.innerHTML += '</ul>';
	}
});