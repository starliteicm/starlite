<html>
<head>
<title>test xml</title>
	<script>
		var schema = [
			"id", "firstName", "lastName", "contactNumber", "non-existent"
		];
		
		var dataSource = new ITAO.util.XmlDataSource("http://localhost:8080/starlite-ui/services/crew", {schema:schema, resultNode:"crewmember"});
		
		dataSource.sendRequest("", null, null);		
		/*
		var callback = {
			success: function(o) {
				var crewMember = parser.parseList("crewmember", o.responseXML)[0];
				alert(crewMember.id + crewMember.firstName + crewMember.lastName);
			}
		};
		
		YAHOO.util.Connect.asyncRequest("GET", "http://localhost:8080/starlite-ui/services/crew", callback);
		*/
	</script>

</head>
<body>
	
</body>
</html>