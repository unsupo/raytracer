package shapes;

import utilities.Point;
import utilities.Ray;
import utilities.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jarndt on 4/22/17.
 */
public class Rectangle extends Shape {
    Triangle t1, t2;
    Point<Double> topLeft, topRight, bottomLeft, bottomRight;

    public Rectangle(Point<Double> topLeft, Point<Double> topRight, Point<Double> bottomLeft, Point<Double> bottomRight) {
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;

        t1 = new Triangle(topLeft,topRight,bottomLeft);
        t2 = new Triangle(bottomLeft,bottomRight,topRight);
    }

    public Rectangle(Triangle t1, Triangle t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    @Override
    public Vector getNormal(Point<Double> intersectionPoint) {
        return t1.getNormal(intersectionPoint).add(t2.getNormal(intersectionPoint));
    }

    @Override
    public double intersects(Ray ray) {
        //check if the first triangle is intercepted
        if(t1.intersects(ray) != -1)
            return t1.intersects(ray);
        //check if second triangle is intercepted
        if(t2.intersects(ray) != -1)
            return t2.intersects(ray);
        return -1;
    }
    List<Point> points;
    Point<Double> center;
    Double radius;
    @Override
    public Point<Double> getCenter() {
        if(center == null)
            center = t1.getCenter().add(t2.getCenter()).divide(2);
        return center;
    }

    @Override
    public boolean contains(Point<Double> point) {
        return t1.contains(point) || t2.contains(point);
    }

    public List<Point> getPoints(){
        if(points == null) {
            points = new ArrayList<>();
            for (Triangle t : Arrays.asList(t1, t2))
                points.addAll(Arrays.asList(t.point1, t.point2, t.point3));
        }
        return  points;
    }

    @Override
    public Double getRadius() {
        if(radius == null){
            List<Double> mags = getPoints().stream().map(a -> new Vector(center.subtract(a)).getMagnitude()).collect(Collectors.toList());
            Double max = Double.MIN_VALUE;
            for(Double d : mags)
                max = max > d ? max : d;
            radius = max;
        }
        return radius;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rectangle rectangle = (Rectangle) o;

        if (t1 != null ? !t1.equals(rectangle.t1) : rectangle.t1 != null) return false;
        if (t2 != null ? !t2.equals(rectangle.t2) : rectangle.t2 != null) return false;
        if (topLeft != null ? !topLeft.equals(rectangle.topLeft) : rectangle.topLeft != null) return false;
        if (topRight != null ? !topRight.equals(rectangle.topRight) : rectangle.topRight != null) return false;
        if (bottomLeft != null ? !bottomLeft.equals(rectangle.bottomLeft) : rectangle.bottomLeft != null) return false;
        if (bottomRight != null ? !bottomRight.equals(rectangle.bottomRight) : rectangle.bottomRight != null)
            return false;
        if (points != null ? !points.equals(rectangle.points) : rectangle.points != null) return false;
        if (center != null ? !center.equals(rectangle.center) : rectangle.center != null) return false;
        return radius != null ? radius.equals(rectangle.radius) : rectangle.radius == null;
    }

    @Override
    public int hashCode() {
        int result = t1 != null ? t1.hashCode() : 0;
        result = 31 * result + (t2 != null ? t2.hashCode() : 0);
        result = 31 * result + (topLeft != null ? topLeft.hashCode() : 0);
        result = 31 * result + (topRight != null ? topRight.hashCode() : 0);
        result = 31 * result + (bottomLeft != null ? bottomLeft.hashCode() : 0);
        result = 31 * result + (bottomRight != null ? bottomRight.hashCode() : 0);
        result = 31 * result + (points != null ? points.hashCode() : 0);
        result = 31 * result + (center != null ? center.hashCode() : 0);
        result = 31 * result + (radius != null ? radius.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Rectangle{" +
                "topLeft=" + topLeft +
                ", topRight=" + topRight +
                ", bottomLeft=" + bottomLeft +
                ", bottomRight=" + bottomRight +
                '}';
    }
}
