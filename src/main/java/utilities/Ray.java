package utilities;

/**
 * Created by jarndt on 3/6/17.
 */
public class Ray {
    private Vector start, end, magnitude;

    public Ray(Vector start, Vector end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "Ray{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ray ray = (Ray) o;

        if (start != null ? !start.equals(ray.start) : ray.start != null) return false;
        return end != null ? end.equals(ray.end) : ray.end == null;
    }

    @Override
    public int hashCode() {
        int result = start != null ? start.hashCode() : 0;
        result = 31 * result + (end != null ? end.hashCode() : 0);
        return result;
    }

    public Vector getStart() {

        return start;
    }

    public void setStart(Vector start) {
        this.start = start;
    }

    public Vector getEnd() {
        return end;
    }

    public Vector getDirection(){
        Vector normal= end.subtract(start);
        return normal.divide(normal.getMagnitude());
    }

    public void setEnd(Vector end) {
        this.end = end;
    }

    public Vector getMagnitude() {
        if(magnitude == null)
            magnitude =end.subtract(start);
        return magnitude;
    }
}
