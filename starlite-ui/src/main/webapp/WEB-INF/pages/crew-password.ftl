<body>
	<h1>Change password for user ${username}</h1>
	<form action="${request.requestURL}" method="POST" style="width:350px;">
		<div style="width: 500px;">
		<input type="hidden" name="username" value="${username}"/>
		<input type="hidden" name="doChange" value="true"/>
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