import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.logging.Logger;

public class Image2Gradient {

    // Debugging --
    private final static Logger logger = Logger.getLogger(Image2Gradient.class.getName());

    // Set defaults (will also be CLI config options) --
    private static String gradientType = "l-r"; // l-r (left to right) | t-b (top to bottom) | bl-tr (bottom left to top right) | br-tl (bottom right to top left)
    private static Integer bandCount   = 10;
    private static double angle        = 45; // angle to rotate image (should angle be passed to linear-gradient)?

    private static LinkedList<Color> averageColors = new LinkedList<>();

    private Image2Gradient(String imagePath) {
        File imageFile = new File(imagePath);

        try {
            BufferedImage image = ImageIO.read(imageFile);
            averageColors = bandize(image);
            CSSGradient cssGradient = new CSSGradient(gradientType, averageColors, getAverageColor());
            cssGradient.print();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private LinkedList<Color> bandize(BufferedImage image) throws Exception {
        int width = image.getWidth();
        int height = image.getHeight();
        int bands[][];

        switch (gradientType) {
            case "t-b":
                bands = horizontalBands(image, width, height);
                break;
            case "l-r":
                bands = verticalBands(image, width, height);
                break;
            case "bl-tr":
                BufferedImage rotatedImage = rotateImage(image, width, height);
                bands = verticalBands(rotatedImage, rotatedImage.getWidth(), rotatedImage.getHeight());
                break;
            case "br-tl":
                rotatedImage = rotateImage(image, width, height);
                bands = horizontalBands(rotatedImage, rotatedImage.getWidth(), rotatedImage.getHeight());
                break;
            default:
                bands = horizontalBands(image, width, height);
        }

        for (int[] band: bands) {
            averageColors.add(getAverageColorInBand(band));
        }

        // reverse list as gradient starts from the bottom. todo: test this is correct / works.
        if (gradientType.equals("br-tr")) {
            Collections.reverse(averageColors);
        }

        return averageColors;
    }

    private int[][] horizontalBands(BufferedImage image, int width, int height) {
        int pixelCount = width * height;
        int bands[][] = new int[bandCount][pixelCount / bandCount];
        int bandHeight = height / bandCount;

        for (int i = 0; i < bandCount; i++) {
            image.getRGB(0, bandHeight * i, width, bandHeight, bands[i], 0, width);
        }

        return bands;
    }

    private int[][] verticalBands(BufferedImage image, int width, int height) {
        int pixelCount = width * height;
        int bands[][] = new int[bandCount][pixelCount / bandCount];
        int bandWidth = width / bandCount;

        for (int i = 0; i < bandCount; i++) {
            image.getRGB(bandWidth * i, 0, bandWidth, height, bands[i], 0, bandWidth);
        }

        return bands;
    }

    private BufferedImage rotateImage(BufferedImage image, int width, int height) throws Exception {
        try {
            AffineTransform transform = new AffineTransform();
            transform.rotate(Math.toRadians(angle), width / 2, height / 2);

            Rectangle bounds = transform.createTransformedShape(new Rectangle(width, height)).getBounds();
            int x = (bounds.width - width) / 2;
            int y = (bounds.height - height) / 2;
            AffineTransform at = new AffineTransform();

            at.rotate(Math.toRadians(angle), x + (width / 2), y + (height / 2));
            at.translate(x, y);

            AffineTransformOp operation = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
            BufferedImage rotatedImage  = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_ARGB);

            operation.filter(image, rotatedImage);
            ImageIO.write(rotatedImage, "png", new File("../../../images/_out.png")); // just for debugging!

            return rotatedImage;
        } catch (Exception e) {
            throw new Exception();
        }
    }

    //  Todo: move this (and getAverageColor) out into a service?
    private Color getAverageColorInBand(int[] band) {
        int sumRed = 0, sumGreen = 0, sumBlue = 0;

        for (int pixel: band) {
            sumRed   += (pixel & 0xff0000) >> 16;
            sumGreen += (pixel & 0xff00) >> 8;
            sumBlue  += pixel & 0xff;
        }

        return new Color(
                sumRed   / band.length,
                sumGreen / band.length,
                sumBlue  / band.length
        );
    }

    // Todo this needs testing.
    private Color getAverageColor() {
        int sumRed = 0, sumGreen = 0, sumBlue = 0;

        for (Color color: averageColors) {
            sumRed   += color.getRed();
            sumGreen += color.getGreen();
            sumBlue  += color.getBlue();
        }

        return new Color(
          sumRed   / averageColors.size(),
          sumGreen / averageColors.size(),
          sumBlue  / averageColors.size()
        );
    }

    public static void main(String[] args) {
        String imagePath = args[0];
        Image2Gradient a = new Image2Gradient(imagePath);
    }
}