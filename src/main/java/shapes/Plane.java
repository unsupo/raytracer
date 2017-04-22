package shapes;

import utilities.Point;
import utilities.Ray;
import utilities.Vector;

/**
 * Created by jarndt on 3/6/17.
 */
public class Plane extends Shape {
    Vector normal;
    Point<Double> point;

    public Plane(Vector normal, Point<Double> point) {
        this.normal = normal.normalized();
        this.point = point;
    }
    @Override
    public Point<Double> getCenter(){
        return point;
    }

    @Override
    public boolean contains(Point<Double> point) {
        return new Vector(point.subtract(this.point)).dotProduct(normal) == 0;
    }

    @Override
    public Double getRadius() {
        return Double.MAX_VALUE;
    }

    @Override
    public Vector getNormal(Point<Double> intersectionPoint) {
        return normal.add(new Vector(intersectionPoint));
    }

    public double intersects(Ray ray) {
        Vector O = ray.getStart();
        double denom = normal.dotProduct(ray.getDirection());
        if(Math.abs(denom) > 0.0001){
            double t = ray.getStart().subtract(point).multiply(-1.).dotProduct(normal)/denom;
            return t;//(ray.getEnd().subtract(ray.getStart())).multiply(t).add(ray.getStart()).getNormalizedPoint();
        }
        return -1;
    }

    @Override
    public String toString() {
        return "Plane{" +
                "normal=" + normal +
                ", point=" + point +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Plane plane = (Plane) o;

        if (normal != null ? !normal.equals(plane.normal) : plane.normal != null) return false;
        return point != null ? point.equals(plane.point) : plane.point == null;
    }

    @Override
    public int hashCode() {
        int result = normal != null ? normal.hashCode() : 0;
        result = 31 * result + (point != null ? point.hashCode() : 0);
        return result;
    }

    public Vector getNormal() {
        return normal;
    }

    public void setNormal(Vector normal) {
        this.normal = normal;
    }

    public Point<Double> getPoint() {
        return point;
    }

    public void setPoint(Point<Double> point) {
        this.point = point;
    }
}