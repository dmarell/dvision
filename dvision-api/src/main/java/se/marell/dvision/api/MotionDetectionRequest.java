/*
 * Created by Daniel Marell 15-06-23 18:34
 */
package se.marell.dvision.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MotionDetectionRequest {
    private String cameraName;
    private int minAreaSize;
    private int areaSizeThreshold;
    private List<ImageRectangle> detectionAreas;
    private ImageData image;
}
