import lights.AmbientLight;
import lights.Light;
import lights.PointLight;
import materials.Material;
import materials.MatteMaterial;
import shapes.Plane;
import shapes.Sphere;
import shapes.Triangle;
import utilities.Color;
import utilities.Point;
import utilities.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jarndt on 3/6/17.
 */
public class Runner {
    public static void main(String[] args) {
        Scene.getCamera().setHeight(500);
        Scene.getCamera().setWidth(500);
        Scene.getCamera().setDistance(50);
        int W = Scene.getCamera().getWidth(), H = Scene.getCamera().getHeight();
        double D = Scene.getCamera().getDistance();
        Sphere s = new Sphere(15,new Point<Double>(W/2.-14,H/2.,2*D));
        Material m = new MatteMaterial();
        m.setColor(Color.BLUE);
        s.setMaterial(m);
//        Scene.addShape(s);
        Sphere ss = new Sphere(15,new Point<Double>(W/2.+36,H/2.+30,2*D-D/4.));
        Material mm = new MatteMaterial();
        mm.setColor(Color.GREEN);
        ss.setMaterial(mm);
//        Scene.addShape(ss);
        double xdist = 0, disty = 20, up = 50, right = 50;
//        Scene.addShape(new Triangle(0.+right,H+up,D,W+right,H+up,D,W/2.+right,H+up, 40*D));
        Scene.addShape(new Plane(new Vector(0.,0.5,1.),new Point<Double>(0.,(double)H*3/2.-79,0.)));
//        Scene.addLight(new AmbientLight(.7));
        Scene.addLight(new PointLight(W/2.-xdist,H/2.-disty, 2*D-2.5*D,1, 1));
        Scene.render();
    }

}
