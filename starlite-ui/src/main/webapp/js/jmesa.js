function TableFacadeManager() {
}

TableFacadeManager.tableFacades = new Object();

TableFacadeManager.addTableFacade = function(tableFacade) {
    TableFacadeManager.tableFacades[tableFacade.limit.id] = tableFacade;
}

TableFacadeManager.getTableFacade = function(id) {
    return TableFacadeManager.tableFacades[id];
}

function TableFacade(id) {
    this.limit = new Limit(id);
    this.worksheet = new Worksheet();
}

function Worksheet() {
    this.save;
    this.filter;
}

function Limit(id) {
    this.id = id;
    this.page;
    this.maxRows;
    this.sortSet = new Array();
    this.filterSet = new Array();
    this.exportType;
}

function Sort(position, property, order) {
    this.position = position;
    this.property = property;
    this.order = order;
}

function Filter(property, value) {
    this.property = property;
    this.value = value;
}

Limit.prototype.getId = function() {
    return this.id;
}

Limit.prototype.setId = function(id) {
    this.id = id;
}

Limit.prototype.getPage = function() {
    return this.page;
}

Limit.prototype.setPage = function(page) {
    this.page = page;
}

Limit.prototype.getMaxRows = function() {
    return this.maxRows;
}

Limit.prototype.setMaxRows = function(maxRows) {
    this.maxRows = maxRows;
}

Limit.prototype.getSortSet = function() {
    return this.sortSet;
}

Limit.prototype.addSort = function(sort) {
    this.sortSet[this.sortSet.length] = sort;
}

Limit.prototype.setSortSet = function(sortSet) {
    this.sortSet = sortSet;
}

Limit.prototype.getFilterSet = function() {
    return this.filterSet;
}

Limit.prototype.addFilter = function(filter) {
    this.filterSet[this.filterSet.length] = filter;
}

Limit.prototype.setFilterSet = function(filterSet) {
    this.filterSet = filterSet;
}

Limit.prototype.getExport = function() {
    return this.exportType;
}

Limit.prototype.setExport = function(exportType) {
    this.exportType = exportType;
}

/*other helper methods*/

TableFacade.prototype.createHiddenInputFields = function(form) {
    var limit = this.limit;
    
    var exists = $(form).find(':hidden[@name=' + limit.id + '_p_]').val();
    if (exists) {
        return false;
    }
    
    if (this.worksheet.save) {
        $(form).append('<input type="hidden" name="' + limit.id + '_sw_" value="true"/>');
    }
    
    if (this.worksheet.filter) {
        $(form).append('<input type="hidden" name="' + limit.id + '_fw_" value="true"/>');
    }
    
    /* tip the API off that in the loop of working with the table */
    $(form).append('<input type="hidden" name="' + limit.id + '_tr_" value="true"/>');
    
    /* the current page */
    $(form).append('<input type="hidden" name="' + limit.id + '_p_" value="' + limit.page + '"/>');
    $(form).append('<input type="hidden" name="' + limit.id + '_mr_" value="' + limit.maxRows + '"/>');
    
    /* the sort objects */
    var sortSet = limit.getSortSet();
    $.each(sortSet, function(index, sort) {
        $(form).append('<input type="hidden" name="' + limit.id + '_s_'  + sort.position + '_' + sort.property + '" value="' + sort.order + '"/>');
    });
    
    /* the filter objects */
    var filterSet = limit.getFilterSet();
    $.each(filterSet, function(index, filter) {
        $(form).append('<input type="hidden" name="' + limit.id + '_f_' + filter.property + '" value="' + filter.value + '"/>');
    });
    
    return true;
}

TableFacade.prototype.createParameterString = function() {
    var limit = this.limit;
    
    var url = '';
    
    /* the current page */
    url += limit.id + '_p_=' + limit.page;
    
    /* the max rows */
    url += '&' + limit.id + '_mr_=' + limit.maxRows;
    
    /* the sort objects */
    var sortSet = limit.getSortSet();
    $.each(sortSet, function(index, sort) {
        url += '&' + limit.id + '_s_' + sort.position + '_' + sort.property + '=' + sort.order;
    });
    
    /* the filter objects */
    var filterSet = limit.getFilterSet();
    $.each(filterSet, function(index, filter) {
        url += '&' + limit.id + '_f_' + filter.property + '=' + encodeURIComponent(filter.value);
    });
    
    /* the export */
    if (limit.exportType) {
        url += '&' + limit.id + '_e_=' + limit.exportType;
    }
    
    /* tip the API off that in the loop of working with the table */
    url += '&' + limit.id + '_tr_=true';
    
    if (this.worksheet.save) {
        url += '&' + limit.id + '_sw_=true';
    }
    
    if (this.worksheet.filter) {
        url += '&' + limit.id + '_fw_=true';
    }
    
    return url;
}

