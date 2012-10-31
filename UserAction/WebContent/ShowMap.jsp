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
      function getLocFromDate() {
    	  cleanInfo();
    	  var imsi = document.getElementById("datepicker1").value;
    	  var begin = document.getElementById("datepicker2").value;
    	  var end = document.getElementById("datepicker3").value;
    	  getLocData(imsi,begin,end);
      }
    </script>
  </head>
  <h2>track</h2>
    imsi:<input type="text" id="datepicker1" name="imsi">
    </input>
    begin(yyyy-mm-dd):<input type="text" id="datepicker2" name="begin">
    </input>
    end(yyyy-mm-dd):<input type="text" id="datepicker3" name="end">
    </input>
    <input type='button' value='search' onClick='return getLocFromDate();' class="btn1_mouseout"
    onmouseover="this.className='btn1_mouseover'" onmouseout="this.className='btn1_mouseout'"/>
  <br>
  <div id="mapcontainer">
  <div id="map">
  </div>
  </div>
  <body onload="initialize()">
    <div id="map_canvas" style="width:100%; height:100%"></div>
  </body>
</html>