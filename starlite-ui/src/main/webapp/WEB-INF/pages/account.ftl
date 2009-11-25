<body>
	<h1>Account Settings</h1>
	<form action="${request.contextPath}/account!save.action" method="POST">
		<div style="width: 500px;">
		<fieldset>
			<legend>Change Password</legend>
			<div class="fm-opt">
				<label for="firstName">Password:</label>
				<input name="newPassword1" type="password" value=""/>
			</div>
			<div class="fm-opt">
				<label for="newPassword2">Re-Enter Password:</label>
				<input name="newPassword2" type="password" value=""/>
			</div>
		</fieldset>
		<button type="submit" style="float:right;"><img src="${request.contextPath}/images/icons/pencil.png"/>Save</button>
		<hr class="clear"/>
		</div>
	</form>
</body>