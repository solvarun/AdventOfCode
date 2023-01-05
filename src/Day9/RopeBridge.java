package Day9;

import utils.Position;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


public class RopeBridge {
    static final String user_dir = System.getProperty("user.dir");

    /*This set will keep track of all the positions tail has been at once*/
    static HashSet<Position> tailTrack = new HashSet<>();

    /*This set will keep track of 9th knot tail has been at once*/
    static HashSet<Position> knot9Track = new HashSet<>();

    static ArrayList<Position> knots = new ArrayList<>(Collections.nCopies(9,new Position(0,0)));

    public static void main(String[] args) {
        String PATH = user_dir + "\\src\\Day9\\challenge9.txt";

        Position head = new Position(0,0); // Position of rope head.

        Position tail = new Position(0,0); // Position of rope tail (1st knot in case of part 2).

        /*Adding the starting position of tail to both maps.*/
        tailTrack.add(tail);
        knot9Track.add(tail);
        
        try {
            /*Storing inputs from the file into a list of Strings.*/
            List<String> inputs = Files.readAllLines(Path.of(PATH));

            /*Parsing the input and performing corresponding actions.*/
            for (String input : inputs)
                tail = moveRope(head, tail, input.trim()); // updating tail after every operation.

            /*Part 1 -> Only two knots are there in the rope - head and tail.
            Requirement is to find the number of positions tail has visited at least once during the operations given in input.
            Answer to part 1 is 5513 positions.*/
            int numberOfPositionsVisitedTail = tailTrack.size();

            /*Part 2 -> Ten knots are there in the rope - head and 9 trailing knots.
            Requirement is to find the number of positions last one has visited at least once during the operations given in input.
            Answer to part 1 is 2427 positions.*/
            int numberOfPositionsVisitedLastKnot = knot9Track.size();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**This method is used to parse and perform the operation based on the current input.
     *
     * @param head - position of head
     * @param tail - position of tail
     * @param input - current input
     * @return tail position after moving
     */
    static Position moveRope(Position head, Position tail, String input){
        char direction = input.split(" ")[0].charAt(0); // direction of movement.
        int steps = Integer.parseInt(input.split(" ")[1]); // number of steps.

        /*Variable to store movement in plane. Movement is positive if we are moving away from origin(when moving Right or Up),
        otherwise movement is negative.*/
        int update =
                (direction == 'R' || direction == 'U') ? 1 : -1;

        /*Variable to store the plane of movement. Plane is
        -> X-axis if direction is Right or Left
        -> Y- axis if direction is Up or Down.*/
        char plane =
                (direction == 'R' || direction == 'L') ? 'X' : 'Y';

        return move(head, tail, plane, steps, update); // returning current tail position.
    }

    /**This method is used to move the tail in different Directions for given number of steps.
     *
     * @param head - position of head
     * @param tail - position of tail
     * @param plane - plane of movement
     * @param steps - number of steps to be moved
     * @param update - movement behavior
     * @return tail position after moving
     */
    private static Position move(Position head, Position tail,char plane, int steps, int update){
        while (steps > 0){
            /*Updating X or Y coordinate of head based on plane and update.*/
            if (plane == 'Y') head.setY(head.getY() + update); // Plane Y
            else head.setX(head.getX() + update); // Plane X

            if (isTailNotClose(head,tail)) { // checking if tail is away from head or not.
                tail = moveTail(head,tail); // moving tail relative to head position.
                tailTrack.add(tail); // adding tail position to Hashmap.
            }
            moveKnots(tail); // for each step head and tail is moved, updating knots for second part of problem.
            steps -= 1;
        }
        return tail; // returning current tail position
    }

    /**This method is used to check if the last knot is moving or not
     *
     * @param tail - position of tail
     */
    private static void moveKnots(Position tail) {
        knots.set(0,tail); // setting the first knot to position of current tail

        /*For following knots checking if the previous knot is away from current one.*/
        for (int i = 1; i < knots.size(); i++) {
            Position previousKnot = knots.get(i - 1);
            Position currentKnot = knots.get(i);

            if (currentKnot.equals(previousKnot)) break; // if previous knot and current one is in the same place, last hasn't moved. So ending loop.

            if (isTailNotClose(previousKnot, currentKnot)) // If current knot is more than 1 coordinate away from previous one
                knots.set(i, moveTail(previousKnot, currentKnot)); // moving the current knot and updating its new value in the list containing all knots.
        }
        knot9Track.add(knots.get(8)); // adding position of knot in focus to hashmap.
    }

    /**This method is used to check if head is more than 1 coordinate away from tail.
     *
     * @param head - position of head
     * @param tail - position of tail
     * @return True if the tail is more than 1 coordinate away from head in any direction.
     */
    private static boolean isTailNotClose(Position head, Position tail) {
        return (Math.abs(head.getX() - tail.getX()) > 1)
                || (Math.abs(head.getY() - tail.getY()) > 1);
    }

    /**This method is used to move the tail according to the positioning of head.
     *
     * @param head - position of head
     * @param tail - position of tail
     * @return tail position after moving
     */
    private static Position moveTail(Position head, Position tail) {
        Position tailNow = new Position(tail.getX(), tail.getY()); // creating a local object.

        /*Case A - If head is more than 1 coordinate away from tail on X-axis.*/
        if (Math.abs(head.getX() - tailNow.getX()) > 1){
            /*Movement of tail in X-axis. Positive if head is ahead of tail and vice-versa*/
            tailNow.setX(tailNow.getX() + ((head.getX() - tail.getX() > 0) ? 1: -1));

            /*Diagonal movement cases*/
            if (head.getY() - tailNow.getY() > 0) //If head is above tail(higher in Y-axis)
                tailNow.setY(tailNow.getY() + 1);
            else if (head.getY() - tailNow.getY() < 0) //If head is below tail(lower in Y-axis)
                tailNow.setY(tailNow.getY() - 1);
        }

        /*Case B - If head is more than 1 coordinate away from tail on Y-axis.*/
        if (Math.abs(head.getY() - tailNow.getY()) > 1){
            /*Movement of tail in Y-axis. Positive if head is above of tail and vice-versa*/
            tailNow.setY(tailNow.getY() + ((head.getY() - tail.getY() > 0) ? 1 : -1));

            /*Diagonal movement cases*/
            if (head.getX() - tailNow.getX() > 0) //If head is ahead of tail(further right on X-axis)
                tailNow.setX(tailNow.getX() + 1);
            else if (head.getX() - tailNow.getX() < 0) //If head is behind tail(further left on X-axis)
                tailNow.setX(tailNow.getX() - 1);
        }
        return tailNow; // returning current tail position
    }
}
