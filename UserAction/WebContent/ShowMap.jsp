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
    	  var imsi=document.getElementById("datepicker1").value;
    	  var year=document.getElementById("datepicker2").value;
    	  var month=document.getElementById("datepicker3").value;
    	  var date=document.getElementById("datepicker4").value;
    	  getLocData(imsi,year,month,date);
      }
    </script>
  </head>
  <h2>track</h2>
    imsi:<input type="text" id="datepicker1" name="imsi">
    </input>
    year:<input type="text" id="datepicker2" name="year">
    </input>
    month:<input type="text" id="datepicker3" name="month">
    </input>
    date:<input type="text" id="datepicker4" name="date">
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