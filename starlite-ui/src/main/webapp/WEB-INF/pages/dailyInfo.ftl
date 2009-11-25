<html>
<head>
	<script type="text/javascript" src="js/itao/itao.js"></script>
	<script type="text/javascript" src="js/yui_2.4.1/yui/build/utilities/utilities.js"></script> 
	<link rel="stylesheet" type="text/css" href="js/yui_2.4.1/yui/build/reset-fonts-grids/reset-fonts-grids.css">
	<link rel="stylesheet" type="text/css" href="styles/slotFiller.css">
	
	<!--aggregate file path for YUI Sam Skin CSS for UI controls--> 
	<link rel="stylesheet" type="text/css" href="js/yui_2.4.1/yui/build/assets/skins/sam/skin.css">	
	<link rel="stylesheet" type="text/css" href="styles/default.css">
</head>
<body class="yui-skin-sam">
	<div id="doc3" class="yui-t3">
	   <div id="hd"><h1>Starlite</h1></div>
	   <div id="bd">
		<div id="yui-main">
		<div class="yui-b"><div class="yui-g">
			<h2>Daily Info - ${aircraftRef!"ABE249"}</h2>
			<br/>
			<h3>Documents</h3>
			<ul>
				<form>
					<label>Upload Captain's Log</label>&nbsp;<input type="file"/><input type="submit" value="Upload"/>
				</form>
			</ul>
		</div>
		</div>
		</div>
		<div class="yui-b">
			<div id="leftList" style="height:600px; overflow:auto;">
				<div id="aircraft">
					<div id="ZS-RUH" class="odd" onclick="location.href='dailyInfo.action?aircraftRef=ZS-RUH'" onmouseover="YAHOO.util.Dom.addClass('ZS-RUH', 'hover');" onmouseout="YAHOO.util.Dom.removeClass('ZS-RUH', 'hover');">
						<h3>ZS-RUH</h3>
						<p>On Charter (SP001)</p>
					</div>
					
					<div id="ZS-HIZ" class="odd" onclick="location.href='dailyInfo.action?aircraftRef=ZS-HIZ'" onmouseover="YAHOO.util.Dom.addClass('ZS-HIZ', 'hover');" onmouseout="YAHOO.util.Dom.removeClass('ZS-HIZ', 'hover');">
						<h3>ZS-HIZ</h3>
						<p>On Charter (SU001)</p>
					</div>
					
					<div id="ZS-HJA" class="odd unavailable" onclick="location.href='dailyInfo.action?aircraftRef=ZS-HJA'" onmouseover="YAHOO.util.Dom.addClass('ZS-HJA', 'hover');" onmouseout="YAHOO.util.Dom.removeClass('ZS-HJA', 'hover');">
						<h3>ZS-HJA</h3>
						<p>Maintenance</p>
					</div>
					
					<div id="ZS-RWO" class="odd available" onclick="location.href='dailyInfo.action?aircraftRef=ZS-RWO'" onmouseover="YAHOO.util.Dom.addClass('ZS-RWO', 'hover');" onmouseout="YAHOO.util.Dom.removeClass('ZS-RWO', 'hover');">
						<h3>ZS-RWO</h3>
						<p>In Hangar</p>
					</div>
					
					<div id="ZS-RNK" class="odd available" onclick="location.href='dailyInfo.action?aircraftRef=ZS-RNK'" onmouseover="YAHOO.util.Dom.addClass('ZS-RNK', 'hover');" onmouseout="YAHOO.util.Dom.removeClass('ZS-RNK', 'hover');">
						<h3>ZS-RNK</h3>
						<p>In Hangar</p>
					</div>
				</div>
			</div>
		</div>
	   <div id="ft">i-Tao: Web Based Solutions</div>
	</div>
	<div style="position:absolute; top:5px; left:330px; font-size:122%;">
		<a href="roster.action">Roster View</a> 
	</div>
</body>
</html>