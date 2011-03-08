<html>
<head>
	<link rel="stylesheet" type="text/css" href="styles/forms.css">
</head>

<body>
	<form action="documents!upload.action"
       enctype="multipart/form-data"
       method="post">
       <div style="float:left; width: 500px;">
		<fieldset>
			<legend>Upload Document</legend>
			<div class="fm-opt">
				<label for="document">File: </label>
				<input name="document" id="document" type="file"/>
			</div>
			<div class="fm-opt">
				<label for="tags">Tags: </label>
				<input name="tags" id="tags" type="text"/><input type="submit" value="Upload"/>
			</div>
		</fieldset>
		</div>
		<hr style="clear:left; background-color:#fff;border: 0;width: 80%;"/>
		<input type="hidden" name="folder" value="${folder}"/>
		<input type="hidden" name="returnUrl" value="${returnUrl!}"/>
	</form>
</body>
</html>
