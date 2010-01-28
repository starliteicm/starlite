<#include "/starlite.ftl">
<#setting number_format = "######.##########"/>

<html>
<head>
	<@enableJQuery/>
	<@enableJwysiwyg/>
	<script type="text/javascript" src="${request.contextPath}/js/tooltip.js"></script>
	<script type="text/javascript" src="${request.contextPath}/js/jquery.dimensions.js"></script>
	<style type="text/css">
    #tooltip.pretty {
    position:absolute;
    margin-top:0px;
	font-family: Arial;
	border: none;
	width: 210px;
	padding:20px;
	height: 135px;
	opacity: 0.9;
	background: url('images/shadow.png');
}
#tooltip.pretty h3 {
	margin-bottom: 0.75em;
	font-size: 9pt;
	width: 210px;
	text-align: center;
}
#tooltip.pretty div { width: 210px; text-align: left; }
.heading {
  width:476px;
  height:20px;
  padding-bottom:5px;
  padding-top:10px;
}

fieldset div input {
    width:50px;
}

    </style>
</head>
<body class="exchange">
	
	<div style="padding-left:10px;padding-bottom:100px;width:1100px;">
		<fieldset>
		<legend>User Administration</legend>
		
		  <div style="border:1px solid silver;padding:20px;float:left;width:300px;margin:10px;height:800px;overflow:auto">
		  
		  <#list users as user>
		  <a style="width:100px;" href="#">${user.user.username!}</a>
		  <#if user.crew?exists>[${user.crew.personal.fullName!}]</#if>
		  <br/>
		  </#list>
		  
          </div>
		
		
		  <div style="border:1px solid silver;padding:20px;float:left;width:300px;margin:10px;height:800px;">
		
		   <div class="fm-opt">
            <label for="">&nbsp;</label>               
            <span><div style="width:60px;float:left;"><B>Read</B></div></span> 
            <span><div style="width:60px;float:left;"><B>Write</B></div></span> 
          </div><br/>
		
		  <div class="fm-opt">
            <label for=""><B>Schedule</B>(Not to be activated yet for new accounts)</label>               
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <div style="width:100%;height:30px;">&nbsp;</div>
          </div><br/><br/>


          <div>
          
            <div class="fm-opt">
            <label for=""><B>Aircraft</B> (Not to be activated yet for new accounts)</label>               
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <div style="width:100%;height:30px;">&nbsp;</div>
            </div>
            
            <div class="fm-opt">
            <label for="">Information</label>
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            </div>
            
            <div class="fm-opt"> 
            <label for="">Hours</label>
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            </div>
            
            <div class="fm-opt"> 
            <label for="">Documents</label> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            </div>
            
          </div><br/><br/>
          
          <div>
            
            <div class="fm-opt"> 
            <label for=""><B>Contract</B> (Not to be activated yet for new accounts)</label>               
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <div style="width:100%;height:30px;">&nbsp;</div>
            </div>
            
            <div class="fm-opt"> 
            <label for="">Administrative</label>
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            </div>
            
            <div class="fm-opt">  
            <label for="">Resources</label>
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            </div>
            
            <div class="fm-opt">  
            <label for="">Pricing</label>
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            </div>
            
            <div class="fm-opt">  
            <label for="">Insurance</label>
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            </div>
            
            <div class="fm-opt"> 
            <label for="">Cost</label>
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            </div>
            
            <div class="fm-opt">  
            <label for="">Hours</label>
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            </div>
            
            <div class="fm-opt">   
            <label for="">Documents</label>
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            </div>
            
            <div class="fm-opt">  
            <label for="">Assignments</label>
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            </div>
             
           </div><br/><br/>
          

            <div>
          
            <div class="fm-opt">
            <label for=""><B>Crew</B></label>               
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <div style="width:100%;height:20px;">&nbsp;</div>
            </div>
            
            <div class="fm-opt">
            <label for="">Personal</label>
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            </div>
            
            <div class="fm-opt"> 
            <label for="">Banking</label>
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            </div>
            
            <div class="fm-opt"> 
            <label for="">Role</label> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            </div>
            
            
            <div class="fm-opt"> 
            <label for="">Payments</label> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            </div>
            
            <div class="fm-opt"> 
            <label for="">PDW</label> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            </div>
            
            <div class="fm-opt"> 
            <label for="">On Contract</label> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            </div>
            
            <div class="fm-opt"> 
            <label for="">Additional Documents</label> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            </div>
            
            <div class="fm-opt"> 
            <label for="">Review</label> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            </div>
            
            <div class="fm-opt"> 
            <label for="">Assignments</label> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
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
            <label for=""><B>Documents</B> (Not to be activated yet for new accounts)</label>               
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <div style="width:100%;height:30px;">&nbsp;</div>
            </div>
          </div><br/><br/>
            
            
            <div>
            <div class="fm-opt">
            <label for=""><B>Reports</B> </label>               
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <div style="width:100%;height:20px;">&nbsp;</div>
            </div>
            
            <div class="fm-opt">
            <label for="">Aircraft/Charter Matrix</label>
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            </div>
            
            <div class="fm-opt"> 
            <label for="">Certificates</label>
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            </div>
            
            <div class="fm-opt"> 
            <label for="">Crew Member Profiles</label> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            </div>
            
            
            <div class="fm-opt"> 
            <label for="">Crew Licences</label> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            </div>
            
            <div class="fm-opt"> 
            <label for="">Crew Deductions</label> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            </div>
            
            <div class="fm-opt"> 
            <label for="">Crew Payments</label> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            </div>
            
            <div class="fm-opt"> 
            <label for="">Crew Payment Analysis</label> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            </div>
            
            <div class="fm-opt"> 
            <label for="">Crew Document Report</label> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            </div>
            
            <div class="fm-opt"> 
            <label for="">Crew Profile</label> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            </div>
            
            <div class="fm-opt"> 
            <label for="">Required Information</label> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            </div>
            
            <div class="fm-opt"> 
            <label for="">Crew Days On Contract</label> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            </div>
            
            <div class="fm-opt"> 
            <label for="">Crew Days Worked</label> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            </div>
            
            <div class="fm-opt"> 
            <label for="">Hours Flown</label> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            </div>
            
            <div class="fm-opt"> 
            <label for="">183 Day Report</label> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            </div>
            
            <div class="fm-opt"> 
            <label for="">Hours</label> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <input name="" type="checkbox" value="ScheludeRead" /> 
            </div>
            
          </div><br/><br/><br/>

          <div class="fm-opt">
            <label for=""><B>Mailout</B> (Not to be activated yet for new accounts)</label>               
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <div style="width:100%;height:30px;">&nbsp;</div>
          </div><br/><br/>
          
          <div class="fm-opt">
            <label for=""><B>Exchange</B> (Not to be activated yet for new accounts)</label>               
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <div style="width:100%;height:30px;">&nbsp;</div>
          </div><br/><br/>
          
          <div class="fm-opt">
            <label for=""><B>User Admin</B> (Not to be activated yet for new accounts)</label>               
            <input name="" type="checkbox" value="ScheludeRead" /> 
            <div style="width:100%;height:30px;">&nbsp;</div>
          </div><br/><br/>
		
		
		  </div>
		
		<br/><br/>
		
		</fieldset>
	</div> 
		
</body>