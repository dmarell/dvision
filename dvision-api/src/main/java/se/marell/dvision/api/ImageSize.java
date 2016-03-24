/*
 * Created by Daniel Marell 15-01-06 17:11
 */
package se.marell.dvision.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageSize {
    private int width;
    private int height;
}
