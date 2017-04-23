package materials;

import lights.Light;
import scene.Scene;
import utilities.Color;
import utilities.Point;
import utilities.Ray;
import utilities.Vector;

/**
 * Created by jarndt on 4/23/17.
 */
public class ReflectantRefractantMaterial extends Material {
    double indexOfRefractionOriginal = 1, getIndexOfRefractionNew = 1;
    boolean reflects = true, refracts = true;

    public ReflectantRefractantMaterial() {    }

    public ReflectantRefractantMaterial(boolean reflects, boolean refracts) {
        this.reflects = reflects;
        this.refracts = refracts;
    }

    @Override
    public Color getColor(Ray lightRay, Light l, Vector normal, Point<Double> intersectionPoint) {
        Color color = super.getColor().add(l.getMaterial().getColor()).multiply(l.getIntensity());
        if(lightRay == null)
            return color;
        Vector start = new Vector(intersectionPoint,new Point<>(0.,0.,0.)).normalized();
        double c1 = normal.dotProduct(lightRay.getDirection()) * (-1);
        if(reflects) {
            Vector r1 = lightRay.getDirection().add(normal.multiply(2 * c1));
            color = color.add(Scene.sendRay(new Ray(start,r1)));
        }if(refracts) {
            double n = indexOfRefractionOriginal/getIndexOfRefractionNew;
            double c2 = Math.sqrt(1-n*n*(1-c1*c1));
            Vector r2 = lightRay.getDirection().multiply(n).add(normal.multiply(n*(c1-c2)));
            color = color.add(Scene.sendRay(new Ray(start,r2)));
        }
        return color;
    }
}
