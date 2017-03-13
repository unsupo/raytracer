package lights;

import shapes.Sphere;
import utilities.Point;
import utilities.Ray;

/**
 * Created by jarndt on 3/9/17.
 */
public class PointLight extends Light {
    Point<Double> center;
    double intensity, radius;

    @Override
    public double intersects(Ray ray) {
        return Sphere.intersectsSphere(ray,center,radius);
    }

    @Override
    public String toString() {
        return "PointLight{" +
                "center=" + center +
                ", intensity=" + intensity +
                ", radius=" + radius +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PointLight that = (PointLight) o;

        if (Double.compare(that.intensity, intensity) != 0) return false;
        if (Double.compare(that.radius, radius) != 0) return false;
        return center != null ? center.equals(that.center) : that.center == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = center != null ? center.hashCode() : 0;
        temp = Double.doubleToLongBits(intensity);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(radius);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public Point<Double> getCenter() {
        return center;
    }

    public void setCenter(Point<Double> center) {
        this.center = center;
    }

    @Override
    public double getIntensity() {
        return intensity;
    }

    @Override
    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public PointLight(Point<Double> center) {
        this.center = center;
        intensity = 1;
        radius = 1;
    }public PointLight(double x, double y, double z){
        center = new Point<>(x,y,z);
        intensity = 1;
        radius = 1;
    }
    public PointLight(Point<Double> center, double intensity) {
        this.center = center;
        this.intensity = intensity;
        radius = 1;
    }public PointLight(double x, double y, double z, double intensity){
        center = new Point<>(x,y,z);
        this.intensity = intensity;
        radius = 1;
    }
    public PointLight(Point<Double> center, double intensity, double radius) {
        this.center = center;
        this.intensity = intensity;
        this.radius = radius;
    }public PointLight(double x, double y, double z, double intensity, double radius){
        center = new Point<>(x,y,z);
        this.intensity = intensity;
        this.radius = radius;
    }


}
