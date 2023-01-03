package Day7;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class SpaceOptimization {
    static final String USER_DIR = System.getProperty("user.dir");

    public static void main(String[] args) {
        try{
            final String PATH = USER_DIR + "\\src\\Day7\\challenge7.txt";
            /*Reading all the inputs at once and storing it in a ArrayList of Strings that can be iterated later*/
            List<String> inputs = Files.readAllLines(Path.of(PATH));
            
            /*HashMap to store Parent Directory and its children.*/
            HashMap<String, ArrayList<String>> directories = new HashMap<>();
            
            /*To keep track of current directory we're in.*/
            ArrayList<String> currentPath = new ArrayList<>();

            /*Parsing the input*/
            for (int i = 0; i < inputs.size(); i++) {
                /*String from the input ArrayList*/
                String line = inputs.get(i);
                
                /*Splitting the current String to check for different commands.*/
                String[] parts = line.split(" ");

                /*Commands starting with $ cd refers to change in directories.*/
                if (line.startsWith("$ cd")) {
                    /* (..) implies that we are moving out of the current directory and going back to previous one.*/
                    if (parts[2].equals("..")) currentPath.remove(currentPath.size() - 1);
                    /*Otherwise, we move into the mentioned directory.*/
                    else currentPath.add(parts[2]);
                }

                /*Commands starting with $ ls indicates that the following commands are either a 
                 * directory wor a file which is a part of current directory.*/
                if (line.startsWith("$ ls")) {
                    /*Creating a list to keep record of all the contents of current directory.*/
                    ArrayList<String> contents = new ArrayList<>();
                    /*Joining the current path values with a space between them.*/
                    String path = String.join(" ", currentPath);

                    /*Parsing the list items*/
                    for (int j = i + 1; j < inputs.size(); j++) {
                        String[] output = inputs.get(j).split(" ");

                        /*If the string starts with $ it means change in directory and thus the list ends.*/
                        if (output[0].equals("$")) break;
                        /*If the string starts with dir, the current directory contains other directory. Name of this directory is added to contents.*/
                        else if (output[0].equals("dir")) contents.add(path + " " + output[1]);
                        /*If the string starts with a digit character, it's a file. Add it to contents as well.*/
                        else contents.add(output[0]);
                    }
                    /*Putting the contents into the HashMap.*/
                    directories.put(path, contents);
                }
            }

            /* Calculating solutions.*/
            long solutionPart1 = solveFirstPart(directories);
            long solutionPart2 = solveSecondPart(directories);

        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** Part 1 - Requirement is to find the sum of all the directories whose size is less than 1 million.
     *
     * @param directories - Hashmap containing all the directories and their children with files.
     * @return sum of all the directories whose size is less than 1 million.
     */
    private static long solveFirstPart(Map<String, ArrayList<String>> directories) {
        return directories.keySet().stream()//converting Key-set of directories map to stream
                .mapToLong(path -> directorySize(directories, path)) // calculating size of each directory.
                .filter(size -> size <= 100000) //filtering directories with size less tha 1 million.
                .sum(); // adding and returning the output.

    }

    /** Part 2 - Requirement is to find the directory which we can delete so that the operation which needs to be performed has enough space.
     * Total space is 70 million out of which we need at least 30 million for the operation.
     *
     * @param directories - Hashmap containing all the directories and their children with files.
     * @return directory just big enough to relieve space for operation
     */
    private static long solveSecondPart(Map<String, ArrayList<String>> directories) {
        long spaceRequired = 30000000; // minimum space required to perform operation
        long emptySpace = 70000000 - directorySize(directories, "/"); // calculating empty space by subtracting total size of root directory and subtracting with total storage available.
        long spaceToClear = spaceRequired - emptySpace; // space that needs to be relieved

        return directories.keySet().stream()//converting Key-set of directories map to stream
                .mapToLong(path -> directorySize(directories, path))// calculating size of each directory.
                .filter(size -> size >= spaceToClear) // checking if the size is greater that size to be relieved.
                .min().getAsLong(); // returning minimum from the above .
    }


    /**This method is used to find the size of the directory with the specified path.
     *
     * @param directories - Hashmap containing all the directories and their children with files.
     * @param path - path of directory for which size needs to be calculated.
     * @return size of the specified directory path.
     */
    private static long directorySize(Map<String, ArrayList<String>> directories, String path) {
        long size = 0; // variable to store size
        for (String content : directories.get(path)) {
            if (directories.containsKey(content)) size += directorySize(directories, content); // recursive call for children directories.
            else size += Integer.parseInt(content); //adding size of files present directly.
        }
        return size; // returning total size of directory.
    }
}