/* convenience methods so do not have to manually work with the api */

function addTableFacadeToManager(id) {
    var tableFacade = new TableFacade(id);
    TableFacadeManager.addTableFacade(tableFacade);	
}

function setSaveToWorksheet(id) {
    TableFacadeManager.getTableFacade(id).worksheet.save='true';
}

function setFilterToWorksheet(id) {
    TableFacadeManager.getTableFacade(id).worksheet.filter='true';
    setPageToLimit(id, '1');
}

function setPageToLimit(id, page) {
    TableFacadeManager.getTableFacade(id).limit.setPage(page);
}

function setMaxRowsToLimit(id, maxRows) {
    TableFacadeManager.getTableFacade(id).limit.setMaxRows(maxRows);
    setPageToLimit(id, '1');
}

function addSortToLimit(id, position, property, order) {
    /*First remove the sort if it is set on the limit, 
    and then set the page back to the first one.*/
    removeSortFromLimit(id, property);
    setPageToLimit(id, '1');
    
    var limit = TableFacadeManager.getTableFacade(id).limit;
    var sort = new Sort(position, property, order); 
    limit.addSort(sort);
}

function removeSortFromLimit(id, property) {
    var limit = TableFacadeManager.getTableFacade(id).limit;
    var sortSet = limit.getSortSet();
    $.each(sortSet, function(index, sort) {
        if (sort.property == property) {
            sortSet.splice(index, 1);
            return false;
        }
    });
}

function removeAllSortsFromLimit(id) {
    var limit = TableFacadeManager.getTableFacade(id).limit;
    limit.setSortSet(new Array());
    setPageToLimit(id, '1');
}

function getSortFromLimit(id, property) {
    var limit = TableFacadeManager.getTableFacade(id).limit;
    var sortSet = limit.getSortSet();
    $.each(sortSet, function(index, sort) {
        if (sort.property == property) {
            return sort;
        }
    });
}

function addFilterToLimit(id, property, value) {
    /*First remove the filter if it is set on the limit, 
    and then set the page back to the first one.*/
    removeFilterFromLimit(id, property);
    setPageToLimit(id, '1');
    
    var limit = TableFacadeManager.getTableFacade(id).limit;
    var filter = new Filter(property, value); 
    limit.addFilter(filter);
}

function removeFilterFromLimit(id, property) {
    var limit = TableFacadeManager.getTableFacade(id).limit;
    var filterSet = limit.getFilterSet();
    $.each(filterSet, function(index, filter) {
        if (filter.property == property) {
            filterSet.splice(index, 1);
            return false;
        }
    });
}

function removeAllFiltersFromLimit(id) {
    var tableFacade = TableFacadeManager.getTableFacade(id);
    
    var limit = tableFacade.limit;
    limit.setFilterSet(new Array());
    setPageToLimit(id, '1');
    
    var worksheet = tableFacade.worksheet;
    worksheet.filter = null;
}

function getFilterFromLimit(id, property) {
    var limit = TableFacadeManager.getTableFacade(id).limit;
    var filterSet = limit.getFilterSet();
    $.each(filterSet, function(index, filter) {
        if (filter.property == property) {
            return filter;
        }
    });
}

function setExportToLimit(id, exportType) {
    TableFacadeManager.getTableFacade(id).limit.setExport(exportType);
}

function createHiddenInputFieldsForLimit(id) {
    var tableFacade = TableFacadeManager.getTableFacade(id);
    var form = getFormByTableId(id);
    tableFacade.createHiddenInputFields(form);
}

function createHiddenInputFieldsForLimitAndSubmit(id) {
    var tableFacade = TableFacadeManager.getTableFacade(id);
    var form = getFormByTableId(id);
    var created = tableFacade.createHiddenInputFields(form);
    if (created) {
        form.submit();
    }
}

