import java.awt.*;
import java.util.*;
import java.util.logging.Logger;

public class CSSGradient {
    // Debugging --
    private final static Logger logger = Logger.getLogger(Image2Gradient.class.getName());

    private Config config = new Config();

    // Set finals --
    private final String PROPERTY                  = "background-image";
    private final String FALLBACK_PROPERTY         = "background-color";
    private final String L2R_PREFIX                = "to right";
    private final String BL2TR_PREFIX              = "to top right";
    private final String BR2TL_PREFIX              = "to top left";
    private final String WEBKIT_GRADIENT_FUNCTION  = "-webkit-linear-gradient";
    private final String MOZ_GRADIENT_FUNCTION     = "-moz-linear-gradient";
    private final String OPERA_GRADIENT_FUNCTION   = "-o-linear-gradient";
    private final String GRADIENT_FUNCTION         = "linear-gradient";

    private final String L2R_GRADIENT              = config.getL2RGradient();
    private final String BL2TR_GRADIENT            = config.getBL2TRGradient();
    private final String BR2TL_GRADIENT            = config.getBR2TLGradient();
    private final String WEBKIT_PREFIX             = config.getWebkitPrefix();
    private final String MOZ_PREFIX                = config.getMozPrefix();
    private final String OPERA_PREFIX              = config.getOperaPrefix();

    private String vendors                         = config.getDefaultVendors();

    private String gradientPrefix                  = "";
    private String gradientFallback                = "";
    private String colorStopList                   = ""; // <color-stop-list> argument of linear-gradient.
    private String cssGradient                     = "";

    CSSGradient(String gradientType, String browsers, ArrayList<Color> averageColors, Color averageColor) {
        vendors = browsers;

        if (gradientType.equals(L2R_GRADIENT)) {
            gradientPrefix = String.format("%s, ", L2R_PREFIX);
        } else if (gradientType.equals(BL2TR_GRADIENT)) {
            gradientPrefix = String.format("%s, ", BL2TR_PREFIX);
        } else if (gradientType.equals(BR2TL_GRADIENT)) {
            gradientPrefix = String.format("%s, ", BR2TL_PREFIX);
        } else {
            gradientPrefix = "";
        }

        gradientFallback = colorToCSSString(averageColor);
        gradientize(averageColors);
    }

    private void gradientize(ArrayList<Color> averageColors) {
        for (Color averageColor: averageColors) {
            colorStopList += colorToCSSString(averageColor);
        }

        colorStopList = colorStopList.replace(")r", "),r"); // add comma separators.
        cssGradient += addFallback();
        cssGradient += addPrefixes();
        cssGradient += buildCSSDeclaration(GRADIENT_FUNCTION);
    }

    private String addFallback() {
        return String.format("%s: %s;", FALLBACK_PROPERTY, gradientFallback);
    }

    // Todo: might not need moz, opera prefixes anymore... https://gist.github.com/alisonailea/c6cba44d6adf872d922b
    private String addPrefixes() {
        String result = "";

        for(String prefix: vendors.split(",")) {
            if (prefix.equals(WEBKIT_PREFIX)) {
                result += buildCSSDeclaration(WEBKIT_GRADIENT_FUNCTION);
            } else if (prefix.equals(MOZ_PREFIX)) {
                result += buildCSSDeclaration(MOZ_GRADIENT_FUNCTION);
            } else if (prefix.equals(OPERA_PREFIX)) {
                result += buildCSSDeclaration(OPERA_GRADIENT_FUNCTION);
            } else {
                System.err.println("Unknown browser prefix: " + prefix);
            }
        }

        return result;
    }

    private String buildCSSDeclaration(String gradientFunction) {
        return String.format("%s: %s(%s%s);", PROPERTY, gradientFunction, gradientPrefix, colorStopList);
    }

    private String colorToCSSString(Color averageColor) {
        return String.format("rgb(%s,%s,%s)", averageColor.getRed(), averageColor.getGreen(), averageColor.getBlue());
    }

    public void print () {
        System.out.println(cssGradient);
    }
}
