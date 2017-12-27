package hello;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

class ScalingManager {

    static BufferedImage lpad(BufferedImage sourceImage, int requestedHeight, int requestedWidth) {
        //#region input validation
        if (sourceImage == null) {
            throw new NullPointerException("Dude! sourceImage is null");
        }

        if (requestedHeight <= 0) {
            throw new IllegalArgumentException("requestedHeight should be larger than 0. Actual value " + requestedHeight);
        }

        if (requestedWidth <= 0) {
            throw new IllegalArgumentException("requestedWidth should be larger than 0. Actual value " + requestedWidth);
        }
        //endregion

        int currentHeight = sourceImage.getHeight();
        int currentWidth = sourceImage.getWidth();

        if (requestedHeight > currentHeight && requestedWidth > currentWidth) {

            // if the dimensions are larger than the original image - it does not scale up. Only padded.
            return padImage(sourceImage, requestedHeight, requestedWidth);

        } else {

            // First we scale down the image and then we pad it to reach the required dimensions

            Dimension newDimension = computeDimensionRetainRatio(dim(sourceImage), dim(requestedWidth, requestedHeight));

            double scaleX = (float) newDimension.width / currentWidth;
            double scaleY = (float) newDimension.height / currentHeight;

            BufferedImage scaledImage = scaleDownImage(sourceImage, scaleX, scaleY);

            if (currentWidth * requestedHeight == currentHeight * requestedWidth) {
                // the requested ratio did not change
                return scaledImage;
            } else {
                return padImage(scaledImage, requestedHeight, requestedWidth);
            }
        }
    }

    private static BufferedImage scaleDownImage(BufferedImage sourceImage, double scaleX, double scaleY) {

        int newWidth = (int) (scaleX * sourceImage.getWidth());
        int newHeight = (int) (scaleY * sourceImage.getHeight());

        BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics2D = scaledImage.createGraphics();
        AffineTransform at = AffineTransform.getScaleInstance(scaleX, scaleY);

        graphics2D.drawImage(sourceImage, at, null);
        graphics2D.dispose();

        return scaledImage;
    }

    private static BufferedImage padImage(BufferedImage sourceImage, int height, int width) {

        // Implicit assumption: the given height and width define boundaries that are larger than the given image

        // The general implementation idea: first draw a rectangle of the final size in the color of the requested pad
        // and then draw the source image on top of the rectangle.

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics g = result.getGraphics();
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, width, height);

        // compute offset so the original image will be centered
        int heightOffset = (height - sourceImage.getHeight()) / 2;
        int widthOffset = (width - sourceImage.getWidth()) / 2;

        g.drawImage(sourceImage, widthOffset, heightOffset, null);
        g.dispose();

        return result;
    }

    // helper constructor methods for the Dimension class to make code more readable
    private static Dimension dim(int x, int y) {
        return new Dimension(x, y);
    }

    private static Dimension dim(BufferedImage bi) {
        return dim(bi.getWidth(), bi.getHeight());
    }

    private static Dimension computeDimensionRetainRatio(Dimension currentDimension, Dimension requestedDimension) {

        // Note! there is an implicit assumption there there is no scaling up, this is according to the requirement of the task.
        // If the requested dimension is absolutely larger than the original dimension, than the original dimension is returned.

        int original_width = currentDimension.width;
        int original_height = currentDimension.height;

        int bound_width = requestedDimension.width;
        int bound_height = requestedDimension.height;

        int new_width = original_width;
        int new_height = original_height;

        // first check if we need to scale width
        if (original_width > bound_width) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // then check if we need to scale even with the new height
        if (new_height > bound_height) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }

        return new Dimension(new_width, new_height);
    }


}
