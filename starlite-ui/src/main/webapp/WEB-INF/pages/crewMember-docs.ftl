<#include "/starlite.ftl">
<html>
<head>
  <title>${crewMember.personal.firstName!} ${crewMember.personal.lastName!}</title>
  
  <link rel="stylesheet" type="text/css" href="styles/forms.css">
  <link rel="stylesheet" type="text/css" href="styles/jquery.autocomplete.css">
  <script type="text/javascript" src="js/jquery.autocomplete.multi.js"></script>
  <@enableHelp/>
  <script>
  		function showUploadPart() {
  			$("#uploadForm").css("display","block");
  			$("#uploadFormShower").css("display","none");
  		}
  </script>
</head>

<body>
	<@subTabs/>
	<div style="width:1050px; border:1px solid #aaa; padding:10px; clear:left;">
		<!--form method="GET" action="${request.requestURL}" style="border:none;">
			<span style="font-size:140%;">Search </span><input type="text" name="q" value="${q!}"/><input type="submit" value="Search"/>
		</form-->
		<a id="uploadFormShower" href="#" onclick="showUploadPart();">Upload New Document</a>
		<form id="uploadForm" class="smart" style="display:none; border:none;" action="documents!upload.action"
       		enctype="multipart/form-data"
       		method="post">
       		<div style="float:left; width: 500px;">
			<fieldset>
				<legend>Upload Additional Document<span>&nbsp;</span>  
				<img class="tooltip" title=" - There is a 5MB Size Limit to documents" style="cursor:help;position:relative;float:right;" src="images/icons/info.png"/>
                </legend>
				<div class="fm-opt">
					<label for="document" style="width:40px;">File: </label>
					<input name="document" id="document" type="file"/>
				</div>
				<!--div class="fm-opt">
					<label for="tags" style="width:40px;">Tags: </label>
					<input name="tags" id="tags" type="text"/>
				</div-->
				<div class="fm-opt">
					<label for="tags" style="width:40px;">Tag: </label>
					<input type="text" name="tags" value="" />
				</div>
			</fieldset>
			</div>
			<hr class="clear"/>
			<button class="smooth" style="margin-left:40px;" type="submit"><img src="images/icons/arrow_up.png"/>Upload</button>
			<hr class="clear"/>
			<input type="hidden" name="folder" value="/crew/${id}"/>
			<input type="hidden" name="shallReturn" value="yes"/>
		</form>
		<#list docs as doc>
		<#assign bookmark=doc.bookmark/>
		<#assign isManager=user.hasPermission("ManagerView")/>
		
		<#assign hide=0/>
		<#list bookmark.tags as tag>
		  <#if tag.tag == "CRM"><#assign hide=0/></#if>
		  <#if tag.tag == "DG"><#assign hide=0/></#if>
		  <#if tag.tag == "HUET"><#assign hide=0/></#if>
		  <#if tag.tag == "HEMS"><#assign hide=0/></#if>
		  <#if tag.tag == "additionalCert"><#assign hide=0/></#if>
		  <#if tag.tag == "LPC"><#assign hide=0/></#if>
		  <#if tag.tag == "OPC"><#assign hide=0/></#if>
		  <#if tag.tag == "opsManual"><#assign hide=0/></#if>
		  <#if tag.tag == "annualTechManual"><#assign hide=0/></#if>
		  <#if tag.tag == "${id}"><#assign hide=0/></#if>
		  <#if tag.tag == "medical"><#assign hide=0/></#if>
		  <#if tag.tag == "licence"><#assign hide=0/></#if>
		  <#if tag.tag == "photo"><#assign hide=0/></#if>
		  <#if tag.tag == "flighthours"><#assign hide=0/></#if>
		  <#if tag.tag.indexOf("passport") != -1><#assign hide=0/></#if>
		</#list>
		
		<#if hide == 0>
		<div style="margin-top:20px;">    
			<h3><a href='${request.contextPath}${bookmark.url!}'>${bookmark.name}</a><#if folder.canWrite(user)> <a onclick="return confirm('Are you sure you wish to delete this document?');" href="documents!delete.action?path=${bookmark.bookmarkedId}">&nbsp;&nbsp;&nbsp;[delete]</a></#if></h3>
			<#if isManager>
			<span style="font-size:90%;">Tags: 
				<#list bookmark.tags as tag>
					<a href="bookmarkSearch.action?q=tag:${tag.tag}" class='tag'>${tag.tag}</a>
				</#list>
			</span>
			</#if>
		</div>
		</#if>
		
	</#list>
	</div>
	<script>
		$(document).ready(function() {
			$("#tags").autocompleteArray(${tagArray}, {delay:10, minChars:1, matchSubset:1, autoFill:true, maxItemsToShow:10, selectFirst:true});
		});
	</script>
</body>
</html>