function createParameterStringForLimit(id) {
    var tableFacade = TableFacadeManager.getTableFacade(id);
    return tableFacade.createParameterString();
}

function getFormByTableId(id) {
    var node = document.getElementById(id);
    var found = false;
    while (!found) {
        node = node.parentNode;
        if (node.nodeName == 'FORM') {
            found = true;
            return node;
        }
    }
}

/* Special Effects */

var dynFilter;

function DynFilter(filter, id, property) {
    this.filter = filter;
    this.id = id;
    this.property = property;
}

function createDynFilter(filter, id, property) {
    if (dynFilter) {
        return;
    }
    
    dynFilter = new DynFilter(filter, id, property);
    
    var cell = $(filter);
    var width = cell.width() + 1;
    var originalValue = cell.text();
    
    cell.html('<div id="dynFilterDiv"><input id="dynFilterInput" name="filter" style="width:' + width + 'px" value="' + originalValue + '" /></div>');
    
    var input = $('#dynFilterInput');
    input.focus();
    
    $(input).keypress(function(event) {
        if (event.keyCode == 13) { // press the enter key 
            var changedValue = input.val();
            cell.html(changedValue);
            addFilterToLimit(dynFilter.id, dynFilter.property, changedValue);
            onInvokeAction(dynFilter.id);
            dynFilter = null;
        }
    });
    
    $(input).blur(function() {
        var changedValue = input.val();
        cell.html(changedValue);
        addFilterToLimit(dynFilter.id, dynFilter.property, changedValue);
        dynFilter = null;
    });
}

function createDynDroplistFilter(filter, id, property, options) {
    if (dynFilter) {
        return;
    }

    dynFilter = new DynFilter(filter, id, property);
    
    if ($('#dynFilterDroplistDiv').size() > 0) {
        return; // filter already created
    }
    
    /* The cell that represents the filter. */
    var cell = $(filter);
    
    /* Get the original filter value and width. */
    var originalValue = cell.text();
    var width = cell.width();
    
    /* Need to first get the size of the arrary. */
    var size = 1;
    $.each(options, function() {
        size++;
        if (size > 10) {
            size = 10;
            return false;
        }
    });
    
    /* Create the dynamic select input box. */
    cell.html('<div id="dynFilterDroplistDiv" style="top:17px">');
    
    var html = '<select id="dynFilterDroplist" name="filter" size="' + size + '">';
    html += '<option value=""> </option>';
    $.each(options, function(key, value) {
        if (key == originalValue) {
            html += '<option selected="selected" value="' + key + '">' + value + '</option>';
        } else {
        html += '<option value="' + key + '">' + value + '</option>';
        }
    });
    
    html += '</select>';

    var div = $('#dynFilterDroplistDiv');
    div.html(html);

    var input = $('#dynFilterDroplist');

    /* Resize the column if it is not at least as wide as the column. */    
    var selectWidth = input.width();
    if (selectWidth < width) {
        input.width(width + 5); // always make the droplist overlap some
    }

    input.focus();

    var originalBackgroundColor = cell.css("backgroundColor");
    cell.css({backgroundColor:div.css("backgroundColor")});

    /* Something was selected or the clicked off the droplist. */

    $(input).change(function() {
        var changedValue = $("#dynFilterDroplistDiv option:selected").val();
        cell.text(changedValue);
        addFilterToLimit(dynFilter.id, dynFilter.property, changedValue);
        onInvokeAction(dynFilter.id);
        dynFilter = null;
    });

    $(input).blur(function() {
        var changedValue = $("#dynFilterDroplistDiv option:selected").val();
        cell.text(changedValue);
        $('#dynFilterDroplistDiv').remove();
        cell.css({backgroundColor:originalBackgroundColor});
        dynFilter = null;
    });
}

/* Create a dropshadow for the tables */
function addDropShadow(imagesPath, theme) {
    if (!theme) {
        theme = 'jmesa';
    }
    $('div.' + theme + ' .table')
    .wrap("<div class='wrap0'><div class='wrap1'><div class='wrap2'><div class='dropShadow'></div></div></div></div>")
    .css({'background': 'url(' + imagesPath + 'shadow_back.gif) 100% repeat'});
    
    $('.' + theme + ' div.wrap0').css({'background': 'url(' + imagesPath + 'shadow.gif) right bottom no-repeat', 'float':'left'});
    $('.' + theme + ' div.wrap1').css({'background': 'url(' + imagesPath + 'shadow180.gif) no-repeat'});
    $('.' + theme + ' div.wrap2').css({'background': 'url(' + imagesPath + 'corner_bl.gif) -18px 100% no-repeat'});
    $('.' + theme + ' div.dropShadow').css({'background': 'url(' + imagesPath + 'corner_tr.gif) 100% -18px no-repeat'});
    
    $('div.' + theme).append('<div style="clear:both">&nbsp;</div>');
}

