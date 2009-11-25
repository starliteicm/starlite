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
		<form id="randForm" name="randForm" action="exchange!setRates.action" method="GET">
		
		<div style="border:1px solid silver;padding:20px;">
		<div  style="text-align:center;padding:5px;width:100px;display:inline;">(ZAR) <img src="images/flagZA.gif"/></div>
		<input name="amount" style="text-align:center;width:100px;margin:10px;display:inline;" value="${rand.amount}">
		<div  style="text-align:center;padding:5px;width:100px;display:inline;">(USD) <img src="images/flagUS.gif"/></div>
		<button type="button" onclick="document.forms.randForm.submit();" class="smooth" style="position:relative;float:right;top:4px"><img src="images/icons/accept.png"/> Set</button>
		
		<!--
		<br/>
		<div style="text-align:center;padding:5px;width:100px;display:inline;">(1 ZAR)</div>
        <div style="text-align:center;width:100px;margin:20px;display:inline;">=</div>
        <div style="text-align:center;padding:5px;width:100px;display:inline;">(? USD)</div>
		-->
		<br/><br/>
		<#if rand.lastUpdated?exists>
		<small style="color:silver">Last Updated: ${rand.lastUpdated?datetime} - ${rand.updatedBy}</small>
		</#if>
		
		</fieldset>
		
		
		</div> 
		
		
		
        </div>

</body>