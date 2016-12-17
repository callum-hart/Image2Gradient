import java.awt.*;
import java.util.*;
import java.util.logging.Logger;

public class CSSGradient {
    // Debugging --
    private final static Logger logger = Logger.getLogger(Image2Gradient.class.getName());

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
    private final String WEBKIT_PREFIX             = "web";
    private final String MOZ_PREFIX                = "moz";
    private final String OPERA_PREFIX              = "opera";

    // Set defaults (will also be CLI config options) --
    private String prefixes                        = "*";
        // "web": webkit
        // "web,moz": webkit and firefox
        // "web,moz,opera": webkit, firefox and opera
        // "*": shorthand for all prefixes

    private String gradientPrefix                  = "";
    private String gradientFallback                = "";
    private String colorStopList                   = ""; // <color-stop-list> argument of linear-gradient.
    private String cssGradient                     = "";

    public CSSGradient(String gradientType, LinkedList<Color> averageColors, Color averageColor) {
        if (prefixes.equals("*")) {
            prefixes = String.format("%s,%s,%s", WEBKIT_PREFIX, MOZ_PREFIX, OPERA_PREFIX);
        }

        switch (gradientType) {
            case "l-r":
                gradientPrefix = String.format("%s, ", L2R_PREFIX);
                break;
            case "bl-tr":
                gradientPrefix = String.format("%s, ", BL2TR_PREFIX);
                break;
            case "br-tl":
                gradientPrefix = String.format("%s, ", BR2TL_PREFIX);
                break;
            default: gradientPrefix = "";
        }

        gradientFallback = colorToCSSString(averageColor);
        gradientize(averageColors);
    }

    private void gradientize(LinkedList<Color> averageColors) {
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

        for(String prefix: prefixes.split(",")) {
            switch (prefix) {
                case WEBKIT_PREFIX:
                     result += buildCSSDeclaration(WEBKIT_GRADIENT_FUNCTION);
                     break;
                case MOZ_PREFIX:
                     result += buildCSSDeclaration(MOZ_GRADIENT_FUNCTION);
                     break;
                case OPERA_PREFIX:
                     result += buildCSSDeclaration(OPERA_GRADIENT_FUNCTION);
                     break;
                default:
                     System.out.println("Unknown browser prefix: " + prefix);
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
