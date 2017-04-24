package utilities;

import shapes.Rectangle;
import shapes.Shape;
import shapes.Sphere;
import shapes.Triangle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static utilities.Utility.randomBetween;

/**
 * Created by jarndt on 4/19/17.
 */
public class BVH extends Shape{
    public static void main(String[] args) {
        BVH bvh = new BVH();
        Random r = new Random();
        List<Shape> list = new ArrayList<>();
        for(int i = 0; i<1e6; i++)
            list.add(new Triangle(r.nextDouble(),r.nextDouble(),r.nextDouble(),r.nextDouble(),r.nextDouble(),r.nextDouble(),r.nextDouble(),r.nextDouble(),r.nextDouble()));

        bvh.addListOfShapes(list);

        System.out.println();
    }
    Rectangle frontCover;
    List<Rectangle> subCovers = new ArrayList<>();
    public BVH addListOfShapes(List<Shape> list){
        Double x=0.,y=0.,z=0.,
                mx=Double.MIN_VALUE,my=Double.MIN_VALUE,mz=Double.MIN_VALUE,
                mix=Double.MAX_VALUE,miy=Double.MAX_VALUE,miz=Double.MAX_VALUE;
        for(Shape s : list){
            x+=s.getCenter().getX();
            y+=s.getCenter().getY();
            z+=s.getCenter().getZ();
            mx = mx < s.getCenter().getX() ? s.getCenter().getX() : mx;
            my = my < s.getCenter().getY() ? s.getCenter().getY() : my;
            mz = mz < s.getCenter().getZ() ? s.getCenter().getZ() : mz;
            mix = mix > s.getCenter().getX() ? s.getCenter().getX() : mix;
            miy = miy > s.getCenter().getY() ? s.getCenter().getY() : miy;
            miz = miz > s.getCenter().getZ() ? s.getCenter().getZ() : miz;
        }
        frontCover = new Rectangle(new Point<Double>(mix,my,miz),new Point<Double>(mx,my,miz),new Point<Double>(mix,miy,miz),new Point<Double>(mx,miy,miz));

        Point left = new Point<Double>(mix,(my-mix)/2.,miz);
        Collections.sort(list,(a,b)->b.getCenter().getX().compareTo(a.getCenter().getX()));

        if(list.size() > shapeSize)
            for(int i = 0; i<bvhSize; i++)
                innerBvhs.add(new BVH().addListOfShapes(list.subList(i * list.size() / bvhSize, (i + 1) * list.size() / bvhSize)));
        else
            shapes.addAll(list);

        return this;
    }

    public BVH addList(List<Shape> list) {
        double x=0,y=0,z=0,mx=Double.MIN_VALUE,my=Double.MIN_VALUE,mz=Double.MIN_VALUE;
        for(Shape s : list){
            x+=s.getCenter().getX();
            y+=s.getCenter().getY();
            z+=s.getCenter().getZ();
            mx = mx < s.getCenter().getX() ? s.getCenter().getX() : mx;
            my = my < s.getCenter().getY() ? s.getCenter().getY() : my;
            mz = mz < s.getCenter().getZ() ? s.getCenter().getZ() : mz;
        }
        Point<Double> center = new Point<>(x/list.size(),y/list.size(),z/list.size());
        double max = center.getX()+mx > center.getY()+my ? center.getX()+mx : center.getY()+my;
        max = max > center.getZ()+mz ? max : center.getZ()+mz;

        BVHShape = new Sphere(max,center);
        if(list.size() > shapeSize) {
            for(int i = 0; i<bvhSize; i++) {
                Point left = center.add(new Point<>(randomBetween(0,max),randomBetween(0,max),randomBetween(0,max)));
                Collections.sort(list,(a,b)->new Vector(b.getCenter().subtract(left)).getMagnitude()
                        .compareTo(new Vector(a.getCenter().subtract(left)).getMagnitude()));

                innerBvhs.add(new BVH().addList(list.subList(i * list.size() / bvhSize, (i + 1) * list.size() / bvhSize)));
            }
        }else
            shapes.addAll(list);

        return this;
    }

    //one BVH initially then as more objects come in more BVH's get created
    private Sphere BVHShape = new Sphere(.01,new Point<Double>(0.,0.,0.));
    private int shapeSize = 100, bvhSize = 2;
    private List<Shape> shapes = new ArrayList<>();
    private List<BVH> innerBvhs = new ArrayList<>();

    public BVH() {    } //default shapeSize of 100, default bvhSize of 2
    public BVH(int size) {
        this.shapeSize = size;
    }public BVH(int shapeSize, int bvhSize) {
        this.shapeSize = shapeSize;
        this.bvhSize = bvhSize;
    }public BVH(Sphere sphere){
        this.BVHShape = sphere;
    }

