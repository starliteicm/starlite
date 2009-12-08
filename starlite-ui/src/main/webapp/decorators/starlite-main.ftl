<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
 "http://www.w3.org/TR/html4/strict.dtd">
<html>
<#setting number_format = "######" />

<#if Session.userObj??>
	<#assign user = Session.userObj>
</#if>

<#if request.serverName?matches(".*starlite.*")>
	<#assign domain = "starlite">
</#if>

<#if request.serverName?matches(".*cri.*")>
	<#assign domain = "cri">
</#if>

<#if user??>
<#assign isManager = user.hasPermission("ManagerView")>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="https://secure.i-tao.com/scripts/yui/build/reset-fonts-grids/reset-fonts-grids.css">
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/styles/default.css">
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/styles/forms.css">
	
	<title>${title}</title>
	${head}
	<script>
	 <#if current?if_exists != "Mailout">
		setTimeout(function() {
			var timeoutTime = new Date();
			//alert("Session Timed Out: " + timeoutTime);
			window.location="${request.contextPath}/doLogin?logout=true";
		}, 10*60*1000);
	</#if>
	</script>
</head>
<#if current??>
<#assign currentClass = current>
<#if currentClass=="reports"><#assign currentClass="aircraft"></#if>
<body class="${currentClass}">
<#else>
<body>
</#if>

<div name="logo" style="float:left;" id="logo">
	<#if isManager>
	<img style="float:left;position:absolute;z-index:100;" src="${request.contextPath}/images/starlite.png"/>
	<#else>
	<img style="float:left;position:absolute;z-index:100;" src="${request.contextPath}/images/cri-logo.png"/>
	</#if>
</div>

<div id="complete" style="position:relative;top:10px">
<div id="tabs">
<#if isManager>
    <ul class="tabs" style="width:900px;position:relative;margin-right:5%;border-bottom:none;left:150px;">
<#else>
    <ul class="tabs" style="width:80%;position:relative;left:20%;border-bottom:none;left:150px;">
</#if>
   
      <#if isManager>
      	  <#if current?? && current=="schedule">
      		<li class="schedule current">
      	  <#else>
      		<li class="schedule">
      	  </#if>
      	  <a href="schedule.action">Schedule</a></li>
          <#if current?? && current=="aircraft">
      	  <li class="aircraft current">
	      <#else>
	      <li class="aircraft">
	      </#if>
	      <a href="aircraft.action">Aircraft</a></li>
	      <#if current?? && current=="charters">
	      <li class="charters current">
	      <#else>
	      <li class="charters">
	      </#if>
	      <a href="charters.action">Contract</a></li>
	      <#if current?? && current=="crew">
	      <li class="crew current">
	      <#else>
	      <li class="crew">
	      </#if>
	      <a href="crew.action">Crew</a></li>
	      
	      <#if current?? && current=="search">
	      <li class="search current">
	      <#else>
	      <li class="search">
	      </#if>
	      <a href="bookmarkSearch.action">Documents</a></li>
	      
	      <#if current?? && current=="reports">
	      <li class="aircraft current">
	      <#else>
	      <li class="aircraft">
	      </#if>
	      <a href="reports.action">Reports</a></li>
	      
	      <#if current?? && current=="Mailout">
	      <li class="search current">
	      <#else>
	      <li class="search">
	      </#if>
	      <a href="mailout.action">Mailout</a></li>
	      
	      <#if current?? && current=="Exchange">
          <li class="charters current">
          <#else>
          <li class="charters">
          </#if>
          <a href="exchange.action">Exchange</a></li>
	      
	      
      
      <#else>
      <#if current?? && current=="crew">
      <li class="crew current">
      <#else>
      <li class="crew">
      </#if>
      <a href="crewMember.action?id=${user.username}">Details</a></li>
      </#if>
      
      <li style="float:right; margin-right:20px;"><a href="${request.contextPath}/doLogin?logout=true">Log Out</a></li>
      <li style="float:right;"><a href="${request.contextPath}/account.action">Account</a></li>
    </ul>
  </div>
  <hr style="clear:left;display:none;"/>
    	<div id="breadcrumbs" style="border-top: 1px solid black;">
    	<#if breadcrumbs??>
    	<span>
    	<#list breadcrumbs as breadcrumb>
    		<#if breadcrumb_index gt 0>
    			&gt;
    		</#if>
    		<#if breadcrumb.url??>
    			<a href="${breadcrumb.url}">${breadcrumb.label}</a>
    		<#else>
    			${breadcrumb.label}
    		</#if>
    		
    	</#list>
    	</span>
    	<#else>
    	&nbsp;
    	</#if>
    	</div>
  <div id="content">
  	<#if notificationMessage??>
  		<div class="notification">
  			${notificationMessage}
  		</div>
  	</#if>
  	<#if errorMessage??>
  		<div class="error">
  			${errorMessage}
  		</div>
  	</#if>
    ${body}
  </div>
</div>
</body>	

<#else>
<head>
	<link rel="stylesheet" type="text/css" href="https://secure.i-tao.com/scripts/yui/build/reset-fonts-grids/reset-fonts-grids.css">
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/styles/default.css">
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/styles/forms.css">
	
	${head}
</head>
<body style="text-align:left;">
<#if domain??>
	<#if domain=="starlite">
		<img src="${request.contextPath}/images/starlite.png"/>
	<#else>
		<img src="${request.contextPath}/images/cri-logo.gif"/>
	</#if>
</#if>
${body}
</body>
</#if>
</html>