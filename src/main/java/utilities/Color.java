package utilities;

/**
 * Created by jarndt on 3/6/17.
 */
public class Color {
    int red;
    int green;
    int blue;
    int alpha;

    public Color(int red, int green, int blue, int alpha) {
        this.red = cap(red);
        this.green = cap(green);
        this.blue = cap(blue);
        this.alpha = alpha;
    }public Color(int red, int green, int blue) {
        this.red = cap(red);
        this.green = cap(green);
        this.blue = cap(blue);
        this.alpha = 255; //alpha goes from 0 completely transparent to 100 completely visible
    }

    public Color add(Color c){
        return new Color((this.red+c.getRed())/2,(this.green+c.getGreen())/2,(this.blue+c.getBlue())/2,(this.getAlpha()+c.getAlpha())/2);
    }
    public Color multiply(double intensity) {
        return new Color(cap(red*intensity),cap(blue*intensity),cap(green*intensity),alpha);
    }
    public Color divide(double intensity) {
        return new Color(cap(red/intensity),cap(blue/intensity),cap(green/intensity),alpha);
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Color color = (Color) o;

        if (red != color.red) return false;
        if (green != color.green) return false;
        if (blue != color.blue) return false;
        return Double.compare(color.alpha, alpha) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = red;
        result = 31 * result + green;
        result = 31 * result + blue;
        temp = Double.doubleToLongBits(alpha);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public static final Color
            BLACK   = new Color(0,0,0),
            WHITE   = new Color(255,255,255),
            RED     = new Color(255,0,0),
            GREEN   = new Color(0,255,0),
            BLUE    = new Color(0,0,255),
            YELLOW  = new Color(255,255,0),
            PURPLE  = new Color(128,0,128),
            ORANGE  = new Color(255,165,0);


    @Override
    public String toString() {
        return "Color{" +
                "red=" + red +
                ", green=" + green +
                ", blue=" + blue +
                ", alpha=" + alpha +
                '}';
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public int getAlpha() {
        return alpha;
    }


    private int cap(double v) {
        if(v>255) return 255;
        if(v<0) return 0;
        return (int)v;
    }
}
