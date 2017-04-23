package materials;

import lights.Light;
import utilities.Color;
import utilities.Point;
import utilities.Ray;
import utilities.Vector;

import java.util.logging.Logger;

/**
 * Created by jarndt on 3/6/17.
 */
abstract public class Material {
    private final static Logger LOGGER = Logger.getLogger(Material.class.getName());

    private Color color;

    public Color getColor() {
        if(this.color == null){
            color = Color.RED;
            LOGGER.info("USING DEFAULT COLOR: "+color);
        }

        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    abstract public Color getColor(Ray lightRay, Light l, Vector normal, Point<Double> intersectionPoint);
//    abstract public Ray getReflectanceRay(Vector normal, Point<Double> intersectionPoint);
}
