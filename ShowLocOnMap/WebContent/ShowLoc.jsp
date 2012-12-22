<!DOCTYPE html>
<html>
	<head>
		<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
		<style type="text/css">
			#titleContainer {
				margin: 10px;
				height: 40px;
			}
			#queryContainer {
				position: absolute;
				top: 40px;
				left: 800px;
			}
			#mapContainer {
				margin: 10px;
				top: 60px;
				width: 720px;
				height: 480px;
			}
			#map {
				border-bottom: black 1px solid;
				border-left: black 1px solid;
				border-top: black 1px solid;
				border-right: black 1px solid;
				width: 100%;
				height: 100%;
			}
		</style>
		<script type="text/javascript"
			src="http://maps.google.com/maps/api/js?sensor=false"></script>
		<script src="js/jquery.min.js" type="text/javascript"></script>
		<script src="js/SqlQuery.js" type="text/javascript"></script>
		<script type="text/javascript">
			function initialize() {
				var mapOptions = {
						center: new google.maps.LatLng(39.90960456049752,116.3972282409668),
						zoom: 12,
						scaleControl: true,
						scaleControlOptions: {
							position: google.maps.ControlPosition.BOTTOM_LEFT
						},
						mapTypeId: google.maps.MapTypeId.ROADMAP
				};
				var map = new google.maps.Map(document.getElementById("map"),mapOptions);
			}
		</script>
	</head>
	<body onload="initialize()">
		<div id="titleContainer">
			<h2>Location and Track</h2>
		</div>
		<div id="queryContainer">
			please input a sql query:<br/><br/>
			<textarea cols="40" rows="8" id="sqlQuery"></textarea><br/><br/>
			<form name="choose_sqlQuery">
				show all location?<input type="radio" name="showAllLoc_sqlQuery" value="yes" checked/>yes
				<input type="radio" name="showAllLoc_sqlQuery" value="no"/>no&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" value="commit" onClick="SqlQuery()"/><br/>
			</form>
			<form name="lat_lng_10_sqlQuery">
			</form>
			<form name="trace_sqlQuery">
			</form>
		</div>
		<form name="lat_lng">
		</form>
		<form name="googleMap">
			<div id="mapContainer">
				<div id="map"></div>
			</div>
		</form>
	</body>
</html>