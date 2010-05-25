<#include "/starlite.ftl">
<#setting number_format = "######.##########"/>

<html>
<head>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/styles/jmesa.css">
<script type="text/javascript" src="${request.contextPath}/js/jmesa.js"></script>
<script type="text/javascript">

   function checkNum(obj){
      if(isNaN(obj.value)){
        obj.value="";
        return false;
      }
      else if(obj.value*1 < 0 ){
        obj.value="";
        return false;
      }
      return true;
    }

        $(document).ready(function() {
           addDropShadow('images/table/');
        });
        
        function onSubmitWsColumn() {
            document.getElementById("saveButton").disabled = false;
        }

    function changeAddLocation(selIndex){
      if(selIndex < 1){
        $("#locDiv").css("background-color","#FFFFFF");
        $("#conLocDiv").css("background-color","#EEEEEE");
        $("#binDiv").css("background-color","#FFFFFF");
        $("#locationInput").css("background-color","#FFFFFF");
        $("#conLocationInput").css("background-color","#EEEEEE");
        $("#binInput").css("background-color","#FFFFFF");
      }
      else{
        $("#locDiv").css("background-color","#EEEEEE");
        $("#binDiv").css("background-color","#EEEEEE"); 
        $("#conLocDiv").css("background-color","#FFFFFF");
        $("#locationInput").css("background-color","#EEEEEE");
        $("#conLocationInput").css("background-color","#FFFFFF");
        $("#binInput").css("background-color","#EEEEEE");
        
      }
      return true;
    }

    function showTab(tab){
      $('.linkTab').removeClass("current");
      $('#'+tab+"Link").addClass("current");
      $('.tabContent').css("display","none");
      $('#'+tab).css("display","");
    }
    
    function cancelEditLocation(obj){
      $("#editMessage").html("");
      $("#editMessage").css("display","none");
      $("#locationId").val("");
      $("#locCurrent").val("1");
      $("#locationInput").val("");
      $("#binInput").val("");
      $("#qtyInput").val("1");
      $("#cancelEditLoc").css("display","none");
      $("#deleteEditLoc").css("display","none");
      return true;
    }
    
    function deleteEditLocation(obj){
      if(confirm("Are you Sure you wish to Remove this Entry?")){
        $("#locCurrent").val("0");
        document.forms.locationForm.submit();
      }
      return true;
    }
    
    function editLoc(locid,location,bin,qty){
    
      $("#editMessage").html("Editing "+location);
      $("#editMessage").css("display","");
      $("#locationId").val(locid);
      $("#locCurrent").val("1");
      $("#locationInput").val(location);
      updateLocationMessage(location);
      $("#binInput").val(bin);
      updateBinMessage(bin);
      $("#qtyInput").val(qty);
      $("#cancelEditLoc").css("display","");
      $("#deleteEditLoc").css("display","");
      return true;
    }
    
    function cancelEditVal(){
      $("#editValMessage").html("");
      $("#editValMessage").css("display","none");
      $("#cancelEditValue").css("display","none");
      $("#valuationId").val("");
      $("#valDate").val("");
      $("#valTime").val("");
      $("#marketVal").val("");
      $("#purchaseVal").val("");
      $("#replacementVal").val("");
      
      $("#marketCurrency option[text=USD]").attr("selected","selected") ;
      $("#purchaseCurrency option[text=USD]").attr("selected","selected") ;
      $("#replacementCurrency option[text=USD]").attr("selected","selected") ;
      
      return true;
    }
    
    function editVal(valid,date,time,mval,mcur,pval,pcur,rval,rcur){
      $("#editValMessage").html("Editing Valuation ("+date+" "+time+")");
      $("#editValMessage").css("display","");
      $("#cancelEditValue").css("display","");
      $("#valDate").val(date);
      $("#valTime").val(time);
      $("#valuationId").val(valid);
      $("#marketVal").val(mval);
      $("#purchaseVal").val(pval);
      $("#replacementVal").val(rval);
      
      $("#marketCurrency option[text=" + mcur +"]").attr("selected","selected") ;
      $("#purchaseCurrency option[text=" + pcur +"]").attr("selected","selected") ;
      $("#replacementCurrency option[text=" + rcur +"]").attr("selected","selected") ;
      
      return true;
    }
    
    function updateLocationMessage(location){
      var message = "Not Valid";
      location = location.toUpperCase();
      var valid = 0;
      if(location.length == 5){
        
        
        
        <#list stores as store>
        if(location == "${store.location!}"){
         valid = 2;
         message = "Existing: ${store.description!}";
        }
        </#list>
        
        if(valid == 0){
          valid = 1;
          message = "Valid New Store";
          if(location.substring(0,2) == "ZS"){message = "Valid New Aircraft";}
          if(location.substring(0,2) == "MS"){message = "Valid New Mobile Store";}
          if(location.substring(0,2) == "WS"){message = "Valid New Workshop Cycle";}
          if(location.substring(0,2) == "DR"){message = "Valid New Depot Cycle";}
        }
      }
      
      $("#location").val(location);
      $("#locationMessage").val(message);
      
      if(valid == 2){
        //GREEN
        $("#locationMessage").css("background-color","#66FF66")
      }
      else if(valid == 1){
        //YELLOW
        $("#locationMessage").css("background-color","#FFFF66")
      }
      else{
        //RED
        $("#locationMessage").css("background-color","#FF6666")
      }
       
      return true;  
    }
    
    function updateBinMessage(bin){
      var message = "Not Valid";
      var valid = 0;
      
      if(bin.length <= 10){
      if(bin.length % 2 == 0){
        message = "Valid Bin Location";
        valid = 1;
      }
      }
      
      if(valid == 1){
        //GREEN
        $("#binMessage").css("background-color","#66FF66")
      }
      else{
        //RED
        $("#binMessage").css("background-color","#FF6666")
      }
      
      $("#binMessage").val(message); 
      return true;  
    }
    
    
    function validate(){return true;}
    function validateTrack(){return true;}
    
    function validateLoc(){
      var locMsg = $("#locationMessage").val();
      var binMsg = $("#binMessage").val();
      if(locMsg != "Not Valid"){
        if(binMsg != "Not Valid"){         
          if(locMsg.indexOf("Existing:") == -1){
            return confirm("The Location entered does not currently exist, do you wish to create the store?");
          }
          return true;
        }
      }
      return false;
    }
    
    function validateVal(){return true;}
    function validateConfig(){return true;}

  </script>
  
  <@enableDatePickers/>
  <@enableTimePickers/>
  
