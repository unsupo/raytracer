import lights.AmbientLight;
import lights.PointLight;
import materials.Material;
import materials.MatteMaterial;
import materials.ReflectantRefractantMaterial;
import org.smurn.jply.*;
import org.smurn.jply.util.NormalMode;
import org.smurn.jply.util.NormalizingPlyReader;
import org.smurn.jply.util.TesselationMode;
import org.smurn.jply.util.TextureMode;
import scene.Scene;
import shapes.Plane;
import shapes.Shape;
import shapes.Sphere;
import shapes.Triangle;
import utilities.*;
import utilities.Timer;
import utilities.Vector;

import java.io.IOException;
import java.util.*;

/**
 * Created by jarndt on 3/6/17.
 */
public class Runner {
    public static void main(String[] args) {
        parseArguments(args);
    }

    private static void parseArguments(String[] args) {
        createScene();
        if (args != null && args.length != 0) {
            Arrays.asList(args).forEach(a -> {
                if (a.endsWith(".ply") || a.endsWith(".gz"))
                    Scene.addShape(new BVH().addListOfShapes(readPLYFile(a)));
//                    scene.Scene.getShapes().addAll(readPLYFile(a));
            });
        }
        double W = Scene.getCamera().getWidth(), H = Scene.getCamera().getHeight();
        double D = Scene.getCamera().getDistance();

//        scene.Scene.addShape(new Triangle(new Point<Double>(0.,H,D+10),new Po3int<Double>(W,H,D+10),new Point<Double>(W/2,H/2,D-10)));

        utilities.Timer timer = new Timer().start();
        Scene.render();
        timer.stop();
        System.out.println(timer.getTime());
    }

    public static List<Shape> readPLYFile(String file) {
        List<TriangleIndices> indices = new ArrayList<>();
        List<Point> points = new ArrayList<>();
        try {
            PlyReader ply = new PlyReaderFile(file);
            ply = new NormalizingPlyReader(ply,
                    TesselationMode.TRIANGLES,
                    NormalMode.ADD_NORMALS_CCW,
                    TextureMode.PASS_THROUGH);

            ElementReader reader = ply.nextElementReader();
            while(reader != null){
                if(reader.getElementType().getName().equals("vertex"))
                    points.addAll(fillVertexBuffer(reader));
                else if ( reader.getElementType().getName().equals("face"))
                    indices.addAll(fillIndexBuffer(reader));
                reader.close();
                reader = ply.nextElementReader();
            }

            List<Shape> triangles = new ArrayList<>();
            indices.stream().forEach(a->{
                triangles.add(new Triangle(points.get(a.p1).multiply(1000).add(new Point<Double>(100.,0.,100.)),points.get(a.p2).multiply(1000).add(new Point<Double>(100.,0.,100.)),points.get(a.p3).multiply(1000).add(new Point<Double>(100.,0.,100.))));
//                Triangle triangle = new Triangle(points.get(a.p1).multiply(1000).add(new Point<Double>(100.,0.,100.)),points.get(a.p2).multiply(1000).add(new Point<Double>(100.,0.,100.)),points.get(a.p3).multiply(1000).add(new Point<Double>(100.,0.,100.)));
//                triangles.add(new Sphere(triangle.getRadius(),triangle.getCenter()));
            });

            return triangles;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class TriangleIndices{
        int p1,p2,p3;

        public TriangleIndices(int p1, int p2, int p3) {
            this.p1 = p1;
            this.p2 = p2;
            this.p3 = p3;
        }
    }
    private static List<TriangleIndices> fillIndexBuffer(ElementReader reader) throws IOException {
        //Go Through the triangles and store the indices
        List<TriangleIndices> indicesList = new ArrayList<>();
        Element triangle = reader.readElement();
        while(triangle != null){
            int[] indices = triangle.getIntList("vertex_index");
            indicesList.add(new TriangleIndices(indices[0],indices[1],indices[2]));
            triangle = reader.readElement();
        }
        return indicesList;
    }

    private static List<Point> fillVertexBuffer(ElementReader reader) throws IOException {
        //Go through the vertices and store the coordinates
        Element vertex = reader.readElement();
        List<Point> points = new ArrayList<>();
        while(vertex != null){
            points.add(new Point(vertex.getDouble("x"),
                    vertex.getDouble("y"),
                    vertex.getDouble("z")));
            vertex = reader.readElement();
        }
        return points;
    }

    private static void createScene() {
        Scene.getCamera().setHeight(500);
        Scene.getCamera().setWidth(500);
        Scene.getCamera().setDistance(300);
        int W = Scene.getCamera().getWidth(), H = Scene.getCamera().getHeight();
        double D = Scene.getCamera().getDistance();
//        scene.Scene.addShape(new Sphere(1,new Point<Double>(W/2.,H/2.,2*D)));
//        scene.Scene.addLight(new PointLight());

        int gap = 40;

        Sphere s = new Sphere(100, new Point<Double>(W / 2. - gap, H / 2.+40, D));
        Material m = new MatteMaterial();
        m.setColor(Color.BLUE);
        s.setMaterial(m);
        Scene.addShape(s);

//        Sphere ss = new Sphere(35, new Point<Double>(W / 2. + gap, H / 2.+gap, D ));
//        Material mm = new ReflectantRefractantMaterial(true,false);
//        mm.setColor(Color.GREEN);
//        ss.setMaterial(mm);
//        Scene.addShape(ss);
//
//        Sphere sss = new Sphere(35, new Point<Double>(W / 2. + 3*gap, H / 2.+gap, D ));
//        Material mmm = new ReflectantRefractantMaterial();
//        mmm.setColor(Color.ORANGE);
//        sss.setMaterial(mmm);
//        Scene.addShape(sss);
//
//        Sphere ssss = new Sphere(35, new Point<Double>(W / 2. - 3*gap, H / 2., D ));
//        Material mmmm = new MatteMaterial();
//        mmmm.setColor(Color.YELLOW);
//        ssss.setMaterial(mmmm);
//        Scene.addShape(ssss);

        double xdist = 70, disty = 10, up = 50, right = 50;
//        scene.Scene.addShape(new Triangle(0.+right,H+up,D,W+right,H+up,D,W/2.+right,H+up, 40*D));
        Scene.addShape(new Plane(new Vector(0., 0.5, 0.), new Point<Double>(0., (double) H * 3 / 2., 0.)));
//        Scene.addLight(new AmbientLight(.2));
        Scene.addLight(new PointLight(W / 2. - xdist, H / 2. - disty, 2 * D - 2 * D + 30, 1, 20));
//        Scene.addLight(new PointLight(-(W / 2. - xdist), H / 2. - disty, 2 * D - 2 * D - 10, 1, 1));
    }

}
