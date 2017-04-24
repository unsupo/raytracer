package lights;

import utilities.Ray;

/**
 * Created by jarndt on 4/23/17.
 */
public class AreaLight extends Light {
    @Override
    public double intersects(Ray ray) {
        return 0;
    }
}
