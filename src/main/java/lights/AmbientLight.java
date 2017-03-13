package lights;

import utilities.Point;
import utilities.Ray;

/**
 * Created by jarndt on 3/7/17.
 */
public class AmbientLight extends Light{
    public AmbientLight() {
        intensity = 1;
        LOGGER.info("USING DEFAULT AMBIENTLIGHT: "+this);
    }

    @Override
    public String toString() {
        return "AmbientLight{" +
                "intensity=" + intensity +
                '}';
    }

    public AmbientLight(double intensity) {
        this.intensity = intensity;
    }

    @Override
    public double intersects(Ray ray) {
        return 0;
    }
}
