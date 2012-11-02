<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <style type="text/css">
      #mapcontainer {
        margin: 10px;
        width: 720px;
        height: 480px;
      }
      #map {
        border-bottom: red 1px solid;
        border-left: green 1px solid;
        border-top: black 1px solid;
        border-right: blue 1px solid;
        width: 100%;
        height: 100%;
      }
    </style>
    <script type="text/javascript"
      src="http://maps.google.com/maps/api/js?sensor=false">
    </script>
    <script type="text/javascript" src="js/GoogleMap.js">
    </script>
    <script src="js/jquery.min.js" type="text/javascript">
    </script>
    <script type="text/javascript">
      function initialize() {
    	  var mapOptions = {
    			  center: new google.maps.LatLng(40.003834809598516,116.3263213634491),
    			  zoom: 16,
    			  mapTypeId: google.maps.MapTypeId.ROADMAP
    	  };
    	  var map = new google.maps.Map(document.getElementById("map"),mapOptions);
      }
    </script>
  </head>
  <h2>User's track</h2>
    imsi:<input type="text" size="20" maxlength="15" value="460028498058743" id="datepicker1"/>
    begin date:<input type="text" size="12" maxlength="10" value="2012-09-18" id="datepicker2"/>
    end date:<input type="text" size="12" maxlength="10" value="2012-09-24" id="datepicker3"/>
    <input type="button" value="search" onClick="getLocFromDate()"/>
    <br/>
    date input as yyyy-mm-dd<br/>
    <form name="orderForm">
      <input type="checkbox" name="day1" onClick="getLocByData1()"/>day1
      <input type="checkbox" name="day2" onClick="getLocByData2()"/>day2
      <input type="checkbox" name="day3" onClick="getLocByData3()"/>day3
      <input type="checkbox" name="day4" onClick="getLocByData4()"/>day4
      <input type="checkbox" name="day5" onClick="getLocByData5()"/>day5
      <input type="checkbox" name="day6" onClick="getLocByData6()"/>day6
      <input type="checkbox" name="day7" onClick="getLocByData7()"/>day7
    </form>
    <div id="mapcontainer"/>
    <div id="map"/>
  <body onload="initialize()">
  </body>
</html>