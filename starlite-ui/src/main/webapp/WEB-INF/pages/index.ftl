<html>
<head>
	<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/2.4.1/build/reset-fonts-grids/reset-fonts-grids.css">
	<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/2.4.1/build/assets/skins/sam/skin.css">
	<link rel="stylesheet" type="text/css" href="styles/default.css">
	
	<script type="text/javascript" src="http://yui.yahooapis.com/2.4.1/build/utilities/utilities.js"></script>
	<script type="text/javascript" src="http://yui.yahooapis.com/2.4.1/build/container/container_core-min.js"></script> 
	<script type="text/javascript" src="http://yui.yahooapis.com/2.4.1/build/menu/menu-min.js"></script> 
	
	<script>
		YAHOO.util.Event.onContentReady("navMenu", function() {
			var oMenu = new YAHOO.widget.Menu("navMenu", {
				position:"static",
				lazyload:true
			});
			oMenu.render();
		});
	</script>
</head>
<body>
	<div id="doc3" class="yui-t2">
		<div id="hd"><h1>Starlite</h1></div>
		<div id="bd">
			<div id="yui-main">
				<div class="yui-b"><div class="yui-g">
					<!-- YOUR DATA GOES HERE -->
				</div></div>
			</div>
			<div class="yui-b">
				<div id="navMenu">
					<ul class="first-of-type">
						<li class="yuimenuitem">
							<a class="yuimenuitemlabel" href="#">
								Schedule
							</a>
						</li>
						<li class="yuimenuitem">
							<a class="yuimenuitemlabel" href="#">
								Aircraft
							</a>
						</li>
						<li class="yuimenuitem">
							<a class="yuimenuitemlabel" href="#">
								Charters
							</a>
						</li>
						<li class="yuimenuitem">
							<a class="yuimenuitemlabel" href="#">
								Crew
							</a>
						</li>
					</ul>
				</div>
			</div>
		</div>
   		<div id="ft"></div>
	</div>
</body>
</html>