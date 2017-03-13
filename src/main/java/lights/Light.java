package lights;

import materials.Material;
import materials.MatteMaterial;
import utilities.Color;
import utilities.Point;
import utilities.Ray;
import utilities.Vector;

import java.util.logging.Logger;

/**
 * Created by jarndt on 3/6/17.
 */
abstract public class Light {
    public final static Logger LOGGER = Logger.getLogger(Light.class.getName());

    Vector normal;
    Material material;
    double intensity;

    public double getIntensity() {
        return intensity;
    }

    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }

    public Vector getNormal() {
        return normal;
    }

    public void setNormal(Vector normal) {
        this.normal = normal;
    }

    public Material getMaterial() {
        if(material == null){
            material = new MatteMaterial();
            material.setColor(Color.WHITE);
            LOGGER.info("USING DEFAULT FOR LIGHT MATERIAL: "+this);
        }
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }


    abstract public double intersects(Ray ray);


}
