package scene;

import renders.RenderAsJPG;
import renders.Renderer;
import utilities.Color;
import utilities.Ray;
import utilities.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BinaryOperator;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static utilities.Utility.randomBetween;

/**
 * Created by jarndt on 3/6/17.
 */
public class Camera {
    private final static Logger LOGGER = Logger.getLogger(Camera.class.getName());

    public static final Camera DEFAULT_CAMERA = new Camera(100,100, 10);
    private int width, height;
    private double distance, wiggleX = 20, wiggleY = 20,wiggleZ = 20;
    private int antiAliasingNumber = 10,
                depthOfFieldNumber = 10;

    public Camera(int width, int height, int distance) {
        this.width = width;
        this.height = height;
        this.distance = distance;
    }

    public Color[][] getScene(double xWiggle, double yWiggle, double zWiggle){
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        Color[][] scene = new Color[width][height];
        for(int x = 0; x<width; x++)
            for(int y = 0; y<height; y++) {
                final int xx = x, yy = y;
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        //anti aliasing
                        List<Ray> rays = new ArrayList<>();
                        for(int i = 0; i<antiAliasingNumber; i++)
                            rays.add(new Ray(new Vector(width / 2.+xWiggle, height / 2.+yWiggle, 0.+zWiggle),
                                    new Vector(randomBetween(xx,xx+1), randomBetween(yy,yy+1), distance)));
                        List<Color> colors = rays.parallelStream().map(a->Scene.sendRay(a)).collect(Collectors.toList());
                        scene[xx][yy] = colors.stream().reduce(colors.get(0),(a,b)->a.add(b));
                        if(scene[xx][yy] == null)
                            scene[xx][yy] = Color.BLACK;
                    }
                });
            }
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return scene;
    }

    public Color[][] getIdentityColorMatrix() {
        Color[][] colors = new Color[width][height];
        for(int i = 0; i< width; i++)
            for(int j = 0; j<height; j++)
                colors[i][j] = Color.BLACK;
        return colors;
    }
    public Color[][] addColorMatrixes(Color[][] a, Color[][] b){
        Color[][] c = new Color[width][height];
        for(int i = 0; i< width; i++)
            for(int j = 0; j<height; j++)
                c[i][j] = a[i][j].add(b[i][j]);
        return c;
    }
    
    public void render(Renderer... r){
        List<Color[][]> scenes = new ArrayList<>();

        Color[][] scene = null;
        //Depth of field
        if(depthOfFieldNumber > 1) {
            IntStream.range(0, depthOfFieldNumber).parallel().forEach(a ->
                    scenes.add(getScene(randomBetween(0, wiggleX), randomBetween(0, wiggleY), randomBetween(0, wiggleZ)))
            );
            scene = new Color[width][height];
            for(int j = 0; j<width; j++)
                for(int k = 0; k<height; k++)
                    for(Color[][] s : scenes)
                        if(scene[j][k] == null)
                            scene[j][k] = s[j][k];
                        else
                            scene[j][k] = scene[j][k].add(s[j][k]);

        }else
            scene = getScene(0,0,0);

        if(r != null && r.length != 0)
            for(Renderer renderer : r)
                renderer.render(scene);
        else {
            String output = System.getProperty("user.dir") + "/testFile.jpg";
            Renderer jpgRenderer = new RenderAsJPG(output);
            LOGGER.info("USING DEFAULT RENDERER: "+jpgRenderer);
            jpgRenderer.render(scene);
        }
    }

    @Override
    public String toString() {
        return "scene.Camera{" +
                "width=" + width +
                ", height=" + height +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Camera camera = (Camera) o;

        if (width != camera.width) return false;
        return height == camera.height;
    }

    @Override
    public int hashCode() {
        int result = width;
        result = 31 * result + height;
        return result;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

}
