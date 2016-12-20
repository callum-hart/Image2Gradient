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

    // Set finals --
    private static final String T2B_GRADIENT   = "t2b"; // top to bottom (default)
    private static final String L2R_GRADIENT   = "l2r"; // left to right
    private static final String BL2TR_GRADIENT = "bl2tr"; // bottom left to top right
    private static final String BR2TL_GRADIENT = "br2tl"; // bottom right to top left

    // Set defaults (will also be CLI config options) --
    private static String gradientType         = T2B_GRADIENT;
    private static Integer bandCount           = 10; // fidelity|precision
    private static double angle                = 45; // angle to rotate image (should angle be passed to linear-gradient)?

    private static LinkedList<Color> averageColors = new LinkedList<>();

    private Config config = new Config();

    private Image2Gradient(String imagePath) {
        File imageFile = new File(imagePath);

        System.out.println("T2B from config: " + config.getT2bGradient());

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
            case T2B_GRADIENT:
                bands = horizontalBands(image, width, height);
                break;
            case L2R_GRADIENT:
                bands = verticalBands(image, width, height);
                break;
            case BL2TR_GRADIENT:
                BufferedImage rotatedImage = rotateImage(image, width, height);
                bands = verticalBands(rotatedImage, rotatedImage.getWidth(), rotatedImage.getHeight());
                break;
            case BR2TL_GRADIENT:
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
        if (gradientType.equals(BR2TL_GRADIENT)) {
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
        // Todo: move config variables used here out into a service (can also be used by Image2Gradient and CSSGradient).

        // Args with arguments:

//        -t: type of gradient (t2b / l2r / bl2tr / br2tl) {String} optional.
//        -f: fidelity, number of bands {Integer} optional.
//        -p: vendor prefixes for gradient ("web,moz,opera") {String} optional.
//        --help: print usage / help.
//        last arg: path to image {String} required.

        // Flags:
//        -c: include CSS comments
//        -bv: show band visualisations

        // usage: Image2Gradient [-t] [-f] [-p] imagePath

        Config config = new Config(args);

        String imagePath    = config.getImagePath();
        String gradientType = config.getGradientType();

        System.out.println("imagePath: " + imagePath);
        System.out.println("gradientType: " + gradientType);

        String wat = "".isEmpty() ? "default" : "ss";

//        String imagePath = args[0];
//        Image2Gradient a = new Image2Gradient(imagePath);
    }
}