</head>
<body>

<@subTabs/>
<#assign currentUser = Session.userObj>


    <div class="tableTabs">
        <ul class="tabs">
            
            <li class="linkTab" class="current" id="componentLink">
            <a onclick="showTab('component');return false" href="#">Component</a></li>
            
            
            <#if id?exists >
            
            <li class="linkTab" id="trackingLink">
            <a onclick="showTab('tracking');return false" href="#">Tracking</a></li>
            
            <li class="linkTab" id="valuationLink">
            <a onclick="showTab('valuation');return false" href="#">Valuation</a></li>
            
            <li class="linkTab" id="locationLink">
            <a onclick="showTab('location');return false" href="#">Location</a></li>
            
            <!--<li class="linkTab" id="configLink">
            <a onclick="showTab('config');return false" href="#">Configuration</a></li>-->
            
            <li class="linkTab" id="historyLink">
            <a onclick="showTab('history');return false" href="#">History</a></li>
            
            </#if>
            
        </ul>
    </div>

    <div id="component" class="tabContent">
    <form action="component!save.action" autocomplete="off" method="POST" class="smart" onsubmit="return validate();" style="clear:left;">
    <input type="hidden" name="id" value="${id!}"/>
    <input type="hidden" name="component.id" value="${id!}"/>
    
    <fieldset>
        <#if id?exists >
        <legend>Edit Component - ${component.name!}</legend>
        <#else>
        <legend>Add Component</legend>
        </#if>
        
        <div class="fm-opt">
            <label for="component.type">Type:</label>
            <select onchange="" name="component.type">
                <#if component.type?exists>
                <option <#if component.type.equals("Class A")>selected</#if> >Class A
                <option <#if component.type.equals("Class E")>selected</#if> >Class E
                <#else>
                <option >Class A
                <option >Class E
                </#if>
            </select>
        </div>
        
        <div class="fm-opt">
            <label for="component.name">Part Name:</label>
            <input id="code" name="component.name" type="text" value="${component.name!}"/>
        </div>
        <div class="fm-opt">
            <label for="component.description">Part Description:</label>
            <input name="component.description" type="text" value="${component.description!}"/>
        </div>
        <div class="fm-opt">
            <label for="component.number">Part Number:</label>
            <input name="component.number" type="text" value="${component.number!}"/>
        </div>
        <div class="fm-opt">
            <label for="component.serial">Serial Number:</label>
            <input name="component.serial" type="text" value="${component.serial!}"/>
        </div>
        <div class="fm-opt">
            <label for="component.manufacturer">Manfucaturer:</label>
            <input name="component.manufacturer" type="text" value="${component.manufacturer!}"/>
        </div>
        <div class="fm-opt">
            <label for="component.manufacturedDate">Manufactured Date:</label>
            <input class="date-pick" name="component.manufacturedDate" type="text" <#if component.manufacturedDate?exists >value="${component.manufacturedDate?string('dd/MM/yyyy')}"<#else>value=""</#if>  />
        </div>
        
        
        <button type="submit" class="smooth" style="float:right; margin-right:10px; margin-bottom: 4px;"><img src="images/icons/pencil.png"/>Save</button> 
       </fieldset>
    </form>
    </div>
    <!--TRACKING-->
    <div id="tracking" style="display:none;" class="tabContent">
    <form action="component!save.action" autocomplete="off" method="POST" class="smart" onsubmit="return validateTrack();" style="clear:left;">
      <input type="hidden" name="id" value="${id!}"/>
      <input type="hidden" name="component.id" value="${id!}"/>
      
      
      <fieldset>
      <legend>Component Tracking - ${component.name!}</legend>
      
        <div class="fm-opt">
            <label for="component.airframeHours">Airframe Hours:</label>
            <input name="component.airframeHours" type="text" value="${component.airframeHours!}" onchange='checkNum(this);'/>
        </div>
        <div class="fm-opt">
            <label for="component.timeBetweenOverhaul">Time Between Overhaul:</label>
            <input name="component.timeBetweenOverhaul" type="text" value="${component.timeBetweenOverhaul!}" onchange='checkNum(this);'/>
        </div>
        <div class="fm-opt">
            <label for="component.hoursRun">Hours Run:</label>
            <input name="component.hoursRun" type="text" value="${component.hoursRun!}" onchange='checkNum(this);'/>
        </div>
        <div class="fm-opt">
            <label for="component.installDate">Install Date:</label>
            <input class="date-pick" name="component.installDate" type="text" <#if component.installDate?exists >value="${component.installDate?string('dd/MM/yyyy')}"<#else>value=""</#if>/>
        </div>
        <div class="fm-opt">
            <label for="component.installTime">Install Time:</label>
            <input name="installTime" class="time-pick" type="text" <#if component.installTime?exists >value="${component.installTime?string('HH:mm')}"<#else>value=""</#if> />
        </div>
        <div class="fm-opt">
            <label for="component.hoursOnInstall">hoursOnInstall:</label>
            <input name="component.hoursOnInstall" type="text" value="${component.hoursOnInstall!}" onchange='checkNum(this);'/>
        </div>
        <div class="fm-opt">
            <label for="component.expiryDate">Expiry Date:</label>
            <input class="date-pick" name="component.expiryDate" type="text" <#if component.expiryDate?exists >value="${component.expiryDate?string('dd/MM/yyyy')}"<#else>value=""</#if>/>
        </div>
        <div class="fm-opt">
            <label for="component.expiryHours">Expiry Hours:</label>
            <input name="component.expiryHours" type="text" value="${component.expiryHours!}"/>
        </div>        
      
      <button type="submit" class="smooth" style="float:right; margin-right:10px; margin-bottom: 4px;"><img src="images/icons/pencil.png"/>Save</button>  
      </fieldset>
      
    </form>
    </div>
     <!--Valuation-->
    <div id="valuation" style="display:none;" class="tabContent">
    <form style="width:500px;float:left; height:300px;" action="component!save.action" autocomplete="off" method="POST" class="smart" onsubmit="return validateVal();" >
      <input type="hidden" name="id" value="${id!}"/>
      <input type="hidden" name="component.id" value="${id!}"/>
      <input type="hidden" name="val" value="1"/>
      <input type="hidden" name="valuationId" id="valuationId" value=""/>
      
      <fieldset>
      <legend>Component Valuation - ${component.name!}</legend>
      
      <div class="fm-opt">
            <label for="valDate">Date:</label>
            <input class="date-pick" id="valDate" name="valDate" type="text" value=""/>
      </div>
      <div class="fm-opt">
            <label for="valTime">Time:</label>
            <input class="time-pick" id="valTime" name="valTime" type="text" value=""/>
      </div>
      
      <div class="fm-opt">
         <label for="nvg">Market Value:</label> 
         <div>
            <input type="text" id="marketVal" name="marketVal" style="width:60px;" onchange='checkNum(this);' />
            <select id="marketCurrency" name="marketCurrency" style="width:60px;">
            <option>USD
            <#list rates?if_exists as rate>
                <option>${rate.currencyCodeFrom}</option>
            </#list>
            </select>
         </div>
      </div>
      
      <div class="fm-opt">
         <label for="nvg">Purchase Value:</label> 
         <div>
            <input type="text" id="purchaseVal" name="purchaseVal" style="width:60px;" onchange='checkNum(this);' />
            <select id="purchaseCurrency" name="purchaseCurrency" style="width:60px;">
            <option>USD
            <#list rates?if_exists as rate>
                <option>${rate.currencyCodeFrom}</option>
            </#list>
            </select>
         </div>
      </div>
      
      <div class="fm-opt">
         <label for="nvg">Replacement Value:</label> 
         <div>
            <input type="text" id="replacementVal" name="replacementVal" style="width:60px;" onchange='checkNum(this);' />
            <select id="replacementCurrency" name="replacementCurrency" style="width:60px;">
            <option>USD
            <#list rates?if_exists as rate>
                <option>${rate.currencyCodeFrom}</option>
            </#list>
            </select>
         </div>
      </div>
      <br/>
      <button type="submit" class="smooth" style="float:right; margin-right:10px; margin-bottom: 4px;"><img src="images/icons/pencil.png"/>Save</button>  
      <button id="cancelEditValue" onclick="cancelEditVal(this);" type="button" class="smooth" style="display:none; float:right; margin-right:10px; margin-bottom: 4px;"><img src="images/icons/cross.png"/>Cancel</button>
      
      </fieldset>
      </form>
      
      <form style="clear:none;width:500px;float:left;margin-left:10px;border-left:1px solid silver;height:300px;" action="component!edit.action" autocomplete="off" method="POST" class="smart" >
        <fieldset>
        <legend>Previous Valuations</legend>
        <@jmesa2 id="valuations" mytableHtml=valTableHtml />
        </fieldset>
      </form>

    
    </div>
     <!--LOCATION-->
    <div id="location" style="display:none;" class="tabContent">
    
    <form id="locationForm" name="locationForm" autocomplete="off" style="clear:left;margin:0px;padding:0px;width:500px;float:left;margin-left:10px;border-left:1px solid silver;height:300px;" action="component!save.action" method="POST" class="smart" onsubmit="return validateLoc();" >
      <input type="hidden" name="id" value="${id!}"/>
      <input type="hidden" name="component.id" value="${id!}"/>
      <input type="hidden" name="loc" value="1"/>
      <input type="hidden" id="locCurrent" name="locCurrent"  value="1"/>
      <input type="hidden" id="locationId" name="locationId" value=""/>
      
      
      <fieldset>
      <legend>Component Location - ${component.name!}</legend>
      
      <div id="editMessage" style="color:green;font-weight:bold;text-align:center;display:none;margin:10px;padding:10px;border:1px dashed silver;"></div>
      
      <div id="locDiv" class="fm-opt">
            <label for="location">Location:</label> 
            <input id="locationInput" type="text" value="" onkeypress="updateLocationMessage(this.value);" onkeyup="updateLocationMessage(this.value);" onchange="updateLocationMessage(this.value);" name="location"/>
            <input type="text" value="Not Valid" style="background-color:#FF6666" DISABLED name="" id="locationMessage"/>
      </div>
      <div id="binDiv" class="fm-opt">
            <label for="bin">Bin:</label> 
            <input id="binInput" type="text" value="" onkeypress="updateBinMessage(this.value);" onkeyup="updateBinMessage(this.value);" onchange="updateBinMessage(this.value);" name="bin"/>
            <input type="text" style="background-color:#66FF66" value="Valid Bin Location" DISABLED name="" id="binMessage"/>
      </div>
      <#if component.type.equals("Class E")>
      <div class="fm-opt">
            <label for="quantity">Quantity:</label> 
            <input id="qtyInput" type="text" value="1" name="quantity" onchange='checkNum(this);'/>
      </div>
      <#else>
      <input id="qtyInput" type="hidden" value="1" name="quantity"/>
      </#if>
      <br/>
      
      <#if component.type.equals("Class E")>
      <div id="conLocDiv" class="fm-opt" style="background-color:#EEEEEE;">
            <label for="quantity">Add To Existing Location:</label> 
            <select id="conLocationInput" style="background-color:#EEEEEE;" name="addLocation" onchange="changeAddLocation(addLocation.options.selectedIndex);">
             <option>- 
             <#list component.getLocations() as location>
             <option value="${location.id}">${location.location?if_exists} - ${location.bin?if_exists}
             </#list>
            </select>
      </div>
      </#if>
      
      
      <button id="deleteEditLoc" onclick="deleteEditLocation(this);" type="button" class="smooth" style="display:none; float:right; margin-right:10px; margin-bottom: 4px;"><img src="images/icons/delete.png"/>Remove</button>
      <button type="submit" class="smooth" style="float:right; margin-right:10px; margin-bottom: 4px;"><img src="images/icons/pencil.png"/>Save</button>
      <button id="cancelEditLoc" onclick="cancelEditLocation(this);" type="button" class="smooth" style="display:none; float:right; margin-right:10px; margin-bottom: 4px;"><img src="images/icons/cross.png"/>Cancel</button>
        
      </fieldset>
      </form>
      

      <form  autocomplete="off" style="clear:none;margin:0px;padding:0px;width:500px;float:left;margin-left:10px;border-left:1px solid silver;height:300px;" action="component!edit.action" method="POST" class="smart" onsubmit="return validate();" style="clear:left;">
      <input type="hidden" name="id" value="${id!}"/>
      <input type="hidden" name="component.id" value="${id!}"/>
      <input type="hidden" name="loc" value="1"/>
      <input type="hidden" name="subtab" value="location"/>
      
      <fieldset>
      <legend>Component Locations</legend>
      
      <@jmesa id="locations"/>
      
      </fieldset>
      </form>

      
    
    </div>
    
     <!--CONFIGURATION-->
    <!--<div id="config" style="display:none;" class="tabContent">
    <form  autocomplete="off" action="component!save.action" method="POST" class="smart" onsubmit="return validateConfig();" style="clear:left;">
      <input type="hidden" name="id" value="${id!}"/>
      <input type="hidden" name="component.id" value="${id!}"/>
      <input type="hidden" name="config" value="1"/>
      
      <fieldset>
      <legend>Component Configuration</legend>
        
       <div style="width:300px;float:left;">
        
       <div class="fm-opt">
            <label for="nvg">NVG:</label> 
            <input type="checkbox" style="width:30px;" value="1" <#if component.nvg?exists >CHECKED</#if>  name="nvg"/>
       </div>
       <div class="fm-opt">
            <label for="nvg">FLIR:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if component.flir?exists >CHECKED</#if> name="flir"/>
       </div>
       <div class="fm-opt">
            <label for="nvg">Night Sight:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if component.night?exists >CHECKED</#if> name="night"/>
       </div>
       <div class="fm-opt">
            <label for="nvg">Floatation:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if component.floa?exists >CHECKED</#if> name="floa"/>
       </div>
       <div class="fm-opt">
            <label for="nvg">Indigo Sat:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if component.indi?exists >CHECKED</#if> name="indi"/>
       </div>
       
       </div>
       <div style="width:300px;float:left;">
       
       <div class="fm-opt">
            <label for="nvg">T/CAS:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if component.tcas?exists >CHECKED</#if> name="tcas"/>
       </div>
       <div class="fm-opt">
            <label for="nvg">Hoist:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if component.hoist?exists >CHECKED</#if> name="hoist"/>
       </div>
       
       <div class="fm-opt">
            <label for="nvg">Cargo:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if component.cargo?exists >CHECKED</#if> name="cargo"/>
       </div>
       <div class="fm-opt">
            <label for="nvg">Bambi:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if component.bambi?exists >CHECKED</#if> name="bambi"/>
       </div>
       <div class="fm-opt">
            <label for="nvg">VIP:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if component.vip?exists >CHECKED</#if> name="vip"/>
       </div>
       
       </div>
       <div style="width:300px;float:left;">
       
       <div class="fm-opt">
            <label for="nvg">Trooper:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if component.troop?exists >CHECKED</#if> name="troop"/>
       </div>
       <div class="fm-opt">
            <label for="ferry">Ferry:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if component.ferry?exists >CHECKED</#if> name="ferry"/>
       </div>
       <div class="fm-opt">
            <label for="fdr">FDR:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if component.fdr?exists >CHECKED</#if> name="fdr"/>
       </div>
       <div class="fm-opt">
            <label for="air">Air Con:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if component.air?exists >CHECKED</#if> name="air"/>
       </div>
      <div class="fm-opt">
            <label for="mmel">MMEL:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if component.mmel?exists >CHECKED</#if> name="mmel"/>
       </div>
      
      </div>
      
      <button type="submit" class="smooth" style="float:right; margin-right:10px; margin-bottom: 4px;"><img src="images/icons/pencil.png"/>Save</button>  
      </fieldset>
      
    </form>
    </div>-->
    
    <!--History-->
    <div id="history" style="display:none;" class="tabContent">
    <form  autocomplete="off" action="component!save.action" method="POST" class="smart" onsubmit="return validateConfig();" style="clear:left;">
      <input type="hidden" name="id" value="${id!}"/>
      <input type="hidden" name="component.id" value="${id!}"/>
      
      <fieldset>
      <legend>Component History - ${component.name!}</legend>
        
        <@jmesa2 id="history" mytableHtml=histTableHtml?if_exists />
      
      </fieldset>
      
    </form>
    </div>


</body>
</html>