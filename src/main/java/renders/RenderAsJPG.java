package renders;

import shapes.Shape;
import utilities.Color;
import utilities.Ray;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jarndt on 3/7/17.
 */
public class RenderAsJPG extends Renderer {
    public RenderAsJPG(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "RenderAsJPG{" +
                "fileName='" + fileName + '\'' +
                '}';
    }

    String fileName;

    @Override
    public void render(Color[][] scene) {
        //image dimension
        int width = scene.length;
        int height = scene[0].length;
        //create buffered image object img
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        //file object
        File f = null;
        //create random image pixel by pixel
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int r = scene[x][y].getRed(); //red
                int g = scene[x][y].getGreen(); //green
                int b = scene[x][y].getBlue(); //blue
                int a = scene[x][y].getAlpha(); //alpha

                int p = (a<<24) | (r<<16) | (g<<8) | b; //pixel
                img.setRGB(x, y, p);
            }
        }
        //write image
        try{
            f = new File(fileName);
            ImageIO.write(img, "png", f);
        }catch(IOException e){
            System.out.println("Error: " + e);
        }
    }

}
