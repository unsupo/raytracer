package utilities;

/**
 * Created by jarndt on 3/6/17.
 */
public class Vector{
    public static Point<Double> ORIGIN = new Point<Double>(0.,0.,0.);

    private Point<Double> point1, point2, normalizedPoint;

    public Vector(Point<Double> point1, Point<Double> point2) {
        this.point1 = point1;
        this.point2 = point2;
        normalizedPoint = new Point<Double>(
                point2.getX()-point1.getX(),
                point2.getX()-point1.getX(),
                point2.getX()-point1.getX()
        );
    }public Vector(Point point1) {
        this.point1 = point1;
        this.point2 = ORIGIN;
        normalizedPoint = point1;
    }public Vector(Double x, Double y, Double z) {
        this.point1 = new Point<Double>(x,y,z);
        this.point2 = ORIGIN;
        normalizedPoint = this.point1;
    }

    public Double getMagnitude(){
        return Math.sqrt(
                normalizedPoint.getX()*normalizedPoint.getX() +
                        normalizedPoint.getY()*normalizedPoint.getY() +
                        normalizedPoint.getZ()*normalizedPoint.getZ()
        );
    }

    public Double dotProduct(Vector a){
        return a.normalizedPoint.getX()*normalizedPoint.getX()
                +a.normalizedPoint.getY()*normalizedPoint.getY()
                +a.normalizedPoint.getZ()*normalizedPoint.getZ();
    }

    public Vector crossProduct(Vector a){
        return new Vector(
                normalizedPoint.getY()*a.normalizedPoint.getZ()-a.normalizedPoint.getY()*normalizedPoint.getZ(),
                a.normalizedPoint.getX()*normalizedPoint.getZ()-normalizedPoint.getX()*a.normalizedPoint.getZ(),
                normalizedPoint.getX()*a.normalizedPoint.getY()-a.normalizedPoint.getX()*normalizedPoint.getY()
            );
    }

    public Vector add(Vector a){
        return new Vector(a.getNormalizedX()+getNormalizedX(),a.getNormalizedY()+getNormalizedY(),a.getNormalizedZ()+getNormalizedZ());
    }
    public Vector subtract(Vector a){
        return new Vector(getNormalizedX()-a.getNormalizedX(),getNormalizedY()-a.getNormalizedY(),getNormalizedZ()-a.getNormalizedZ());
    }public Vector subtract(Point<Double> a){
        return new Vector(getNormalizedX()-a.getX(),getNormalizedY()-a.getY(),getNormalizedZ()-a.getZ());
    }
    public Vector multiply(Double a){
        return new Vector(getNormalizedX()*a,getNormalizedY()*a,getNormalizedZ()*a);
    }
    public Vector divide(Double a){
        return new Vector(getNormalizedX()/a,getNormalizedY()/a,getNormalizedZ()/a);
    }

    @Override
    public String toString() {
        return "Vector{" +
                "point1=" + point1 +
                ", point2=" + point2 +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector vector = (Vector) o;

        if (point1 != null ? !point1.equals(vector.point1) : vector.point1 != null) return false;
        return point2 != null ? point2.equals(vector.point2) : vector.point2 == null;
    }

    @Override
    public int hashCode() {
        int result = point1 != null ? point1.hashCode() : 0;
        result = 31 * result + (point2 != null ? point2.hashCode() : 0);
        return result;
    }

    public Point getPoint1() {
        return point1;
    }

    public void setPoint1(Point point1) {
        this.point1 = point1;
    }

    public Point getPoint2() {
        return point2;
    }

    public void setPoint2(Point point2) {
        this.point2 = point2;
    }

    public Point<Double> getNormalizedPoint(){return normalizedPoint;}
    public Double getNormalizedX(){return normalizedPoint.getX();}
    public Double getNormalizedY(){return normalizedPoint.getY();}
    public Double getNormalizedZ(){return normalizedPoint.getZ();}

    public Vector normalized() {
        double mg = getMagnitude();
        return new Vector(getNormalizedX()/mg,getNormalizedY()/mg,getNormalizedZ()/mg);
    }
}
