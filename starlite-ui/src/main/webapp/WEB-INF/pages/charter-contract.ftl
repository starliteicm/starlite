<#include "/starlite.ftl">
<html>
<head>

<@enableJQuery/>
<@enableDatePickers/>

<link rel="stylesheet" type="text/css" href="styles/jmesa.css">
<script type="text/javascript" src="js/jmesa.js"></script>

<style>
table tr th{
    font-weight:bold;
    padding:5px;
}
table tr td{
    padding:5px;
}
</style>

<script type="text/javascript">
        $(document).ready(function() {
           addDropShadow('images/table/');
        });
      
      var selectedEmail = 0; 
        
      function updateSelected(obj){
      //alert("here "+obj.className);
        if (obj.checked == true){
          selectedEmail++;
        }
        else {
          if(selectedEmail > 0){
            selectedEmail--;
            }
        }
        var esn = document.getElementById("selectNo");
        //alert(esn+" "+ selectedEmail);
        esn.innerHTML = ""+selectedEmail;
      };
        
</script>
</head>
<body>
	<@subTabs/>
	<div style="clear:left; border: 1px solid silver; padding: 10px;height:450px;width:1050px;">
	
	        <div style="width:530px;float:right;border:1px solid silver;height:430px;padding:10px;">
            
            <div class="fm-opt">
            <label for="dateFrom">View Period:</label>
            <input name="dateFrom" type="text" class="date-pick" value="${dateFrom}"/>
            <input name="dateTo" type="text"  class="date-pick" value="${dateTo}"/>
            <button type="submit" class="smooth" style="float:right; margin-right:50px; margin-bottom: 4px;"><img src="images/icons/zoom_in.png"/>View</button>
            </div>
            <br/><br/>
            
            <table width="504px" style="border:1px solid silver;" >
            
            <tr>
            <th width="300px">Name</th>
            <th width="100px;">Start</th>
            <th width="100px;">End</th>
            </tr>
            </table>
            
            <div style="overflow:auto;width:520px;height:350px;border-bottom:1px solid silver;">
            <table width="504px" style="border-left:1px solid silver;border-right:1px solid silver;" >
                        
            
            <tr>
            <td width="300px;">XXXX</td>
            <td width="100px;">XXXX</td>
            <td width="100px;">XXXX</td>
            </tr>
            <tr>
            <td>XXXX</td>
            <td>XXXX</td>
            <td>XXXX</td>
            </tr>
            <tr>
            <td>XXXX</td>
            <td>XXXX</td>
            <td>XXXX</td>
            </tr>
            <tr>
            <td>XXXX</td>
            <td>XXXX</td>
            <td>XXXX</td>
            </tr>
            <tr>
            <td>XXXX</td>
            <td>XXXX</td>
            <td>XXXX</td>
            </tr>
            <tr>
            <td>XXXX</td>
            <td>XXXX</td>
            <td>XXXX</td>
            </tr>
            <tr>
            <td>XXXX</td>
            <td>XXXX</td>
            <td>XXXX</td>
            </tr>
            <tr>
            <td>XXXX</td>
            <td>XXXX</td>
            <td>XXXX</td>
            </tr>
            <tr>
            <td>XXXX</td>
            <td>XXXX</td>
            <td>XXXX</td>
            </tr>
            <tr>
            <td>XXXX</td>
            <td>XXXX</td>
            <td>XXXX</td>
            </tr>
            <tr>
            <td>XXXX</td>
            <td>XXXX</td>
            <td>XXXX</td>
            </tr>
            <tr>
            <td>XXXX</td>
            <td>XXXX</td>
            <td>XXXX</td>
            </tr>
            <tr>
            <td>XXXX</td>
            <td>XXXX</td>
            <td>XXXX</td>
            </tr>
            <tr>
            <td>XXXX</td>
            <td>XXXX</td>
            <td>XXXX</td>
            </tr>
            <tr>
            <td>XXXX</td>
            <td>XXXX</td>
            <td>XXXX</td>
            </tr>
            <tr>
            <td>XXXX</td>
            <td>XXXX</td>
            <td>XXXX</td>
            </tr>
            <tr>
            <td>XXXX</td>
            <td>XXXX</td>
            <td>XXXX</td>
            </tr>
            <tr>
            <td>XXXX</td>
            <td>XXXX</td>
            <td>XXXX</td>
            </tr>
            <tr>
            <td>XXXX</td>
            <td>XXXX</td>
            <td>XXXX</td>
            </tr>
            <tr>
            <td>XXXX</td>
            <td>XXXX</td>
            <td>XXXX</td>
            </tr>
            <tr>
            <td>XXXX</td>
            <td>XXXX</td>
            <td>XXXX</td>
            </tr>
            <tr>
            <td>XXXX</td>
            <td>XXXX</td>
            <td>XXXX</td>
            </tr>
            <tr>
            <td>XXXX</td>
            <td>XXXX</td>
            <td>XXXX</td>
            </tr>
            <tr>
            <td>XXXX</td>
            <td>XXXX</td>
            <td>XXXX</td>
            </tr>
            <tr>
            <td>XXXX</td>
            <td>XXXX</td>
            <td>XXXX</td>
            </tr>
            <tr>
            <td>XXXX</td>
            <td>XXXX</td>
            <td>XXXX</td>
            </tr>
            <tr>
            <td>XXXX</td>
            <td>XXXX</td>
            <td>XXXX</td>
            </tr>
            <tr>
            <td>XXXX</td>
            <td>XXXX</td>
            <td>XXXX</td>
            </tr>
            </table>
            </div>
            </div>
	
	
	    <div style="padding:20px;padding-bottom:0px;width:450px;">
          <form action="crewMember!saveRange.action" method="GET">
            <input type="hidden" name="id" value="${id}">
            <fieldset>
            <legend>Set Crew on Contract</legend>
        
            <div class="fm-opt">
            <label for="dateFrom">Date Range:</label>
            <input name="dateFrom" type="text" class="date-pick" value=""/>
            <input name="dateTo" type="text" class="date-pick" value=""/>
            </div>
            
            <div class="fm-opt">
            <label for="activity">Activity:</label>
            <select name="activity">
                <option SELECTED value="W">Work [W]</option>
                <option value="T">Travel [T]</option>
                <option value="D">Training [D]</option>
                <option value="L">Leave [L]</option>
                <option value="R">Rest [R]</option>
                <option value="U">Unavailable [U]</option>
            </select>
            </div>
            
            <div class="fm-opt">
            <label for="tail">Aircraft:</label>
            <select name="tail" id="tail">
              <option></option>
              <#list allAircraft! as a>
                  <option SELECTED value="${a.id}">${a.ref}</option>
              </#list>
            </select>
            </div>
            
            <br/>
            
            <div class="heading" style="margin-left:20px;"><span style="float:left;">Crew: - [<span id="selectNo">0</span> selected] </span><img class="tooltip" title=" Crew On Contract: - Please check the boxes to the left of the member / members names you wish to include on this charter."  style="cursor:help;position:static;float:right;" src="images/icons/info.png"/>
            </div>
        
            <div style="height:100px; width:400px; overflow:auto; padding:5px; margin-bottom:10px;margin-left:20px; border: 1px solid silver;">
            <ul class="mktree" style="">
            <#list allCrew as member>
            <#if member.code?exists>
            <li><input type="checkbox"  onclick="updateSelected(this)" style="border:none;width:50px;" name="members" value="${member.code?if_exists}" /> ${member.code?if_exists} - (${member.personal.firstName?if_exists} ${member.personal.lastName?if_exists})</li>
            </#if>
            </#list>
            </ul>
            </div>
            
            
            <button type="submit" class="smooth" style="float:right; margin-right:10px; margin-bottom: 4px;"><img src="images/icons/pencil.png"/>Set</button>
            
            
            </fieldset>
            
            
            
            
          </form>
        </div>
        

        
	
	
	</div>
</body>
</html>