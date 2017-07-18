/*
 * Created by Daniel Marell 15-06-23 18:50
 */
package se.marell.dvision.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageAnalyzeResponse {
    private long timestamp;
    private ImageSize imageSize;
    private Map<String, List<ImageRectangle>> areas;
}
