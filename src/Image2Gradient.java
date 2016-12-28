import image_2_gradient.Config;
import image_2_gradient.ImageProcessor;

public class Image2Gradient {
    public static void main(String[] args) {
//        -t: type of gradient (t2b / l2r / bl2tr / br2tl) {String} optional.
//        -f: fidelity, number of bands {Integer} optional. (number of color stops in gradient)
//        -v: vendor prefixes for gradient ("web,moz,opera") {String} optional. (IDENTIFIER)
//        --help: print usage / help.
//        last arg: path to image {String} required.

        // usage: Image2Gradient [-t] [-f] [-p] imagePath

        Config config = new Config(args);

        String imagePath    = config.getImagePath();
        String gradientType = config.getGradientType();
        Integer fidelity    = config.getFidelity();
        String vendors      = config.getVendors();

        if (imagePath.isEmpty()) {
            System.out.println("Can't initialize Image2Gradient. Missing image path.");
        } else {
            ImageProcessor foo = new ImageProcessor(imagePath, gradientType, fidelity, vendors);
        }
    }
}