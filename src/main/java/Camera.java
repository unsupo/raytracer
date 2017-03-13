import renders.RenderAsJPG;
import renders.Renderer;
import utilities.Color;
import utilities.Ray;
import utilities.Vector;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jarndt on 3/6/17.
 */
public class Camera {
    private final static Logger LOGGER = Logger.getLogger(Camera.class.getName());

    public static final Camera DEFAULT_CAMERA = new Camera(100,100, 10);
    private int width, height;
    private double distance;

    public Camera(int width, int height, int distance) {
        this.width = width;
        this.height = height;
        this.distance = distance;
    }

    public void render(Renderer... r){
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        Color[][] scene = new Color[width][height];
        for(int x = 0; x<width; x++)
            for(int y = 0; y<height; y++) {
                final int xx = x, yy = y;
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        scene[xx][yy] = Scene.sendRay(
                                new Ray(new Vector(width / 2., height / 2., 0.),
                                        new Vector((double) xx, (double) yy, distance))
                        );
                    }
                });
            }
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        return "Camera{" +
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
