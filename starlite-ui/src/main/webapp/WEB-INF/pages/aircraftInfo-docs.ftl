<#include "/starlite.ftl">
<html>
<head>
  <title>${aircraft.ref!}</title>
  
  <link rel="stylesheet" type="text/css" href="styles/forms.css">
  <link rel="stylesheet" type="text/css" href="styles/jquery.autocomplete.css">
  <script type="text/javascript" src="js/jquery.autocomplete.multi.js"></script>
  
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
				<legend>Upload Document</legend>
				<div class="fm-opt">
					<label for="document" style="width:40px;">File: </label>
					<input name="document" id="document" type="file"/>
				</div>
				<!--div class="fm-opt">
					<label for="tags" style="width:40px;">Tags: </label>
					<input name="tags" id="tags" type="text"/>
				</div-->
				<div class="fm-opt">
					<label for="tags" style="width:40px;">Type: </label>
					<select name="tags">
						<option value=""> </option>
						<option value="CoA certificate">CoA</option>
						<option value="CoR certificate">CoR</option>
						<option value="operating certificate">A/C Operating Certificate</option>
						<option value="primary_co-insured insurance">Insurance (Primary or Co-Insured)</option>
						<option value="hull insurance">All Risk (Hull) Insurance</option>
						<option value="repatriation insurance">Repatriation Insurance</option>
						<option value="aircraft misc">Other</option>
					</select>
				</div>
			</fieldset>
			</div>
			<hr class="clear"/>
			<button class="smooth" style="margin-left:40px;" type="submit"><img src="images/icons/arrow_up.png"/>Upload</button>
			<hr class="clear"/>
			<input type="hidden" name="folder" value="/aircraft/${aircraft.ref}"/>
			<input type="hidden" name="shallReturn" value="yes"/>
		</form>

		<#list docs as doc>
		<#assign bookmark=doc.bookmark/>
		<#assign isManager=user.hasPermission("ManagerView")/>
		<div style="margin-top:20px;">
			<h3><a href='${request.contextPath}${bookmark.url!}'>${bookmark.name}</a></h3>
			<#if isManager>
			<span style="font-size:90%;">Tags: 
				<#list bookmark.tags as tag>
					<a href="bookmarkSearch.action?q=tag:${tag.tag}" class='tag'>${tag.tag}</a>
				</#list>
			</span>
			</#if>
		</div>
	</#list>
	</div>
	<script>
		$(document).ready(function() {
			$("#tags").autocompleteArray(${tagArray}, {delay:10, minChars:1, matchSubset:1, autoFill:true, maxItemsToShow:10, selectFirst:true});
		});
	</script>
</body>
</html>
