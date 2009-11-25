<#include "/starlite.ftl">
<html>
<head>
  <title>${charter.administrative.firstName!} ${charter.administrative.lastName!}</title>
  <link rel="stylesheet" type="text/css" href="${request.contextPath}/styles/muffin.css">
</head>

<body>
	<@subTabs/>
	<form action="charter!save.action" method="POST" class="smart" style="clear:left; padding:20px;">
		<input type="hidden" name="id" value="${charter.id!}"/>
		<input type="hidden" name="charter.id" value="${charter.id!}"/>
		<input type="hidden" name="tab" value="insurance"/>
		<div style="float:left; width: 1000px;">
		<fieldset>
            <#assign currency=charter.administrative.currency!/>
            <legend>Insurance : 
            <#if currency == 'USD'>USD ($)</#if>
            <#if currency == 'EUR'>EUR (&euro;)</#if>
            <#if currency == 'GBP'>GBP (&pound;)</#if></legend>
            <table>
            <tr>
            <th>Item/Type</th>
            <th>Required</th>
            <th>Limit/Value</th>
            <th>Policy No</th>
            <th>Broker Details</th>
            <th>Expiry Date</th>
            <th>Premium Due Date</th>
            <th>Comments</th>
            </tr>
            <#list charter.insurance.items as item>
            <tr>
            <td>${item.item}</td>
            <td>
            <select name="charter.insurance.items[${item_index}].required">
                <option value="false" <#if item.required?string = "false">SELECTED</#if>>No</option>
                <option value="true" <#if item.required?string = "true">SELECTED</#if>>Yes</option>
            </select>
            </td>
            <td><input name="charter.insurance.items[${item_index}].limit.amountAsDouble" type="text" style="width:80px;" value="${item.limit.amountAsDouble!}"/></td>
            <td><input name="charter.insurance.items[${item_index}].policyNumber" type="text" style="width:80px;" value="${item.policyNumber!}"/></td>
            <td><input name="charter.insurance.items[${item_index}].brokerDetails" type="text" style="width:80px;" value="${item.brokerDetails!}"/></td>
            <td><input name="charter.insurance.items[${item_index}].policyExpDate" type="text" style="width:70px;" value="<#if item.policyExpDate??>${item.policyExpDate?string('dd/MM/yyyy')}</#if>"/></td>
            <td><input name="charter.insurance.items[${item_index}].premiumDueDate" type="text" style="width:70px;" value="<#if item.premiumDueDate??>${item.premiumDueDate?string('dd/MM/yyyy')}</#if>"/></td>
            <td><input name="charter.insurance.items[${item_index}].comments" type="text" style="width:300px;" value="${item.comments!}"/></td>
            </tr>
            </#list>
            </table>
        </fieldset>
        </div>
        <hr class="clear"/>
		<button type="submit" class="smooth" style="float:right; margin-right:10px; margin-bottom: 4px;"><img src="images/icons/pencil.png"/>Save</button>
		<hr class="clear"/>
	</form>
</body>
</html>