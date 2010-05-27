<#include "/starlite.ftl">
<html>
<head>
<link rel="stylesheet" type="text/css" href="styles/jmesa.css">
<script type="text/javascript" src="js/jmesa.js"></script>
<script type="text/javascript">
        $(document).ready(function() {
           addDropShadow('images/table/');
        });
</script>
</head>
<body>
	<@subTabs/>
	<!--<div style="clear:left; border: 1px solid silver; padding: 10px;">-->
	
     <!--CONFIGURATION-->
     <form  autocomplete="off" action="component!save.action" method="POST" class="smart" onsubmit="return validateConfig();" style="clear:left;">
      <input type="hidden" name="id" value="${id!}"/>
      <input type="hidden" name="config" value="1"/>
      
      <fieldset>
      <legend>Aircraft Configuration</legend>
       <div style="width:300px;float:left;"> 
       <div class="fm-opt">
            <label for="nvg">NVG:</label> 
            <input type="checkbox" style="width:30px;" value="1" <#if aircraft.nvg?exists >CHECKED</#if>  name="nvg"/>
       </div>
       <div class="fm-opt">
            <label for="nvg">FLIR:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if aircraft.flir?exists >CHECKED</#if> name="flir"/>
       </div>
       <div class="fm-opt">
            <label for="nvg">Night Sight:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if aircraft.night?exists >CHECKED</#if> name="night"/>
       </div>
       <div class="fm-opt">
            <label for="nvg">Floatation:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if aircraft.floa?exists >CHECKED</#if> name="floa"/>
       </div>
       <div class="fm-opt">
            <label for="nvg">Indigo Sat:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if aircraft.indi?exists >CHECKED</#if> name="indi"/>
       </div>
       
       </div>
       <div style="width:300px;float:left;">
       
       <div class="fm-opt">
            <label for="nvg">T/CAS:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if aircraft.tcas?exists >CHECKED</#if> name="tcas"/>
       </div>
       <div class="fm-opt">
            <label for="nvg">Hoist:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if aircraft.hoist?exists >CHECKED</#if> name="hoist"/>
       </div>
       
       <div class="fm-opt">
            <label for="nvg">Cargo:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if aircraft.cargo?exists >CHECKED</#if> name="cargo"/>
       </div>
       <div class="fm-opt">
            <label for="nvg">Bambi:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if aircraft.bambi?exists >CHECKED</#if> name="bambi"/>
       </div>
       <div class="fm-opt">
            <label for="nvg">VIP:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if aircraft.vip?exists >CHECKED</#if> name="vip"/>
       </div>
       
       </div>
       <div style="width:300px;float:left;">
       
       <div class="fm-opt">
            <label for="nvg">Trooper:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if aircraft.troop?exists >CHECKED</#if> name="troop"/>
       </div>
       <div class="fm-opt">
            <label for="ferry">Ferry:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if aircraft.ferry?exists >CHECKED</#if> name="ferry"/>
       </div>
       <div class="fm-opt">
            <label for="fdr">FDR:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if aircraft.fdr?exists >CHECKED</#if> name="fdr"/>
       </div>
       <div class="fm-opt">
            <label for="air">Air Con:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if aircraft.air?exists >CHECKED</#if> name="air"/>
       </div>
      <div class="fm-opt">
            <label for="mmel">MMEL:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if aircraft.mmel?exists >CHECKED</#if> name="mmel"/>
       </div>
      
      <button type="submit" class="smooth" style="float:right; margin-right:10px; margin-bottom: 4px;"><img src="images/icons/pencil.png"/>Save</button>  
      </fieldset>
    </form>
    <!--</div>-->

	</div>
</body>
</html>