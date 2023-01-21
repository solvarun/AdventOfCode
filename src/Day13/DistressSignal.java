package Day13;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*Class to create a pair of the Lists to be compared for being in right order.*/
class NumListPair{
    List<Object> first; // first list
    List<Object> second; // second list

    /* Constructor*/
    public NumListPair() {
        first = new ArrayList<>();
        second = new ArrayList<>();
    }

}

public class DistressSignal {
    static final String user_dir = System.getProperty("user.dir");

    /*List<Object> to store all the pairs for comparison.*/
    static ArrayList<NumListPair> pairs = new ArrayList<>();

    /*List<Object> to store only the numbers from each list.*/
    static ArrayList<List<Integer>> unsortedList = new ArrayList<>();


    public static void main(String[] args) {
        String PATH = user_dir + "\\src\\Day13\\challenge13.txt";
        try {
            /*Using NIO to convert the input from file to a list of Strings.*/
            List<String> inputs = Files.readAllLines(Path.of(PATH));
            int idx = 0;
            while (idx < inputs.size()) {
                idx = parseList(inputs, idx); // parsing input
            }

            int distressSignal = getDistressSignal(); // solution to part 1.

            /*Adding [[2]] and [[6]] to unsortedList<Object> for further calculation.*/
            unsortedList.add(List.of(2));
            unsortedList.add(List.of(6));
            /*Sorting the unsortedList*/
            unsortedList.sort((a, b) ->
                    Integer.compare(a.get(0), b.get(0)));

            int dividerPackets = getDividerPackets(); // solution to part 2.
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**This method is used to find the sum of the indices of all the pairs that are in right order.
     *
     * @return sum of indices of pairs that are in correct order.
     */
    private static int getDistressSignal() {
        int result = 0; // initializing result

        for (int i = 0; i < pairs.size(); i++)
            if (Boolean.TRUE.equals(compare(pairs.get(i).first, pairs.get(i).second))) // comparing the lists of a pair to check for all the valid scenarios.
                result += i + 1; // 1-based indexing
        return result; // returning result

        /*Result to part 1 of the problem is 5003.*/
    }

    /**This method is used to find the product of divider packets in the inputs when sorted by comparing first integer of each list.
     *
     * @return Product of position of Divider Packets 2 and 6.
     */
    private static int getDividerPackets() {
        int dividerPacket2 = 0, dividerPacket6 = 0; // variables to keep track of [[2]] and [[6]].
        for (int i = 0; i < unsortedList.size(); i++) {
            if (dividerPacket2 * dividerPacket6 > 0) break; // if both variables have values greater than 1 we have found the solution.
            /*First occurrence of a list having first number greater than 1 with variable dividerPacket2 being 0.*/
            if (unsortedList.get(i).get(0) > 1 && dividerPacket2 == 0)
                dividerPacket2 = i + 1; // 1- based indexing.
            /*First occurrence of a list having first number greater than 1 with variable dividerPacket2 not being 0 and dividerPacket6 being 0.*/
            if (unsortedList.get(i).get(0) > 5 && dividerPacket2 != 0 && dividerPacket6 == 0)
                dividerPacket6 = i + 1; // 1- based indexing.
        }
        return dividerPacket2 * dividerPacket6; // returning result.

        /*Result to part 2 of the problem is 20280.*/
    }

    /** This method is used to parse the input in the form of List containing integers or Lists. Form a pair out of two given lists and add it to the ArrayList<Object> pair.
     *
     * @param inputs - Input in form of list of Strings.
     * @param idx - current index in inputs
     * @return index after operation is performed
     */
    private static int parseList(List<String> inputs, int idx) {
        if (!inputs.get(idx).isBlank()) { // excluding blank lines separating pairs.
            NumListPair current = new NumListPair(); // creating pair object
            idx = parseNumberList(inputs.get(idx).trim(), current.first, idx); // parsing first list
            idx = parseNumberList(inputs.get(idx).trim(), current.second, idx); // parsing second list
            pairs.add(current); // adding the pair to pairList.
            return idx; // returning index
        } else return idx + 1; // returning next index in case of blank line.
    }


    /**This method is used to parse current input into a List containing Integers or Lists.
     *
     * @param input - current input String.
     * @param idx - current index in inputs
     * @param list - list for which parsing is being done.
     * @return index after operation is performed
     */
    private static int parseNumberList(String input, List<Object> list, int idx) {
        parseNumbers(input); // creating a list containing integers only for second part of the problem.
        List<Object> curr = list; // variable to keep track of current list.
        List<Object> prev = null; // variable to keep track of parent list
        int strIdx = 1; // variable to keep index of string character.

        while (strIdx < input.length()) {
            char ch = input.charAt(strIdx); // current character
            /* [ (91) marks the start of a list.*/
            if (ch == 91) {
                prev = curr; // making current list as parent
                curr = new ArrayList<>(); // creating new list and making it current
                prev.add(curr); // adding current list o parent.
            }
            if (ch == 93) /* ] (93) marks the start of a list.*/
                curr = prev; // making parent as current

            /*If current character is integer adding it to current List.*/
            if (Character.isDigit(ch))
                mapInteger(input, curr, strIdx, ch);
            strIdx++; // incrementing index
        }
        return idx + 1; //returning index.
    }

    /**This method is used to create a list containing integers only for second part of the problem.
     * 
     * @param input - current inout string.
     */
    private static void parseNumbers(String input) {
        List<Integer> list = new ArrayList<>(); // creating list
        int strIdx = 1; // variable to keep track of index of character odf string

        while (strIdx < input.length()) {
            char ch = input.charAt(strIdx); // current character
            if (Character.isDigit(ch))  // adding integers to list
                mapInteger(input, Collections.singletonList(list), strIdx, ch);
            /*[ (93) marks end of a list. In case the first element in the string is an empty list, adding -1 for later comparison.*/
            if (ch == 93) list.add(-1);
            strIdx++; // incrementing string index
        }
        unsortedList.add(list); // adding list formed to unsortedList.
    }

    /**This method is used to map integers to the specified list.
     *
     * @param input - current input string
     * @param curr - current list to which integer will be added
     * @param strIdx - index of current character in string
     * @param ch - current character
     */
    private static void mapInteger(String input, List<Object> curr, int strIdx, char ch) {
        int num; // declaring variable to store the number .
        /*If a double-digit number is present. Only checking for 1 since we only have 10 in this example.
         * For any double-digit number, first condition can be replaced with a digit check for current character.*/
        if (ch == '1' && strIdx + 2 < input.length() && input.charAt(strIdx + 1) == '0')
            num = Integer.parseInt(input.substring(strIdx, strIdx + 2)); // parsing number
        else num = ch - '0'; // converting number to original value since integers start from '0' character in ASCII table.
        curr.add(num); // adding number to current list
    }

    /**This method is used to compare the two given lists over the applicable conditions for being in right order.
     * 
     * @param first - first list
     * @param second - second list
     * @return Boolean result after comparing the two given lists for correct order.
     */
    private static Boolean compare(List<Object> first, List<Object> second) {
        for (int i = 0; i < first.size(); i++) {
            if (second.size() <= i) return Boolean.FALSE; // if the second list ends first, not in right order.
            /*If at-least one of two is a list.*/
            if (first.get(i) instanceof List || second.get(i) instanceof List) {
                List innerFirst; // first list
                List innerSecond; // second list
                if (first.get(i) instanceof Integer) // if current index is Integer, wrapping current to form list.
                    innerFirst = wrapList((Integer) first.get(i));
                else innerFirst = (List) first.get(i); // otherwise fetching list.
                
                if (second.get(i) instanceof Integer) // if current index is Integer, wrapping current to form list.
                    innerSecond = wrapList((Integer) second.get(i));
                else innerSecond = (List) second.get(i); // otherwise fetching list.
                
                /*Comparing the above two lists.*/
                Boolean res = compare(innerFirst, innerSecond);
                return Boolean.TRUE.equals(res); // returning result of comparison
            } else {
                //Comparing each integer.
                if ((int) first.get(i) < (int) second.get(i)) return true;
                if ((int) first.get(i) > (int) second.get(i)) return false;
            }
        }
        /*Last check for size of List<Object> first being not equal to second.*/
        return first.size() != second.size() ? Boolean.TRUE : null;
    }

    /**This method is used to wrap given integer into a list for comparison.
     * 
     * @param num - number to be wrapped into list 
     * @return wrapped integer
     */
    private static List<Object> wrapList(Integer num) {
        List<Object>  newList= new ArrayList<>(); // creating list
        newList.add(num); // adding number to list
        return newList; // returning list
    }
}
