package image_2_gradient;

import java.util.Arrays;
import java.util.List;

public class Config {

    // Set finals --
    private static final String T2B      = "t2b";   // top to bottom (default)
    private static final String L2R      = "l2r";   // left to right
    private static final String BL2TR    = "bl2tr"; // bottom left to top right
    private static final String BR2TL    = "br2tl"; // bottom right to top left

    private static final String WEBKIT_IDENTIFIER     = "web";
    private static final String MOZ_IDENTIFIER        = "moz";
    private static final String OPERA_IDENTIFIER      = "opera";

    // Set defaults --
    private static final Integer DEFAULT_FIDELITY = 10;
    private static final String DEFAULT_VENDORS   = String.format("%s,%s,%s", WEBKIT_IDENTIFIER, MOZ_IDENTIFIER, OPERA_IDENTIFIER);
    private static final Integer DEFAULT_ROTATION = 45; // todo make this configurable?

    // CLI flags --
    private static final String GRADIENT_FLAG     = "-t";
    private static final String FIDELITY_FLAG     = "-f";
    private static final String VENDOR_FLAG       = "-v";
    private static final String HELP_FLAG         = "--help";

    private List<String> cliArgs;

    Config() {}

    public Config(String[] args) {
        cliArgs = Arrays.asList(args);

        if (cliArgs.contains(HELP_FLAG)) {
            printHelp();
        }
    }

    // Getters --

    public String getT2B() {
        return T2B;
    }

    public String getL2R() {
        return L2R;
    }

    public String getBL2TR() {
        return BL2TR;
    }

    public String getBR2TL() {
        return BR2TL;
    }

    public String getWebkitIdentifier() {
        return WEBKIT_IDENTIFIER;
    }

    public String getMozIdentifier() {
        return MOZ_IDENTIFIER;
    }

    public String getOperaIdentifier() {
        return OPERA_IDENTIFIER;
    }

    public Integer getDefaultRotation() {
        return DEFAULT_ROTATION;
    }

    public String getDefaultVendors() {
        return DEFAULT_VENDORS;
    }

    // Process CLI args --

    public String getImagePath() {
        String imagePath = "";

        try {
            imagePath = cliArgs.get(cliArgs.size() - 1);

            if (imagePath.equals(HELP_FLAG)) {
                imagePath = "";
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            // Todo use logs in ParamValidator
            System.err.println("Image path missing, this is required");
        }

        return imagePath;
    }

    public String getGradientType() {
        String gradientType = T2B;

        if (cliArgs.contains(GRADIENT_FLAG)) {
            gradientType = cliArgs.get(cliArgs.indexOf(GRADIENT_FLAG) + 1);
        }

        return gradientType;
    }

    public Integer getFidelity() {
        Integer fidelity = DEFAULT_FIDELITY;

        if (cliArgs.contains(FIDELITY_FLAG)) {
            try {
                fidelity = Integer.parseInt(cliArgs.get(cliArgs.indexOf(FIDELITY_FLAG) + 1));
            } catch (NumberFormatException e) {
                // Todo use logs in ParamValidator
                System.err.println("Fidelity needs to be a number");
            }
        }

        return fidelity;
    }

    public String getVendors() {
        String vendors = DEFAULT_VENDORS;

        if (cliArgs.contains(VENDOR_FLAG)) {
            vendors = cliArgs.get(cliArgs.indexOf(VENDOR_FLAG) + 1);
        }

        return vendors;
    }

    public void printHelp() {
        System.out.println("Print help");
    }
}

