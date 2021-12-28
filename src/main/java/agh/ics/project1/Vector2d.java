package agh.ics.project1;

import java.util.Objects;

public class Vector2d  {
    private final int x;
    private final int y;
    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }
    public boolean precedes(Vector2d position) {
        return (this.x <= position.x) && (this.y <= position.y);
    }
    public boolean follows(Vector2d position) {
        return (this.x >= position.x) && (this.y >= position.y);
    }
    public Vector2d add(Vector2d position) {
        return new Vector2d(this.x + position.x , this.y + position.y);
    }
    public Vector2d opposite() {
        return new Vector2d(-this.x,-this.y);
    }
    public Vector2d lowerLeft(Vector2d position) {
        int a = java.lang.Math.min(this.x, position.x);
        int b = java.lang.Math.min(this.y, position.y);
        return new Vector2d(a, b);
    }
    public Vector2d upperRight(Vector2d position) {
        int a = java.lang.Math.max(this.x, position.x);
        int b = java.lang.Math.max(this.y, position.y);
        return new Vector2d(a, b);
    }

    @Override
    public boolean equals(Object other){
        if (this == other)
            return true;
        else if (!(other instanceof Vector2d))
            return false;
        Vector2d vector = (Vector2d) other;
        return this.x == vector.x && this.y == vector.y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
