package Day1;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Calorie {
    static final String USER_DIR = System.getProperty("user.dir");

    private static List<Integer> calculateCalorieForEachElf(List<String> inputs) {
        List<Integer> sortedCalorieList = new ArrayList<>();
        int currentCalorie = 0;

        for(String s : inputs) {
            /*Adding calories until there is an empty line.*/
            if (!s.trim().isEmpty()) currentCalorie += Integer.parseInt(s);
            else {
                /*Adding total calories until now to calorieList and resetting currentCalories to 0 for next calculation.*/
                sortedCalorieList.add(currentCalorie);
                currentCalorie = 0;
            }
        }
        Collections.sort(sortedCalorieList);
        return sortedCalorieList;
    }

    public static void main(String[] args) {
        try {
            final String PATH = USER_DIR + "\\src\\Day1\\challenge1.txt";
            /*Reading all the inputs at once and storing it in a List of Strings that can be iterated later*/
            List<String> inputs = Files.readAllLines(Path.of(PATH));
            List<Integer> calorieWithEachElf = calculateCalorieForEachElf(inputs);
            /*Elf carrying most calories*/
            int mostCalories = calorieWithEachElf.get(calorieWithEachElf.size() - 1);

            /*Total calories with top 3 elves*/
            int caloriesWithTop3 = (calorieWithEachElf.get(calorieWithEachElf.size() - 1)
                    + calorieWithEachElf.get(calorieWithEachElf.size() - 2) + calorieWithEachElf.get(calorieWithEachElf.size() - 3));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
