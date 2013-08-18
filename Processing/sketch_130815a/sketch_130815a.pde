PImage photo;
int rate = 20;
String[] location;
String[][] stuff;
int len;
int index = 0;
int count = 0;
float[] pTraffic;
float[] slope;

void setup(){
  size(994,722);
  frameRate(300);
  
  photo = loadImage("Map.png");
  location = loadStrings("LatLng.txt");
  
  len = location.length;
  stuff = new String[len][];
  pTraffic = new float[len];
  slope = new float[len];
  
  for(int i = 0; i < len; i++){
    String[] temp = split(location[i]," ");
    int script = parseInt(temp[0]);
    stuff[i] = loadStrings("Coor-" + script + ".txt");
    pTraffic[i] = 0.0;
  }
}

void draw() {
  if(count == rate){
    println("index: " + index);
    index++;
    count = 0;
  }
  if(index == 1440){
    return;
  }
  image(photo,0,0);
  
  String[] time = new String[2];
  String[] temp = split(stuff[0][index]," ");
  time[0] = temp[0];
  time[1] = temp[1];
  colorMode(RGB);
  fill(255,0,0);
  text(time[0],720,640);
  text(time[1],840,640);
  
  for(int i = 0; i < len; i++){
    drawOneLoc(i);
  }
  count++;
}

void drawOneLoc(int i){
  String[] temp = split(location[i]," ");
  int x = parseInt(temp[3]);
  int y = parseInt(temp[4]);
  stroke(0);
  strokeWeight(2);
  point(x,y);
  
  temp = split(stuff[i][index]," ");
  float traffic;
  if(count == 0){
    float proTraffic = parseFloat(temp[2]);
    traffic = log(proTraffic + 1);
    slope[i] = (traffic - pTraffic[i]) / rate;
    pTraffic[i] = traffic;
  }
  
  float rTraffic = pTraffic[i] - ((rate - 1 - count) * slope[i]);
  int radius = (int)(rTraffic * 10);
  /*
  int saturation = (int)((rTraffic - (int)rTraffic) * 100);
  int hue = (int)((rTraffic - (int)rTraffic) * 360);
  */
  int grayScale = (int)((1 - rTraffic / 12) * 255);
  
  noStroke();
  /*
  colorMode(HSB,360,100,100);
  fill(hue,saturation,saturation,100);
  */
  fill(grayScale,50);
  ellipse(x,y,radius,radius);
}
