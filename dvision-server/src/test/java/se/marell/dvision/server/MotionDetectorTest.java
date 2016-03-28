/*
 * Created by Daniel Marell 15-01-06 13:53
 */
package se.marell.dvision.server;

import org.junit.Test;
import se.marell.dvision.api.ImageRectangle;
import se.marell.dvision.api.ImageRectangleUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MotionDetectorTest {

    @Test
    public void testCam1Event1() throws Exception {
        MotionDetector m = new MotionDetector();
        testCamEvent(1000, 100, new ImageRectangle(0, 0, 640, 480), m, "testdata/motiondetector/cam1/event1",
                Arrays.asList(
                        "cam1-00070.png: n",
                        "cam1-00071.png: n",
                        "cam1-00072.png: n",
                        "cam1-00073.png: n",
                        "cam1-00074.png: n",
                        "cam1-00075.png: n",
                        "cam1-00076.png: n",
                        "cam1-00077.png: n",
                        "cam1-00078.png: n",
                        "cam1-00079.png: n",
                        "cam1-00080.png: n",
                        "cam1-00081.png: n",
                        "cam1-00082.png: n",
                        "cam1-00083.png: y",
                        "cam1-00084.png: y",
                        "cam1-00085.png: y",
                        "cam1-00086.png: y",
                        "cam1-00087.png: y",
                        "cam1-00088.png: y",
                        "cam1-00089.png: y",
                        "cam1-00090.png: y",
                        "cam1-00091.png: y",
                        "cam1-00092.png: n",
                        "cam1-00093.png: n",
                        "cam1-00094.png: n",
                        "cam1-00095.png: n",
                        "cam1-00096.png: n",
                        "cam1-00097.png: n",
                        "cam1-00098.png: n",
                        "cam1-00099.png: n"
                ));
    }

    @Test
    public void testCam1Event2() throws Exception {
        MotionDetector m = new MotionDetector();
        testCamEvent(1000, 100, new ImageRectangle(0, 0, 640, 480), m, "testdata/motiondetector/cam1/event2",
                Arrays.asList(
                        "cam1-00148.png: n",
                        "cam1-00149.png: n",
                        "cam1-00150.png: n",
                        "cam1-00151.png: y",
                        "cam1-00152.png: y",
                        "cam1-00153.png: y",
                        "cam1-00154.png: y",
                        "cam1-00155.png: y",
                        "cam1-00156.png: y",
                        "cam1-00157.png: y",
                        "cam1-00158.png: y",
                        "cam1-00159.png: y",
                        "cam1-00160.png: n",
                        "cam1-00161.png: y",
                        "cam1-00162.png: n",
                        "cam1-00163.png: n",
                        "cam1-00164.png: n",
                        "cam1-00165.png: n",
                        "cam1-00166.png: n",
                        "cam1-00167.png: n",
                        "cam1-00168.png: n",
                        "cam1-00169.png: n"
                ));
    }

    @Test
    public void testCam2Event1() throws Exception {
        MotionDetector m = new MotionDetector();
        testCamEvent(1000, 100, new ImageRectangle(0, 0, 640, 480), m, "testdata/motiondetector/cam2/event1",
                Arrays.asList(
                        "cam2-00001.png: n",
                        "cam2-00002.png: n",
                        "cam2-00003.png: n",
                        "cam2-00004.png: n",
                        "cam2-00005.png: n",
                        "cam2-00006.png: n",
                        "cam2-00007.png: n",
                        "cam2-00008.png: n",
                        "cam2-00009.png: n",
                        "cam2-00080.png: y",
                        "cam2-00081.png: y",
                        "cam2-00082.png: y",
                        "cam2-00083.png: y",
                        "cam2-00084.png: y",
                        "cam2-00085.png: y",
                        "cam2-00086.png: y",
                        "cam2-00087.png: y",
                        "cam2-00088.png: y",
                        "cam2-00089.png: y",
                        "cam2-00090.png: y",
                        "cam2-00091.png: y",
                        "cam2-00092.png: y",
                        "cam2-00093.png: y",
                        "cam2-00094.png: y",
                        "cam2-00095.png: y",
                        "cam2-00096.png: n",
                        "cam2-00097.png: n",
                        "cam2-00098.png: n",
                        "cam2-00099.png: n"
                ));
    }

    @Test
    public void testCam2Event2() throws Exception {
        MotionDetector m = new MotionDetector();
        testCamEvent(1000, 100, new ImageRectangle(0, 0, 640, 480), m, "testdata/motiondetector/cam2/event2",
                Arrays.asList(
                        "cam2-00140.png: n",
                        "cam2-00141.png: n",
                        "cam2-00142.png: n",
                        "cam2-00143.png: n",
                        "cam2-00144.png: n",
                        "cam2-00145.png: n",
                        "cam2-00146.png: n",
                        "cam2-00147.png: y",
                        "cam2-00148.png: y",
                        "cam2-00149.png: y",
                        "cam2-00150.png: y",
                        "cam2-00151.png: y",
                        "cam2-00152.png: y",
                        "cam2-00153.png: y",
                        "cam2-00154.png: y",
                        "cam2-00155.png: y",
                        "cam2-00156.png: y",
                        "cam2-00157.png: y",
                        "cam2-00158.png: y",
                        "cam2-00159.png: y",
                        "cam2-00160.png: y",
                        "cam2-00161.png: y",
                        "cam2-00162.png: y",
                        "cam2-00163.png: n",
                        "cam2-00164.png: n",
                        "cam2-00165.png: n",
                        "cam2-00166.png: n",
                        "cam2-00167.png: n",
                        "cam2-00168.png: n",
                        "cam2-00169.png: n"
                ));
    }

    @Test
    public void testCam3Event1() throws Exception {
        MotionDetector m = new MotionDetector();
        testCamEvent(50, 50, new ImageRectangle(0, 240, 300, 240), m, "testdata/motiondetector/cam3/event1",
                Arrays.asList(
                        "cam3-00090.png: n",
                        "cam3-00091.png: n",
                        "cam3-00092.png: n",
                        "cam3-00093.png: n",
                        "cam3-00094.png: n",
                        "cam3-00095.png: n",
                        "cam3-00096.png: n",
                        "cam3-00097.png: n",
                        "cam3-00098.png: n",
                        "cam3-00099.png: n",
                        "cam3-00100.png: n",
                        "cam3-00101.png: n",
                        "cam3-00102.png: y",
                        "cam3-00103.png: y",
                        "cam3-00104.png: y",
                        "cam3-00105.png: n"
                ));
    }

    @Test
    public void testCam3Event2() throws Exception {
        MotionDetector m = new MotionDetector();
        testCamEvent(50, 50, new ImageRectangle(0, 240, 300, 240), m, "testdata/motiondetector/cam3/event2",
                Arrays.asList(
                        "cam3-00106.png: n",
                        "cam3-00107.png: n",
                        "cam3-00108.png: *",
                        "cam3-00109.png: *",
                        "cam3-00110.png: *",
                        "cam3-00111.png: *",
                        "cam3-00112.png: *",
                        "cam3-00113.png: *",
                        "cam3-00114.png: y",
                        "cam3-00115.png: y",
                        "cam3-00116.png: y",
                        "cam3-00117.png: y",
                        "cam3-00118.png: n",
                        "cam3-00119.png: n",
                        "cam3-00120.png: n",
                        "cam3-00121.png: n",
                        "cam3-00122.png: n",
                        "cam3-00123.png: n",
                        "cam3-00124.png: n",
                        "cam3-00125.png: n",
                        "cam3-00126.png: n",
                        "cam3-00127.png: n",
                        "cam3-00128.png: n",
                        "cam3-00129.png: n"
                ));
    }

    @Test
    public void testCam3Event3() throws Exception {
        MotionDetector m = new MotionDetector();
        testCamEvent(50, 50, new ImageRectangle(0, 240, 300, 240), m, "testdata/motiondetector/cam3/event3",
                Arrays.asList(
                        "cam3-00130.png: n",
                        "cam3-00131.png: n",
                        "cam3-00132.png: n",
                        "cam3-00133.png: n",
                        "cam3-00134.png: n",
                        "cam3-00135.png: n",
                        "cam3-00136.png: n",
                        "cam3-00137.png: n",
                        "cam3-00138.png: n",
                        "cam3-00139.png: n",
                        "cam3-00140.png: n",
                        "cam3-00141.png: n",
                        "cam3-00142.png: n",
                        "cam3-00143.png: n",
                        "cam3-00144.png: n",
                        "cam3-00145.png: n",
                        "cam3-00146.png: n",
                        "cam3-00147.png: n",
                        "cam3-00148.png: n",
                        "cam3-00149.png: n",
                        "cam3-00150.png: n",
                        "cam3-00151.png: n",
                        "cam3-00152.png: n",
                        "cam3-00153.png: n",
                        "cam3-00154.png: n",
                        "cam3-00155.png: n",
                        "cam3-00156.png: n",
                        "cam3-00157.png: n",
                        "cam3-00158.png: n",
                        "cam3-00159.png: n",
                        "cam3-00160.png: n",
                        "cam3-00161.png: n",
                        "cam3-00162.png: n",
                        "cam3-00163.png: n",
                        "cam3-00164.png: n",
                        "cam3-00165.png: n",
                        "cam3-00166.png: n",
                        "cam3-00167.png: n",
                        "cam3-00168.png: n",
                        "cam3-00169.png: n",
                        "cam3-00170.png: n",
                        "cam3-00171.png: n",
                        "cam3-00172.png: n",
                        "cam3-00173.png: n",
                        "cam3-00174.png: n",
                        "cam3-00175.png: n",
                        "cam3-00176.png: n",
                        "cam3-00177.png: n",
                        "cam3-00178.png: n",
                        "cam3-00179.png: n",
                        "cam3-00180.png: n",
                        "cam3-00181.png: n",
                        "cam3-00182.png: n",
                        "cam3-00183.png: n",
                        "cam3-00184.png: n",
                        "cam3-00185.png: n"
                ));
    }

    @Test
    public void testCam11Event1() throws Exception {
        MotionDetector m = new MotionDetector();
        testCamEvent(50, 50, new ImageRectangle(0, 0, 640, 480), m, "testdata/motiondetector/cam11/event1",
                Arrays.asList(
                        "cam11-00080.png: n",
                        "cam11-00081.png: n",
                        "cam11-00082.png: n",
                        "cam11-00083.png: n",
                        "cam11-00084.png: n",
                        "cam11-00085.png: n",
                        "cam11-00086.png: n",
                        "cam11-00087.png: n",
                        "cam11-00088.png: n",
                        "cam11-00089.png: n",
                        "cam11-00090.png: y",
                        "cam11-00091.png: y",
                        "cam11-00092.png: y",
                        "cam11-00093.png: y",
                        "cam11-00094.png: y",
                        "cam11-00095.png: y",
                        "cam11-00096.png: y",
                        "cam11-00097.png: y",
                        "cam11-00098.png: n",
                        "cam11-00099.png: n"
                ));
    }

    @Test
    public void testCam11Event2() throws Exception {
        MotionDetector m = new MotionDetector();
        testCamEvent(50, 50, new ImageRectangle(0, 0, 640, 480), m, "testdata/motiondetector/cam11/event2",
                Arrays.asList(
                        "cam11-00134.png: n",
                        "cam11-00135.png: *",
                        "cam11-00136.png: *",
                        "cam11-00137.png: *",
                        "cam11-00138.png: *",
                        "cam11-00139.png: y",
                        "cam11-00140.png: y",
                        "cam11-00141.png: y",
                        "cam11-00142.png: y",
                        "cam11-00143.png: y",
                        "cam11-00144.png: y",
                        "cam11-00145.png: y",
                        "cam11-00146.png: y",
                        "cam11-00147.png: y",
                        "cam11-00148.png: y",
                        "cam11-00149.png: y",
                        "cam11-00150.png: y",
                        "cam11-00151.png: y",
                        "cam11-00152.png: n",
                        "cam11-00153.png: n",
                        "cam11-00154.png: n",
                        "cam11-00155.png: n",
                        "cam11-00156.png: n",
                        "cam11-00157.png: n",
                        "cam11-00158.png: n",
                        "cam11-00159.png: n"
                ));
    }

    private void testCamEvent(int minAreaSize, int areaSizeThreshold, ImageRectangle detectionArea, MotionDetector m, String path, List<String> expected) throws IOException {
        List<ImageResult> actualResult = detectMotion(m, new File(path));
        //printMotionResult(actualResult);
        for (int i = 0; i < expected.size(); ++i) {
            if (!expected.get(i).endsWith("*")) {
                ImageResult result = actualResult.get(i);
                int area = calcArea(ImageRectangleUtil.filterByIntersection(
                        ImageRectangleUtil.filterByAreaSize(result.getRectangles(), areaSizeThreshold),
                        detectionArea));
                String actual = result.getFile() + ": " + (area >= minAreaSize ? "y" : "n");
                assertThat(actual, is(expected.get(i)));
            }
        }
    }

    private void printMotionResult(List<ImageResult> result) {
        for (ImageResult r : result) {
            System.out.print(r.getFile() + ": " + (r.getRectangles().isEmpty() ? "n" : calcArea(r.getRectangles())));
            System.out.print(" ");
            for (ImageRectangle b : r.getRectangles()) {
                System.out.print(b);
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    private int calcArea(List<ImageRectangle> rectangles) {
        int area = 0;
        for (ImageRectangle b : rectangles) {
            area += b.getArea();
        }
        return area;
    }

    private List<ImageResult> detectMotion(MotionDetector m, File dir) throws IOException {
        List<ImageResult> result = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null) {
            Arrays.sort(files);
            BufferedImage prevImage = null;
            for (File file : files) {
                if (file.getName().endsWith(".png")) {
                    BufferedImage image = ImageIO.read(file);
                    if (prevImage != null) {
                        List<ImageRectangle> rectangles = m.getMotionAreas(image, prevImage);
                        result.add(new ImageResult(file.getName(), rectangles));
                    } else {
                        result.add(new ImageResult(file.getName(), new ArrayList<>()));
                    }
                    prevImage = image;
                }
            }
        }
        return result;
    }

    private static class ImageResult {
        String file;
        List<ImageRectangle> rectangles;

        private ImageResult(String file, List<ImageRectangle> rectangles) {
            this.file = file;
            this.rectangles = rectangles;
        }

        public String getFile() {
            return file;
        }

        public List<ImageRectangle> getRectangles() {
            return rectangles;
        }
    }
}
