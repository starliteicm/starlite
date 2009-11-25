if (typeof ITAO == "undefined" || !ITAO) {
    /**
     * The ITAO global namespace object.  If ITAO is already defined, the
     * existing ITAO object will not be overwritten so that defined
     * namespaces are preserved.
     * @class ITAO
     * @static
     */
    var ITAO = {};
}

/**
 * Returns the namespace specified and creates it if it doesn't exist
 * <pre>
 * ITAO.namespace("property.package");
 * ITAO.namespace("ITAO.property.package");
 * </pre>
 * Either of the above would create ITAO.property, then
 * ITAO.property.package
 *
 * Be careful when naming packages. Reserved words may work in some browsers
 * and not others. For instance, the following will fail in Safari:
 * <pre>
 * ITAO.namespace("really.long.nested.namespace");
 * </pre>
 * This fails because "long" is a future reserved word in ECMAScript
 *
 * @method namespace
 * @static
 * @param  {String*} arguments 1-n namespaces to create 
 * @return {Object}  A reference to the last namespace object created
 */
ITAO.namespace = function() {
    var a=arguments, o=null, i, j, d;
    for (i=0; i<a.length; i=i+1) {
        d=a[i].split(".");
        o=ITAO;

        // ITAO is implied, so it is ignored if it is included
        for (j=(d[0] == "ITAO") ? 1 : 0; j<d.length; j=j+1) {
            o[d[j]]=o[d[j]] || {};
            o=o[d[j]];
        }
    }

    return o;
};

ITAO.namespace("ITAO.editors");
ITAO.namespace("ITAO.formatters");

//Custom editor to allow enter to submit data
ITAO.editors.SimpleText = function(oEditor, oSelf) {
            var elCell = oEditor.cell;
            var oRecord = oEditor.record;
            var oColumn = oEditor.column;
            var elContainer = oEditor.container;
            var value = oRecord.getData(oColumn.key);

            // Textbox
            var elTextbox = elContainer.appendChild(document.createElement("input"));
            elTextbox.type = "text";
            elTextbox.style.width = (elCell.offsetWidth + 20) + "px";
            elTextbox.value = value;

            // Set up a listener
            YAHOO.util.Event.addListener(elTextbox, "keyup", function(v){
                // Save on "enter"
                if(v.keyCode === 13) {
                    oSelf.saveCellEditor();
                }
                // Update the tracker value
                else {
                    oSelf._oCellEditor.value = elTextbox.value;
                }
            });

            // Select the text
            elTextbox.select();
        };
	
// Custom formatter for decimal strings
ITAO.formatters.DecimalFormatter = function(elCell, oRecord, oColumn, oData) {
    elCell.innerHTML = parseFloat(oData).toFixed(2);
};

