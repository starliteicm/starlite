<#include "/starlite.ftl">
<#setting number_format = "######.##########"/>

<html>
<head>
	<@enableHelp/>
	<style type="text/css">
    fieldset div input {
      width:50px;
    }
    .linkout{background-color:#f}
    .linkover{background-color:#CFC}
    .linkselect{background-color:#FFB944}
    </style>
    
    <script>
    
    function addUser(){
    
    //get Username from textbox
    var username = $("#username_input").val();
    
    //Styling
    var leg = document.getElementById("legend");
    leg.innerHTML = "User Administration - "+username+" [New Admin]";
    
    //enable fields
    $(".adminSelect").removeAttr('disabled');
    $(".crewSelect").removeAttr('disabled');
    
    //fields for saving
    $("#username").val(username);
    $("#newUser").val("1");
    
    alert($("#username").val());
    
    
    }
    
    function selectUser(link,username,type,permissions){
      //alert(this+" : "+username);
      
      //Styling
      var leg = document.getElementById("legend");
      leg.innerHTML = "User Administration - "+username+" ["+type+"]";
      $(".linkover").addClass("linkout");
      $(".linkover").removeClass("linkover");
      $(".linkselect").removeClass("linkselect");
      $(link).addClass("linkselect");
      
      //Clear Checks
      $(".adminSelect").removeAttr('checked');
      $(".crewSelect").removeAttr('checked');
      
      //Enable disable fields
      if(type=="Crew"){
        $(".adminSelect").attr('disabled', true);
        $(".crewSelect").removeAttr('disabled');
      }
      else{
        $(".adminSelect").removeAttr('disabled');
        $(".crewSelect").removeAttr('disabled');
      }
      
      //Check Boxes from permissions
      
      //fields for saving
      $("#username").val(username);
      $("#newUser").val("0");
      
      var perms = permissions.split("|");
      //alert(perms);
      for(var i=0; i < perms.length; i++ ){
        var p = perms[i];
        if(p != ""){
          $("#"+p).attr('checked',true);
        }
      }
      
      
      return false;
    }
    
    
    function validate(){
    if(document.forms.permissionForm.username.value != ""){
      document.forms.permissionForm.submit();
    }
    }
    </script>
    
</head>
<body class="exchange">
	
	
	
	<form autocomplete="off" name="permissionForm" action="user!update.action" method="POST" >
	
	<input type="hidden" id="newUser"  name="newUser"  value="0" />
	<input type="hidden" id="username" name="username" value=""  />
	
	<div style="padding-left:10px;padding-bottom:100px;width:1100px;">
		<fieldset>
		<legend id="legend">User Administration</legend>
		
		  <div style="float:left;height:800px;">
		  <div style="border:1px solid silver;padding:20px;width:300px;margin:10px;height:735px;overflow:auto">
		  <#list users as user>
		  <div class="linkout" id="link${user.user.username!}" onmouseover="if(this.className=='linkout'){this.className='linkover'}" onmouseout="if(this.className=='linkover'){this.className='linkout'}">
		  
		  <#if user.crew?exists>
		  <a onclick="selectUser('#link${user.user.username!}','${user.user.username!}','Crew','${user.user.permissionStr()}');resetTimer();"  style="width:100px;" href="#">${user.user.username!}</a>
		  [${user.crew.personal.fullName!}]
		  <#else>
		  <a onclick="selectUser('#link${user.user.username!}','${user.user.username!}','Admin','${user.user.permissionStr()}');resetTimer();"  style="width:100px;" href="#">${user.user.username!}</a>
		  </#if>
		  </div>
		  </#list>
          </div>
          
          <div style="border:1px solid silver;padding:10px;padding-left:20px;padding-right:20px;width:300px;margin:10px;height:30px;">
		   <input name="username_input" id="username_input" style="width:130px;height:25px;">
		   <img class="tooltip" style="float:right" title=" - Add Admin User: - Admin Users are not Crew members they are granted extra permissions and can view details other than their own. To Create a Crew member please do so using the add button on the crew list page" style="cursor:help;position:relative;float:right;" src="images/icons/info.png"/>
		   <button type="button" onclick="addUser();" class="smooth" style="float:right; margin-right:10px; margin-bottom: 4px;"><img src="images/icons/pencil.png"/> Add Admin</button>
		  </div>
		  </div>
		
		  <div style="border:1px solid silver;padding:20px;float:left;width:300px;margin:10px;height:800px;">
		
		   <div class="fm-opt">
            <label for="">&nbsp;</label>               
            <span><div style="width:60px;float:left;"><B>Read</B></div></span> 
            <span><div style="width:60px;float:left;"><B>Write</B></div></span> 
          </div><br/>
		
		  <div class="fm-opt">
            <label for=""><B>Schedule</B>(NA)</label>               
            <input disabled="disabled" class="adminSelect" name="permissions" id="scheduleRead" type="checkbox" value="scheduleRead" /> 
            <div style="width:100%;height:20px;">&nbsp;</div>
          </div><br/>


          <div>
          
            <div class="fm-opt">
            <label for=""><B>Aircraft</B>(NA)</label>               
            <input disabled="disabled" class="adminSelect" name="permissions" id="aircraftRead" type="checkbox" value="aircraftRead" /> 
            <div style="width:100%;height:20px;">&nbsp;</div>
            </div>
            
            <div class="fm-opt">
            <label for="">Information</label>
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="aircraftInfoRead"  onclick="if($('#aircraftInfoWrite').is(':checked')){$('#aircraftInfoWrite').attr('checked',$('#aircraftInfoRead').is(':checked'));}" value="aircraftInfoRead" /> 
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="aircraftInfoWrite" onclick="if($('#aircraftInfoWrite').is(':checked')){$('#aircraftInfoRead').attr('checked','true');}" value="aircraftInfoWrite" /> 
            </div>
            
            <div class="fm-opt"> 
            <label for="">Hours</label>
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="aircraftHoursRead"  onclick="if($('#aircraftHoursWrite').is(':checked')){$('#aircraftHoursWrite').attr('checked',$('#aircraftHoursRead').is(':checked'));}" value="aircraftHoursRead" /> 
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="aircraftHoursWrite" onclick="if($('#aircraftHoursWrite').is(':checked')){$('#aircraftHoursRead').attr('checked','true');}" value="aircraftHoursWrite" /> 
            </div>
            
            <div class="fm-opt"> 
            <label for="">Documents</label> 
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="aircraftDocRead"  onclick="if($('#aircraftDocWrite').is(':checked')){$('#aircraftDocWrite').attr('checked',$('#aircraftDocRead').is(':checked'));}" value="aircraftDocRead" /> 
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="aircraftDocWrite" onclick="if($('#aircraftDocWrite').is(':checked')){$('#aircraftDocRead').attr('checked','true');}" value="aircraftDocWrite" /> 
            </div>
            
          </div><br/><br/>
          
          <div>
            
            <div class="fm-opt"> 
            <label for=""><B>Contract</B>(NA)</label>               
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="contractRead" value="contractRead" /> 
            <div style="width:100%;height:20px;">&nbsp;</div>
            </div>
            
            <div class="fm-opt"> 
            <label for="">Administrative</label>
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="contractAdminRead"  onclick="if($('#contractAdminWrite').is(':checked')){$('#contractAdminWrite').attr('checked',$('#contractAdminRead').is(':checked'));}" value="contractAdminRead" /> 
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="contractAdminWrite" onclick="if($('#contractAdminWrite').is(':checked')){$('#contractAdminRead').attr('checked','true');}" value="contractAdminWrite" /> 
            </div>
            
            <div class="fm-opt">  
            <label for="">Resources</label>
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="contractResourcesRead"  onclick="if($('#contractResourcesWrite').is(':checked')){$('#contractResourcesWrite').attr('checked',$('#contractResourcesRead').is(':checked'));}" value="contractResourcesRead" /> 
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="contractResourcesWrite" onclick="if($('#contractResourcesWrite').is(':checked')){$('#contractResourcesRead').attr('checked','true');}" value="contractResourcesWrite" /> 
            </div>
            
            <div class="fm-opt">  
            <label for="">Pricing</label>
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="contractPriceRead"  onclick="if($('#contractPriceWrite').is(':checked')){$('#contractPriceWrite').attr('checked',$('#contractPriceRead').is(':checked'));}" value="contractPriceRead" /> 
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="contractPriceWrite" onclick="if($('#contractPriceWrite').is(':checked')){$('#contractPriceRead').attr('checked','true');}" value="contractPriceWrite" /> 
            </div>
            
            <div class="fm-opt">  
            <label for="">Insurance</label>
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="contractInsRead"  onclick="if($('#contractInsWrite').is(':checked')){$('#contractInsWrite').attr('checked',$('#contractInsRead').is(':checked'));}" value="contractInsRead" /> 
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="contractInsWrite" onclick="if($('#contractInsWrite').is(':checked')){$('#contractInsRead').attr('checked','true');}" value="contractInsWrite" /> 
            </div>
            
            <div class="fm-opt"> 
            <label for="">Cost</label>
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="contractCostRead"  onclick="if($('#contractCostWrite').is(':checked')){$('#contractCostWrite').attr('checked',$('#contractCostRead').is(':checked'));}" value="contractCostRead" /> 
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="contractCostWrite" onclick="if($('#contractCostWrite').is(':checked')){$('#contractCostRead').attr('checked','true');}" value="contractCostWrite" /> 
            </div>
            
            <div class="fm-opt">  
            <label for="">Hours</label>
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="contractHoursRead"  onclick="if($('#contractHoursWrite').is(':checked')){$('#contractHoursWrite').attr('checked',$('#contractHoursRead').is(':checked'));}" value="contractHoursRead" /> 
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="contractHoursWrite" onclick="if($('#contractHoursWrite').is(':checked')){$('#contractHoursRead').attr('checked','true');}" value="contractHoursWrite" /> 
            </div>
            
            <div class="fm-opt">  
            <label for="">On Contract</label>
            <div style="width:55px;height:10px;float:left;">&nbsp;</div> 
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="contractOnContractWrite" value="contractOnContractWrite" /> 
            </div>
            
            <div class="fm-opt">   
            <label for="">Documents</label>
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="contractDocRead"  onclick="if($('#contractDocWrite').is(':checked')){$('#contractDocWrite').attr('checked',$('#contractDocRead').is(':checked'));}" value="contractDocRead" /> 
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="contractDocWrite" onclick="if($('#contractDocWrite').is(':checked')){$('#contractDocRead').attr('checked','true');}" value="contractDocWrite" /> 
            </div>
            
            <div class="fm-opt">  
            <label for="">Assignments</label>
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="contractAssignRead"  onclick="if($('#contractAssignWrite').is(':checked')){$('#contractAssignWrite').attr('checked',$('#contractAssignRead').is(':checked'));}" value="contractAssignRead" /> 
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="contractAssignWrite" onclick="if($('#contractAssignWrite').is(':checked')){$('#contractAssignRead').attr('checked','true');}" value="contractAssignWrite" /> 
            </div>
             
           </div><br/><br/>
          

            <div>
          
            <div class="fm-opt">
            <label for=""><B>Crew</B></label>               
            <input disabled="disabled" class="crewSelect" name="permissions" type="checkbox" id="crewRead"  value="crewRead" /> 
            <div style="width:100%;height:20px;">&nbsp;</div>
            </div>
            
            <div class="fm-opt">
            <label for="">Personal</label>
            <input disabled="disabled" class="crewSelect" name="permissions" type="checkbox" id="crewPersonalRead"  onclick="if($('#crewPersonalWrite').is(':checked')){$('#crewPersonalWrite').attr('checked',$('#crewPersonalRead').is(':checked'));}" value="crewPersonalRead" /> 
            <input disabled="disabled" class="crewSelect" name="permissions" type="checkbox" id="crewPersonalWrite" onclick="if($('#crewPersonalWrite').is(':checked')){$('#crewPersonalRead').attr('checked','true');}" value="crewPersonalWrite" /> 
            </div>
            
            <div class="fm-opt"> 
            <label for="">Banking</label>
            <input disabled="disabled" class="crewSelect" name="permissions" type="checkbox" id="crewBankRead"  onclick="if($('#crewBankWrite').is(':checked')){$('#crewBankWrite').attr('checked',$('#crewBankRead').is(':checked'));}" value="crewBankRead" /> 
            <input disabled="disabled" class="crewSelect" name="permissions" type="checkbox" id="crewBankWrite" onclick="if($('#crewBankWrite').is(':checked')){$('#crewBankRead').attr('checked','true');}" value="crewBankWrite" /> 
            </div>
            
            <div class="fm-opt"> 
            <label for="">Role</label> 
            <input disabled="disabled" class="crewSelect" name="permissions" type="checkbox" id="crewRoleRead"  onclick="if($('#crewRoleWrite').is(':checked')){$('#crewRoleWrite').attr('checked',$('#crewRoleRead').is(':checked'));}" value="crewRoleRead" /> 
            <input disabled="disabled" class="crewSelect" name="permissions" type="checkbox" id="crewRoleWrite" onclick="if($('#crewRoleWrite').is(':checked')){$('#crewRoleRead').attr('checked','true');}" value="crewRoleWrite" /> 
            </div>
            
            
            <div class="fm-opt"> 
            <label for="">Payments</label> 
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="crewPayRead"  onclick="if($('#crewPayWrite').is(':checked')){$('#crewPayWrite').attr('checked',$('#crewPayRead').is(':checked'));}" value="crewPayRead" /> 
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="crewPayWrite" onclick="if($('#crewPayWrite').is(':checked')){$('#crewPayRead').attr('checked','true');}" value="crewPayWrite" /> 
            </div>
            
            <div class="fm-opt"> 
            <label for="">PDW</label> 
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="crewPDWRead"  onclick="if($('#crewPDWWrite').is(':checked')){$('#crewPDWWrite').attr('checked',$('#crewPDWRead').is(':checked'));}" value="crewPDWRead" /> 
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="crewPDWWrite" onclick="if($('#crewPDWWrite').is(':checked')){$('#crewPDWRead').attr('checked','true');}" value="crewPDWWrite" /> 
            </div>
            
            <div class="fm-opt"> 
            <label for="">On Contract</label> 
            <div style="width:55px;height:10px;float:left;">&nbsp;</div> 
            <input disabled="disabled" class="crewSelect" name="permissions" type="checkbox" id="crewContWrite" value="crewContWrite" /> 
            </div>
            
            <div class="fm-opt"> 
            <label for="">Additional Documents</label> 
            <input disabled="disabled" class="crewSelect" name="permissions" type="checkbox" id="crewDocRead"  onclick="if($('#crewDocWrite').is(':checked')){$('#crewDocWrite').attr('checked',$('#crewDocRead').is(':checked'));}" value="crewDocRead" /> 
            <input disabled="disabled" class="crewSelect" name="permissions" type="checkbox" id="crewDocWrite" onclick="if($('#crewDocWrite').is(':checked')){$('#crewDocRead').attr('checked','true');}" value="crewDocWrite" /> 
            </div>
            
            <div class="fm-opt"> 
            <label for="">Review</label> 
            <input disabled="disabled" class="crewSelect" name="permissions" type="checkbox" id="crewRevRead"  onclick="if($('#crewRevWrite').is(':checked')){$('#crewRevWrite').attr('checked',$('#crewRevRead').is(':checked'));}" value="crewRevRead" /> 
            <input disabled="disabled" class="crewSelect" name="permissions" type="checkbox" id="crewRevWrite" onclick="if($('#crewRevWrite').is(':checked')){$('#crewRevRead').attr('checked','true');}" value="crewRevWrite" /> 
            </div>
            
            <div class="fm-opt"> 
            <label for="">Assignments</label> 
            <input disabled="disabled" class="crewSelect" name="permissions" type="checkbox" id="crewAssignRead"  onclick="if($('#crewAssignWrite').is(':checked')){$('#crewAssignWrite').attr('checked',$('#crewAssignRead').is(':checked'));}" value="crewAssignRead" /> 
            <input disabled="disabled" class="crewSelect" name="permissions" type="checkbox" id="crewAssignWrite" onclick="if($('#crewAssignWrite').is(':checked')){$('#crewAssignRead').attr('checked','true');}" value="crewAssignWrite" /> 
            </div>
            
          </div><br/><br/>
          
          </div>
          
          <div style="border:1px solid silver;padding:20px;float:left;width:300px;margin:10px;height:800px;">
          
          <div class="fm-opt">
            <label for="">&nbsp;</label>               
            <span><div style="width:60px;float:left;"><B>Read</B></div></span> 
            <span><div style="width:60px;float:left;"><B>Write</B></div></span> 
          </div><br/>

          <div>
            <div class="fm-opt">
            <label for=""><B>Documents</B>(NA)</label>               
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="docRead"  onclick="if($('#docWrite').is(':checked')){$('#docWrite').attr('checked',$('#docRead').is(':checked'));}" value="docRead" /> 
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="docWrite" onclick="if($('#docWrite').is(':checked')){$('#docRead').attr('checked','true');}"   value="docWrite" /> 
            </div>
          </div><br/><br/>
            
            
            <div>
            <div class="fm-opt">
            <label for=""><B>Reports</B> </label>               
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="reportsRead" value="reportsRead" /> 
            <div style="width:100%;height:20px;">&nbsp;</div>
            </div>
            
            <div class="fm-opt">
            <label for="">Aircraft/Charter Matrix</label>
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="reportMatrixRead" value="reportMatrixRead" /> 
            <div style="width:100%;height:1px;">&nbsp;</div>
            </div>
            
            <!--<div class="fm-opt"> 
            <label for="">Certificates</label>
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="reportCertRead" value="reportCertRead" /> 
            <div style="width:100%;height:1px;">&nbsp;</div>
            </div>-->
            
            <!--<div class="fm-opt"> 
            <label for="">Crew Member Profiles</label> 
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="reportMemberProfileRead" value="reportMemberProfileRead" /> 
            <div style="width:100%;height:1px;">&nbsp;</div>
            </div>-->
            
            <!--<div class="fm-opt"> 
            <label for="">Crew Licences</label> 
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="reportLicenceRead" value="reportLicenceRead" />
            <div style="width:100%;height:1px;">&nbsp;</div>
            </div>-->
            
            <div class="fm-opt"> 
            <label for="">Crew Deductions</label> 
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="reportDeductionRead" value="reportDeductionRead" />
            <div style="width:100%;height:1px;">&nbsp;</div>
            </div>
            
            <div class="fm-opt"> 
            <label for="">Crew Payments</label> 
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="reportPaymentsRead" value="reportPaymentsRead" />
            <div style="width:100%;height:1px;">&nbsp;</div>
            </div>
            
            <div class="fm-opt"> 
            <label for="">Crew Payment Analysis</label> 
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="reportPaymentsAnalysisRead" value="reportPaymentsAnalysisRead" /> 
            <div style="width:100%;height:1px;">&nbsp;</div>
            </div>
            
            <div class="fm-opt"> 
            <label for="">Crew Document Report</label> 
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="reportDocRead" value="reportDocRead" /> 
            <div style="width:100%;height:1px;">&nbsp;</div>
            </div>
            
            <div class="fm-opt"> 
            <label for="">Crew Profile</label> 
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="reportProfileRead" value="reportProfileRead" /> 
            <div style="width:100%;height:1px;">&nbsp;</div>
            </div>
            
            <div class="fm-opt"> 
            <label for="">Required Information</label> 
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="reportRequiredRead" value="reportRequiredRead" /> 
            <div style="width:100%;height:1px;">&nbsp;</div>
            </div>
            
            <div class="fm-opt"> 
            <label for="">Crew Days On Contract</label> 
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="reportOnContractRead" value="reportOnContractRead" /> 
            <div style="width:100%;height:1px;">&nbsp;</div>
            </div>
            
            <!--<div class="fm-opt"> 
            <label for="">Crew Days Worked</label> 
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="reportWorkedRead" value="reportWorkedRead" /> 
            <div style="width:100%;height:1px;">&nbsp;</div>
            </div>-->
            
            <div class="fm-opt"> 
            <label for="">Hours Flown</label> 
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="reportHoursFlownRead" value="reportHoursFlownRead" /> 
            <div style="width:100%;height:1px;">&nbsp;</div>
            </div>
            
            <div class="fm-opt"> 
            <label for="">183 Day Report</label> 
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="report183Read" value="report183Read" /> 
            <div style="width:100%;height:1px;">&nbsp;</div>
            </div>
            
            <div class="fm-opt"> 
            <label for="">Hours</label> 
            <input disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="reportHoursRead" value="reportHoursRead" /> 
            <div style="width:100%;height:1px;">&nbsp;</div>
            </div>
            
          </div><br/>

          <div class="fm-opt">
            <label for=""><B>Mailout</B></label>      
             <div style="width:55px;height:10px;float:left;">&nbsp;</div>         
            <input style="float:left" disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="mailoutWrite" value="mailoutWrite" /> 
          </div><br/><br/><br/>
          
          <div class="fm-opt">
            <label for=""><B>Exchange</B></label>  
             <div style="width:55px;height:10px;float:left;">&nbsp;</div>             
            <input style="float:left" disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="exchangeWrite" value="exchangeWrite" /> 
          </div><br/><br/><br/>
          
          <div class="fm-opt">
            <label for=""><B>User Admin</B></label>     
             <div style="width:55px;height:10px;float:left;">&nbsp;</div>          
            <input style="float:left" disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="userWrite" value="userWrite" /> 
          </div><br/><br/><br/>
		   
		   <div class="fm-opt">
            <label for=""><B>Stores</B></label>     
             <div style="width:55px;height:10px;float:left;">&nbsp;</div>          
            <input style="float:left" disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="storesRead" value="storesRead" /> 
          </div><br/><br/><br/>
          
          <div class="fm-opt">
            <label for=""><B>Conponents</B></label>     
             <div style="width:55px;height:10px;float:left;">&nbsp;</div>          
            <input style="float:left" disabled="disabled" class="adminSelect" name="permissions" type="checkbox" id="componentsRead" value="componentsRead" /> 
          </div><br/><br/>
		   
           
		   <div style="height:1px;">&nbsp;</div>
		
		 <button type="button" onclick="validate();" class="smooth" style="float:right; margin-right:10px; margin-bottom: 4px;"><img src="images/icons/pencil.png"/> Save Permissions</button>
		
		
		  </div>
		
		<br/><br/>
		
		</fieldset>
	</div> 
	</form>	
</body>