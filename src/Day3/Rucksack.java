package Day3;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Rucksack {
    static final String USER_DIR = System.getProperty("user.dir");

    /*Method used to calculate priority based on common character among the next three
    * strings from the input list.*/
    static int prioritySumThree(List<String> list) {
        int result = 0;

        for (int i = 0; i < list.size(); i += 3) {
            result += findCommonCharacter(list.get(i), list.get(i + 1), list.get(i + 2));
        }
        return result;
    }

    /*This method is used to get Common Characters from two Strings*/
    static int findCommonCharacter(String a, String b, String c) {
        char matched = '!'; //default value.

        for (char ch : c.toCharArray()){
            if (a.contains(ch + "") && b.contains(ch + "")) {
                if (matched != '!') matched = getPriority(matched,ch);
                else matched = ch;
            }
        }
        return Character.isLowerCase(matched) ? (matched - 'a') + 1 : (matched - 'A') + 27;
    }

    /*This method is used to return the higher priority character.
    * Uppercase characters have more priority than Lowercase characters.*/
    static char getPriority(char matched, char ch){
        if(!Character.isUpperCase(matched)){
            if (!Character.isUpperCase(ch)){
                return (matched > ch) ? matched : ch;
            }
            return ch;
        }
        return (matched > ch) ? matched : ch;
    }

    /*This method is used to get Common Characters from two Strings*/
    static int findCommonCharacter(String a, String b){
        char matched = '!'; // default value.

        for (char ch : b.toCharArray()) {
            if (a.contains(ch + "")){
                if (matched != '!') matched = getPriority(matched,ch);
                else matched = ch;
            }
        }
        /*'a' -> 'z' have priority 1 -> 26.
        * 'A' -> 'Z' have priority 27 -> 52.*/
        return Character.isLowerCase(matched) ? (matched - 'a') + 1 : (matched - 'A') + 27;
    }

    /*Method used to calculate priority based on common character within two substrings of
    *  equal length derived from each string of the input list.
    * Example :  1st string of input -> "tablet" will lead to two substrings -> "tab" and "let"
    * priority is "t". (Priority is case sensitive.)*/
    static int prioritySumForEach(List<String> list){
        int result = 0;

        for (String s : list) {
            result += findCommonCharacter(s.substring(0,s.length()/2), //first half of string s
                    s.substring(s.length()/2)); //second half p+of string s
        }
        return result;
    }

    public static void main(String[] args) {
        try{
            final String PATH = USER_DIR + "\\src\\Day3\\challenge3.txt";
            /*Reading all the inputs at once and storing it in a List of Strings that can be iterated later*/
            List<String> inputs = Files.readAllLines(Path.of(PATH));
            /*Priority is calculated based on substrings having common character for each substring.*/
            int priorityCaseA = prioritySumForEach(inputs);
            /*Priority is calculated based on common character in 3 strings from input list.*/
            int priorityCaseB = prioritySumThree(inputs);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
