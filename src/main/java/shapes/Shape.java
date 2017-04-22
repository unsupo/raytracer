package shapes;

import materials.Material;
import materials.MatteMaterial;
import utilities.*;

import java.util.logging.Logger;

/**
 * Created by jarndt on 3/6/17.
 */
abstract public class Shape {
    private final static Logger LOGGER = Logger.getLogger(Shape.class.getName());

    Vector normal;
    Material material;
    Color color;

    abstract public Vector getNormal(Point<Double> intersectionPoint);

    public Material getMaterial() {
        if(material == null){
            material = new MatteMaterial();
            LOGGER.info("USING DEFAULT MATERIAL FOR SHAPE: "+this);
        }
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }


    abstract public double intersects(Ray ray);

    abstract public Point<Double> getCenter();

    abstract public boolean contains(Point<Double> point);

    abstract public Double getRadius();
}
