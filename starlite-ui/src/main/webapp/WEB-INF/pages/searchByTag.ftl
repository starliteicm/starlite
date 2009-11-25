<body>
	<h1>Search Results</h1>
	
	<#if bookmarks.empty>
		<h2>There were no matches</h2>
	<#else>
		<#list bookmarks as bookmark>
			<div style="margin-bottom:20px;">
				<h3>${bookmark.bookmarkType.name}: <a href='${request.contextPath}${bookmark.url!}'>${bookmark.name}</a></h3>
				<span style="font-size:90%;">Tags: 
					<#list bookmark.tags as tag>
						<a href="searchByTag.action?tags=${tag.tag}">${tag.tag}</a>
					</#list>
				</span>
			</div>
		</#list>
	</#if>
</body>