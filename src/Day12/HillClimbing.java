package Day12;

import utils.Position;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

class Distance implements Comparable<Object> {
    int steps;
    Position position;

    public Distance(int steps, Position position) {
        this.steps = steps;
        this.position = position;
    }

    public int getSteps() {
        return steps;
    }

    public Position getPosition() {
        return position;
    }

    /**
     * @param o the object to be compared.
     * @return compared result of two distance objects
     */
    @Override
    public int compareTo(Object o) {
        if (o instanceof Distance){
            return Integer.compare(this.steps, ((Distance) o).getSteps());
        }
        return -22;
    }
}

public class HillClimbing {
    static final String user_dir = System.getProperty("user.dir");
    static char[][] heightMap;

    static Position start;

    static Position end = new Position(4,3);
    static boolean[][] visited;

    public static void main(String[] args) {
        String PATH = user_dir +"\\src\\Day12\\challenge12.txt";
        try{
            List<String> inputs = Files.readAllLines(Path.of(PATH));
            parseInput(inputs);
//            int  part1 = solve("START");
            int part2 = solve("END_AT_A");
            System.out.println(part2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static int solve(String s) {
        PriorityQueue<Distance> queue;

        queue = (s.equalsIgnoreCase("start")) ?
                 new PriorityQueue<>(List.of(new Distance(0,start))) : new PriorityQueue<>(List.of(new Distance(0,end)));

        while (true) {
           Distance curr = queue.poll();

            if (visited[curr.getPosition().getX()][curr.getPosition().getY()]) continue;
            visited[curr.getPosition().getX()][curr.getPosition().getY()] = true;

            if (s.equalsIgnoreCase("start")){
                if (curr.getPosition().equals(end))
                    return curr.getSteps();
            } else {
                if (heightMap[curr.getPosition().getX()][curr.getPosition().getY()] == 'a')
                    return curr.getSteps();
            }
            for (Position p : findNeighbors(curr.getPosition(), s))
                queue.add(new Distance(curr.getSteps() + 1, p));
        }
    }

    private static void parseInput(List<String> inputs) {
        int colLength = inputs.get(0).trim().length();
         heightMap = new char[inputs.size()][colLength];
         visited = new boolean[inputs.size()][colLength];

        for (int i = 0; i < inputs.size(); i++) {
            for (int j = 0; j < colLength; j++) {
                heightMap[i][j] = inputs.get(i).trim().charAt(j);
                visited[i][j] = false;
                if (inputs.get(i).trim().charAt(j) == 'S') start = new Position(i, j);
                else if (inputs.get(i).trim().charAt(j) == 'E') end = new Position(i, j);
            }
        }
    }

    private static List<Position> findNeighbors(Position position, String s){
        List<Position> neighbors = new ArrayList<>();

        for (Position p : List.of(new Position(1,0), new Position(0,1), new Position(-1,0), new Position(0, -1))) {
            Position neighbor = null;
            if (position.getX() + p.getX() >= 0 && position.getX() + p.getX() < heightMap.length
                    && position.getY() + p.getY() >= 0 && position.getY() + p.getY() < heightMap[0].length) {
                neighbor = new Position(position.getX() + p.getX(), position.getY() + p.getY());
            }
            if (neighbor != null) {
                if (s.equalsIgnoreCase("start")){
                    if (defineHeight(neighbor) <= defineHeight(new Position(position.getX(), position.getY())) + 1 && !neighbors.contains(neighbor))
                        neighbors.add(neighbor);
                } else {
                    if (defineHeight(neighbor) >= defineHeight(new Position(position.getX(), position.getY())) - 1 && !neighbors.contains(neighbor))
                        neighbors.add(neighbor);
                }
            }
        }
        return neighbors;
    }

    private static int defineHeight(Position p) {
        char ch = heightMap[p.getX()][p.getY()];

        return switch (ch) {
            case 'S' -> 0;
            case 'E' -> 25;
            default -> ch - 'a';
        };
    }
}
