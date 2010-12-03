<#include "/starlite.ftl">
<#setting number_format = "######.##########"/>

<html>
<head>
<link rel="stylesheet" type="text/css" href="styles/jmesa.css">
<script type="text/javascript" src="js/jmesa.js"></script>
<script type="text/javascript">
        $(document).ready(function() {
           addDropShadow('images/table/');
        });
        function onInvokeAction(id) {
            var parameterString = createParameterStringForLimit(id);
            location.href = '${request.contextPath}/transaction.action?' + parameterString;
        }
</script>
<style>
#componentTable{
    width:950px;
}
</style>
</head>
<body>

    <div style="padding:10px;border:1px solid silver;width:1000px;">
    <form name="transForm" id="transForm" action="transaction.action">
    <fieldset>
        <legend>Component Transactions</legend>
        
        ${tableHtml}
        
    </fieldset>
    </form>
    </div><br/>
    
    <#if type?exists>
    <div style="padding:10px;border:1px solid silver;width:1000px;">
    <form name="transCreateForm" id="transCreateForm" action="transaction!create.action">
    <input type="hidden" name="id" value="${component.id}">
    <input type="hidden" name="type" value="${type}">
    <fieldset>
        <legend>${type} - ${component.number} ${component.serial}</legend>
        
        <#if type == "Move" || type == "Reserve" || type == "Sell" || type == "Scrap" || type == "Consume" || type == "Repair" >
        <div class="fm-opt">
        <label>Current Location:</label>
        <select name="locCurrent" style="width:600px;">
        <#list component.locations as c>
        <option value="${c.id}">${c.location?if_exists} ${c.bin?if_exists} (${c.quantity?if_exists})
        </#list>
        </select>
        <span>(Location) (Bin) (Quantity)
        </div>
        </#if>
        
        
        
        <#if type == "Move" || type == "Purchase" || type == "Repair"  >
        <div class="fm-opt">
        <label>Destination Store:</label>
        <select name="location" style="width:280px;">
        <#list stores as c>
        <option value="${c.location}">${c.location}
        </#list>
        </select>
        <input name="bin" style="width:310px;"/>
        <span>(Store) (Bin)
        </div>
        </#if>
        
        <#if type == "Purchase" || type == "Repair"  >
        <div class="fm-opt">
        <label>Batch:</label>
        <input name="batch" type="text" />
        </div>
        </#if>
        
        <#if type == "Purchase" || type == "Sell" || type == "Consume" || type == "Scrap" >
        <div class="fm-opt">
        <label>Value:</label>
        <input name="purchaseValue" type="text" />
        <select id="purchaseCurrency" name="purchaseCurrency" style="width:60px;">
            <option>USD
            <#list rates?if_exists as rate>
                <option>${rate.currencyCodeFrom}</option>
            </#list>
            </select>
        </div>
        </#if>
        
        <#if component.type.equals("Class E")>
        <div class="fm-opt">
        <label>Quantity:</label>
        <input name="quantity" type="text" />
        </div>
        <#else>
        <input type="hidden" name="quantity" value="1"/>
        </#if>
        
        <div class="fm-opt">
        <label>Notes:</label>
        <textarea style="width:600px;" name="note"></textarea>
        </div> 
        
        
        <button id="createTransButton" style="position: relative; float: right; top: 4px;" class="smooth" onclick="document.forms.transCreateForm.submit();" type="button"><img src="images/icons/accept.png"> Create Transaction</button>
        
        
    </fieldset>
    </form>
    </div>
    </#if>


</body>
</html>