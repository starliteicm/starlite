<#include "/starlite.ftl">
<html>
<head>
  <title>${crewMember.personal.firstName!} ${crewMember.personal.lastName!}</title>
  <link rel="stylesheet" type="text/css" href="styles/forms.css">
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
		<#if readOnly>
		<form action="#" method="POST" style="padding:20px;" class="smart readonly">
		<#else>
		<form action="crewMember!save.action" method="POST" class="smart" style="padding:20px;">
		</#if>
			<input type="hidden" name="id" value="${id!}"/>
			<input type="hidden" name="crewMember.id" value="${crewMember.code!}"/>
			<input type="hidden" name="tab" value="review"/>
			<fieldset>
				<legend>Comments</legend>
				<textarea name="crewMember.review.comments" style="width:600px; height:400px;">${crewMember.review.comments!}</textarea>
				<br/>
			</fieldset>
			<#if !readOnly>
			<button type="submit" class="smooth" style="float:left; margin-left:10px; margin-bottom: 4px;"><img src="images/icons/pencil.png"/>Save</button>
			<hr class="clear"/>
			</#if>
		</form>
	</#if>
</body>
</html>
