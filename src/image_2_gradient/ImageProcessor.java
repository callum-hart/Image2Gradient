package image_2_gradient;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class ImageProcessor {
    private Config config = new Config();

    // Set finals --
    private final String T2B                              = config.getT2B();
    private final String L2R                              = config.getL2R();
    private final String BL2TR                            = config.getBL2TR();
    private final String BR2TL                            = config.getBR2TL();

    private static ArrayList<Color> averageColors         = new ArrayList<>();

    // Globals / options --
    private static String gradientType;
    private static Integer fidelity;


    public ImageProcessor(String imagePath, String rawGradient, Integer rawFidelity, String rawVendors) {
        ParamValidator paramValidator = new ParamValidator();

        if (paramValidator.check(rawGradient, rawFidelity, rawVendors)) {
            File imageFile = new File(imagePath);
            gradientType   = rawGradient;
            fidelity       = rawFidelity;

            try {
                BufferedImage image = ImageIO.read(imageFile);
                averageColors = bandize(image);
                CSSGradient cssGradient = new CSSGradient(gradientType, rawVendors, averageColors, getDominantColor());
                cssGradient.print();
                cssGradient.copyToClipboard();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private ArrayList<Color> bandize(BufferedImage image) throws Exception {
        int width = image.getWidth();
        int height = image.getHeight();
        int bands[][];

        if (gradientType.equals(T2B)) {
            bands = horizontalBands(image, width, height);
        } else if (gradientType.equals(L2R)) {
            bands = verticalBands(image, width, height);
        } else if (gradientType.equals(BL2TR)) {
            BufferedImage rotatedImage = rotateImage(image, width, height);
            bands = verticalBands(rotatedImage, rotatedImage.getWidth(), rotatedImage.getHeight());
        } else if (gradientType.equals(BR2TL)) {
            BufferedImage rotatedImage = rotateImage(image, width, height);
            bands = horizontalBands(rotatedImage, rotatedImage.getWidth(), rotatedImage.getHeight());
        } else {
            bands = horizontalBands(image, width, height);
        }

        for (int[] band: bands) {
            averageColors.add(getAverageColorInBand(band));
        }

        // When gradient starts from bottom right this equates to end of array (hence the reverse).
        // Todo test this is correct / works.
        if (gradientType.equals(BR2TL)) {
            Collections.reverse(averageColors);
        }

        return averageColors;
    }

    private int[][] horizontalBands(BufferedImage image, int width, int height) {
        int pixelCount = width * height;
        int bands[][] = new int[fidelity][pixelCount / fidelity];
        int bandHeight = height / fidelity;

        for (int i = 0; i < fidelity; i++) {
            image.getRGB(0, bandHeight * i, width, bandHeight, bands[i], 0, width);
        }

        return bands;
    }

    private int[][] verticalBands(BufferedImage image, int width, int height) {
        int pixelCount = width * height;
        int bands[][] = new int[fidelity][pixelCount / fidelity];
        int bandWidth = width / fidelity;

        for (int i = 0; i < fidelity; i++) {
            image.getRGB(bandWidth * i, 0, bandWidth, height, bands[i], 0, bandWidth);
        }

        return bands;
    }

    private BufferedImage rotateImage(BufferedImage image, int width, int height) throws Exception {
        try {
            double angle = config.getDefaultRotation();
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
    private Color getDominantColor() {
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
}

