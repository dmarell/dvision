# DVISION

Two modules:

## dvision-api

API classes shared with client and servers.

## dvision-client

Client module. Implementes the REST client interface.

### Release notes
* Version 3.0.0 - 2017-07-17
   * Moved dvision-server to it's own repo
   * Generalized the API. The new API known about areas and tags (not specifically "motion" and "faces"). The tag can represent "motion", "face" or something else.
* Version 2.2.9 - 2017-07-17
   * Addressing memory leaks in native memory, maybe in javacv
* Version 2.2.6 - 2017-07-13
   * Upgraded Spring Boot to 1.5.4.
* Version 2.2.2 - 2016-04-04
   * Upgraded JavaCV to 1.3.1 and restored face detection.
* Version 2.2.1 - 2016-04-04
   * Removed face detection until I figure out why it woes not work in docker container.
* Version 2.2.0 - 2016-03-28
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

### Maven usage

```
    <repositories>
      <repository>
        <id>marell</id>
        <url>http://artifactory.caglabs.se/artifactory/libs-release</url>
      </repository>
    </repositories>
...
    <dependency>
        <groupId>se.marell.dvision</groupId>
        <artifactId>dvision-client</artifactId>
        <version>2.2.0</version>
    </dependency>
```


## API usage

Call image-analyze-request along with the image to analyze.
Next, call the same request again with another image. The response contains motion between the two requests and detected
faces.

## Build and run

Build and deploy maven modules:
   
``` 
mvn deploy
``` 
