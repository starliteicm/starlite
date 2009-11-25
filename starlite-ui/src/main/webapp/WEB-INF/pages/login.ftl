<html>
<head>
	<title>Authorization Required</title>
	<link rel="stylesheet" type="text/css" href="styles/forms.css"/>
</head>
<body>
	<h1>You must login to access this page</h1>
	<form class="smart" action="doLogin" method="POST" style="width:500px;">
		<fieldset>
			<legend>Login</legend>
			<div class="fm-opt">
				<label labelfor="username"/>
			</div>
			
			<div class="fm-opt">
				<label for="username">Username</label>
				<input name="username" id="username" type="text"/>
			</div>
			<div class="fm-opt">
				<label for="password">Password</label>
				<input name="password" id="password" type="password"/>
			</div>
			<div class="fm-opt">
				<label for="rememberMe">Remember Me?</label>
				<input name="rememberMe" id="rememberMe" type="checkbox" class="checkbox" value="true"/>
			</div>
			<input type="submit" value="Log In"/>
		</fieldset>
	</form>
</body>
</html>
