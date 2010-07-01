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
     <form  autocomplete="off" action="aircraftInfo!save.action" method="POST" class="smart" style="clear:left;">
      <input type="hidden" name="id" value="${id!}"/>
      <input type="hidden" name="config" value="1"/>
      
      <fieldset>
      <legend>Aircraft Configuration</legend>
       <div style="width:300px;float:left;"> 
       <div class="fm-opt">
            <label for="nvg">NVG:</label> 
            <input type="checkbox" style="width:30px;" value="1" <#if aircraft.nvg?exists >CHECKED</#if>  name="aircraft.nvg"/>
       </div>
       <div class="fm-opt">
            <label for="nvg">FLIR:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if aircraft.flir?exists >CHECKED</#if> name="aircraft.flir"/>
       </div>
       <div class="fm-opt">
            <label for="nvg">Night Sight:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if aircraft.night?exists >CHECKED</#if> name="aircraft.night"/>
       </div>
       <div class="fm-opt">
            <label for="nvg">Floatation:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if aircraft.floa?exists >CHECKED</#if> name="aircraft.floa"/>
       </div>
       <div class="fm-opt">
            <label for="nvg">Indigo Sat:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if aircraft.indi?exists >CHECKED</#if> name="aircraft.indi"/>
       </div>
       
       </div>
       <div style="width:300px;float:left;">
       
       <div class="fm-opt">
            <label for="nvg">T/CAS:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if aircraft.tcas?exists >CHECKED</#if> name="aircraft.tcas"/>
       </div>
       <div class="fm-opt">
            <label for="nvg">Hoist:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if aircraft.hoist?exists >CHECKED</#if> name="aircraft.hoist"/>
       </div>
       
       <div class="fm-opt">
            <label for="nvg">Cargo:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if aircraft.cargo?exists >CHECKED</#if> name="aircraft.cargo"/>
       </div>
       <div class="fm-opt">
            <label for="nvg">Bambi:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if aircraft.bambi?exists >CHECKED</#if> name="aircraft.bambi"/>
       </div>
       <div class="fm-opt">
            <label for="nvg">VIP:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if aircraft.vip?exists >CHECKED</#if> name="aircraft.vip"/>
       </div>
       
       </div>
       <div style="width:300px;float:left;">
       
       <div class="fm-opt">
            <label for="nvg">Trooper:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if aircraft.troop?exists >CHECKED</#if> name="aircraft.troop"/>
       </div>
       <div class="fm-opt">
            <label for="ferry">Ferry:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if aircraft.ferry?exists >CHECKED</#if> name="aircraft.ferry"/>
       </div>
       <div class="fm-opt">
            <label for="fdr">FDR:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if aircraft.fdr?exists >CHECKED</#if> name="aircraft.fdr"/>
       </div>
       <div class="fm-opt">
            <label for="air">Air Con:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if aircraft.air?exists >CHECKED</#if> name="aircraft.air"/>
       </div>
      <div class="fm-opt">
            <label for="mmel">MMEL:</label>
            <input type="checkbox" style="width:30px;" value="1" <#if aircraft.mmel?exists >CHECKED</#if> name="aircraft.mmel"/>
       </div>
      
      <button type="submit" class="smooth" style="float:right; margin-right:10px; margin-bottom: 4px;"><img src="images/icons/pencil.png"/>Save</button>  
      </fieldset>
    </form>
    <!--</div>-->

	</div>
</body>
</html>