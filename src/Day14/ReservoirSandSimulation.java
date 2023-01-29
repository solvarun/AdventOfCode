package Day14;

import utils.Position;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;

public class ReservoirSandSimulation {
    static final String user_dir = System.getProperty("user.dir");
    /*HashSet to keep track of all the positions where either sand or rocks are present.*/
    static HashSet<Position> rocksAndSand = new HashSet<>();
    /*Source of sand dropping into the reservoir.*/
    static Position sandSource = new Position(500,0);
    /*Lowest rock position from the given input*/
    static int lowestRock = Integer.MIN_VALUE;

    public static void main(String[] args) {
        try{
            String PATH = user_dir + "\\src\\Day14\\challenge14.txt";
            /*Reading the input in the form of a List of String for parsing.*/
            List<String> inputs = Files.readAllLines(Path.of(PATH));
            for (String input : inputs) // Mapping rocks from the given input.
                mapRocks(input.trim());

            /*Answers to both part of the problem. To get answer of one part, comment other one.*/
//            int sandCollected = sandIntoVoid();

            int sandCollectedWithBase = sandWithBase();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**This method returns the number of sand grains deposited on the rocks until there is no space left when there was a base present in the reservoir.
     * 
     * @return number of sand grains that fell from the source until there was no space left
     */
    private static int sandWithBase() { // PART -2
        int sandGrains = 0; // initialising result

        while (true){ // endless loop
            Position res = simulateSandWithBase(); // getting position of current sand grain.

            sandGrains += 1; // incrementing number of sand grains that fell from source
            if (res.equals(sandSource)) break;
        }
        return sandGrains; // returning result

        /*Number of sand grains that fell from source until there was no space left in the reservoir with a base are 27976.*/
    }

    /**This method returns the number of sand grains deposited on the rocks until the sand starts falling into the endless void.
     * 
     * @return number of sand grains that fell from the source until there was no space left
     */
    private static int sandIntoVoid() { // PART - 1
        int sandGrains = 0; // initialising result

        while (simulateSand()) //looping until there's space on rocks.
            sandGrains += 1; // incrementing number of sand grains that fell from source

        return sandGrains; // returning result

        /*Number of sand grains that settled down on rocks before falling into void are 1001.*/
    }

    /**This method returns the position of current sand grain that fell from the source.
     *
     * @return position of current sand grain
     */
    private static Position simulateSandWithBase(){
        Position curr = new Position(sandSource); // starting from source.

        if (rocksAndSand.contains(curr)) return curr; // if there's no space and sand is present till mouth of source.

        while (lowestRock >= curr.getY()) { // looping until lowest rock
            /*Checking if the space down of current position is occupied by rock or sand. If not move into that space.*/
            if (!rocksAndSand.contains(new Position(curr.getX(), curr.getY() + 1))) {
                curr = new Position(curr.getX(), curr.getY() + 1);
                continue;
            }
            /*Checking if the space down-left of current position is occupied by rock or sand. If not move into that space.*/
            if (!rocksAndSand.contains(new Position(curr.getX() - 1, curr.getY() + 1))) { // fall down and left
                curr = new Position(curr.getX() - 1, curr.getY() + 1);
                continue;
            }
            /*Checking if the space down-right of current position is occupied by rock or sand. If not move into that space.*/
            if (!rocksAndSand.contains(new Position(curr.getX() + 1, curr.getY() + 1))) {
                curr = new Position(curr.getX() + 1, curr.getY() + 1);
                continue;
            }
            break; // breaking when can't move further.
        }
        rocksAndSand.add(curr); // adding current position to HashSet to indicate position if filled.
        return curr; // returning current position.
    }

    /**This method is used to check for the possibility of movement of current sand grain.
     *
     * @return true if there's space for movement of current sand grain
     */
    private static boolean simulateSand() {
        Position curr = new Position(sandSource); // starting from source.

        while(lowestRock >= curr.getY()) { // looping until lowest rock
            /*Checking if the space down of current position is occupied by rock or sand. If not move into that space.*/
            if (!rocksAndSand.contains(new Position(curr.getX(), curr.getY() + 1))) { // fall down
                curr = new Position(curr.getX(), curr.getY() + 1);
                continue;
            }
            /*Checking if the space down-left of current position is occupied by rock or sand. If not move into that space.*/
            if (!rocksAndSand.contains(new Position(curr.getX() - 1, curr.getY() + 1))) { // fall down and left
                curr = new Position(curr.getX() - 1, curr.getY() + 1);
                continue;
            }
            /*Checking if the space down-right of current position is occupied by rock or sand. If not move into that space.*/
            if (!rocksAndSand.contains(new Position(curr.getX() + 1, curr.getY() + 1))) { // fall down and right
                curr = new Position(curr.getX() + 1, curr.getY() + 1);
                continue;
            }
            rocksAndSand.add(curr); // adding current position to HashSet to indicate position if filled.
            return true;// returning true since we were able to move.
        }
        return false; // returning false when no movement of sand grain is possible as all positions are occupied.
    }

    /**This method is used to map the rocks.
     *
     * @param input - input to be used for mapping the rocks.
     */
    private static void mapRocks(String input) {
        String[] rockPosition = input.split("->"); // splitting the input.

        for (int i = 0; i < rockPosition.length - 1; i++) { // looping over string list.
            /*Creating a new position from where the mapping of rocks of current iteration needs to start.*/
            Position startsFrom = new Position(Integer.parseInt(rockPosition[i].split(",")[0].trim()),
                    Integer.parseInt(rockPosition[i].split(",")[1].trim()));
            /*Creating a new position for the mapping of rocks of current iteration needs to go to.*/
            Position rocksTill = new Position(Integer.parseInt(rockPosition[i + 1].split(",")[0].trim()),
                    Integer.parseInt(rockPosition[i + 1].split(",")[1].trim()));
            /*Calculating lowest position of rock by comparing Y-coordinate values of both start and end point.*/
            lowestRock = Math.max(lowestRock, Math.max(startsFrom.getY(), rocksTill.getY()));
            fillRocks(startsFrom,rocksTill);
        }
    }

    /**This method is used to fill the rocks into ROCKS_AND_SAND HashSet for later operations.
     *
     * @param startsFrom starting position
     * @param rocksTill ending position
     */
    private static void fillRocks(Position startsFrom, Position rocksTill) {
        boolean movementPlaneX = (startsFrom.getX() != rocksTill.getX()); // variable to check if plane of movement is X-axis while mapping rocks.
        int start = (movementPlaneX) ? Math.min(startsFrom.getX(), rocksTill.getX())
                : Math.min(startsFrom.getY(), rocksTill.getY()); // calculating starting point of current rock line.
        int end = (movementPlaneX) ? Math.max(startsFrom.getX(), rocksTill.getX())
                : Math.max(startsFrom.getY(), rocksTill.getY()); // calculating ending point of current rock line.

        for (int i = start; i <= end; i++) { // looping over range.
            /*Creating a new position base on boolean variable and value of i.*/
            Position curr = movementPlaneX ? new Position(i, startsFrom.getY())  // if movement plane is X, X-axis values will increase after each iteration.
                    : new Position(startsFrom.getX(), i); // else Y-axis values will increase.
            rocksAndSand.add(curr); // adding current position to HashSet.
        }
    }
}
