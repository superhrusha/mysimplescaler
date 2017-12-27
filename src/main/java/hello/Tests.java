package hello;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Tests {

    public static void main(String[] args) throws Exception {

        Tests t = new Tests();
        t.internalIntegrationTests();

    }

    void internalIntegrationTests() throws IOException {

        BufferedImage sourceImage = loadImageFromResource("source.png");

        // Given that I have an oracle of truth, the below tests will test gainst predefined
        // results given to me by him (in this case, I bootsrapped the results)

        BufferedImage expected100x100 = loadImageFromResource("100x100.png");
        testScale(sourceImage, expected100x100, 100, 100);

        BufferedImage expected200x300 = loadImageFromResource("200x300.png");
        testScale(sourceImage, expected200x300, 200, 300);

        BufferedImage expected247x236 = loadImageFromResource("247x236.png");
        testScale(sourceImage, expected247x236, 247, 236);

        BufferedImage expected300x200 = loadImageFromResource("300x200.png");
        testScale(sourceImage, expected300x200, 300, 200);

        BufferedImage expected300x300 = loadImageFromResource("300x300.png");
        testScale(sourceImage, expected300x300, 300, 300);

        // We can test for individual properties as well, for example, test that the dimensions are as expected
        BufferedImage resultForDimension = ScalingManager.lpad(sourceImage, 246, 135);
        assert resultForDimension.getHeight() == 246 && resultForDimension.getWidth() == 135;

        // I would add tests here that hot evey branch inside the implementation
        // (no scaling, scaling width only, scaling height only, scaling both)
        // Arguably this can also be made with unit tests to some extent, I prefer the confidence of integration tests
        // Of course there is a trade-off for execution time and the boundaries.
    }

    private void testScale(BufferedImage source, BufferedImage expected, int height, int width){
        BufferedImage actual = ScalingManager.lpad(source, height, width);

        if(!imagesAreEqual(expected, actual) ){
            throw new RuntimeException(String.format("scale for %sx%s did not work as expected.", height, width));
        }
    }

    private BufferedImage loadImageFromResource(String name){
        InputStream is = getClass().getResourceAsStream("/" + name);
        try {
            BufferedImage read = ImageIO.read(is);
            return read;
        } catch (IOException e) {
            throw new RuntimeException(e); // I hate checked exceptions and I don't like Java very much ;)
        }
    }

    private boolean imagesAreEqual(BufferedImage img1, BufferedImage img2) {


        if (img1.getWidth() == img2.getWidth() && img1.getHeight() == img2.getHeight()) {
            for (int x = 0; x < img1.getWidth(); x++) {
                for (int y = 0; y < img1.getHeight(); y++) {
                    if (img1.getRGB(x, y) != img2.getRGB(x, y))
                        return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }
}
