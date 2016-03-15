# DVISION

Spring Boot REST server implementing a motion detector for network cameras. Uses an image motion detection algorithm
from JavaCV/OpenCV.

### Release notes
* Version 1.0.5 - 2016-03-16
   * Added basic authentication to server and client.
   * Added possibility to set parameters via environment variables: apiuser, apipassword apibaseurl outdirectory
   * Renamed config parameter prefix from motion-detection-service to dvision.
* Version 1.0.1 - 2015-07-19
   * First release.

## API usage

Call requestMotionDetection with information of camera URL, thresholds and areas of interest in the image.
The server starts polling the camera with the requested interval. Call requestMotionDetection for example once a second
in order to get information on in what areas(s) in the image motion was detected. As long as you continue call 
requestMotionDetection the server will continue to capture and analyze images from this camera.

When you stop calling requestMotionDetection the camera will be garbage collected in 10 seconds. If
requestMotionDetection is called again after this the camera will be unknown and Bad request will be received.
Call requestMotionDetection in order to start capturing again.

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

Profile "local" is default:
``` 
java -jar target/dvision-server-1-SNAPSHOT.jar
``` 

Profile "prod":
``` 
java -Dspring.profiles.active=prod -jar target/dvision-server-1-SNAPSHOT.jar
``` 

Configuration for different profiles are in application.yaml.


Captured images with detected motion are saved in directory "cams". Areas with motion are marked with green 
rectangles.

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
