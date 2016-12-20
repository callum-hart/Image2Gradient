import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.StreamSupport;

public class Config {

    // Set finals --
    private static final String T2B_GRADIENT   = "t2b"; // top to bottom (default)
    private static final String L2R_GRADIENT   = "l2r"; // left to right
    private static final String BL2TR_GRADIENT = "bl2tr"; // bottom left to top right
    private static final String BR2TL_GRADIENT = "br2tl"; // bottom right to top left

    // CLI --
    private static final String GRADIENT_FLAG  = "-t";
    private static final String FIDELITY_FLAG  = "-f";

    private List<String> cliArgs;

    public Config() {}

    public Config(String[] args) {
        cliArgs = Arrays.asList(args);
    }

    // Getters

    public String getT2bGradient() {
        return T2B_GRADIENT;
    }

    // Todo: added getters for other gradients

    // Process CLI args

    public String getImagePath() {
        return cliArgs.get(cliArgs.size() - 1);
    }

    public String getGradientType() {

        System.out.println("---------cliArgs---------");

        for (String arg: cliArgs) {
            System.out.println(arg);
        }

        System.out.println("---------/cliArgs---------");

        if (cliArgs.contains(GRADIENT_FLAG)) {
//            ListIterator<String> lit = cliArgs.listIterator(cliArgs.indexOf(GRADIENT_FLAG ));
//            String foo = lit.next(); // oddly next seems to return the current

            String foo = cliArgs.get(cliArgs.indexOf(GRADIENT_FLAG) + 1);
            // Todo check that foo is a valid gradient type
            return foo;
        } else {
            return T2B_GRADIENT;
        }
    }
}
