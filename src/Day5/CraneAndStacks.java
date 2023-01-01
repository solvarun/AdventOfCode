package Day5;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CraneAndStacks {
    static final String USER_DIR = System.getProperty("user.dir");

    /*Hashmap to store input in organised form*/
    static HashMap<Integer, String> stringStack = new HashMap<>();

    /*List to store all the operations to be performed on the Stacks.*/
    static List<int[]> operationsList = new ArrayList<>();

    /**
     * This method is used to initialize the Hashmap to avoid NPE when a certain Stack is accessed.
     * In this case we know we only have 9 Stacks. We can also calculate number of Stacks by counting the lines of input
     * before we get a blank line to distinguish the input from operations.
     */
    static void initializeStacks(){
        for (int i = 1; i < 10; i++) stringStack.put(i, "");
    }

    public static void main(String[] args) {
        try {
            final String PATH = USER_DIR + "\\src\\Day5\\challenge5.txt";
            /*Reading all the inputs at once and storing it in a List of Strings that can be iterated later*/
            List<String> inputs = Files.readAllLines(Path.of(PATH));
            int idx = 0;
            initializeStacks();
            /*Forming a table for all the supplies stacked.*/
            while(idx < inputs.size()) {
                String curr = inputs.get(idx); // current string
                int stackIndex = 1; // keeping track of hte stack where current character will be added.

                if (inputs.get(idx + 1).isBlank()) {idx += 2;break;} // ending the loop when all characters are set.

                /*Filling the stringStack with input*/
                for (int i = 1; i < curr.length(); i += 4) {
                    /*Adding the current character to the respective Stack if it is not a space character.*/
                    stringStack.put(stackIndex,
                            (curr.charAt(i) == ' ' ? "" : curr.charAt(i)) + stringStack.get(stackIndex));
                    stackIndex++; // incrementing stack index.
                }
                idx += 1; // incrementing index.
            }
            /*Now only operations are remaining in the input.*/
            while (idx < inputs.size()) {
                /*Extracting operation from the string and adding it to the operationList.*/
                int[] operation = getOperation(inputs.get(idx++));
                operationsList.add(operation);
            }

            /*Uncomment either one and comment the other to get respective answer*/
//            solvePart1(stringStack,operationsList);
            solvePart2(stringStack,operationsList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**This method is used to parse the operation string to form an integer array containing all the info we need.
     * @param s - String to be parsed.
     * @return int[] containing the info inf following order : 0 -> no. of stacks to be moved, 2 -> moved from, 3 -> moved to.
     */
    static int[] getOperation(String s) {
        int[] op = new int[3]; // initializing result
        StringBuilder num = new StringBuilder(); // String to contain the number values of info from string
        int idx = 0; // internal index of array.
        /*Iterating over the string and finding the respective info.*/
        for (int i = 0; i < s.length(); i++) {
            char curr = s.charAt(i);
            if (Character.isDigit(curr)) num.append(curr); //digit check for current character
            /*If the curr is a space or if we have reached the end of the String with the num being not empty.*/
            if ((curr == ' ' || i == s.length() - 1)&& (num.length() > 0)) {
                /*Parse the num string and add it the output array.*/
                op[idx++] = Integer.parseInt(num.toString());
                num = new StringBuilder(); // resetting num for next values.
            }
        }
        return op;
    }

    /** Part 1 - For each operation we take the topmost element of the Stack and move it to other Stack (one at a time).
     *
     * @param stringStack - map with the index of Stack and values to be used for solving.
     * @param operationsList - list of operations to be performed on the StringStack.
     */
    static void solvePart1(HashMap<Integer, String> stringStack, List<int[]> operationsList) {
        StringBuilder answer = new StringBuilder(); //initializing result
        for (int[] a : operationsList){
            /*
            * a[0] - number of items to be moved
            * a[1] - Stack from which items are being moved
            * a[2] - Stack to which items are being moved.
            * */

            /*Number of items to be moved from Stack (a[1]) are selected and combined into a substring from the end.*/
            String toBeTransferred = stringStack.get(a[1]).substring(stringStack.get(a[1]).length() - a[0]);
            /*Since we are moving one at a time, it'll follow FIFO (the item we picked at first will be added first and
            * consecutive items on top of that). Reversing the substring for the same reason before adding.*/
            String addTo = stringStack.get(a[2]) + new StringBuilder(toBeTransferred).reverse();

            /*Updating from and to Strings in the Hashmap*/
            stringStack.put(a[1], stringStack.get(a[1]).substring(0, stringStack.get(a[1]).length() - a[0]));
            stringStack.put(a[2], addTo);
        }
        for (int i = 1; i < stringStack.size() + 1; i++)
            /*We only to show the top view of the Stacks i.e, The last character of each String.*/
            answer.append(stringStack.get(i).charAt(stringStack.get(i).length() - 1));

        /*Answer to part 1 is WCZTHTMPS*/
    }

    /** Part 2 - For each operation we take the topmost element of the Stack and move it to other Stack (one at a time).
     *
     * @param stringStack - map with the index of Stack and values to be used for solving.
     * @param operationsList - list of operations to be performed on the StringStack.
     */
    static void solvePart2(HashMap<Integer, String> stringStack, List<int[]> operationsList) {
        StringBuilder answer = new StringBuilder();
        for (int[] a : operationsList){
            /*
             * a[0] - number of items to be moved
             * a[1] - Stack from which items are being moved
             * a[2] - Stack to which items are being moved.
             * */

            /*Number of items to be moved from Stack (a[1]) are selected and combined into a substring from the end.*/
            String toBeTransferred = stringStack.get(a[1]).substring(stringStack.get(a[1]).length() - a[0]);
            /*Since we are moving one at a time, it'll follow FILO (the item we picked at first will be added last and
             * consecutive items before the current one which means the last picked will be placed at the bottom).
             * No need to reverse the substring.*/
            String addTo = stringStack.get(a[2]) + toBeTransferred;

            /*Updating from and to Strings in the Hashmap*/
            stringStack.put(a[1], stringStack.get(a[1]).substring(0, stringStack.get(a[1]).length() - a[0]));
            stringStack.put(a[2], addTo);
        }
        for (int i = 1; i < stringStack.size() + 1; i++)
            /*We only to show the top view of the Stacks i.e, The last character of each String.*/
            answer.append(stringStack.get(i).charAt(stringStack.get(i).length() - 1));

        /*Answer to part 2 is BLSGJSDTS*/
    }
}
