<#include "/starlite.ftl">
<#setting number_format = "######.##########"/>

<html>
<head></head>
<body>

    <div style="padding:10px;border:1px solid silver;width:1000px;">
    <form name="transForm" id="transForm" action="transaction.action">
    <fieldset>
        <legend>Component Transactions</legend>
        
        <div class="fm-opt">
        <label>Select Component:</label>
        <select name="id" style="width:600px;">
        <#list components as c>
        <option value="${c.id?if_exists}">${c.number?if_exists} ${c.serial?if_exists}
        </#list>
        </select>
        <span>(Part) (Serial)
        </div>
        
        <div class="fm-opt">
        <label>Transaction Type:</label>
        <select name="type" style="width:150px;">
        <#if type?exists>
        <option <#if type=="Purchase">SELECTED</#if>>Purchase
        <option <#if type=="Repair">SELECTED</#if>>Repair 
        <option <#if type=="Move">SELECTED</#if>>Move 
        <option <#if type=="Reserve">SELECTED</#if>>Reserve  
        <option <#if type=="Sell">SELECTED</#if>>Sell 
        <option <#if type=="Scrap">SELECTED</#if>>Scrap
        <option <#if type=="Consume">SELECTED</#if>>Consume
        <#else>
        <option>Purchase
        <option>Repair 
        <option>Move 
        <option>Reserve  
        <option>Sell 
        <option>Scrap
        <option>Consume
        </#if>
        </select>
        </div>
        
        <button style="position: relative; float: right; top: 4px;" class="smooth" onclick="document.forms.transForm.submit();" type="button"><img src="images/icons/accept.png"> Select Transaction Type</button>
        
        
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
        
        <#if type == "Move" || type == "Reserve" || type == "Sell" || type == "Scrap" || type == "Repair" >
        <div class="fm-opt">
        <label>Current Location:</label>
        <select name="locCurrent" style="width:600px;">
        <#list component.locations as c>
        <option value="${c.id}">${c.location} ${c.bin} (${c.quantity})
        </#list>
        </select>
        <span>(Location) (Bin) (Quantity)
        </div>
        </#if>
        
        
        
        <#if type == "Move" || type == "Purchase" || type == "Repair" >
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
        
        <#if type == "Purchase" || type == "Repair" >
        <div class="fm-opt">
        <label>Batch:</label>
        <input name="batch" type="text" />
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
        
        
        <button style="position: relative; float: right; top: 4px;" class="smooth" onclick="document.forms.transCreateForm.submit();" type="button"><img src="images/icons/accept.png"> Create Transaction</button>
        
        
    </fieldset>
    </form>
    </div>
    </#if>


</body>
</html>