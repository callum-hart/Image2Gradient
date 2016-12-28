package image_2_gradient;

public class ParamValidator {

    // Set finals --
    private static final String PASS_SYMBOL   = "\u2713";     // ✓
    private static final String PASS_COLOR    = "\u001B[32m"; // green
    private static final String FAIL_SYMBOl   = "\u2717";     // ✗
    private static final String FAIL_COLOR    = "\u001B[31m"; // red
    private static final String DEFAULT_COLOR = "\u001B[0m";  // default
    private static final Integer MIN_FIDELITY = 2;  // minimum number of color stops

    private Config config = new Config();

    ParamValidator() {}

    public boolean check(String rawGradient, Integer rawFidelity, String rawVendors) {
        boolean allValid          = false;
        boolean validGradientType = checkGadientType(rawGradient);
        boolean validFidelity     = checkFidelity(rawFidelity);
        boolean validVendors      = checkVendors(rawVendors);

        if (validGradientType && validFidelity && validVendors) {
            allValid = true;
        }

        return allValid;
    }

    // Validators --

    private boolean checkGadientType(String rawGradient) {
        boolean isValid = false;

        if (rawGradient.equals(config.getT2B()) || rawGradient.equals(config.getL2R()) ||
                rawGradient.equals(config.getBL2TR()) || rawGradient.equals(config.getBR2TL())) {
            isValid = true;

            pass("gradientType is valid");
        } else {
            fail("Unrecognized gradient type: " + rawGradient);
        }

        return isValid;
    }

    private boolean checkFidelity(Integer rawFidelity) {
        boolean isValid = false;

        if (rawFidelity >= MIN_FIDELITY) {
            isValid = true;
            pass("Fidelity is valid");
        } else {
            fail("Fidelity has to be 2 or above: " + rawFidelity);
        }

        return isValid;
    }

    private boolean checkVendors(String rawVendors) {
        boolean isValid = false;
        String[] vendors = rawVendors.split(",");

        for (String vendor: vendors) {
            if (vendor.equals(config.getWebkitIdentifier()) || vendor.equals(config.getMozIdentifier()) ||
                    vendor.equals(config.getOperaIdentifier())) {
                isValid = true;
            } else {
                fail("Unrecognized vendor: " + vendor);
                break;
            }
        }

        if (isValid) {
            pass("Vendors valid");
        }

        return isValid;
    }

    // Logs --

    private void pass(String message) {
        System.out.println(PASS_COLOR + String.format("[%s] %s", PASS_SYMBOL, message) + DEFAULT_COLOR);
    }

    private void fail(String message) {
        System.err.println(FAIL_COLOR + String.format("[%s] %s", FAIL_SYMBOl, message) + DEFAULT_COLOR);
    }
}

