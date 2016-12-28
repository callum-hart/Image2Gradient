package image_2_gradient;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CSSGradient {
    private Config config = new Config();

    // Set finals --
    private final String T2B                              = config.getT2B();
    private final String L2R                              = config.getL2R();
    private final String BL2TR                            = config.getBL2TR();
    private final String BR2TL                            = config.getBR2TL();

    private final static String PROPERTY                  = "background-image";
    private final static String FALLBACK_PROPERTY         = "background-color";

    private final Map<String, Map> GRADIENT_MODELS        = new HashMap<String, Map>(){{
        put("standard", new HashMap<String, String>(){{
            put("function", "linear-gradient");
            put("support",  "Standard syntax");
            put(L2R,        "to right");
            put(BL2TR,      "to top right");
            put(BR2TL,      "to top left");
        }});

        put(config.getWebkitIdentifier(), new HashMap<String, String>(){{
            put("function", "-webkit-linear-gradient");
            put("support",  "Safari 5.1 to 6");
            put(L2R,        "right|left"); // left when more than 2 color stops
            put(BL2TR,      "bottom left");
            put(BR2TL,      "bottom right");
        }});

        put(config.getMozIdentifier(), new HashMap<String, String>(){{
            put("function", "-moz-linear-gradient");
            put("support",  "Firefox 3.6 to 15");
            put(L2R,        "right|left"); // left when more than 2 color stops
            put(BL2TR,      "bottom left");
            put(BR2TL,      "bottom right");
        }});

        put(config.getOperaIdentifier(), new HashMap<String, String>(){{
            put("function", "-o-linear-gradient");
            put("support",  "Opera 11.1 to 12");
            put(L2R,        "right|left"); // left when more than 2 color stops
            put(BL2TR,      "bottom left");
            put(BR2TL,      "bottom right");
        }});
    }};

    private String cssGradient                             = "";

    protected CSSGradient(String gradientType, String vendors, ArrayList<Color> averageColors, Color dominantColor) {
        String fallback = addFallback(colorToCSSString(dominantColor));
        String function = GRADIENT_MODELS.get("standard").get("function").toString();
        String direction = gradientType.equals(T2B) ? "" : GRADIENT_MODELS.get("standard").get(gradientType).toString();
        String colorStops = "";

        for (Color averageColor: averageColors) {
            colorStops += colorToCSSString(averageColor);
        }

        cssGradient += fallback;
        cssGradient += buildCSSDeclaration(function, direction, colorStops);

        // handle vendor prefixes
        for (String vendor: vendors.split(",")) {
            function = GRADIENT_MODELS.get(vendor).get("function").toString();

            if (!gradientType.equals(T2B)) {
                direction = GRADIENT_MODELS.get(vendor).get(gradientType).toString();
            }

            // When there are more than 2 color stops direction changes in Opera 11.1 - 12.0 and Fx 3.6 - 15 ಠ_ಠ
            if (gradientType.equals(L2R)) {
                String [] directions = direction.split("\\|");
                direction = (averageColors.size() > 2) ? directions[1] : directions[0];
            }

            cssGradient += buildCSSDeclaration(function, direction, colorStops);
        }
    }

    private String addFallback(String fallbackColor) {
        return String.format("%s: %s;", FALLBACK_PROPERTY, fallbackColor);
    }

    private String buildCSSDeclaration(String function, String direction, String colorStops) {
        direction = direction.isEmpty() ? direction : direction.concat(", ");
        colorStops = colorStops.replace(")r", "),r");
        return String.format("%s: %s(%s%s);", PROPERTY, function, direction, colorStops);
    }

    private String colorToCSSString(Color averageColor) {
        return String.format("rgb(%s,%s,%s)", averageColor.getRed(), averageColor.getGreen(), averageColor.getBlue());
    }

    protected void print () {
        System.out.println(cssGradient);
    }

    protected void copyToClipboard() {
        StringSelection selection = new StringSelection(cssGradient);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
        System.out.println("Gradient copied to clipboard");
    }
}

