package Day8;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TreeTopTreehouse {
    static final String user_dir = System.getProperty("user.dir");

    public static void main(String[] args) {

        String PATH = user_dir + "\\src\\Day8\\challenge8.txt";

        try{
            List<String> inputs = Files.readAllLines(Path.of(PATH)); //reading input from file.

            /*Creating a 2-D Array for the forest to store the height of trees in it.*/
            int[][] forest = new int[inputs.size()][inputs.size()];

            int rowIdx = 0; // variable to keep track of current row.
            
            /*Creating the forest from input.*/
            for (String s : inputs) {
                for (int i = 0; i < s.trim().length(); i++) {
                    forest[rowIdx][i] = s.charAt(i) - '0'; // converting from char to int.
                }
                rowIdx += 1; // incrementing next row.
            }

            int visibleTrees = numberOfTreesVisible(forest);
            int bestScenicScore = bestScenicScore(forest);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**This method is used to count the number of trees visible from outside the forest.
     * 
     * @param forest - 2-D array representation of forest.
     * @return number of trees visible from outside the forest.
     */
    private static int numberOfTreesVisible(int[][] forest) {
        int visible = 0; // variable to store result.
        
        /*Checking for each tree if it's visible from outside of forest.*/
        for (int i = 0; i < forest.length; i++) {
            for (int j = 0; j < forest.length; j++) {
                int tree = forest[i][j]; // current tree
                
                /*Checking if the current tree is located at the outermost row or column of the forest*/
                if (( i == 0 || j == 0 ) 
                        || ( i == forest.length - 1 || j == forest.length - 1)) visible += 1;
                /*Checking if the tree is visible from any direction(top, bottom, left or right).*/
                else if ( isVisibleFromTop(tree,i,j,forest)
                        || isVisibleFromBottom(tree,i,j,forest) 
                        || isVisibleFromLeft(tree,i,j,forest) 
                        || isVisibleFromRight(tree,i,j,forest)) visible += 1;
            }
        }
        return visible; // returning result

        /*Number of visible trees from outside the forest are 1713.*/
    }

    /**This method is used to check if the current tree is visible from top-view.
     * 
     * @param tree - current tree
     * @param row - row of current tree
     * @param col - column of current tree
     * @param forest - 2-D array representation of forest.
     * @return True if the tree is visible from Top view.
     */
    private static boolean isVisibleFromTop(int tree, int row, int col, int[][] forest) {
        while (row > 0) 
            if (tree <= forest[--row][col]) return false; // checking if any tree is taller than current towards top
        return true;
    }

    /**This method is used to check if the current tree is visible from bottom-view.
     *
     * @param tree - current tree
     * @param row - row of current tree
     * @param col - column of current tree
     * @param forest - 2-D array representation of forest.
     * @return True if the tree is visible from Bottom view.
     */
    private static boolean isVisibleFromBottom(int tree, int row, int col, int[][] forest) {
        while (row < forest.length - 1) 
            if (tree <= forest[++row][col]) return false; // checking if any tree is taller than current towards bottom
        return true;
    }

    /**This method is used to check if the current tree is visible from left-view.
     *
     * @param tree - current tree
     * @param row - row of current tree
     * @param col - column of current tree
     * @param forest - 2-D array representation of forest.
     * @return True if the tree is visible from Left view.
     */
    private static boolean isVisibleFromLeft(int tree, int row, int col, int[][] forest) {
        while (col > 0) 
            if (tree <= forest[row][--col]) return false; // checking if any tree is taller than current towards left
        return true;
    }

    /**This method is used to check if the current tree is visible from right-view.
     *
     * @param tree - current tree
     * @param row - row of current tree
     * @param col - column of current tree
     * @param forest - 2-D array representation of forest.
     * @return True if the tree is visible from Right view.
     */
    private static boolean isVisibleFromRight(int tree, int row, int col, int[][] forest) {
        while (col < forest.length - 1) 
            if (tree <= forest[row][++col]) return false; // checking if any tree is taller than current towards right
        return true;
    }


    /**This method is used to calculate the best scenic view in the forest.
     * Scenic view refers to the number of trees that can be seen from the top of the current tree.
     * 
     * @param forest - 2-D array representation of forest.
     * @return Best scenic view from the forest.
     */
    private static int bestScenicScore(int[][] forest) {
        int scenicScore = Integer.MIN_VALUE; // initializing result.
        /*Calculating scenic view for each tree.*/
        for (int i = 0; i < forest.length; i++) {
            for (int j = 0; j < forest.length; j++) {
                int tree = forest[i][j]; // current tree
                /*Comparing current scenic score with previous max scored under scenicScore variable.*/
                scenicScore = Math.max(scenicScore,calculate(tree,i,j,forest));
            }
        }
        return scenicScore; // returning result

        /*Best Scenic Score int the given forest is 268464*/
    }

    /**This method is used to calculate the scenic view of current tree.
     *
     * @param tree - current tree
     * @param row - row of current tree
     * @param col - column of current tree
     * @param forest - 2-D array representation of forest.
     * @return scenic score of current tree.
     */
    private static int calculate(int tree, int row, int col, int[][] forest) {
        int score = 1; // initializing result
        
        /*Scenic score for a tree = (scenic score from bottom * scenic score from top * scenic score from left * scenic score from right)*/
        score *= scoreOnTop(tree,row,col,forest) * scoreOnBottom(tree,row,col,forest)
                * scoreOnLeft(tree,row,col,forest) * scoreOnRight(tree,row,col,forest);
        return score; /// returning result
    }

    /**This method is used to calculate the scenic view of current tree towards bottom of forest.
     *
     * @param tree - current tree
     * @param row - row of current tree
     * @param col - column of current tree
     * @param forest - 2-D array representation of forest.
     * @return scenic score of current tree towards bottom of forest.
     */
    private static int scoreOnBottom(int tree, int row, int col, int[][] forest) {
        int res = 0; // initializing result
        while (row < forest.length - 1) {
            res += 1; // increasing number of trees visible
            if (forest[++row][col] >= tree) break; // stop counting if tree is of same height or taller than current tree.
        }
        return res; // returning result
    }

    /**This method is used to calculate the scenic view of current tree towards top of forest.
     *
     * @param tree - current tree
     * @param row - row of current tree
     * @param col - column of current tree
     * @param forest - 2-D array representation of forest.
     * @return scenic score of current tree towards top of forest.
     */
    private static int scoreOnTop(int tree, int row, int col, int[][] forest) {
        int res = 0; // initializing result
        while (row > 0) {
            res += 1; // increasing number of trees visible
            if (forest[--row][col] >= tree) break; // stop counting if tree is of same height or taller than current tree.
        }
        return res; // returning result
    }

    /**This method is used to calculate the scenic view of current tree towards left of forest.
     *
     * @param tree - current tree
     * @param row - row of current tree
     * @param col - column of current tree
     * @param forest - 2-D array representation of forest.
     * @return scenic score of current tree towards bottom of forest.
     */
    private static int scoreOnLeft(int tree, int row, int col, int[][] forest) {
        int res = 0; // initializing result
        while (col > 0) {
            res += 1; // increasing number of trees visible
            if (forest[row][--col] >= tree) break; // stop counting if tree is of same height or taller than current tree.
        }
        return res; // returning result
    }

    /**This method is used to calculate the scenic view of current tree towards right of forest.
     *
     * @param tree - current tree
     * @param row - row of current tree
     * @param col - column of current tree
     * @param forest - 2-D array representation of forest.
     * @return scenic score of current tree towards bottom of forest.
     */
    private static int scoreOnRight(int tree, int row, int col, int[][] forest) {
        int res = 0; // initializing result
        while (col < forest.length - 1) {
            res += 1; // increasing number of trees visible
            if (forest[row][++col] >= tree) break; // stop counting if tree is of same height or taller than current tree.
        }
        return res; // returning result
    }
}
