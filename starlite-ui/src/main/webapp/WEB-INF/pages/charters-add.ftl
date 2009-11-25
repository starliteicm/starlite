<body>
	<form action="${request.requestURL}" method="POST">
		<div style="width: 500px;">
		<fieldset>
			<legend>New Charter</legend>
			<input type="hidden" name="prepareAdd" value="false"/>
			<div class="fm-opt">
				<label for="charterCode">Charter Code:</label>
				<input name="charterCode" type="text" value="${charterCode!}"/>
			</div>
		</fieldset>
		<button type="submit"><img src="${request.contextPath}/images/icons/add.png"/>Add</button>
		</div>
	</form>
</body>