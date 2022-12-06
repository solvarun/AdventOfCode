package Day4;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/*Range class to be used for considering ranges.*/
class Range{
    int start;
    int end;

    public Range(String start, String end) {
        this.start = Integer.parseInt(start);
        this.end = Integer.parseInt(end);
    }
}

public class CampCleanup {
    static final String USER_DIR = System.getProperty("user.dir");

    /*Checks if range A already contains all the items in range B.*/
    static boolean rangeOverlaps(Range a, Range b) {
        return (a.start <= b.start) && (a.end >= b.end);
    }

    /*Checks if the ranges overlap at all.*/
    static boolean commonInRange(Range a, Range b) {
        return ((b.start <= a.end) && (b.end >= a.start));
    }

    public static void main(String[] args) {
        try {
            final String PATH = USER_DIR + "\\src\\Day4\\challenge4.txt";
            /*Reading all the inputs at once and storing it in a List of Strings that can be iterated later*/
            List<String> inputs = Files.readAllLines(Path.of(PATH));
            /*Variable to store saved effort when one elf is already covering second one's assigned tasks.*/
            int savedEffort = 0;
           /*Variable to store saved effort if even one item from the two ranges is same.*/
            int savedPairEffort = 0;
            for (String s : inputs) {
                /*first range is the left side of , eg - "2-9,4-5" -> first range is 2-9*/
                Range first = new Range(s.trim().split(",")[0].split("-")[0],
                        s.trim().split(",")[0].split("-")[1]);
                /*second range is the right side of , eg - "2-9,4-5" -> second range is 4-5*/
                Range second = new Range(s.trim().split(",")[1].split("-")[0],
                        s.trim().split(",")[1].split("-")[1]);
                /*If either range is inclusive of the other, add 1 to saved effort.*/
                if (rangeOverlaps(first, second)
                        || rangeOverlaps(second,first)) savedEffort += 1;
                /*If there is a common item, add 1 to saved pair effort.*/
                if (commonInRange(first,second)) savedPairEffort += 1;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
