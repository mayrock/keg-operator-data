var GMap = {};

GMap.bound = function(value,min,max){
	if(min != null){
		value = Math.max(value,min);
	}
	if(max != null){
		value = Math.min(value,max);
	}
	
	return value;
}

GMap.degreesToRadians = function(deg){
	return deg * (Math.PI / 180);
}

GMap.getWorldCoor = function(lat,lng){
	var worldCoor = new Object;
	var pixelsPerLonDegree = 256 / 360;
	var pixelsPerLonRadian = 256 / (2 * Math.PI);
	worldCoor.x = 128 + lng * pixelsPerLonDegree;
	var siny = GMap.bound(Math.sin(GMap.degreesToRadians(lat)),-0.9999,0.9999);
	worldCoor.y = 128 + 0.5 * Math.log((1 + siny) / (1 - siny)) * -pixelsPerLonRadian;
	
	return worldCoor;
};

GMap.getPixelCoor = function(worldCoor,zoom){
	var numTiles = 1 << zoom;
	var pixelCoor = new Object;
	pixelCoor.x = worldCoor.x * numTiles;
	pixelCoor.y = worldCoor.y * numTiles;
	
	return pixelCoor;
}

GMap.getTileCoor = function(pixelCoor){
	var tileCoor = new Object;
	tileCoor.x = Math.floor(pixelCoor.x / 256);
	tileCoor.y = Math.floor(pixelCoor.y / 256);
	
	return tileCoor;
}