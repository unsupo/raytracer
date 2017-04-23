package shapes;

import utilities.Point;
import utilities.Ray;
import utilities.Vector;

/**
 * Created by jarndt on 3/6/17.
 */
public class Triangle extends Shape {
    private static final double EPSILON = 0.000001;
    Point<Double> point1, point2, point3, center;
    Double radius;

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
    public Point<Double> getCenter(){
        if(center == null)
            center = new Point<>(
                (point1.getX()+point2.getX()+point3.getX())/3.,
                (point1.getY()+point2.getY()+point3.getY())/3.,
                (point1.getZ()+point2.getZ()+point3.getZ())/3.
            );
        return center;
    }

    private boolean check(Point<Double> p1, Point<Double> p2, Point<Double> a, Point<Double> b){
        Vector c = new Vector(b.subtract(a));
        Vector cp1 = c.crossProduct(new Vector(p1.subtract(a))),
                cp2 = c.crossProduct(new Vector(p2.subtract(a)));
        return cp1.dotProduct(cp2) >= 0;
    }
    @Override
    public boolean contains(Point<Double> point) {
        return check(point,point1,point2,point3) && check(point,point2,point1,point3)
                && check(point,point3,point1,point2);
    }

    @Override
    public Double getRadius() {
        if(radius == null) {
            Double m1 = new Vector(getCenter().subtract(point1)).getMagnitude(),
                    m2 = new Vector(getCenter().subtract(point2)).getMagnitude(),
                    m3 = new Vector(getCenter().subtract(point3)).getMagnitude();
            Double max = m1;
            if(m2 > max)
                max = m2;
            if(m3 > max)
                max = m3;
            radius = max;
        }
        return radius;
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
    public static class Intersection{//Möller–Trumbore intersection algorithm
        public boolean intersected = false;
        public Double distance;
        public Double u;
        public Double v;
        public static Intersection createIntersection(Double[] origin, Double directionX, Double directionY, Double directionZ, Triangle f){
            Double[] direction = {directionX,directionY,directionZ};
            return new Intersection(origin,direction,f);
        }
        public static Intersection createIntersection(Double originX, Double originY, Double originZ, Double directionX, Double directionY, Double directionZ, Triangle f){
            Double[] origin = {originX,originY,originZ};
            Double[] direction = {directionX,directionY,directionZ};
            return new Intersection(origin,direction,f);
        }
        public static final float EPSILON = .000001f;
        public static void cross(Double[] dest,Double[] v1,Double[] v2){
            dest[0] = v1[1]*v2[2]-v1[2]*v2[1];
            dest[1] = v1[2]*v2[0]-v1[0]*v2[2];
            dest[2] = v1[0]*v2[1]-v1[1]*v2[0];
        }
        public static Double dot(Double[] v1,Double[] v2){
            return v1[0] * v2[0] + v1[1] * v2[1] + v1[2] * v2[2];
        }
        public static void sub(Double[] dest, Double[] v1, Double[] v2){
            dest[0] = v1[0] - v2[0];
            dest[1] = v1[1] - v2[1];
            dest[2] = v1[2] - v2[2];
        }
        public Intersection(Double[] orig, Double[] dir, Triangle f){
            Double t;
            Double[] vert0 = {f.point1.getX(),f.point1.getY(),f.point1.getZ()};
            Double[] vert1 = {f.point2.getX(),f.point2.getY(),f.point2.getZ()};
            Double[] vert2 = {f.point3.getX(),f.point3.getY(),f.point3.getZ()};
            Double[] edge1 = new Double[3];
            Double[] edge2 = new Double[3];
            Double[] tvec = new Double[3];
            Double[] pvec = new Double[3];
            Double[] qvec = new Double[3];
            Double det;
            Double inv_det;
            sub(edge1, vert1, vert0);
            sub(edge2, vert2, vert0);
            cross(pvec, dir, edge2);
            det = dot(edge1, pvec);
            if(det > -EPSILON && det < EPSILON)return;
            inv_det = 1f / det;
            sub(tvec, orig, vert0);
            u = dot(tvec, pvec) * inv_det;
            if(u < 0 || u > 1)return;
            cross(qvec, tvec, edge1);
            v = dot(dir, qvec) * inv_det;
            if(v < 0 || u + v > 1)return;
            t = dot(edge2, qvec) * inv_det;
            distance = t;
            intersected = true;
        }
    }
    public double intersects(Ray ray) {
        Point<Double> p1 = ray.getStart().getNormalizedPoint(),
                        p2 = ray.getEnd().getNormalizedPoint();
        Intersection s = new Intersection(new Double[]{p1.getX(),p1.getZ(),p1.getZ()},new Double[]{p2.getX(),p2.getZ(),p2.getZ()},this);
        return s.intersected?s.distance:-1;

        /*
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
        */
    }
}
