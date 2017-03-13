package shapes;

import utilities.Point;
import utilities.Ray;
import utilities.Vector;

/**
 * Created by jarndt on 3/6/17.
 */
public class Triangle extends Shape {
    private static final double EPSILON = 0.000001;
    Point<Double> point1, point2, point3;

    public Triangle(Point<Double> point1, Point<Double> point2, Point<Double> point3) {
        this.point1 = point1;
        this.point2 = point2;
        this.point3 = point3;
    }public Triangle(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3){
        point1 = new Point<>(x1,y1,z1);
        point2 = new Point<>(x2,y2,z2);
        point3 = new Point<>(x3,y3,z3);
    }

    @Override
    public Vector getNormal(Point<Double> intersectionPoint) {
        Vector V = new Vector(point2.subtract(point1)),
                W = new Vector(point3.subtract(point1));
        return V.crossProduct(W);
    }

    @Override
    public String toString() {
        return "Triangle{" +
                "point1=" + point1 +
                ", point2=" + point2 +
                ", point3=" + point3 +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Triangle triangle = (Triangle) o;

        if (point1 != null ? !point1.equals(triangle.point1) : triangle.point1 != null) return false;
        if (point2 != null ? !point2.equals(triangle.point2) : triangle.point2 != null) return false;
        return point3 != null ? point3.equals(triangle.point3) : triangle.point3 == null;
    }

    @Override
    public int hashCode() {
        int result = point1 != null ? point1.hashCode() : 0;
        result = 31 * result + (point2 != null ? point2.hashCode() : 0);
        result = 31 * result + (point3 != null ? point3.hashCode() : 0);
        return result;
    }

    public double intersects(Ray ray) {
        Vector D = ray.getEnd(),
                O = ray.getStart();
        //find vectors for two edges sharing point1
        Vector e1 = new Vector(point2.subtract(point1)),
                e2 = new Vector(point3.subtract(point1));
        //begin determinant calculation used for calculating u as well
        Vector P = D.crossProduct(e2);
        //if determinant is near zero, ray lies in plane of triangle, or ray is parallel to plane of triangle
        double det = e1.dotProduct(P);
        //NOT CULLING
        if(det>-EPSILON && det < EPSILON) return -1;
        double inv_det = 1/det;
        //calculate distance from point1 to ray origin
        Vector T = O.subtract(point1);

        //calculate u parameter and test bound
        double u = T.dotProduct(P)*inv_det;

        //the intersection lies outside of triangle
        if(u<0 || u > 1) return -1;

        //prepare to test v parameter
        Vector Q = T.crossProduct(e1);
        //calculate V parameter and test bounds
        double v = D.dotProduct(Q)*inv_det;
        //intersection lies outside triangle
        if(v<0 || u+v > 1) return -1;

        //distance from origin
        double t = e2.dotProduct(Q)*inv_det;
        if(t< EPSILON) return -1;

//        Vector U = O.subtract(ray.getEnd());
//        Vector l = U.divide(U.getMagnitude());
//        return O.add(l.multiply(t)).getNormalizedPoint();
        return t;//(ray.getEnd().subtract(ray.getStart())).multiply(t).add(ray.getStart()).getNormalizedPoint();
    }
}
