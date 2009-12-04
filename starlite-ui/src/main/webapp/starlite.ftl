<#macro jmesa id>
 <#if tableTabs??>
    <div class="tableTabs">
    	<ul class="tabs">
    	<#list tableTabs as tab>
    		<#if tab.current>
    		<li class="current">
    		<#else>
    		<li>
    		</#if>
			<a href="${tab.url}">${tab.label}</a></li>
		</#list>
		</ul>
	</div>
	<hr class="clear"/>
</#if>
    
<form name="${id}Form" action="${request.requestURL}${params!}">
<div id="${id}">
${tableHtml}
</div>
</form>

<script type="text/javascript">
function onInvokeAction(id) {
	setExportToLimit(id, '');
    createHiddenInputFieldsForLimitAndSubmit(id);
}
</script>
</#macro>

<#macro subTabs>
 <#if tableTabs??>
    <div class="tableTabs">
    	<ul class="tabs">
    	<#list tableTabs as tab>
    		<#if tab.current>
    		<li class="current">
    		<#else>
    		<li>
    		</#if>
			<a href="${tab.url}">${tab.label}</a></li>
		</#list>
		</ul>
	</div>
	<!--hr class="clear"/-->
</#if>
</#macro>

<#macro symbol currencyCode><#if currencyCode == "GBP">&#163;<#assign currencySymbolSet = true/></#if><#if currencyCode == "USD">&#36;<#assign currencySymbolSet = true/></#if><#if currencyCode == "EUR">&#8364;<#assign currencySymbolSet = true/></#if><#if !currencySymbolSet??>${currencyCode} </#if></#macro>

<#macro currencyDropDown name current>
<select name="${name}" type="text" style="width:80px;">
	<option value=''></option>
	<option <#if current == 'EUR'>selected</#if> value='EUR'>EUR (&euro;)</option>
	<option <#if current == 'GBP'>selected</#if> value='GBP'>GBP (&pound;)</option>
	<option <#if current == 'USD'>selected</#if> value='USD'>USD ($)</option>
</select>
</#macro>

<#macro enableJQuery>
	<script type="text/javascript" src="${request.contextPath}/js/jquery-1.2.3.min.js"></script>	
</#macro>

<#macro enableJwysiwyg>
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/styles/jquery.wysiwyg.css">
	<script type="text/javascript" src="${request.contextPath}/js/jquery.wysiwyg.js"></script>
</#macro>

<#macro enableDatePickers>
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/styles/jquery.datePicker.css">
	<script type="text/javascript" src="${request.contextPath}/js/date.js"></script>
	<!--[if IE]><script type="text/javascript" src="${request.contextPath}/js/jquery.bgiframe.js"></script><![endif]-->
	<script type="text/javascript" src="${request.contextPath}/js/jquery.datePicker.js"></script>
	<script>
		$(function()
		{
			$('.date-pick').datePicker({startDate:'01/01/1900'});
		});
	</script>
</#macro>

<#macro datePicker name current="">
	<input type="text" name="${name}" value="${current}" class="date-pick" style="width:70px;"/>
</#macro>

<#macro enableTimePickers>
  <link rel="stylesheet" type="text/css" href="${request.contextPath}/styles/jquery.timeentry.css" />
  <script type="text/javascript" src="${request.contextPath}/js/jquery.timeentry.min.js" ></script>
  <script>
  
  function timeBetween(start,finish,hours){
    var startTime = $(start).timeEntry('getTime');
    var finishTime = $(finish).timeEntry('getTime');
    
    if((startTime != null)&&(finishTime != null)){
      if((startTime != "")&&(finishTime != "")){
        var diffHours = finishTime.getHours() - startTime.getHours();
        var diffMins = finishTime.getMinutes() - startTime.getMinutes();
    
        if (diffMins < 0){
          diffHours = diffHours - 1;
          diffMins = diffMins + 60;
        }
        
        if(diffHours >= 0){
    
          //alert(diffHours + ":" + diffMins);
          if(diffHours < 10){diffHours = "0"+diffHours;}
          if(diffMins < 10){diffMins = "0"+diffMins;}
          $(hours).val(diffHours + ":" + diffMins);
        }
        else{
          $(hours).val("00:00");
        }   
      }
      else{
        $(hours).val("00:00");
      }
    }
    else{
      $(hours).val("00:00");
    }
  }
  
        $(document).ready(function()
        {
            $('.time-pick').timeEntry({defaultTime: new Date(0, 0, 0, 0, 0, 0), show24Hours: true, spinnerImage: ''});
            $("#loading").css("display","none");
        });
  </script>
</#macro>

<#macro enableCalculator>
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/styles/jquery.calculator.css">
	<script type="text/javascript" src="${request.contextPath}/js/jquery.calculator.min.js"></script>
</#macro>

<#macro enableHelp>
	<script type="text/javascript" src="${request.contextPath}/js/tooltip.js"></script>
	<script type="text/javascript" src="${request.contextPath}/js/jquery.dimensions.js"></script>
	<script type="text/javascript">
        $(document).ready(function() {
        	$('.tooltip').tooltip({ 
    			track: true, 
    			delay: 10, 
    			showURL: false, 
    			showBody: " - ", 
    			extraClass: "pretty", 
    			fixPNG: true, 
    			opacity: 0.95,
    			top: 100,
    			left: 30
			});
        });
	</script>
	<style type="text/css">
    #tooltip.pretty {
    	position:absolute;
    	margin-top:80px;
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
    </style>
</#macro>

