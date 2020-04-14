import java.io.File;

public class Main {
    private static final String USAGE_MESSAGE = "Usage: java Main [MAC | FC] [csp_file_path]";

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(USAGE_MESSAGE);
            System.exit(1);
        }

        File csp = new File(args[1]);
        // check if the given constraint file exists
        if (!csp.exists()) {
            System.out.println("File \"" + args[1] + "\" not exists!");
            System.exit(1);
        }

        // execute program with corresponding solver
        if (args[0].strip().equals("FC")) {
            new FC(args[1]);
        } else if (args[0].strip().equals("MAC")) {
            new MAC(args[1]);
        } else {
            System.out.println(USAGE_MESSAGE);
            System.exit(1);
        }
    }
}