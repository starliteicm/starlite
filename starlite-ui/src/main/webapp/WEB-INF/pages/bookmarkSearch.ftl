<html>
<head>
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/styles/mktree.css">
	<script type="text/javascript" src="${request.contextPath}/js/tree.js"></script>
  	<script type="text/javascript" src="${request.contextPath}/js/jquery-1.2.3.min.js"></script>
  	<link rel="stylesheet" type="text/css" href="${request.contextPath}/styles/jquery.autocomplete.css">
  	<script type="text/javascript" src="${request.contextPath}/js/jquery.autocomplete.multi.js"></script>
  	
  	<script>
  		function showUploadPart() {
  			$("#uploadForm").css("display","block");
  			$("#uploadFormShower").css("display","none");
  		}
  		function hideUploadPart() {
  			$("#uploadForm").css("display","none");
  			$("#uploadFormShower").css("display","block");
  		}
  </script>
</head>
<body>
	<form method="GET" action="${request.requestURL}">
	<span style="font-size:140%;">Search </span><input type="text" id="searchBox" name="q" value="${q!}" style="width:400px;" /><input type="submit" value="Search"/>
	</form>
	<div style="font-size:150%;">
	${cloudHtml}
	</div>
	<#if q??>
		<#if bookmarks.empty>
			<h3>There were no matches</h2>
		<#else>
			<#list bookmarks as bookmark>
				<div style="margin-top:20px;">
					<h3>${bookmark.bookmarkType.name}: <a href='${request.contextPath}${bookmark.url!}' target="_blank">${bookmark.name!"untitled"}</a></h3>
					<div style="font-size:100%;">Location: ${bookmark.bookmarkedId}</div>
					<span style="font-size:90%;">Tags: 
						<#list bookmark.tags as tag>
							<a href="bookmarkSearch.action?q=tag:${tag.tag}" class='tag'>${tag.tag}</a>
						</#list>
					</span>
				</div>
			</#list>
		</#if>
	<#else>
		<#assign currentFolder = root/>
		
		<#macro treeSegment folder path rootPath>
			<#if folder.subFolders?size &gt; 0 || folder.docs?size &gt; 0>
			<ul>
				<#list folder.subFolders as subFolder>
					<li>${subFolder.path}<@treeSegment subFolder path+folder.path+'/' rootPath/></li>
				</#list>
				<#list folder.docs as doc>
					<li><a href="${rootPath}${path}${folder.path}/${doc.name}" target="_blank">${doc.name}</a><#if folder.canWrite(user)> <a href="${request.contextPath}/documents!delete.action?path=${path}${folder.path}/${doc.name}">x</a></#if></li>
				</#list>
			</ul>
			</#if>
		</#macro>
			
			<ul class="mktree" style="margin-top:20px;">
				<#list root.subFolders as subFolder>
					<li>${subFolder.path}<@treeSegment subFolder '/' '${request.contextPath}/documents'/></li>
				</#list>
			</ul>
			<br/>
			<a id="uploadFormShower" href="#" onclick="showUploadPart();">Upload General Document</a>
		<form id="uploadForm" class="smart" style="display:none; border:none;" action="documents!upload.action"
       		enctype="multipart/form-data"
       		method="post">
       		<div style="float:left; width: 500px;">
			<fieldset>
				<legend>Upload Document <span style="font-size:60%;"><a href="#" onclick="hideUploadPart();">[hide]</a></span></legend>
				<div class="fm-opt">
					<label for="document" style="width:40px;">Folder: </label>
					<select name="folder" >
					<option SELECTED value="/general">general</option>
					<option SELECTED value="/financial">financial</option>
					<option SELECTED value="/distribution">distribution</option>
					</select>
				</div>
				<div class="fm-opt">
					<label for="document" style="width:40px;">File: </label>
					<input name="document" id="document" type="file"/>
				</div>
				<div class="fm-opt">
					<label for="tags" style="width:40px;">Tags: </label>
					<input name="tags" id="tags" type="text"/>
				</div>
			</fieldset>
			</div>
			<hr class="clear"/>
			<button class="smooth" style="margin-left:40px;" type="submit"><img src="images/icons/arrow_up.png"/>Upload</button>
			<hr class="clear"/>
			<input type="hidden" name="shallReturn" value="yes"/>
		</form>
	</#if>
	<script>
		$(document).ready(function() {
			$("#searchBox").autocompleteArray(${tagArray}, {delay:10, minChars:1, matchSubset:1, autoFill:true, maxItemsToShow:10, prefix:"tag:", selectFirst:true});
			$("#tags").autocompleteArray(${tagArray}, {delay:10, minChars:1, matchSubset:1, autoFill:true, maxItemsToShow:10, selectFirst:true});
		});
	</script>
</body>
</html>