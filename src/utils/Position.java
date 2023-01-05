package utils;

import java.util.Objects;

/*Utility class for 2-D positioning of a point.*/
public class Position{
    int X; // position on X-axis
    int Y; // position on Y-axis

    /*Constructor*/
    public Position(int x, int y) {
        X = x;
        Y = y;
    }
    /*Overriding equals method for comparing two positions.*/
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position position) return (X == position.X) && (Y == position.Y);
        return super.equals(obj);
    }

    /*Overriding hashCode method*/
    @Override
    public int hashCode() {
        return Objects.hash(X, Y);
    }
    /*Getters and Setters*/
    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

}
