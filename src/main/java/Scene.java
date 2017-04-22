import lights.AmbientLight;
import lights.Light;
import lights.PointLight;
import renders.Renderer;
import shapes.Shape;
import shapes.Sphere;
import utilities.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by jarndt on 3/6/17.
 */
public class Scene {
    private final static Logger LOGGER = Logger.getLogger(Scene.class.getName());

    private Scene(){}

    static public Scene getScene() {
        if(scene == null)
            scene = new Scene();
        return scene;
    }

    private static Scene scene;
    private Camera camera;
    private List<Shape> shapes;
    private List<Light> lights;
    private BVH bvh;

    static public void addShape(Shape shape){
        getScene().getShapesInstance().add(shape);
    }static public void addLight(Light light){
        getScene().getLightInstance().add(light);
    }

    public static List<Shape> getShapes() {
        if(getScene().shapes == null)
            getScene().shapes = new ArrayList<>();
        return getScene().shapes;
    }

    public static void setShapes(List<Shape> shapes) {
        getScene().shapes = shapes;
    }
    public static List<Light> getLights() {
        return getScene().lights;
    }

    public static void setLights(List<Light> lights) {
        getScene().lights = lights;
    }

    public static Camera getCamera() {
        return getScene().getCameraInstance();
    }

    public static void setCamera(Camera camera) {
        getScene().camera = camera;
    }

    public static BVH getBVH() {
        return getScene().bvh;
    }

    private static class HIT{
        double point;
        Shape shape;
        Light light;

        public HIT(double point, Shape shape) {
            this.point = point;
            this.shape = shape;
        }

        public HIT(double lightIntersectionPoint, Light l) {
            this.point = lightIntersectionPoint;
            this.light = l;
        }
    }

    public static List<HIT> getIntersectionPoints(Ray ray, HIT... hits){
        //send ray into shapes get the points of intersection
        List<HIT> intersectionPoints = getShapes().parallelStream()
                .map(a -> new HIT(a.intersects(ray),a))
                .filter(a -> a.point > 0)// && (Double)a.point.getZ() > getCamera().getDistance())
                .collect(Collectors.toList());

        if(hits != null)
            intersectionPoints.addAll(Arrays.asList(hits));

        Collections.sort(intersectionPoints,(a,b)->(int)(
                a.point-b.point
        ));
        return intersectionPoints;
    }

    public static Color sendRay(Ray ray) {
        //find all objects that the scene's ray intercepts with
        List<HIT> intersectionPoints = getIntersectionPoints(ray);

        //scene starts out as black
        Color c = Color.BLACK;
        if(intersectionPoints.isEmpty())
            return c;
        Shape s = intersectionPoints.get(0).shape;
        double t = intersectionPoints.get(0).point;
        Vector u = ray.getEnd().subtract(ray.getStart());
        Point intersectionPoint = ray.getStart().add(u.divide(u.getMagnitude()).multiply(t)).getNormalizedPoint();
        Vector normal = s.getNormal(intersectionPoint);
        for(Light l : getLights()) {
            if(l instanceof AmbientLight) {
                c = c.add(s.getMaterial().getColor(null,l).multiply(l.getIntensity()));
                continue;
            }
            PointLight ll = (PointLight) l;
            Vector dist = new Vector(ll.getCenter());
            if(normal.dotProduct(dist) <= 0.)
                continue;

            //lambertian lighting calculations used for shadows
            Ray lightRay = new Ray(normal, dist);
            double lightIntersectionPoint;
            if ((lightIntersectionPoint = l.intersects(lightRay))>=0) {
                //See if it hits any other objects before the light source
                List<HIT> points = getIntersectionPoints(lightRay,new HIT(lightIntersectionPoint,l));
                //remove if it hit the same object (hit itself)
                if(points.get(0).light==null) {
                    if (points.get(0).shape.equals(s))
                        points.remove(0);
                }
                //first hit object is a light then color the pixel
                if(points.get(0).light != null) {
                    double dt = 1;
                    dt = dist.normalized().dotProduct(normal.normalized());
                    c = c.add(s.getMaterial().getColor(lightRay, l).multiply(dt));
                }
            }
        }
        return c;
    }

    public static void render(Renderer...renderer) {
        getCamera().render(renderer);
    }

    public Camera getCameraInstance() {
        if(camera == null) {
            camera = Camera.DEFAULT_CAMERA;
            LOGGER.info("USING DEFAULT CAMERA: "+camera);
        }
        return camera;
    }

    public List<Shape> getShapesInstance() {
        if(shapes == null)
            shapes = new ArrayList<>();
        return shapes;
    }

    public List<Light> getLightInstance() {
        if(lights == null)
            lights = new ArrayList<>();
        return lights;
    }
}
