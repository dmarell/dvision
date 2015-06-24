# DVISION

Spring Boot REST server implementing a motion detector for network cameras. Uses JavaCV/OpenCV. It handles
virtually any number of network cameras in parallell.

## API usage

Call requestMotionDetection with information of camera URL, thresholds and areas of interest in the image.
The server starts polling the camera with the requested interval. Call requestMotionDetection for example once a second
in order to get information on in what areas(s) in the image motion was detected. As long as you continue call 
requestMotionDetection the server will continue to capture and analyze images from this camera.

When you stop calling requestMotionDetection the camera will be garbage collected in 10 seconds. If you call
requestMotionDetection again after this the camera will be unknown and Bad request will be received. In order to 
start capturing this camera again requestMotionDetection has to be called again. 

## Build and run

Build with:
   
``` 
mvn install
``` 

Run integration tests:

``` 
mvn -Pint-test verify
``` 

Run the main method in the Application. It starts an embedded Tomcat on port 14562.

``` 
java -jar target/dvision-1-SNAPSHOT.jar
``` 

Captured images with detected motion are saved in directory "cams". Areas with motion are marked with green rectangles.

## Example request/response

HTTP POST http://localhost:14562/motiondetectionrequest

Request body:
``` 
{
  "camera": {
    "name": "cam1",
    "url": "http://83.140.123.181/ImageHarvester/Images/556-slussen_panorama_1.jpg",
    "username": "user1",
    "password": "pass1"
  },
  "minAreaSize": 100,
  "areaSizeThreshold": 10,
  "detectionAreas": [
    {
      "x": 0,
      "y": 0,
      "width": 1024,
      "height": 385
    }
  ]
}
``` 

HTTP GET http://localhost:14562/motiondetectionresponse/cam1?since=0

Response body:

``` 
{
  "timestamp": 1435161891,
  "imageSize": {
    "width": 1024,
    "height": 384
  },
  "areas": [
    {
      "x": 946,
      "y": 370,
      "width": 15,
      "height": 6,
      "area": 90
    },
    {
      "x": 98,
      "y": 359,
      "width": 3,
      "height": 9,
      "area": 27
    }
  ]
}
``` 
