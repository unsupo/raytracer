package shapes;

import utilities.Point;
import utilities.Ray;
import utilities.Vector;

/**
 * Created by jarndt on 3/6/17.
 */
public class Sphere extends Shape {
    double radius;
    Point<Double> center;

    public Sphere(double radius, Point<Double> center) {
        this.radius = radius;
        this.center = center;
    }

    @Override
    public Vector getNormal(Point<Double> intersectionPoint) {
        return new Vector(center.subtract(intersectionPoint).divide(radius));
    }

    public double intersects(Ray ray) {
        return intersectsSphere(ray, center,radius);
    }

    public static double intersectsSphere(Ray ray, Point<Double> center, double radius){
        Vector dist = new Vector(center).subtract(ray.getStart());
        double B = ray.getDirection().dotProduct(dist);
        double D = B*B-dist.dotProduct(dist)+radius*radius;
        if(D<0.)
            return -1;
        double t0 = B - Math.sqrt(D);
        double t1 = B+Math.sqrt(D);
        double t = Double.MAX_VALUE;
        if((t0>.1) &&(t0<t))
            t = t0;
        if((t1>.1)&&(t1<t))
            t = t1;
        return t;

//        Vector o = ray.getStart();
////        Double d = ray.getEnd().getMagnitude();
//        Vector u = o.subtract(ray.getEnd());
//        Vector l = u.divide(u.getMagnitude());
//        Vector oc = o.subtract(center);
//        double b = 2*l.dotProduct(oc);
//        double disc = b*b-(oc.getMagnitude()*oc.getMagnitude()-radius*radius);
//        if(disc < 0) return -1;
//        disc = Math.sqrt(disc);
//        double t0 = -b - disc;
//        double t1 = -b + disc;
//        double t = (t0<t1)?t0:t1;
//
//        return t;//o.add(l.multiply(t)).getNormalizedPoint();

//        return (ray.getEnd().subtract(ray.getStart())).multiply(t).add(ray.getStart()).getNormalizedPoint();
    }

    @Override
    public String toString() {
        return "Sphere{" +
                "radius=" + radius +
                ", center=" + center +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sphere sphere = (Sphere) o;

        if (Double.compare(sphere.radius, radius) != 0) return false;
        return center != null ? center.equals(sphere.center) : sphere.center == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(radius);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + (center != null ? center.hashCode() : 0);
        return result;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Point<Double> getCenter() {
        return center;
    }

    public void setCenter(Point<Double> center) {
        this.center = center;
    }
}