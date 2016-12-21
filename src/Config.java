import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public class Config {

    // Set finals --
    private static final String T2B_GRADIENT      = "t2b"; // top to bottom (default)
    private static final String L2R_GRADIENT      = "l2r"; // left to right
    private static final String BL2TR_GRADIENT    = "bl2tr"; // bottom left to top right
    private static final String BR2TL_GRADIENT    = "br2tl"; // bottom right to top left
    private static final String WEBKIT_PREFIX     = "web";
    private static final String MOZ_PREFIX        = "moz";
    private static final String OPERA_PREFIX      = "opera";
    private static final String ALL_VENDORS       = "*";

    // Set defaults --
    private static final Integer DEFAULT_FIDELITY = 10;
    private static final String DEFAULT_VENDORS   = ALL_VENDORS;

    // CLI --
    private static final String GRADIENT_FLAG     = "-t";
    private static final String FIDELITY_FLAG     = "-f";
    private static final String VENDOR_FLAG       = "-v";

    private List<String> cliArgs;

    public Config() {}

    public Config(String[] args) {
        cliArgs = Arrays.asList(args);

        System.out.println("---------cliArgs---------");
        for (String arg: cliArgs) {
            System.out.println(arg);
        }
        System.out.println("---------/cliArgs---------");
    }

    // Getters

    public String getT2bGradient() {
        return T2B_GRADIENT;
    }

    // Todo: added getters for other gradients

    // Process CLI args

    public String getImagePath() {
        String rawImagePath = "";

        try {
            rawImagePath = cliArgs.get(cliArgs.size() - 1);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Image path missing, this is required");
        }

        return rawImagePath;
    }

    public String getGradientType() {
        String gradientType = T2B_GRADIENT;

        if (cliArgs.contains(GRADIENT_FLAG)) {
            String rawGradient = cliArgs.get(cliArgs.indexOf(GRADIENT_FLAG) + 1);

            if (rawGradient.equals(T2B_GRADIENT) || rawGradient.equals(L2R_GRADIENT) ||
                    rawGradient.equals(BL2TR_GRADIENT) || rawGradient.equals(BR2TL_GRADIENT)) {
                gradientType = rawGradient;
            } else {
                System.err.println("Unrecognized gradient type: " + rawGradient);
            }

        }

        return gradientType;
    }

    public Integer getFidelity() {
        Integer fidelity = DEFAULT_FIDELITY;

        if (cliArgs.contains(FIDELITY_FLAG)) {
            try {
                int rawFidelity = Integer.parseInt(cliArgs.get(cliArgs.indexOf(FIDELITY_FLAG) + 1));

                if (rawFidelity > 0) {
                    fidelity = rawFidelity;
                } else {
                    System.err.println("Fidelity has to be a positive number");
                }
            } catch (NumberFormatException e) {
                System.err.println("Fidelity needs to be a number");
            }
        }

        return fidelity;
    }

    public String getVendors() {
        String vendors = DEFAULT_VENDORS;

        if (cliArgs.contains(VENDOR_FLAG)) {
            String rawVendors = cliArgs.get(cliArgs.indexOf(VENDOR_FLAG) + 1);
            // Todo: check rawVendors are valid
            vendors = rawVendors;
        }

        return vendors;
    }
}
