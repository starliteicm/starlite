<body>
	<form action="${request.requestURL}" method="POST">
	<input name="typeId" type="hidden" value="${aircraftType?if_exists.id?if_exists}"/>
		<div style="width: 500px;">
		<fieldset>
			<legend>Aircraft Type</legend>
			<input type="hidden" name="prepareAdd" value="false"/>
			<div class="fm-opt">
				<label for="typeName">Name:</label>
				<input name="typeName" type="text" value="${aircraftType?if_exists.name?if_exists}"/>
			</div>
			<div class="fm-opt">
				<label for="typeDescription">Description:</label>
				<input name="typeDescription" type="text" value="${aircraftType?if_exists.description?if_exists}"/>
			</div>
		</fieldset>
		<button type="submit"><img src="${request.contextPath}/images/icons/add.png"/>Save</button>
		</div>
	</form>
</body>