# DVISION

Spring Boot REST server implementing a motion detector for network cameras. Uses an image motion detection algorithm
from JavaCV/OpenCV.

### Release notes
* Version 2.0.0 - 2016-03-24
   * Heavily simplified: Do not store images, do not access cameras - client uploads all images
* Version 1.0.6 - 2016-03-21
   * Updated dvesta in order to get log-level endpoints
* Version 1.0.5 - 2016-03-16
   * Added basic authentication to server and client
   * Added possibility to set parameters via environment variables: apiuser, apipassword apibaseurl outdirectory
   * Renamed config parameter prefix from motion-detection-service to dvision
* Version 1.0.1 - 2015-07-19
   * First release

## API usage

Call requestMotionDetection with information of camera name, thresholds and areas of interest in the image, and the image.
Next, call the same request again with another image. The response is motion between the two requests, or null if
no motion was detected.

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

## Example request/response

HTTP POST http://localhost:14562/motion-detection-request

Request body:
``` 
{ 
  "name": "cam1",
  "minAreaSize": 100,
  "areaSizeThreshold": 10,
  "detectionAreas": [
    {
      "x": 0,
      "y": 0,
      "width": 1024,
      "height": 385
    }
  ],
  "imageData": {
    "mediaType": "image/jpeg",
    "base64EncodedData": "..."
  }
}
``` 

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
    },
    {
      "x": 107,
      "y": 354,
      "width": 10,
      "height": 16,
      "area": 160
    },
    {
      "x": 136,
      "y": 345,
      "width": 13,
      "height": 4,
      "area": 52
    },
    {
      "x": 120,
      "y": 346,
      "width": 14,
      "height": 14,
      "area": 196
    },
    {
      "x": 990,
      "y": 343,
      "width": 6,
      "height": 5,
      "area": 30
    },
    {
      "x": 147,
      "y": 333,
      "width": 4,
      "height": 8,
      "area": 32
    },
    {
      "x": 168,
      "y": 328,
      "width": 13,
      "height": 4,
      "area": 52
    },
    {
      "x": 154,
      "y": 326,
      "width": 10,
      "height": 4,
      "area": 40
    },
    {
      "x": 148,
      "y": 322,
      "width": 5,
      "height": 5,
      "area": 25
    },
    {
      "x": 164,
      "y": 317,
      "width": 6,
      "height": 3,
      "area": 18
    },
    {
      "x": 175,
      "y": 310,
      "width": 6,
      "height": 6,
      "area": 36
    },
    {
      "x": 374,
      "y": 272,
      "width": 11,
      "height": 13,
      "area": 143
    },
    {
      "x": 318,
      "y": 253,
      "width": 8,
      "height": 3,
      "area": 24
    }
  ],
  "imageData": {
    "mediaType": "image/png",
    "base64EncodedData": "..."
  }
}
``` 
