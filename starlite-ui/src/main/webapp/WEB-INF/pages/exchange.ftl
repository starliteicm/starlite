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
    </style>
</head>
<body class="exchange">

		
		
		<div style="padding-left:100px;padding-bottom:100px;width:600px;">
		
		<fieldset>
		<legend>Exchange Rates</legend>
		
		<div style="border:1px solid silver;padding:20px;">
		<form id="randForm" name="randForm" action="exchange!setRates.action" method="GET">
		<input name="type" value="ZAR" type="hidden" />
		<div  style="text-align:center;padding:5px;width:100px;display:inline;">(ZAR) <img style="position:relative;top:10px;" src="images/flagZA.png"/></div>
		<input name="amount" style="text-align:center;width:100px;margin:10px;display:inline;" value="${rand.amount}" />
		<div  style="text-align:center;padding:5px;width:100px;display:inline;">(USD) <img style="position:relative;top:10px;" src="images/flagUS.png"/></div>
		<button type="button" onclick="document.forms.randForm.submit();" class="smooth" style="position:relative;float:right;top:4px"><img src="images/icons/accept.png"/> Set</button>
		</form>
		<br/><br/>
		<#if rand.lastUpdated?exists>
		<small style="color:silver">Last Updated: ${rand.lastUpdated?datetime} - ${rand.updatedBy}</small>
		</#if>
		</div>
		
		<br/><br/>
		
		<div style="border:1px solid silver;padding:20px;">
        <form id="gbpForm" name="gbpForm" action="exchange!setRates.action" method="GET">
        <input name="type" value="GBP" type="hidden" />
        <div  style="text-align:center;padding:5px;width:100px;display:inline;">(GBP) <img style="position:relative;top:10px;" src="images/flagGB.png"/></div>
        <input name="amount" style="text-align:center;width:100px;margin:10px;display:inline;" value="${gbp.amount}" />
        <div  style="text-align:center;padding:5px;width:100px;display:inline;">(USD) <img style="position:relative;top:10px;" src="images/flagUS.png"/></div>
        <button type="button" onclick="document.forms.gbpForm.submit();" class="smooth" style="position:relative;float:right;top:4px"><img src="images/icons/accept.png"/> Set</button>
        </form>
        <br/><br/>
        <#if gbp.lastUpdated?exists>
        <small style="color:silver">Last Updated: ${gbp.lastUpdated?datetime} - ${gbp.updatedBy}</small>
        </#if>
        </div>
		
		<br/><br/>
        
        <div style="border:1px solid silver;padding:20px;">
        <form id="randForm" name="eurForm" action="exchange!setRates.action" method="GET">
        <input name="type" value="EUR" type="hidden" />
        <div  style="text-align:center;padding:5px;width:100px;display:inline;">(EUR) <img style="position:relative;top:10px;" src="images/flagEU.png"/></div>
        <input name="amount" style="text-align:center;width:100px;margin:10px;display:inline;" value="${eur.amount}" />
        <div  style="text-align:center;padding:5px;width:100px;display:inline;">(USD) <img style="position:relative;top:10px;" src="images/flagUS.png"/></div>
        <button type="button" onclick="document.forms.eurForm.submit();" class="smooth" style="position:relative;float:right;top:4px"><img src="images/icons/accept.png"/> Set</button>
        </form>
        <br/><br/>
        <#if eur.lastUpdated?exists>
        <small style="color:silver">Last Updated: ${eur.lastUpdated?datetime} - ${eur.updatedBy}</small>
        </#if>
        </div>
        
        <br/><br/>
        
        <div style="border:1px solid silver;padding:20px;">
        <form id="audForm" name="audForm" action="exchange!setRates.action" method="GET">
        <input name="type" value="AUD" type="hidden" />
        <div  style="text-align:center;padding:5px;width:100px;display:inline;">(AUD) <img style="position:relative;top:10px;" src="images/flagAU.png"/></div>
        <input name="amount" style="text-align:center;width:100px;margin:10px;display:inline;" value="${aud.amount}" />
        <div  style="text-align:center;padding:5px;width:100px;display:inline;">(USD) <img style="position:relative;top:10px;" src="images/flagUS.png"/></div>
        <button type="button" onclick="document.forms.audForm.submit();" class="smooth" style="position:relative;float:right;top:4px"><img src="images/icons/accept.png"/> Set</button>
        </form>
        <br/><br/>
        <#if aud.lastUpdated?exists>
        <small style="color:silver">Last Updated: ${aud.lastUpdated?datetime} - ${aud.updatedBy}</small>
        </#if>
        </div>

		
		</fieldset>
		
		
		</div> 
		
		
		
        </div>

</body>