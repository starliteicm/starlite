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
		<input type="hidden" name="tab" value="resources"/>
		<div style="float:left; width: 500px;">
		<fieldset>
			<legend>Crew requirement</legend>
			<div class="fm-opt">
				<label for="charter.resources.captainRequirement">Captain:</label>
				<input name="charter.resources.captainRequirement" type="text" style="width:76px;" value="${charter.resources.captainRequirement!}"/>
			</div>
			<div class="fm-opt">
				<label for="charter.resources.copilotRequirement">Co-pilot:</label>
				<input name="charter.resources.copilotRequirement" type="text" style="width:76px;" value="${charter.resources.copilotRequirement!}"/>
			</div>
            <div class="fm-opt">
				<label for="charter.resources.flightEngineerRequirement">Flight engineer:</label>
				<input name="charter.resources.flightEngineerRequirement" type="text" style="width:76px;" value="${charter.resources.flightEngineerRequirement!}"/>
			</div>
			<div class="fm-opt">
				<label for="charter.resources.groundEngineerRequirement">Ground engineer:</label>
				<input name="charter.resources.groundEngineerRequirement" type="text" style="width:76px;" value="${charter.resources.groundEngineerRequirement!}"/>
			</div>
            <div class="fm-opt">
				<label for="charter.resources.groundCrewRequirement">Ground crew:</label>
				<input name="charter.resources.groundCrewRequirement" type="text" style="width:76px;" value="${charter.resources.groundCrewRequirement!}"/>
			</div>
			<div class="fm-opt">
				<label for="charter.resources.comments">Comments:</label>
				<input name="charter.resources.comments" type="text" style="width:300px;" value="${charter.resources.comments!}"/>
			</div>
        </fieldset>
        <fieldset>
            <legend>Aircraft requirement</legend>
            <div class="fm-opt">
				<label for="charter.resources.aircraftType">Aircraft Type:</label>
				<input name="charter.resources.aircraftType" type="text" style="width:300px;" value="${charter.resources.aircraftType!}"/>
			</div>
        </fieldset>
        <fieldset>
            <legend>Aircraft unavailablity limits</legend>
            <div class="fm-opt">
				<label for="charter.resources.maximumTerm">Maximum term:</label>
				<input name="charter.resources.maximumTerm" type="text" style="width:76px;" value="${charter.resources.maximumTerm!}"/>
			</div>
            <div class="fm-opt">
				<label for="charter.resources.forfeitPerDay.amountAsDouble">Forfeit per day:</label>
				<input name="charter.resources.forfeitPerDay.amountAsDouble" type="text" style="width:76px;" value="${charter.resources.forfeitPerDay.amountAsDouble!}"/>
			</div>
		</fieldset>
        </div>
        <hr class="clear"/>
		<button type="submit" class="smooth" style="float:right; margin-right:10px; margin-bottom: 4px;"><img src="images/icons/pencil.png"/>Save</button>
		<hr class="clear"/>
	</form>
</body>
</html>