/* Worksheet */

var wsColumn;

function WsColumn(column, id, uniqueProperties, property) {
    this.column = column;
    this.id = id;
    this.uniqueProperties = uniqueProperties;
    this.property = property;
}

function createWsColumn(column, id, uniqueProperties, property) {
    if (wsColumn) {
        return;
    }

    wsColumn = new WsColumn(column, id, uniqueProperties, property);
    
    var cell = $(column);
    var width = cell.width();
    var originalValue = cell.text();
    
    cell.parent().width(width); // set the outer width to avoid dynamic column width changes
    
    cell.html('<div id="wsColumnDiv"><input id="wsColumnInput" name="column" style="width:' + (width + 1) + 'px" value="' + originalValue + '"/></div>');
    
    var input = $('#wsColumnInput'); 
    input.focus();
    
    $('#wsColumnInput').keypress(function(event) {
        if (event.keyCode == 13 || event.keyCode == 9 || (event.keyCode >=37 && event.keyCode <=40) ) { // press the enter key 
            var changedValue = input.val();
            cell.html(changedValue);
            if (changedValue != originalValue) {
                submitWsColumn(originalValue, changedValue);
            }
            wsColumn = null;
            if (event.keyCode == 9 || event.keyCode == 37 || event.keyCode == 39) {
            	if (event.keyCode == 37 || (event.keyCode == 9 && event.shiftKey)) {
            		var previousTd = cell.parent().prev();
            		if (previousTd.get().length > 0) {
            			previousTd.children(0).click();
            			event.stopPropagation();
            		} else {
            			var prevRow = cell.parent().parent().prev();
            			if (prevRow.get().length > 0) {
            				var lastCell = prevRow.children(":last");
            				lastCell.children(0).click();
            			}
            		}
            	} else {
            		var nextTd = cell.parent().next();
            		if (nextTd.get().length > 0) {
	            		nextTd.children(0).click();
            			event.stopPropagation();
            		} else {
            			var nextRow = cell.parent().parent().next();
            			if (nextRow.get().length > 0) {
            				var firstCell = nextRow.children(":first");
            				firstCell.children(0).click();
            			}
            		}
            	}
            } else if (event.keyCode == 38 || event.keyCode == 40) {
            	var td = cell.parent();
            	
            	var prev = td.prev();
            	var i=0;
            	while (prev.get().length > 0) {
            		i++;
            		prev = prev.prev();
            	}
            	
            	var nextRow = null;
            	if (event.keyCode == 38) {
            		nextRow = td.parent().prev();
            	} else {
            		nextRow = td.parent().next();
            	}
            	
            	if (nextRow.get().length > 0) {
            		var td = nextRow.children(":eq("+i+")");
            		var newCell = td.children(":first");
            		if (newCell) {
            			newCell.click();
            		}
            	}
            }
        }
    });
    
    $('#wsColumnInput').blur(function() {
        var changedValue = input.val();
        cell.html(changedValue);
        if (changedValue != originalValue) {
            submitWsColumn(originalValue, changedValue);
        }
        wsColumn = null;
    });
}

function submitWsCheckboxColumn(column, id, uniqueProperties, property) {
    wsColumn = new WsColumn(column, id, uniqueProperties, property);
    
    var checked = column.checked;
    
    var changedValue = 'unchecked';
    if (checked) {
        changedValue = 'checked';
    }
    
    var originalValue = 'unchecked';
    if (!checked) { // the first time the original value is the opposite
        originalValue = 'checked';
    }
    
    submitWsColumn(originalValue, changedValue);
    
    wsColumn = null;
}

