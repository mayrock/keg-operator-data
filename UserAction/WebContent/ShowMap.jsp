<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <style type="text/css">
      #mapContainer {
        margin: 10px;
        width: 720px;
        height: 480px;
      }
       #inputContainer{
        position: absolute;
        top: 60px;
        left: 800px;
      }
      #locContainer {
        position: absolute;
        top: 120px;
        left: 800px;
        margin: 10px;
        width: 300px;
        height: 480px;
        border-bottom: black 1px solid;
        border-left: black 1px solid;
        border-top: black 1px solid;
        border-right: black 1px solid;
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
      src="http://maps.google.com/maps/api/js?sensor=false">
    </script>
    <script src="js/showTraceByImsi.js" type="text/javascript">
    </script>
    <script src="js/showTraceByLoc.js" type="text/javascript">
    </script>
    <script src="js/jquery.min.js" type="text/javascript">
    </script>
    <script type="text/javascript">
      function initialize() {
    	 Init();
      }
    </script>
  </head>
  <body onload="initialize()">
    <form name="getImsi">
      <h2>User's track</h2>
      imsi:<input type="text" size="20" maxlength="15" value="460028498058743" id="datepicker1"/>
      begin date:<input type="text" size="12" maxlength="10" value="2012-09-18" id="datepicker2"/>
      end date:<input type="text" size="12" maxlength="10" value="2012-09-24" id="datepicker3"/>
      <input type="button" value="search" onClick="getLocFromDates()"/><br/>
      date input as yyyy-mm-dd<br/>
    </form>
    <form name="dateForm">
    </form>
   <div id="inputContainer">
    <form name="getLoc">
      location a:<input type="text" size="15" maxlength="11" value="18421_18943" id="datepicker4"/>
      location b:<input type="text" size="15" maxlength="11" value="18421_44683" id="datepicker5"/><br/>
      hour:<input type="text" size="5" maxlength="2" value="8" id="datepicker6"/>
      user count:<input type="text" size="5" id="datepicker7"/>
      <input type="button" value="search" onClick="getMsgFromLoc()"/><br/></div>
    </form><div id="locContainer">
    <form name="adjLoc">
    </form></div>
    <form name="googleMap">
      <div id="mapContainer">
        <div id="map">
        </div>
      </div>
    </form>
  </body>
</html>