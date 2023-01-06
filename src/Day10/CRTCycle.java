package Day10;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CRTCycle {
    static final String user_dir = System.getProperty("user.dir");
    /*Array to store value of register after each cycle.
     *Preset value of 241 as we only need values till cycle 240.*/
    static int[] registerValues = new int[241];
    /*Register value starts from 1.*/
    static int register = 1;

    public static void main(String[] args) {
        String PATH = user_dir + "\\src\\Day10\\";
        try {
            /*Setting the output from console to file so that the pattern will be stored in file.*/
            PrintStream out = new PrintStream(new FileOutputStream(PATH + "\\SecretCode.txt"));
            System.setOut(out);

            /*TTaking input in form of list of strings.*/
            List<String> inputs = Files.readAllLines(Path.of(PATH + "challenge10.txt"));

            /*Parsing the input and filling RegisterValue array.*/
            fillRegisterValues(inputs);

            /*Solutions to part 1 and 2 of problem.*/
            int signalStrengthSum = calculateSignalStrengthSum(registerValues);
            extractSecretCode(registerValues);
        }catch (Exception e) {
            throw  new RuntimeException(e);
        }
    }

    /**This method is used to calculate the sum of signal strengths for the given inputs using value of register at every 20th cycle.
     *  Signal Strength sum = Strength of signal at cycles (20, 60, 100, 140, 180, 220).
     *
     * @param registerValues - Array containing value of register after each cycle
     * @return sum of signal strength.
     */
    private static int calculateSignalStrengthSum(int[] registerValues) {
        int signalStrength = 0; // initializing result
        for (int i = 20; i < 221; i += 40) // for every 40th cycle starting from cycle 20.
            signalStrength += i * registerValues[i - 1]; // signalStrength for ith cycle = i * registerValues[i - 1]
        return signalStrength; // returning result

        /*Sum of Signal Strength is 11780.*/
    }

    /**This method is used to extract the Secret Code encrypted in the input to SecretCodeFile.
     * @param registerValues - Array containing value of register after each cycle
     */
    private static void extractSecretCode(int[] registerValues) {
        for (int i = 0; i < 240; i++) {
            int cycle = i % 40; // Cycle can only range from 1 to 40. Therefore, taking remainder of index i.
            if (cycle == 0 && i != 0) System.out.println(); //For every cycle when it's the 40th element(except the first time) in registerValue array, go to next line.
            int cycleValue = registerValues[i]; // corresponding value of register during ith cycle.
            if (cycle >= cycleValue-1 && cycle <= cycleValue + 1) System.out.print("##"); // if the cycle is in range of (cycleValue - 1, cycleValue + 1) add #
            else System.out.print("  "); //otherwise add space.
        }
    }

    /**
     * @param inputs - list of string representing input.
     */
    private static void fillRegisterValues(List<String> inputs) {
        int idx = 0; // starting index.
        for (String s : inputs) { // for each string
            idx = evaluate(s.trim(), idx); // evaluating current value of register and adding it to registerValues array.
            if (idx >= registerValues.length - 1) break; // breaking out of loop when 240 is reached.
        }
    }

    /**This method is used to evaluate the two input strings starting with "noop" or "add".
     * "noop" - does nothing. Value of register remains same for the cycle.
     * "add " - adds the latter integer value to register after 2 cycles.
     *
     * @param s - string to be evaluated.
     * @param idx - current index
     * @return updated index.
     */
    private static int evaluate(String s, int idx) {
        registerValues[idx] = register; // setting current value of register in current idx
        if (!s.equalsIgnoreCase("noop")) { // in case of add string input
            idx = evaluate("noop", idx); // add input waits for 1 cycle to perform its operation of adding.
            register += Integer.parseInt(s.split(" ")[1]); // adding the value to register.
        }
        if (idx < registerValues.length - 1) registerValues[++idx] = register; // if we have not reached 240 yet, setting register as value of next index.
        return idx; // returning current index.
    }
}
