ITAO.namespace("ITAO.starlite.widget.dd");

ITAO.starlite.widget.dd.SlotFiller = function(containingDiv, slots, availableLabel, availableElements) {
	this.slots = slots;
	this.availableLabel = availableLabel;
	this.availableElements = availableElements;
	
	if (!slots)
		slots = [];
		
	if (!availableElements)
		availableElements = [];
	
	this.element = new YAHOO.util.Element(containingDiv);
	
	{
		if (this.element.hasChildNodes()) {
			this.element.removeChild(this.element.get('firstChild'));
		}
		
		
		YAHOO.util.Dom.addClass(containingDiv, "slotFiller");
	
		var slotId = 1;
		var elementId = 1;
		var slotIds = [];
		//SETUP SLOTS
		{
			var slotsEl = document.createElement('div');
			this.element.appendChild(slotsEl);
			YAHOO.util.Dom.addClass(slotsEl, "slots");
		
			if (!slots) {
				slots = [];
			}
		
			for (var i=0; i<slots.length; i++) {
				var slot = slots[i];
				var slotEl = document.createElement('div');
				slotsEl.appendChild(slotEl);
				YAHOO.util.Dom.addClass(slotEl, "slot");
				var labelSpan = document.createElement('span');
				
				//Create Slot Label
				var labelText = document.createTextNode(slot.label);
				labelSpan.appendChild(labelText);
				slotEl.appendChild(labelSpan);
				YAHOO.util.Dom.addClass(labelSpan, "slotLabel");
				
				//Create Slot List
				var slotList = document.createElement('ul');
				slotList.id = containingDiv+"s"+slotId;
				new YAHOO.util.DDTarget(containingDiv+"s"+slotId, containingDiv);
				slotId++;			
				slotEl.appendChild(slotList);
				slotIds[i] = slotList.id;
			}
		}

		//SETUP AVAILABLE LIST
		{
			var availableSlotsEl = document.createElement('div');
			this.element.appendChild(availableSlotsEl);
			YAHOO.util.Dom.addClass(availableSlotsEl, "availableList");
			
			var labelSpan = document.createElement('span');
			var labelText = document.createTextNode(availableLabel);
			labelSpan.appendChild(labelText);
			availableSlotsEl.appendChild(labelSpan);
			YAHOO.util.Dom.addClass(labelSpan, "availableLabel");
			
			var listEl = document.createElement('ul');
			availableSlotsEl.appendChild(listEl);
			listEl.id=containingDiv+"available";
			
			new YAHOO.util.DDTarget(listEl.id, containingDiv);
			
			for (var i=0; i<availableElements.length; i++) {
				var e = availableElements[i];
				var availableEl = document.createElement('li');
				availableEl.id=containingDiv+"e"+elementId;
				new ITAO.starlite.widget.dd.SlotFillerElement(containingDiv+"e"+elementId, containingDiv, null, slotIds, listEl.id);
				elementId++;
				YAHOO.util.Dom.addClass(availableEl, "slotElement");
				var elementText = document.createTextNode(e.label);
				availableEl.appendChild(elementText);
				listEl.appendChild(availableEl);
			}
		}
		
	
		var hrClear = document.createElement('hr');
		this.element.appendChild(hrClear);
		YAHOO.util.Dom.addClass(hrClear, "clearLeft");
	}
	
};

var Dom = YAHOO.util.Dom;
var Event = YAHOO.util.Event;
var DDM = YAHOO.util.DragDropMgr;

ITAO.starlite.widget.dd.SlotFillerElement = function(id, sGroup, config, slots, availableList) {

    ITAO.starlite.widget.dd.SlotFillerElement.superclass.constructor.call(this, id, sGroup, config);

    this.logger = this.logger || YAHOO;
    var el = this.getDragEl();
    Dom.setStyle(el, "opacity", 0.67); // The proxy is slightly transparent

	this.swappedElement = null;
	this.slots = slots;
	this.availableList = availableList;
    this.goingUp = false;
    this.lastY = 0;
};

