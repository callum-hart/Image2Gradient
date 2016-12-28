import image_2_gradient.Config;
import image_2_gradient.ImageProcessor;

public class Image2Gradient {
    public static void main(String[] args) {
        Config config       = new Config(args);
        String imagePath    = config.getImagePath();
        String gradientType = config.getGradientType();
        Integer fidelity    = config.getFidelity();
        String vendors      = config.getVendors();

        if (imagePath.isEmpty()) {
            System.out.println("Can't initialize Image2Gradient. Missing image path.");
        } else {
            new ImageProcessor(imagePath, gradientType, fidelity, vendors);
        }
    }
}