function submitWsColumn(originalValue, changedValue) {
    var data = '{ "id" : "' + wsColumn.id + '"';
    
    data += ', "cp_" : "' + wsColumn.property + '"';
    
    var props = wsColumn.uniqueProperties;
    $.each(props, function(key, value) {
        data += ', "up_' + key + '" : "' + value + '"';
    });
    
    data += ', "ov_" : "' + originalValue + '"';
    data += ', "cv_" : "' + changedValue + '"';
    
    data += '}'
    
    $.post('jmesa.wrk?', eval('(' + data + ')'), function(data) {});
    if (onSubmitWsColumn) {
    	onSubmitWsColumn(wsColumn.id, wsColumn.property, originalValue, changedValue);
    }
}

function sumTotals(tableId, colsToTotalIndices) {
	var totalsRow = document.getElementById(tableId+"Totals");
	var tbody = totalsRow.parentNode;
	var rows = tbody.childNodes;
	
	var totals = [];
	for (var i=0; i<colsToTotalIndices; i++) {
		totals[colsToTotalIndicies] = 0;
	}
	
	for (var i=0; i<rows.length; i++) {
		var row = rows[i];
		if (row != totalsRow) {
			for (var j=0; j<colsToTotalIndices.length; j++) {
				var index = colsToTotalIndices[j];
				var cell = row.childNodes[index];
				var val = findValue(cell);
				if (val)
					totals[index] += parseFloat(val);
			}
		}
	}
	
	alert(totals);
}


function findValue(cell) {
	if (!cell)
		return null;
	var nodeValue = cell.nodeValue;
	while (!nodeValue && cell) {
		cell = cell.firstChild;
		nodeValue = cell.nodeValue;
	}
	return nodeValue;
}

function createWsSelectColumn(column, id, uniqueProperties, property, labels, vals) {
    if (wsColumn) {
        return;
    }

    wsColumn = new WsColumn(column, id, uniqueProperties, property);
    
    var cell = $(column);
    var width = cell.width();
    var originalValue = cell.text();
    
    cell.parent().width(width); // set the outer width to avoid dynamic column width changes
    
    var ht = '<div id="wsColumnDiv"><select id="wsSelectColumnInput", name="column", style="width:' + (width + 1) + 'px;"><option value=""></option>';
    
    for (var i=0; i<labels.length; i++) {
    	ht += '<option value='+vals[i];
    	if (vals[i] == originalValue) {
    		ht += ' selected';
    	}
    	ht += '>' + labels[i] + '</option>';
    }
    
    ht += '</select></div>';
    cell.html(ht);
    
    var input = $('#wsSelectColumnInput'); 
    input.focus();
    
    /*
    $('#wsSelectColumnInput').change(function(event) {
    	var selectedIndex = input.attr('selectedIndex');
    	var changedValue;
    	if (!selectedIndex || selectedIndex == 0)
    		changedValue = "";
    	else
        	changedValue = vals[selectedIndex-1];
        cell.html(changedValue);
        if (changedValue != originalValue) {
            submitWsColumn(originalValue, changedValue);
        }
        wsColumn = null;
    });
    */
    
    $('#wsSelectColumnInput').keypress(function(event) {
    	if (event.keyCode == '9') {
    		wsColumn.tabWasPressed = true;
			wsColumn.shiftWasPressed = event.shiftKey;
			input.blur();
    	} else {
    		wsColumn.tabWasPressed = false;
    	}
    	if (event.keyCode >= 'A' && event.keyCode <= 'z') {
    		return true;
    	} else {
    		event.stopPropagation();
    		return false;
    	}
    });
    
    $('#wsSelectColumnInput').blur(function() {
        var selectedIndex = input.attr('selectedIndex');
    	var changedValue;
    	if (!selectedIndex || selectedIndex == 0)
    		changedValue = "";
    	else
        	changedValue = vals[selectedIndex-1];
        cell.html(changedValue);
        if (changedValue != originalValue) {
           submitWsColumn(originalValue, changedValue);
        }
        
        var tabWasPressed = wsColumn.tabWasPressed;
        var shiftWasPressed = wsColumn.shiftWasPressed;
        wsColumn = null;
        if (tabWasPressed) {
        	wsColumn = null;
			if (shiftWasPressed) {
            	var previousTd = cell.parent().prev();
            	if (previousTd) {
            		previousTd.children(0).click();
            	}
            } else {
            	var nextTd = cell.parent().next();
            	if (nextTd) {
	           		nextTd.children(0).click();
            	}
            }
        }
    });
    
}
