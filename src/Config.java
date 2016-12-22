import java.util.Arrays;
import java.util.List;

public class Config {

    // Set finals --
    private static final String T2B_GRADIENT      = "t2b";   // top to bottom (default)
    private static final String L2R_GRADIENT      = "l2r";   // left to right
    private static final String BL2TR_GRADIENT    = "bl2tr"; // bottom left to top right
    private static final String BR2TL_GRADIENT    = "br2tl"; // bottom right to top left
    private static final String WEBKIT_PREFIX     = "web";
    private static final String MOZ_PREFIX        = "moz";
    private static final String OPERA_PREFIX      = "opera";

    // Set defaults --
    private static final Integer DEFAULT_FIDELITY = 10;
    private static final String DEFAULT_VENDORS   = String.format("%s,%s,%s", WEBKIT_PREFIX, MOZ_PREFIX, OPERA_PREFIX);
    private static final Integer DEFAULT_ROTATION = 45; // todo make this configurable?

    // CLI flags --
    private static final String GRADIENT_FLAG     = "-t";
    private static final String FIDELITY_FLAG     = "-f";
    private static final String VENDOR_FLAG       = "-v";
    private static final String HELP_FLAG         = "--help";

    private List<String> cliArgs;

    Config() {}

    Config(String[] args) {
        cliArgs = Arrays.asList(args);

        if (cliArgs.contains(HELP_FLAG)) {
            printHelp();
        }
    }

    // Getters --

    public String getT2BGradient() {
        return T2B_GRADIENT;
    }

    public String getL2RGradient() {
        return L2R_GRADIENT;
    }

    public String getBL2TRGradient() {
        return BL2TR_GRADIENT;
    }

    public String getBR2TLGradient() {
        return BR2TL_GRADIENT;
    }

    public String getWebkitPrefix() {
        return WEBKIT_PREFIX;
    }

    public String getMozPrefix() {
        return MOZ_PREFIX;
    }

    public String getOperaPrefix() {
        return OPERA_PREFIX;
    }

    public Integer getDefaultRotation() {
        return DEFAULT_ROTATION;
    }

    public String getDefaultVendors() {
        return DEFAULT_VENDORS;
    }

    // Setters --

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
        String gradientType = T2B_GRADIENT;

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
