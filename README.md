# DVISION

Spring Boot REST server implementing a motion detector for network cameras. Uses an image motion detection algorithm
from JavaCV/OpenCV.

### Release notes
* Version 2.1.0 - 2016-03-28
   * Simplified client API
* Version 2.0.0 - 2016-03-28
   * Heavily simplified: Do not store images, do not access cameras - client uploads all images. Motion detection result
    is filtred by client (areaSizeThreshold, minAreaSize, detectionAreas). Added face detection.
* Version 1.0.6 - 2016-03-21
   * Updated dvesta in order to get log-level endpoints
* Version 1.0.5 - 2016-03-16
   * Added basic authentication to server and client
   * Added possibility to set parameters via environment variables: apiuser, apipassword apibaseurl outdirectory
   * Renamed config parameter prefix from motion-detection-service to dvision
* Version 1.0.1 - 2015-07-19
   * First release

## API usage

Call image-analyze-request along with the image to analyze.
Next, call the same request again with another image. The response contains motion between the two requests and detected
faces.

## Build and run

Build with:
   
``` 
mvn install
``` 

Run integration tests:

``` 
mvn -Pint-test verify
``` 

Run the main method in the Application. It starts an embedded Tomcat on port 8080.

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

HTTP POST http://localhost:14562/image-analyze-request/cam1

Request body is a multipart request with the image file.

Response body:

``` 
{
  "timestamp": 1435161891,
  "imageSize": {
    "width": 1024,
    "height": 384
  },
  "motionAreas": [
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
    ...
  ]
}
``` 
