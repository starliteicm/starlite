<body>
	<form action="${request.requestURL}" method="POST">
		<fieldset>
			<legend>New Staff</legend>
			<div class="fm-opt">
				<label for="newUsername">Username:</label>
				<input name="newUsername" type="text" value=""/>
			</div>
			<div class="fm-opt">
				<label for="newUserRole">Role:</label>
				<select name="newUserRole">
					<option value="Manager">Manager (Highest)</option>
					<option value="Supervisor">Supervisor</option>
					<option value="Office">Office (Lowest)</option>
				</select>
			</div>
			<input type="submit" value="Add"/>
			<input type="hidden" name="completed" value="true"/>
		</fieldset>
	</form>
</body>