/*
 * Created by Daniel Marell 2017-02-10.
 */
package se.marell.dvision.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabeledRectangle {
    @NonNull
    private ImageRectangle rectangle;
    private String label;
}