    //top bvh will house all shapes
    //it'll resize when shapeSize is reached and split it into two BVH shapes
    //that will grow recursively
    public BVH addShape(Shape shape){
        if(isInside(shape)) //shape fits inside this bvh
            if(shapes.size() < shapeSize) //if shapeSize hasn't been reach
                shapes.add(shape); //then add the shape
            else{ //else create the inner bvhs and decide where it goes
                if(innerBvhs.size() < bvhSize)
                    createInnerBVH();
                boolean isInside = false;
                for(BVH bvh : innerBvhs) //if it fits inside an existing bvh then just add it there
                    if(bvh.isInside(shape)){
                        bvh.addShape(shape);
                        isInside = true;
                        break;
                    }
                if(!isInside){ //if it doesn't fit then get the closest fit
                    Point c = shape.getCenter();
                    Collections.sort(innerBvhs,(a,b)->new Vector(a.getBVHShape().getCenter().subtract(c)).getMagnitude()
                            .compareTo(new Vector(b.getBVHShape().getCenter().subtract(c)).getMagnitude()));
                    innerBvhs.get(0).addShape(shape); //and add it to that one
                    if(!isInside(innerBvhs.get(0).getBVHShape())) //then if the new size is outside the bigger bvh then grow the bigger one
                        growBVHShape(innerBvhs.get(0).getBVHShape());
                    //move other bvhs
                    for(int i = 1; i<innerBvhs.size(); i++)
                        if(innerBvhs.get(0).isInside(innerBvhs.get(i).getBVHShape())){
                            int j = new Random().nextInt(3);
                            Sphere s = innerBvhs.get(i).getBVHShape();
                            if(j == 0)
                                innerBvhs.get(i).setBVHShape(new Sphere(s.getRadius(),s.getCenter().add(new Point<>(innerBvhs.get(0).getBVHShape().getRadius(),0.,0.))));
                            if(j == 1)
                                innerBvhs.get(i).setBVHShape(new Sphere(s.getRadius(),s.getCenter().add(new Point<>(0.,innerBvhs.get(0).getBVHShape().getRadius(),0.))));
                            if(j == 2)
                                innerBvhs.get(i).setBVHShape(new Sphere(s.getRadius(),s.getCenter().add(new Point<>(0.,0.,innerBvhs.get(0).getBVHShape().getRadius()))));
                        }
                }
            }
        else { //if shapes doesn't fit inside then grow this bvh such that it does fit inside
            growBVHShape(shape);
            addShape(shape);
        }
        return this;
    }

    private void createInnerBVH() {
        for(int i = 0; i< bvhSize; i++) {
            BVH bvh = null;
            boolean keepGoing = true;
            while(keepGoing) {
                bvh = new BVH(new Sphere(BVHShape.getRadius()/bvhSize/bvhSize,
                        new Point<>(randomBetween(BVHShape.getCenter().getX(),BVHShape.getRadius()),
                                randomBetween(BVHShape.getCenter().getY(),BVHShape.getRadius()),
                                randomBetween(BVHShape.getCenter().getZ(),BVHShape.getRadius())))
                );
                if(innerBvhs.size() == 0) break;
                for (BVH b : innerBvhs)
                    if (b.isInside(bvh.BVHShape)) {
                        keepGoing = true;
                        break;
                    }else keepGoing = false;
            }
            innerBvhs.add(bvh); //loop until you get a bvhshape not inside the other bvh shapes
        }
    }

    private void growBVHShape(Shape shape) {
        Sphere sphere = new Sphere(shape.getRadius(),shape.getCenter());
        Double centerDistance = new Vector(BVHShape.getCenter().subtract(sphere.getCenter())).getMagnitude();
        BVHShape.setRadius(centerDistance+BVHShape.getRadius()+sphere.getRadius());
    }

    public boolean isInside(Shape shape){
        Sphere sphere = new Sphere(shape.getRadius(),shape.getCenter());
        if(BVHShape.getRadius() < sphere.getRadius())
            return false;
        Double centerDistance = new Vector(BVHShape.getCenter().subtract(sphere.getCenter())).getMagnitude();
        //if(centerDistance > r1+r2) then no overlap
        //if(centerDistance <= Math.abs(r1-r2)) then inside
        //else overlap but not completely inside
        double r1 = sphere.getRadius(), r2 = BVHShape.getRadius();
        return centerDistance <= Math.abs(r1-r2);
    }

    public Sphere getBVHShape() {
        return BVHShape;
    }

    public void setBVHShape(Sphere BVHShape) {
        this.BVHShape = BVHShape;
    }

    public int getShapeSize() {
        return shapeSize;
    }

    public int getBvhSize() {
        return bvhSize;
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public List<BVH> getInnerBvhs() {
        return innerBvhs;
    }

    @Override
    public Vector getNormal(Point<Double> intersectionPoint) {
        return frontCover.getNormal(intersectionPoint);
    }

    @Override
    public double intersects(Ray ray) {
        if(frontCover.intersects(ray) == -1)
            return -1;

        double intersect = -1;
        if(innerBvhs != null && innerBvhs.size() != 0)
            for(BVH bvh : innerBvhs)
                if((intersect = bvh.intersects(ray)) != -1)
                    return intersect;
        else if(shapes!=null && shapes.size()!= 0)
            for(Shape s : shapes)
                if((intersect = s.intersects(ray))!= -1)
                    return intersect;

        return -1;
    }

    @Override
    public Point<Double> getCenter() {
        return frontCover.getCenter();
    }

    @Override
    public boolean contains(Point<Double> point) {
        return frontCover.contains(point);
    }

    @Override
    public Double getRadius() {
        return frontCover.getRadius();
    }
}
