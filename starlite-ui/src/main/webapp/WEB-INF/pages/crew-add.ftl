<body>
	<form action="${request.requestURL}" method="POST">
		<div style="width: 500px;">
		<fieldset>
			<legend>New Crew Member</legend>
			<input type="hidden" name="prepareAdd" value="false"/>

			
			<div class="fm-opt">
				<label for="">Title</label>
	    		<select name="title">
	    			<option>Mr
	    			<option>Master
	    			<option>Mrs
	    			<option>Miss
	    			<option>Ms
	    			<option>Doctor
	    			<option>Professor
	    			<option>Sir
	    		</select>
	    		<br/>
	    	</div>
			
			<div class="fm-opt">
				<label for="firstName">First Name:</label>
				<input name="firstName" type="text" value="${firstName!}"/>
			</div>
			<div class="fm-opt">
				<label for="lastName">Last Name:</label>
				<input name="lastName" type="text" value="${lastName!}"/>
			</div>
			
		</fieldset>
		<button type="submit"><img src="${request.contextPath}/images/icons/add.png"/>Add</button>
		</div>
	</form>
</body>