YAHOO.extend(ITAO.starlite.widget.dd.SlotFillerElement, YAHOO.util.DDProxy, {

    startDrag: function(x, y) {
        this.logger.log(this.id + " startDrag");

        // make the proxy look like the source element
        var dragEl = this.getDragEl();
        var clickEl = this.getEl();
        Dom.setStyle(clickEl, "visibility", "hidden");

        dragEl.innerHTML = clickEl.innerHTML;

        Dom.setStyle(dragEl, "color", Dom.getStyle(clickEl, "color"));
        Dom.setStyle(dragEl, "backgroundColor", Dom.getStyle(clickEl, "backgroundColor"));
        Dom.setStyle(dragEl, "border", "2px solid gray");
    },

    endDrag: function(e) {

        var srcEl = this.getEl();
        var proxy = this.getDragEl();

        // Show the proxy element and animate it to the src element's location
        Dom.setStyle(proxy, "visibility", "");
        var a = new YAHOO.util.Motion( 
            proxy, { 
                points: { 
                    to: Dom.getXY(srcEl)
                }
            }, 
            0.2, 
            YAHOO.util.Easing.easeOut 
        )
        var proxyid = proxy.id;
        var thisid = this.id;

        // Hide the proxy and show the source element when finished with the animation
        a.onComplete.subscribe(function() {
                Dom.setStyle(proxyid, "visibility", "hidden");
                Dom.setStyle(thisid, "visibility", "");
            });
        a.animate();
    },

    onDragDrop: function(e, id) {

        // If there is one drop interaction, the li was dropped either on the list,
        // or it was dropped on the current location of the source element.
        if (DDM.interactionInfo.drop.length === 1) {

            // The position of the cursor at the time of the drop (YAHOO.util.Point)
            var pt = DDM.interactionInfo.point; 

            // The region occupied by the source element at the time of the drop
            var region = DDM.interactionInfo.sourceRegion; 

            // Check to see if we are over the source element's location.  We will
            // append to the bottom of the list once we are sure it was a drop in
            // the negative space (the area of the list without any list items)
            if (!region.intersect(pt)) {
                var destEl = Dom.get(id);
                if (destEl.nodeName.toLowerCase() == 'ul') {
                	var destDD = DDM.getDDById(id);
                	destEl.appendChild(this.getEl());
                	destDD.isEmpty = false;
                	DDM.refreshCache();
                }
            }

        }
    },

    onDrag: function(e) {

        // Keep track of the direction of the drag for use during onDragOver
        var y = Event.getPageY(e);

        if (y < this.lastY) {
            this.goingUp = true;
        } else if (y > this.lastY) {
            this.goingUp = false;
        }

        this.lastY = y;
    },

    onDragOver: function(e, id) {
        var srcEl = this.getEl();
        var destEl = Dom.get(id);
        

        // We are only concerned with list items, we ignore the dragover
        // notifications for the list.
        if (destEl.nodeName.toLowerCase() == "li") {
            var orig_p = srcEl.parentNode;
            var p = destEl.parentNode;

			if (p.id == this.availableList) {
			
            	if (this.goingUp) {
	                p.insertBefore(srcEl, destEl); // insert above
            	} else {
	                p.insertBefore(srcEl, destEl.nextSibling); // insert below
            	}

	            DDM.refreshCache();
	        } else {
	        	orig_p.insertBefore(destEl, srcEl);
	        	p.appendChild(srcEl);
	        	this.swappedElement = destEl;
	        	DDM.refreshCache();
	        }
        }
    },
    
    onDragOut: function(e, id) {
    	var elementOut = Dom.get(id);      
    	if (elementOut.nodeName.toLowerCase() == "ul" && this.swappedElement) {
    		var srcEl = this.getEl();
    		var orig_p = this.swappedElement.parentNode;
            var p = srcEl.parentNode;
        	orig_p.insertBefore(srcEl, this.swappedElement);
        	p.appendChild(this.swappedElement);
        	this.swappedElement = null;
        	DDM.refreshCache();
        }
    }
});