package Day6;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TuningTrouble {
    static final String USER_DIR = System.getProperty("user.dir");

    public static void main(String[] args) {
        try {
            final String PATH = USER_DIR + "\\src\\Day6\\challenge6.txt";
            /*Reading all the inputs at once and storing it in a List of Strings that can be iterated later*/
            List<String> inputs = Files.readAllLines(Path.of(PATH));

            /*Since the input is only 1 line.*/
            String input = inputs.get(0);

            /*Answer to part 1 - 1582*/
            int tuningForPart1 = solvePart1(input);
            /*Answer to part 2 - 3588*/
            int tuningForPart2 = solvePart2(input);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**Part 1 - Requirement is to find the consecutive four characters in the String which do not have any repeating character.
     *
     * @param input - String input
     * @return Answer to part 1 of the problem.
     */
    private static int solvePart1(String input) {
        int idx = 0; // variable to keep track of current index
        HashSet<Character> tuning = new HashSet<>(); //Set to check for duplicates.

        /*Traversing the String to find possible output.*/
        while (idx < input.length()) {
            if (tuning.size() > 3) return idx; // initial check to know if we found the required String of length 4 with non-repeating characters.

            /*If any duplicate character is found, emptying the resetting the HashSet.*/
            if (tuning.contains(input.charAt(idx))) tuning = new HashSet<>();
            /*Adding the current character to the tuning Set.*/
            tuning.add(input.charAt(idx++));
        }
        return -1; // if no result is found, returning -1 to show no results found.
    }

    /**Part 2 - Requirement is to find the fourteen characters in the String which do not have any repeating character.
     *
     * @param input - String input
     * @return Answer to part 2 of the problem.
     */
    private static int solvePart2(String input) {
        int idx; // variable to keep track of current index
        HashSet<Character> tuning; //Set to check for duplicates.
        ArrayList<Character> tuningList = new ArrayList<>(); //List to keep track of elements to be added and removed.

        /*Adding the first 14 characters of the string to the list.*/
        for (idx = 0; idx < 14; idx++) tuningList.add(input.charAt(idx));

        /*Traversing the remaining characters to find possible solution.*/
        while(idx < input.length()) {

            /*For every iteration filling the HashSet with current 14 elements from the tuningList.*/
            tuning = new HashSet<>(tuningList);
            /*Returning current index if all 14 characters in tuningList are different.*/
            if (tuning.size() > 13) return idx;

            /*Adding current character o tuningList and removing the first character(which is out of scope now)
             to keep the size of list = 14.*/
            tuningList.add(input.charAt(idx++));
            tuningList.remove(0);
        }
        return -1;  // if no result is found, returning -1 to show no results found.
    }
}