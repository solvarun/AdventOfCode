package Day2;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class RockPaperScissor {

    /*Win - 6 points,
    * Draw - 3 points,
    * Lose - 0 points.*/

    /*A,X -> Rock
    * B,Y - > Paper
    * C,Z - > Scissors*/
    static Map<String,Integer> shape = Map.of(
            "B X",1, 
            "C Y",2,
            "A Z",3,
            "A X",4,
            "B Y",5,
            "C Z",6,
            "C X",7,
            "A Y",8,
            "B Z",9);

    /*A -> Rock,
    * B -> Paper,
    * C -> Scissor,
    * X -> Lose,
    * Y -> Draw
    * Z -> Win.*/
    static Map<String,Integer> outcome = Map.of("A X",3,
            "B X",1,
            "C X",2,
            "A Y",4,
            "B Y",5,
            "C Y",6,
            "A Z",8,
            "B Z",9,
            "C Z",7);

    /*String constants to check how score will be calculated. String defines the nature of second input in every line of input.*/
    static final String SHAPE = "Shape";
    static final String OUTCOME = "Outcome";
    static final String USER_DIR = System.getProperty("user.dir");

    /*Method to calculate score based on 2nd input type.*/
    static int calculateScore(List<String> list,String type){
        if (SHAPE.equals(type)){
            /*score if 2nd input is shape.*/
             return list.stream().
                     map(line -> "" + line).
                     mapToInt(shape :: get).sum();
        }else {
            /*score if second input is outcome.*/
            return list.stream().
                    map(line -> "" + line).
                    mapToInt(outcome::get).sum();
        }
    }
    public static void main(String[] args) {
        try {
            final String PATH = USER_DIR + "\\src\\Day2\\challenge2.txt";
            /*Reading all the inputs at once and storing it in a List of Strings that can be iterated later*/
            List<String> inputs = Files.readAllLines(Path.of(PATH));

            /*Total score based on Shape being second input*/
            int shapeScore = calculateScore(inputs,SHAPE);

            /*Total score based on Outcome being second input*/
            int outcomeScore = calculateScore(inputs,OUTCOME);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}