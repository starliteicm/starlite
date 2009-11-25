<html>
<head>
	<title>Self Service</title>
	<link rel="stylesheet" type="text/css" href="styles/forms.css">
</head>

<body>
	<h1>Self Service</h1>

	<form>
	<div style="float:left; width: 500px;">
		<fieldset>
			<legend>Personal Information</legend>
			<div class="fm-opt">
				<label for="firstName">First Name:</label>
				<input name="firstName" id="firstName" type="text"/>
			</div>
			<div class="fm-opt">
				<label for="lastName">Last Name:</label>
				<input name="lastName" id="lastName" type="text"/>
			</div>
			<div class="fm-opt">
				<label for="contactNumber">Contact Number:</label>
				<input name="contactNumber" id="contactNumber" type="text"/>
			</div>
		</fieldset>
		
		<fieldset>
			<legend>Credentials</legend>
			<div class="fm-opt">
				<label for="licenceNumber">Licence Number:</label>
				<input name="licenceNumber" id="licenceNumber" type="text"/>
			</div>
			<div class="fm-opt">
				<label for="licenceNumber">Licence Type:</label>
				<select name="licenceNumber" id="licenceNumber">
					<option value="type1">Licence Type 1</option>
					<option value="type2">Licence Type 2</option>
					<option value="type3">Licence Type 3</option>
				</select>
			</div>
			<div class="fm-opt">
				<label for="licenceExpiryDate">Licence Expiry Date:</label>
				<input name="licenceExpiryDate" id="licenceExpiryDate" type="text" style="width:78px;"/>
				<button id="showLicenceDatePicker">Change</button>
			</div>
			<div class="fm-opt">
				<label for="ratings">Ratings:</label>
				<input name="ratings" id="ratings" type="text"/>
			</div>
			<div class="fm-opt">
				<label for="medicalExpiry">Medical Expiry:</label>
				<input name="medicalExpiryDate" id="medicalExpiryDate" type="text" style="width:78px;"/>
				<button id="showMedicalDatePicker">Change</button>
			</div>
			<div class="fm-opt">
				<label for="ifExpiry">IF Expiry:</label>
				<input name="ifExpiryDate" id="ifExpiryDate" type="text" style="width:78px;"/>
				<button id="showIfDatePicker">Change</button>
			</div>
			<div class="fm-opt">
				<label for="instructorExpiry">Instructor Expiry:</label>
				<input name="instructorExpiry" id="instructorExpiry" type="text" style="width:78px;"/>
				<button id="showInstructorDatePicker">Change</button>
			</div>
			<div class="fm-opt">
				<label for="dgExpiry">DG Expiry:</label>
				<input name="dgExpiry" id="dgExpiry" type="text" style="width:78px;"/>
				<button id="showDgDatePicker">Change</button>
			</div>
			<div class="fm-opt">
				<label for="crmExpiry">CRM Expiry:</label>
				<input name="crmExpiry" id="crmExpiry" type="text" style="width:78px;"/>
				<button id="showCrmDatePicker">Change</button>
			</div>
			<div class="fm-opt">
				<label for="baseCheckDone">Base Check - Date Done:</label>
				<input name="baseCheckDone" id="baseCheckDone" type="text" style="width:78px;"/>
				<button id="showBaseCheckDoneDatePicker">Change</button>
			</div>
			<div class="fm-opt">
				<label for="typeDoneOn">Type Done On:</label>
				<select name="typeDoneOn" id="typeDoneOn">
					<option value="type1">Type 1</option>
					<option value="type2">Type 2</option>
					<option value="type3">Type 3</option>
				</select>
			</div>
			<div class="fm-opt">
				<label for="nvgTraining">NVG Training:</label>
				<input name="nvgTraining" id="nvgTraining" type="text"/>
			</div>
			<div class="fm-opt">
				<label for="underslingRating">Undersling Rating:</label>
				<input name="underslingRating" id="underslingRating" type="text"/>
			</div>
		</fieldset>
	</div>
	<div style="float:left; width: 500px; margin-left:50px;">
		<fieldset>
			<legend>Documents</legend>
			<div class="fm-opt">
				<label for="technicalQuizOnFile">Technical Quiz On File:</label>
				<input name="technicalQuizOnFile" id="technicalQuizOnFile" type="checkbox"/>
			</div>
			<div class="fm-opt">
				<label for="cvOnFile">CV On File:</label>
				<input name="cvOnFile" id="cvOnFile" type="checkbox"/>
			</div>
			<div class="fm-opt">
				<label for="contractOnFile">Contract On File:</label>
				<input name="contractOnFile" id="contractOnFile" type="checkbox"/>
			</div>
			<div class="fm-opt">
				<label for="part127Complete">Part 127 Read And Signed:</label>
				<input name="part127Complete" id="part127Complete" type="checkbox"/>
			</div>
			<div class="fm-opt">
				<label for="updatedHoursOnFile">Updated Hours On File:</label>
				<input name="updatedHoursOnFile" id="updatedHoursOnFile" type="checkbox"/>
			</div>
		</fieldset>
		
		<fieldset>
			<legend>Hours</legend>
			<div class="fm-opt">
				<label for="r22Hours">R22 Hours:</label>
				<input name="r22Hours" id="r22Hours" type="text"/>
			</div>
			<div class="fm-opt">
				<label for="r44Hours">R44 Hours:</label>
				<input name="r44Hours" id="r44Hours" type="text"/>
			</div>
			<div class="fm-opt">
				<label for="b206Hours">B206 Hours:</label>
				<input name="b206Hours" id="b206Hours" type="text"/>
			</div>
			<div class="fm-opt">
				<label for="ec120Hours">EC120 Hours:</label>
				<input name="ec120Hours" id="ec120Hours" type="text"/>
			</div>
			<div class="fm-opt">
				<label for="b2Hours">B2 Hours:</label>
				<input name="b2Hours" id="b2Hours" type="text"/>
			</div>
			<div class="fm-opt">
				<label for="b4Hours">B4 Hours:</label>
				<input name="b4Hours" id="b4Hours" type="text"/>
			</div>
			<div class="fm-opt">
				<label for="totalHeliHours">Total Heli Hours:</label>
				<input name="totalHeliHours" id="totalHeliHours" type="text"/>
			</div>
		</fieldset>
		
		<fieldset>
			<legend>Other</legend>
			<div class="fm-opt">
				<label for="330onLicence">330 On Licence:</label>
				<input name="330onLicence" id="330onLicence" type="text"/>
			</div>
			<div class="fm-opt">
				<label for="330hours">330 Hours:</label>
				<input name="330hours" id="330hours" type="text"/>
			</div>
			<div class="fm-opt">
				<label for="instructorOn330">Instructor On 330:</label>
				<input name="instructorOn330" id="instructorOn330" type="text"/>
			</div>
			<div class="fm-opt">
				<label for="passportExpires">Passport Expires:</label>
				<input name="passportExpires" id="passportExpires" type="text" style="width:78px;"/>
				<button id="showPassportExpiresDatePicker">Change</button>
			</div>
			<div class="fm-opt">
				<label for="Position">Position:</label>
				<input name="Position" id="Position" type="text"/>
			</div>
		</fieldset>
		<div id="fm-submit" class="fm-opt">
      		<input name="Submit" value="Save" type="submit"/>
      		<input name="Cancel" value="Cancel" type="reset"/>
    	</div>
    </div>
	</form>
</body>
</html>