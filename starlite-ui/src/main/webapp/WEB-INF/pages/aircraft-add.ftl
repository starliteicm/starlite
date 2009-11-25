<body>
	<form action="${request.requestURL}" method="POST">
		<div style="width: 500px;">
		<fieldset>
			<legend>New Aircraft</legend>
			<input type="hidden" name="prepareAdd" value="false"/>
			<div class="fm-opt">
				<label for="aircraftReg">Registration:</label>
				<input name="aircraftReg" type="text" value="${aircraftReg!}"/>
			</div>
		</fieldset>
		<button type="submit"><img src="${request.contextPath}/images/icons/add.png"/>Add</button>
		</div>
	</form>
</body>