package materials;

import lights.Light;
import utilities.Color;
import utilities.Point;
import utilities.Ray;
import utilities.Vector;

/**
 * Created by jarndt on 3/8/17.
 */
public class MatteMaterial extends Material {
    @Override
    public Color getColor(Ray lightRay, Light l) {
        return super.getColor().add(l.getMaterial().getColor()).multiply(l.getIntensity());
    }

    @Override
    public Ray getReflectanceRay(Vector normal, Point<Double> intersectionPoint) {
        return null;
    }
}
