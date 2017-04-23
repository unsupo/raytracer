package utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jarndt on 3/7/17.
 */
public class Point<T extends Number> {
    T x,y,z;
    ArrayList<T> values;

    public Point(T x, T y, T z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public ArrayList<T> getListOfPoints(){
        if(values == null)
            values = new ArrayList<T>(Arrays.asList(x,y,z));
        return values;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point<?> point = (Point<?>) o;

        if (x != null ? !x.equals(point.x) : point.x != null) return false;
        if (y != null ? !y.equals(point.y) : point.y != null) return false;
        return z != null ? z.equals(point.z) : point.z == null;
    }

    @Override
    public int hashCode() {
        int result = x != null ? x.hashCode() : 0;
        result = 31 * result + (y != null ? y.hashCode() : 0);
        result = 31 * result + (z != null ? z.hashCode() : 0);
        return result;
    }

    public T getX() {
        return x;
    }

    public void setX(T x) {
        this.x = x;
    }

    public T getY() {
        return y;
    }

    public void setY(T y) {
        this.y = y;
    }

    public T getZ() {
        return z;
    }

    public void setZ(T z) {
        this.z = z;
    }

    public Point<T> subtract(Point<T> point){
        return new Point<T>((T)Utility.subtractNumbers(x,point.getX()),(T)Utility.subtractNumbers(y,point.getY()),(T)Utility.subtractNumbers(z,point.getZ()));
    }public Point<T> divide(double scalar){
        return new Point<T>((T)Utility.divideNumbers(x,scalar),(T)Utility.divideNumbers(y,scalar),(T)Utility.divideNumbers(z,scalar));
    }

    public Point<T> add(Point<T> point) {
        return new Point<T>((T)Utility.addNumbers(x,point.getX()),(T)Utility.addNumbers(y,point.getY()),(T)Utility.addNumbers(z,point.getZ()));
    }

    public Point<T> multiply(double d) {
        return new Point<T>((T)Utility.multiplyNumbers(x,d),(T)Utility.multiplyNumbers(y,d),(T)Utility.multiplyNumbers(z,d));
    }
}
