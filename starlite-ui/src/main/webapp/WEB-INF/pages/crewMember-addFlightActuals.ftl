<#include "/starlite.ftl">
<html>
<head>
	<@enableDatePickers/>
</head>
<body>
	<@subTabs/>
	<script>
		var amountPaidChangedByUser = false;
		
		var monthlyRate = ${actuals.monthlyRate.amountAsDouble?c};
		var areaRate = ${actuals.areaRate.amountAsDouble?c};
		var dailyRate = ${actuals.dailyRate.amountAsDouble?c};
		var flightRate = ${actuals.flightRate.amountAsDouble?c};
		<#if actuals.basePilotRate??>
		var basePilotRate = ${actuals.basePilotRate.amountAsDouble?c};
		<#else>
		var basePilotRate = 0;
		</#if>
		<#if actuals.safetyLevelRate??>
		var safetyLevelRate = ${actuals.safetyLevelRate.amountAsDouble?c};
		<#else>
		var safetyLevelRate = 0;
		</#if>
		<#if actuals.instructorRate??>
		var instructorRate = ${actuals.instructorRate.amountAsDouble?c};
		<#else>
		var instructorRate = 0;
		</#if>
		
		var symbol = "${actuals.monthlyRate.currency}";
		if (symbol == "GBP")
			symbol = "\u00a3";
		else if (symbol == "USD")
			symbol = "\u0024";
		else if (symbol == "EUR")
			symbol = "\u20ac";
		else
			symbol = symbol + " ";
			
		var newRowIndex = 0;
		var newDeductionRowIndex = 0;
		var newAdditionRowIndex = 0;
		
		var noEntriesRowVisible;
		var noDeductionsRowVisible;
		var noAdditionsRowVisible;
		
		function getSelected(selection){
            for(i=0;i<selection.length;i++) {
                //alert(selection[i].value);
                if(selection[i].selected ){
                    return selection[i].value;
                }
            }
            return false;
        }
        
        function saveAddition(reason,currencyId,amountId,amountUSDId){
          //alert(reason+" - "+amountId);
          var amou = document.getElementById(amountId);
          var amount = amou.value;
          
          var amouUSD = document.getElementById(amountUSDId);
          var amountUSD = amouUSD.value;
          
          var cur = document.getElementById(currencyId);
          var currency = getSelected(cur);
          
          var additionForm = document.forms.mainForm;
          
          if((0+amount > 0)||(0+amountUSD > 0)){
              var action = "crewMember!addAddition.action";
              additionForm.action          = action;
              additionForm.amount.value    = amount;
              additionForm.currency.value  = currency;
              additionForm.amountUSD.value = amountUSD;
              additionForm.reason.value    = reason;
              additionForm.submit();
          }
          else if((0+amount == 0)||(0+amountUSD == 0)){
              var action = "crewMember!remAddition.action";
              additionForm.action       = action;
              additionForm.reason.value = reason; 
              additionForm.submit();        
          }
          else {
            alert(amount+" is not a valid amount");
          }
          
          return false;
        }
		
		function saveDeduction(reason,currencyId,amountId,amountUSDId){
		  //alert(reason+" - "+amountId);
		  var amou = document.getElementById(amountId);
		  var amount = amou.value;
		  
		  var amouUSD = document.getElementById(amountUSDId);
		  var amountUSD = amouUSD.value;
		  
		  var cur = document.getElementById(currencyId);
		  var currency = getSelected(cur);
		  
		  var deductForm = document.forms.mainForm;
		  
		  if((0+amount > 0)||(0+amountUSD > 0)){
		      var action = "crewMember!addDeduction.action";
              deductForm.action          = action;
              deductForm.amount.value    = amount;
              deductForm.currency.value  = currency;
              deductForm.amountUSD.value = amountUSD;
              deductForm.reason.value    = reason;
              deductForm.submit();
		  }
	      else if((0+amount == 0)||(0+amountUSD == 0)){
	          var action = "crewMember!remDeduction.action";
	          deductForm.action       = action;
	          deductForm.reason.value = reason;	
	          deductForm.submit();        
	      }
	      else {
            alert(amount+" is not a valid amount");
          }
		  
		  return false;
		}

        function saveNewAddition(reasonId,currencyId,amountId,amountUSDId){
          //alert(reasonId+" - "+amountId);
          var reas = document.getElementById(reasonId);
          var amou = document.getElementById(amountId);
          var cur = document.getElementById(currencyId);
          var reason = getSelected(reas); 
          var currency = getSelected(cur);
          var amount = amou.value;
          
          var amouUSD = document.getElementById(amountUSDId);
          var amountUSD = amouUSD.value;
          
          var additionForm = document.forms.mainForm;
          
          
          if((0+amount > 0)||(0+amountUSD > 0)){
              var action = "crewMember!addAddition.action";
              additionForm.action          = action;
              additionForm.amount.value    = amount;
              additionForm.currency.value  = currency;
              additionForm.amountUSD.value = amountUSD;
              additionForm.reason.value    = reason;
              additionForm.submit();
          }
          else if((0+amount == 0)||(0+amountUSD == 0)){
              var action = "crewMember!remAddition.action";
              additionForm.action       = action;
              additionForm.reason.value = reason; 
              additionForm.submit();        
          }
          else {
            alert(amount+" is not a valid amount");
          }
          
          return false;
        }
		
		function saveNewDeduction(reasonId,currencyId,amountId,amountUSDId){
		  //alert(reasonId+" - "+amountId);
          var reas = document.getElementById(reasonId);
          var amou = document.getElementById(amountId);
          var cur = document.getElementById(currencyId);
          var reason = getSelected(reas); 
          var currency = getSelected(cur);
          var amount = amou.value;
          
          var amouUSD = document.getElementById(amountUSDId);
          var amountUSD = amouUSD.value;
          
          var deductForm = document.forms.mainForm;
          
          
          if((0+amount > 0)||(0+amountUSD > 0)){
              var action = "crewMember!addDeduction.action";
              deductForm.action          = action;
              deductForm.amount.value    = amount;
              deductForm.currency.value  = currency;
              deductForm.amountUSD.value = amountUSD;
              deductForm.reason.value    = reason;
              deductForm.submit();
          }
          else if((0+amount == 0)||(0+amountUSD == 0)){
              var action = "crewMember!remDeduction.action";
              deductForm.action       = action;
              deductForm.reason.value = reason; 
              deductForm.submit();        
          }
          else {
            alert(amount+" is not a valid amount");
          }
          
          return false;
		}
		
		
		function addRow() {
			var entryTableBody = document.getElementById("entryTableBody");
			var newRow = document.createElement("tr");
			
			var tmpTd = document.createElement("td");
			tmpTd.setAttribute("style", "padding:5px;");
			var select = document.createElement("select");
			select.setAttribute("name", "newEntryFirKey"+newRowIndex);
			select.setAttribute("style", "width:100px;");
			var tmpOption;
			<#list allCharters as c>
			tmpOption = document.createElement("option");
			tmpOption.setAttribute("value", '${c.code}');
			tmpOption.appendChild(document.createTextNode('${c.code}'));
			select.appendChild(tmpOption);
			</#list>
			tmpTd.appendChild(select);
			newRow.appendChild(tmpTd);
			
			//do the same for aircraft here.
			
			var tmpTd = document.createElement("td");
			tmpTd.setAttribute("style", "padding:5px;");
			var select = document.createElement("select");
			select.setAttribute("name", "newEntrySecKey"+newRowIndex);
			select.setAttribute("style", "width:100px;");
		    <#list allAircraft as a>
			tmpOption = document.createElement("option");
			tmpOption.setAttribute("value", '${a.ref}');
			tmpOption.appendChild(document.createTextNode('${a.ref}'));
			select.appendChild(tmpOption);
			</#list>
			tmpTd.appendChild(select);
			newRow.appendChild(tmpTd);
			
			
			var tmpInput;
			<#list ['Area', 'Daily', 'Flight', 'BasePilot', 'SafetyLevel', 'Instructor'] as field>
			tmpTd = document.createElement("td");
			tmpTd.setAttribute("style", "padding:5px;");
			tmpInput = document.createElement("input");
			tmpInput.setAttribute("type", "text");
			tmpInput.setAttribute("name", "newEntry${field}"+newRowIndex);
			tmpInput.setAttribute("value", "0");
			tmpInput.setAttribute("style", "width:40px;");
			tmpTd.appendChild(tmpInput);
			newRow.appendChild(tmpTd);
			</#list>
			
			tmpTd = document.createElement("td");
			tmpTd.setAttribute("style", "padding:5px;");
			select = document.createElement("select");
            select.setAttribute("name", "newEntryDiscomfort"+newRowIndex);
            select.setAttribute("style", "width:40px;");
            <#list [0,1,2,3,4,5,6,7,8,9] as field>
            tmpOption = document.createElement("option");
            tmpOption.setAttribute("value", '${field}');
            tmpOption.appendChild(document.createTextNode('${field}'));
            select.appendChild(tmpOption);
            </#list>
			tmpTd.appendChild(select);
			newRow.appendChild(tmpTd);
			
			if (noEntriesRowVisible) {
				entryTableBody.removeChild(document.getElementById("noEntriesRow"));
				noEntriesRowVisible = false;
			}
			entryTableBody.appendChild(newRow);
			newRowIndex++;
		}
		
		
		function addRowDeductions() {
            var deductionTableBody = document.getElementById("deductionTableBody");
            var newRow = document.createElement("tr");
            
            var tmpTd = document.createElement("td");
            var select = document.createElement("select");
            select.setAttribute("name", "newDeductionFirKey"+newDeductionRowIndex);
            select.setAttribute("id", "newDeductionFirKey"+newDeductionRowIndex);
            select.setAttribute("style", "width:100px;");
            var tmpOption;
            
            
            
            <#list ["Medical Aid","GAP cover","Offshore Investment","Sundries","Vitality"] as c>
            tmpOption = document.createElement("option");
            tmpOption.setAttribute("value", '${c}');
            tmpOption.appendChild(document.createTextNode('${c}'));
            select.appendChild(tmpOption);
            </#list>
            tmpTd.appendChild(select);
            newRow.appendChild(tmpTd);
          
            var tmpInput;
            tmpTd = document.createElement("td");
            tmpInput = document.createElement("input");
            tmpInput.setAttribute("type", "text");
            tmpInput.setAttribute("name", "newDeductionAmount"+newDeductionRowIndex);
            tmpInput.setAttribute("id", "newDeductionAmount"+newDeductionRowIndex);
            tmpInput.setAttribute("value", "0");
            tmpInput.setAttribute("style", "width:60px;");
            tmpTd.appendChild(tmpInput);
            newRow.appendChild(tmpTd);
            
            
            tmpTd = document.createElement("td");
            select = document.createElement("select");
            select.setAttribute("name", "newDeductionCurrency"+newDeductionRowIndex);
            select.setAttribute("id", "newDeductionCurrency"+newDeductionRowIndex);
            select.setAttribute("style", "width:60px;");
         
            <#list rates as rate>
            tmpOption = document.createElement("option");
            tmpOption.setAttribute("value", '${rate.currencyCodeFrom}');
            tmpOption.appendChild(document.createTextNode('${rate.currencyCodeFrom}'));
            select.appendChild(tmpOption);
            </#list>
            tmpTd.appendChild(select);
            newRow.appendChild(tmpTd);
            
            var tmpInput;
            tmpTd = document.createElement("td");
            tmpInput = document.createElement("input");
            tmpInput.setAttribute("type", "text");
            tmpInput.setAttribute("name", "newDeductionAmountUSD"+newDeductionRowIndex);
            tmpInput.setAttribute("id", "newDeductionAmountUSD"+newDeductionRowIndex);
            tmpInput.setAttribute("value", "0");
            tmpInput.setAttribute("style", "width:60px;");
            tmpTd.appendChild(tmpInput);
            newRow.appendChild(tmpTd);
            
            var tmpInput;
            tmpTd = document.createElement("td");
            tmpInput = document.createElement("button");
            tmpInput.setAttribute("type", "button");
            tmpInput.setAttribute("value", "Save");
            tmpInput.setAttribute("onclick", "saveNewDeduction('newDeductionFirKey"+newDeductionRowIndex+"','newDeductionCurrency"+newDeductionRowIndex+"','newDeductionAmount"+newDeductionRowIndex+"','newDeductionAmountUSD"+newDeductionRowIndex+"');return false;");
            tmpInput.setAttribute("class", "smooth");
            tmpInput.innerHTML = "<img src='images/icons/pencil.png'/>Save";
            tmpTd.appendChild(tmpInput);
            newRow.appendChild(tmpTd);
            
            if (noDeductionsRowVisible) {
                deductionTableBody.removeChild(document.getElementById("noDeductionsRow"));
                noDeductionsRowVisible = false;
            }
            deductionTableBody.appendChild(newRow);
            newDeductionRowIndex++;
        }
		
		
		function addRowAdditions() {
				
            var additionTableBody = document.getElementById("additionTableBody");
            var newRow = document.createElement("tr");
            
            var tmpTd = document.createElement("td");
            var select = document.createElement("select");
            select.setAttribute("name", "newAdditionFirKey"+newAdditionRowIndex);
            select.setAttribute("id", "newAdditionFirKey"+newAdditionRowIndex);
            select.setAttribute("style", "width:100px;");
            var tmpOption;
            
            
            
            <#list ["Medical Aid","GAP cover","Offshore Investment","Sundries","Sundries2","Sundries3","Sundries4","Sundries5","Pension"] as c>
            tmpOption = document.createElement("option");
            tmpOption.setAttribute("value", '${c}');
            tmpOption.appendChild(document.createTextNode('${c}'));
            select.appendChild(tmpOption);
            </#list>
            tmpTd.appendChild(select);
            newRow.appendChild(tmpTd);
          
            var tmpInput;
            tmpTd = document.createElement("td");
            tmpInput = document.createElement("input");
            tmpInput.setAttribute("type", "text");
            tmpInput.setAttribute("name", "newAdditionAmount"+newAdditionRowIndex);
            tmpInput.setAttribute("id", "newAdditionAmount"+newAdditionRowIndex);
            tmpInput.setAttribute("value", "0");
            tmpInput.setAttribute("style", "width:60px;");
            tmpTd.appendChild(tmpInput);
            newRow.appendChild(tmpTd);
            
            
            tmpTd = document.createElement("td");
            select = document.createElement("select");
            select.setAttribute("name", "newAdditionCurrency"+newAdditionRowIndex);
            select.setAttribute("id", "newAdditionCurrency"+newAdditionRowIndex);
            select.setAttribute("style", "width:60px;");
         
            <#list rates as rate>
            tmpOption = document.createElement("option");
            tmpOption.setAttribute("value", '${rate.currencyCodeFrom}');
            tmpOption.appendChild(document.createTextNode('${rate.currencyCodeFrom}'));
            select.appendChild(tmpOption);
            </#list>
            tmpTd.appendChild(select);
            newRow.appendChild(tmpTd);
            
            var tmpInput;
            tmpTd = document.createElement("td");
            tmpInput = document.createElement("input");
            tmpInput.setAttribute("type", "text");
            tmpInput.setAttribute("name", "newAdditionAmountUSD"+newAdditionRowIndex);
            tmpInput.setAttribute("id", "newAdditionAmountUSD"+newAdditionRowIndex);
            tmpInput.setAttribute("value", "0");
            tmpInput.setAttribute("style", "width:60px;");
            tmpTd.appendChild(tmpInput);
            newRow.appendChild(tmpTd);
            
            var tmpInput;
            tmpTd = document.createElement("td");
            tmpInput = document.createElement("button");
            tmpInput.setAttribute("type", "button");
            tmpInput.setAttribute("value", "Save");
            tmpInput.setAttribute("onclick", "saveNewAddition('newAdditionFirKey"+newAdditionRowIndex+"','newAdditionCurrency"+newAdditionRowIndex+"','newAdditionAmount"+newAdditionRowIndex+"','newAdditionAmountUSD"+newAdditionRowIndex+"');return false;");
            tmpInput.setAttribute("class", "smooth");
            tmpInput.innerHTML = "<img src='images/icons/pencil.png'/>Save";
            tmpTd.appendChild(tmpInput);
            newRow.appendChild(tmpTd);
            
            if (noAdditionsRowVisible) {
                additionTableBody.removeChild(document.getElementById("noAdditionsRow"));
                noAdditionsRowVisible = false;
            }
            additionTableBody.appendChild(newRow);
            newAdditionRowIndex++;
        }
		
		
		/*
		function onFormChange() {
			var total = 0;
			
			if (document.getElementById("payMonthly").checked) {
				total += ${crewMember.payments.monthlyBaseRate.amountAsDouble?c};
			}
			
			var areaDays = 0;
			if (document.getElementById("areaDays").value) {
				areaDays = parseInt(document.getElementById("areaDays").value);
			}
			total += areaDays * ${crewMember.payments.areaAllowance.amountAsDouble?c};
			
			var dailyDays = 0;
			if (document.getElementById("dailyDays").value) {
				dailyDays = parseInt(document.getElementById("dailyDays").value);
			}
			total += dailyDays * ${crewMember.payments.dailyAllowance.amountAsDouble?c};
			
			var instructorDays = 0;
			if (document.getElementById("instructorDays").value) {
				instructorDays = parseInt(document.getElementById("instructorDays").value);
			}
			total += instructorDays * ${crewMember.payments.instructorAllowance.amountAsDouble?c};
			
			var flightDays = 0;
			if (document.getElementById("flightDays").value) {
				flightDays = parseInt(document.getElementById("flightDays").value);
			}
			total += flightDays * ${crewMember.payments.flightAllowance.amountAsDouble?c};
			
			var basePilotDays = 0;
			if (document.getElementById("basePilotDays").value) {
				basePilotDays = parseInt(document.getElementById("basePilotDays").value);
			}
			total += basePilotDays * ${crewMember.payments.basePilotAllowance.amountAsDouble?c};
			
			var safetyLevelDays = 0;
			if (document.getElementById("safetyLevelDays").value) {
				safetyLevelDays = parseInt(document.getElementById("safetyLevelDays").value);
			}
			total += safetyLevelDays * ${crewMember.payments.safetyLevelAllowance.amountAsDouble?c};
			
			var totalDiv = document.getElementById("total");
			totalDiv.replaceChild(document.createTextNode(symbol+total), totalDiv.firstChild);
			
		}
		*/
	</script>
	<form id="mainForm" action="crewMember!addFlightActuals.action" method="POST" class="smart" style="clear:left; border: 0px solid silver; padding: 10px;">
		<div style="float:left; width: 500px;">
		<fieldset>
			<legend>Period</legend>
			<input type="hidden" name="actualsCompleted" value="true"/>
			<input type="hidden" name="actuals.id" value="${actuals.id!}"/>
			<input type="hidden" name="id" value="${id}"/>
			<input type="hidden" name="actualsId" value="${actualsId!}"/>
			
			<div class="fm-opt">
				<label for="month">Month</label>
				<select name="month">
					<option value="1"<#if currentMonth==0> selected</#if>>January</option>
					<option value="2"<#if currentMonth==1> selected</#if>>February</option>
					<option value="3"<#if currentMonth==2> selected</#if>>March</option>
					<option value="4"<#if currentMonth==3> selected</#if>>April</option>
					<option value="5"<#if currentMonth==4> selected</#if>>May</option>
					<option value="6"<#if currentMonth==5> selected</#if>>June</option>
					<option value="7"<#if currentMonth==6> selected</#if>>July</option>
					<option value="8"<#if currentMonth==7> selected</#if>>August</option>
					<option value="9"<#if currentMonth==8> selected</#if>>September</option>
					<option value="10"<#if currentMonth==9> selected</#if>>October</option>
					<option value="11"<#if currentMonth==10> selected</#if>>November</option>
					<option value="12"<#if currentMonth==11> selected</#if>>December</option>
				</select>
			</div>
			<div class="fm-opt">
				<label for="year">Year</label>
				<input name="year" type="text" value="${currentYear?string('####')}"/>
			</div>
			<div class="fm-opt">
				<label for="actuals.monthlyRate.amountAsDouble">Monthly <@symbol "${actuals.monthlyRate.currency}"/></label>
				<input type="text" id="actuals.monthlyRate.amountAsDouble" name="actuals.monthlyRate.amountAsDouble" style="width:50px;" value="${actuals.monthlyRate.amountAsDouble}"/>
			</div>
			<div class="fm-opt">
			    <label for="actuals.payMonthlyRate">Pay Monthly Rate?</label>
				<select name="actuals.payMonthlyRate">
					<option value="true"<#if actuals.payMonthlyRate==true> selected</#if>>yes</option>
					<option value="false"<#if actuals.payMonthlyRate==false> selected</#if>>no</option>
				</select>
			</div>
		</fieldset>
		<fieldset>
			<legend>Allowance Values</legend>
			<div class="fm-opt">
				<label for="actuals.areaRate.amountAsDouble">Daily <@symbol "${actuals.areaRate.currency}"/></label>
				<input type="text" id="areaDays" onchange="onFormChange();" name="actuals.areaRate.amountAsDouble" style="width:50px;" value="${actuals.areaRate.amountAsDouble}"/>
			</div>
			<div class="fm-opt">
				<label for="actuals.dailyRate.amountAsDouble">Training <@symbol "${actuals.dailyRate.currency}"/></label>
				<input type="text" id="dailyDays" onchange="onFormChange();" name="actuals.dailyRate.amountAsDouble" style="width:50px;" value="${actuals.dailyRate.amountAsDouble}"/>
			</div>
			<div class="fm-opt">
				<label for="actuals.instructorRate.amountAsDouble">Instructor <@symbol "${actuals.instructorRate.currency}"/></label>
				<input type="text" id="instructorDays" onchange="onFormChange();" name="actuals.instructorRate.amountAsDouble" style="width:50px;" value="${actuals.instructorRate.amountAsDouble}"/>
			</div>
			<div class="fm-opt">
				<label for="actuals.flightRate.amountAsDouble">Travel <@symbol "${actuals.flightRate.currency}"/></label>
				<input type="text" id="flightDays" onchange="onFormChange();" name="actuals.flightRate.amountAsDouble" style="width:50px;" value="${actuals.flightRate.amountAsDouble}"/>
			</div>
			<div class="fm-opt">
			    <#if actuals.basePilotRate??>
				<label for="actuals.basePilotRate.amountAsDouble">Senior Base Pilot <@symbol "${actuals.basePilotRate.currency}"/></label>
				<input type="text" id="basePilotDays" onchange="onFormChange();" name="actuals.basePilotRate.amountAsDouble" style="width:50px;" value="${actuals.basePilotRate.amountAsDouble}"/>
			    <#else>
			    <label for="actuals.basePilotRate.amountAsDouble">Senior Base Pilot - Value Not Set<@symbol "USD"/></label>
			    
			    </#if>
			</div>
			<div class="fm-opt">
			    <#if actuals.safetyLevelRate??>
				<label for="actuals.safetyLevelRate.amountAsDouble">Safety Level <@symbol "${actuals.safetyLevelRate.currency}"/></label>
				<input type="text" id="safetyLevelDays" onchange="onFormChange();" name="actuals.safetyLevelRate.amountAsDouble" style="width:50px;" value="${actuals.safetyLevelRate.amountAsDouble!}"/>
				<#else>
				<label for="actuals.safetyLevelRate.amountAsDouble">Safety Level - Value Not Set<@symbol "USD"/></label>
				</#if>
				
			</div>
			

			
		</fieldset>
		</div>
		<div style="float:left; width: 540px; padding-left:10px;">
		<fieldset>
			<legend>Entries</legend>
			<table>
			<thead><tr><th style="padding:5px;">Charter</th><th style="padding:5px;">Aircraft</th><th style="padding:5px;">Daily</th><th style="padding:5px;">Training</th><th style="padding:5px;">Travel</th><th style="padding:5px;">SB Pilot</th><th style="padding:5px;">Safety Level</th><th style="padding:5px;">Instructor</th><th style="padding:5px;">Discomfort</th></tr></thead>
			<tbody id="entryTableBody">
			<#if actuals.entries.isEmpty()>
			<tr id="noEntriesRow"><td colspan="6">No Entries</td></tr>
			<script>
			noEntriesRowVisible=true;
			</script>
			<#else>
			<#list actuals.entries.keySet() as key>
			<#assign entry=actuals.entries.get(key)/>
			<#if entry.charter?exists>
			<tr><td style="width:100px;padding:5px;">${entry.charter?if_exists}</td>
			<#else>
		    <tr><td style="width:100px;padding:5px;">${key}</td>
			</#if>
			<td style="width:100px;padding:5px;">${entry.aircraft?if_exists}&nbsp;</td>
			<td style="padding:5px;"><input style="width:40px;" type="text" name='actuals.entries["${key}"].areaDays' value="${entry.areaDays}"/></td>
			<td style="padding:5px;"><input style="width:40px;" type="text" name='actuals.entries["${key}"].dailyDays' value="${entry.dailyDays}"/></td>
			<td style="padding:5px;"><input style="width:40px;" type="text" name='actuals.entries["${key}"].flightDays' value="${entry.flightDays}"/></td>
			<td style="padding:5px;"><input style="width:40px;" type="text" name='actuals.entries["${key}"].basePilotDays' value="${entry.basePilotDays}"/></td>
			<td style="padding:5px;"><input style="width:40px;" type="text" name='actuals.entries["${key}"].safetyLevelDays' value="${entry.safetyLevelDays}"/></td>
			<td style="padding:5px;"><input style="width:40px;" type="text" name='actuals.entries["${key}"].instructorDays' value="${entry.instructorDays}"/></td>
			<td style="padding:5px;">
			<select style="width:40px;" name='actuals.entries["${key}"].discomfort' >
			  <option>0</option>
			  <option <#if entry.discomfort==1 >SELECTED</#if>>1</option>
			  <option <#if entry.discomfort==2 >SELECTED</#if>>2</option>
			  <option <#if entry.discomfort==3 >SELECTED</#if>>3</option>
			  <option <#if entry.discomfort==4 >SELECTED</#if>>4</option>
			  <option <#if entry.discomfort==5 >SELECTED</#if>>5</option>
			  <option <#if entry.discomfort==6 >SELECTED</#if>>6</option>
			  <option <#if entry.discomfort==7 >SELECTED</#if>>7</option>
			  <option <#if entry.discomfort==8 >SELECTED</#if>>8</option>
			  <option <#if entry.discomfort==9 >SELECTED</#if>>9</option>
			</select>
			</tr>
			</#list>
			</#if>
			</tbody>
			</table>
			<#if user.hasPermission("ManagerEdit")>
			<a href="#" onclick="addRow();">Add New Entry</a>
			</#if>
		</fieldset>
		<br/>
		
		<fieldset>
            <legend>Deductions</legend>
            <table>
            <thead><tr><th style="width:200px">Reason</th><th style="width:100px">Amount</th><th style="width:100px">Currency</th><th style="width:100px">Amount (USD)</th><th style="width:100px">&nbsp;</th></tr></thead>
            <tbody id="deductionTableBody">
            <#if actuals.deductions.isEmpty()>
            <tr id="noDeductionsRow"><td colspan="3">No Deductions</td></tr>
            <script>
            noDeductionsRowVisible=true;
            </script>
            <#else>
            <#list actuals.deductions.keySet() as key>
            <#assign deduction=actuals.deductions.get(key)/>
            <#if deduction.reason?exists>
            <tr><td style="width:200px;">${deduction.reason?if_exists}</td>
            <#else>
            <tr><td style="width:200px;">${key}</td>
            </#if>
            <td><input style="width:60px;" onfocus="document.getElementById('Deduction${key.replace(" ","_")}AmountUSD').value = '';" type="text" name='Deduction${key.replace(" ","_")}Amount' id='Deduction${key.replace(" ","_")}Amount' value="${deduction.enteredStr}"/></td>
            <td>
                <select style="width:60px;" id="Deduction${key.replace(" ","_")}Currency" name="Deduction${key.replace(" ","_")}Currency">
                <#list rates as rate>
                   <option <#if deduction.currency == rate.currencyCodeFrom >SELECTED</#if>  >${rate.currencyCodeFrom}</option>
                </#list>
                </select>
            </td>
            <td><input style="width:60px;" onfocus="document.getElementById('Deduction${key.replace(" ","_")}Amount').value = '';" type="text" name='Deduction${key.replace(" ","_")}AmountUSD' id='Deduction${key.replace(" ","_")}AmountUSD' value="${deduction.USD}"/></td>
            <td><button type="button" class="smooth" onclick="saveDeduction('${deduction.reason?if_exists}','Deduction${key.replace(" ","_")}Currency','Deduction${key.replace(" ","_")}Amount','Deduction${key.replace(" ","_")}AmountUSD');return false;" ><img src="images/icons/pencil.png"/>Save</button></td>
            </tr>
            </#list>
            </#if>
            </tbody>
            </table>
            <#if user.hasPermission("ManagerEdit")>
            <a href="#" onclick="addRowDeductions();">Add New Deduction</a>
            </#if>
        </fieldset>
		
		<br/>
		
		<fieldset>
            <legend>Contributions</legend>
            <table>
            <thead><tr><th style="width:200px">Reason</th><th style="width:100px">Amount</th><th style="width:100px">Currency</th><th style="width:100px">Amount (USD)</th><th style="width:100px">&nbsp;</th></tr></thead>
            <tbody id="additionTableBody">
            <#if actuals.additions.isEmpty()>
            <tr id="noAdditionsRow"><td colspan="3">No Contributions</td></tr>
            <script>
            noAdditionsRowVisible=true;
            </script>
            <#else>
            <#list actuals.additions.keySet() as key>
            <#assign addition=actuals.additions.get(key)/>
            <#if addition.reason?exists>
            <tr><td style="width:200px;">${addition.reason?if_exists}</td>
            <#else>
            <tr><td style="width:200px;">${key}</td>
            </#if>
            <td><input style="width:60px;" onfocus="document.getElementById('Addition${key.replace(" ","_")}AmountUSD').value = '';" type="text" name='Addition${key.replace(" ","_")}Amount' id='Addition${key.replace(" ","_")}Amount' value="${addition.enteredStr}"/></td>
            <td>
                <select style="width:60px;" id="Addition${key.replace(" ","_")}Currency" name="Addition${key.replace(" ","_")}Currency">
                <#list rates as rate>
                   <option <#if addition.currency == rate.currencyCodeFrom >SELECTED</#if>  >${rate.currencyCodeFrom}</option>
                </#list>
                </select>
            </td>
            <td><input style="width:60px;" onfocus="document.getElementById('Addition${key.replace(" ","_")}Amount').value = '';" type="text" name='Addition${key.replace(" ","_")}AmountUSD' id='Addition${key.replace(" ","_")}AmountUSD' value="${addition.USD}"/></td>
            <td><button type="button" class="smooth" onclick="saveAddition('${addition.reason?if_exists}','Addition${key.replace(" ","_")}Currency','Addition${key.replace(" ","_")}Amount','Addition${key.replace(" ","_")}AmountUSD');return false;" ><img src="images/icons/pencil.png"/>Save</button></td>
            </tr>
            </#list>
            </#if>
            </tbody>
            </table>
            <#if user.hasPermission("ManagerEdit")>
            <a href="#" onclick="addRowAdditions();">Add New Contribution</a>
            </#if>
        </fieldset>
        
        <br/>
		
		<fieldset>
			<legend>Payment</legend>
			<div class="fm-opt">
				<label for="datePaidPDW">Date Paid</label>
				<input name="datePaidPDW" type="text" class="date-pick" value="<#if actuals.paidDate??>${actuals.paidDate?string('dd/MM/yyyy')}<#else>${datePaidPDW!}</#if>"/>
			</div>
			<div class="fm-opt">
				<label for="actuals.paidAmount.amountAsDouble">Amount</label>
				<input type="text" id="paidAmount" onchange="amountPaidChangedByUser=true;" name="actuals.paidAmount.amountAsDouble" style="width:70px;" value="<#if actuals.paidAmount??>${actuals.paidAmount.amountAsDouble}<#else>0</#if>"/>
			</div>
			
		</fieldset>		
		</div>
		<hr class="clear"/>
					<div>
		    <#if actuals.paidDate??>
                      <button type="button" onclick="if(confirm('Are you sure you wish to email this pay slip?')){document.forms.emailForm.submit();}" class="smooth" style="position:relative;float:right;margin-right:10px; margin-bottom: 4px;"><img src="images/icons/email.png"/>Email Pay Slip</button>
            <#else>
                      <button type="button" onclick="alert('Please enter Date & Amount paid information before emailing the Pay Slip.')" class="smooth"  style="cursor:help;position:relative;float:right;margin-right:10px; margin-bottom: 4px;"><img src="images/icons/email.png"/>Email Pay Slip</button>
            </#if>
			<#if user.hasPermission("ManagerEdit")>
		<button type="submit" class="smooth" style="position:relative;float:right;margin-right:10px; margin-bottom: 4px;"><img src="images/icons/pencil.png"/>Save</button>
		</#if>
			</div>
			<hr class="clear"/>
			
			
	    <input type="hidden" name="amount" value="0" />
	    <input type="hidden" name="currency" value="ZAR" />
	    <input type="hidden" name="amountUSD" value="0" />
        <input type="hidden" name="reason" value="" />
		</form>
		
		

		
		<form name="emailForm" id="emailForm" action="crew!emailPayPDF.action" method="GET">
		<input type="hidden" name="ids" value="${crewMember.id!}" />
		<input type="hidden" name="cmId" value="${id!}" />
		<input type="hidden" name="acId" value="${actualsId!}" />
		<#if currentMonth == 9>
			<input type="hidden" name="date" value="01/10/${currentYear?string('####')}" />
		<#elseif currentMonth?string?length == 1>
			<input type="hidden" name="date" value="01/0${(currentMonth+1)?string('#')}/${currentYear?string('####')}" />
		<#else>
		<input type="hidden" name="date" value="01/${(currentMonth+1)?string('##')}/${currentYear?string('####')}" />
		</#if>
		</form>
		
	
</body>
</html>
