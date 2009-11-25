<#include "/starlite.ftl">
<html>
<head>
  <title>${charter.administrative.firstName!} ${charter.administrative.lastName!}</title>
  <link rel="stylesheet" type="text/css" href="${request.contextPath}/styles/muffin.css">
</head>

<body>
	<@subTabs/>

	<#if notAuthorised>
	<form action="#" method="POST" style="padding:20px;" class="smart readonly">
	<fieldset>
		<legend>Not Authorised</legend>
		<br/>
	</fieldset>
	</form>
	<#else>

	<form action="charter!save.action" method="POST" class="smart" style="clear:left; padding:20px;">
		<input type="hidden" name="id" value="${charter.id!}"/>
		<input type="hidden" name="charter.id" value="${charter.id!}"/>
		<input type="hidden" name="tab" value="pricing"/>
		<div style="float:left; width: 700px;">
		<fieldset>
            <#assign currency=charter.administrative.currency!/>
            <legend>Pricing :
            <#if currency == 'USD'>USD ($)</#if>
            <#if currency == 'EUR'>EUR (&euro;)</#if>
            <#if currency == 'GBP'>GBP (&pound;)</#if></legend>
            <table>
            <tr>
            <th>Item/Type</th>
            <th>Status</th>
            <th>Limit/Value</th>
            <th>Comments</th>
            </tr>
            <#list charter.pricing.items as item>
            <tr>
            <td>${item.item}</td>
            <td>
            <select name="charter.pricing.items[${item_index}].status">
                <#if item.status??>
                <#assign istatus=item.status>
                <#else>
                <#assign istatus="No">
                </#if>
                <option value="No" <#if istatus="No">SELECTED</#if>>No</option>
                <option value="Yes" <#if istatus="Yes">SELECTED</#if>>Yes</option>
                <option value="Extra" <#if istatus="Extra">SELECTED</#if>>Extra</option>
                <option value="Included" <#if istatus="Included">SELECTED</#if>>Included</option>
                <option value="Excluded" <#if istatus="Excluded">SELECTED</#if>>Excluded</option>
            </select>
            </td>
            <td><input name="charter.pricing.items[${item_index}].limit.amountAsDouble" type="text" style="width:100px;" value="${item.limit.amountAsDouble!}"/></td>
            <td><input name="charter.pricing.items[${item_index}].comments" type="text" style="width:280px;" value="${item.comments!}"/></td>
            </tr>
            </#list>
            </table>
        </fieldset>
        </div>
        <hr class="clear"/>
		<button type="submit" class="smooth" style="float:right; margin-right:10px; margin-bottom: 4px;"><img src="images/icons/pencil.png"/>Save</button>
		<hr class="clear"/>
	</form>

	</#if>
</body>
</html>