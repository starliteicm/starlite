<#include "/starlite.ftl">
<#setting number_format = "######.##########"/>

<html>
<head>
  <script>
    function showTab(tab){
      $('.linkTab').removeClass("current");
      $('#'+tab+"Link").addClass("current");
      $('.tabContent').css("display","none");
      $('#'+tab).css("display","");
    }
    
    function updateLocationMessage(location){
      var message = "Not Valid";
      if(location.length == 5){
        message = "Valid Store";
        if(location.substring(0,2) == "ZS"){message = "Valid Aircraft";}
        if(location.substring(0,2) == "MS"){message = "Valid Mobile Store";}
        if(location.substring(0,2) == "WS"){message = "Valid Workshop Cycle";}
        if(location.substring(0,2) == "DR"){message = "Valid Depot Cycle";}
      }
      $("#locationMessage").val(message); 
      return true;  
    }
    
    function updateBinMessage(bin){
      var message = "Not Valid";
      
      if(bin.length % 2 == 0){
        message = "Valid Store";
      }
      
      $("#binMessage").val(message); 
      return true;  
    }

  </script>
  
</head>
<body>

<@subTabs/>
<#assign currentUser = Session.userObj>


    <div class="tableTabs">
        <ul class="tabs">
            <li class="linkTab" class="current" id="componentLink">
            <a onclick="showTab('component');return false" href="#">Component</a></li>
            <li class="linkTab" id="trackingLink">
            <a onclick="showTab('tracking');return false" href="#">Tracking</a></li>
            <li class="linkTab" id="valuationLink">
            <a onclick="showTab('valuation');return false" href="#">Valuation</a></li>
            <li class="linkTab" id="locationLink">
            <a onclick="showTab('location');return false" href="#">Location</a></li>
            <li class="linkTab" id="configLink">
            <a onclick="showTab('config');return false" href="#">Configuration</a></li>
        </ul>
    </div>

    <div id="component" class="tabContent">
    <form action="component!save.action" method="POST" class="smart" onsubmit="return validate();" style="clear:left;">
    <input type="hidden" name="id" value="${id!}"/>
    <input type="hidden" name="component.id" value="${id!}"/>
    
    <fieldset>
        <#if id?exists >
        <legend>Edit Component</legend>
        <#else>
        <legend>Add Component</legend>
        </#if>
        
        <div class="fm-opt">
            <label for="component.type">Type:</label>
            <select onchange="" name="component.type">
                <#if component.type?exists>
                <option <#if component.type.equals("Class C")>selected</#if> >Class C
                <option <#if component.type.equals("Class E")>selected</#if> >Class E
                <#else>
                <option >Class C
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
            <input name="component.manufacturedDate" type="text" value="${component.manufacturedDate!}"/>
        </div>
        
        
        <button type="submit" class="smooth" style="float:right; margin-right:10px; margin-bottom: 4px;"><img src="images/icons/pencil.png"/>Save</button> 
       </fieldset>
    </form>
    </div>
    <!--TRACKING-->
    <div id="tracking" style="display:none;" class="tabContent">
    <form action="component!save.action" method="POST" class="smart" onsubmit="return validate();" style="clear:left;">
      <input type="hidden" name="id" value="${id!}"/>
      <input type="hidden" name="component.id" value="${id!}"/>
      
      
      <fieldset>
      <legend>Component Tracking</legend>
      
        <div class="fm-opt">
            <label for="component.timeBetweenOverhaul">Time Between Overhaul:</label>
            <input name="component.timeBetweenOverhaul" type="text" value="${component.timeBetweenOverhaul!}"/>
        </div>
        <div class="fm-opt">
            <label for="component.hoursRun">Hours Run:</label>
            <input name="component.hoursRun" type="text" value="${component.hoursRun!}"/>
        </div>
        <div class="fm-opt">
            <label for="component.installDate">Install Date:</label>
            <input name="component.installDate" type="text" value="${component.installDate!}"/>
        </div>
        <div class="fm-opt">
            <label for="component.installTime">Install Time:</label>
            <input name="component.installTime" type="text" value="${component.installTime!}"/>
        </div>
        <div class="fm-opt">
            <label for="component.hoursOnInstall">hoursOnInstall:</label>
            <input name="component.hoursOnInstall" type="text" value="${component.hoursOnInstall!}"/>
        </div>
        <div class="fm-opt">
            <label for="component.expiryDate">Expiry Date:</label>
            <input name="component.expiryDate" type="text" value="${component.expiryDate!}"/>
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
    <form action="component!save.action" method="POST" class="smart" onsubmit="return validate();" style="clear:left;">
      <input type="hidden" name="id" value="${id!}"/>
      <input type="hidden" name="component.id" value="${id!}"/>
      <input type="hidden" name="val" value="1"/>
      
      <div style="width:500px;float:left; height:300px;">
      <fieldset>
      <legend>Component Valuation</legend>
      
      <div class="fm-opt">
         <label for="nvg">Market Value:</label> 
         <div>
            <input type="text" name="marketVal" style="width:60px;" />
            <select id="marketCurrency" name="marketCurrency" style="width:60px;">
            <#list rates?if_exists as rate>
                <option>${rate.currencyCodeFrom}</option>
            </#list>
            </select>
         </div>
      </div>
      
      <div class="fm-opt">
         <label for="nvg">Purchase Value:</label> 
         <div>
            <input type="text" name="purchaseVal" style="width:60px;" />
            <select id="purchaseCurrency" name="purchaseCurrency" style="width:60px;">
            <#list rates?if_exists as rate>
                <option>${rate.currencyCodeFrom}</option>
            </#list>
            </select>
         </div>
      </div>
      
      <div class="fm-opt">
         <label for="nvg">Replacement Value:</label> 
         <div>
            <input type="text" name="replacementVal" style="width:60px;" />
            <select id="replacementCurrency" name="replacementCurrency" style="width:60px;">
            <#list rates?if_exists as rate>
                <option>${rate.currencyCodeFrom}</option>
            </#list>
            </select>
         </div>
      </div>
      
      <button type="submit" class="smooth" style="float:right; margin-right:10px; margin-bottom: 4px;"><img src="images/icons/pencil.png"/>Save</button>  
      </fieldset>
      </div>
      
      <div style="width:500px;float:left;margin-left:10px;border-left:1px solid silver;height:300px;">
        <fieldset>
        <legend>Previous Valuations</legend>
        </fieldset>
      </div>

    </form>
    </div>
     <!--LOCATION-->
    <div id="location" style="display:none;" class="tabContent">
    <form action="component!save.action" method="POST" class="smart" onsubmit="return validate();" style="clear:left;">
      <input type="hidden" name="id" value="${id!}"/>
      <input type="hidden" name="component.id" value="${id!}"/>
      <input type="hidden" name="loc" value="1"/>
      
      <div style="width:500px;float:left; height:300px;">
      <fieldset>
      <legend>Component Location</legend>
      
      <div class="fm-opt">
            <label for="location">Location:</label> 
            <input type="text" value="" onkeypress="updateLocationMessage(this.value);" onkeyup="updateLocationMessage(this.value);" onchange="updateLocationMessage(this.value);" name="location"/>
            <input type="text" value="Not Valid" DISABLED name="" id="locationMessage"/>
      </div>
      <div class="fm-opt">
            <label for="bin">Bin:</label> 
            <input type="text" value="" onkeypress="updateBinMessage(this.value);" onkeyup="updateBinMessage(this.value);" onchange="updateBinMessage(this.value);" name="bin"/>
            <input type="text" value="Not Valid" DISABLED name="" id="binMessage"/>
      </div>
      <div class="fm-opt">
            <label for="quantity">Quantity:</label> 
            <input type="text" value="1" name="quantity"/>
      </div>
      
      <button type="submit" class="smooth" style="float:right; margin-right:10px; margin-bottom: 4px;"><img src="images/icons/pencil.png"/>Save</button>  
      </fieldset>
      </div>
      
      <div style="width:500px;float:left;margin-left:10px;border-left:1px solid silver;height:300px;">
      <fieldset>
      <legend>Component Locations</legend>
      </fieldset>
      </div>
      
    </form>
    </div>
    
     <!--CONFIGURATION-->
    <div id="config" style="display:none;" class="tabContent">
    <form action="component!save.action" method="POST" class="smart" onsubmit="return validate();" style="clear:left;">
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
            <label for="nvg">Ferry:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if component.ferry?exists >CHECKED</#if> name="ferry"/>
       </div>
       <div class="fm-opt">
            <label for="nvg">FDR:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if component.fdr?exists >CHECKED</#if> name="fdr"/>
       </div>
       <div class="fm-opt">
            <label for="nvg">Air Con:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if component.air?exists >CHECKED</#if> name="air"/>
       </div>
      
      </div>
      
      <button type="submit" class="smooth" style="float:right; margin-right:10px; margin-bottom: 4px;"><img src="images/icons/pencil.png"/>Save</button>  
      </fieldset>
      
    </form>
    </div>


</body>
</html>