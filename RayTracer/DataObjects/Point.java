package DataObjects;

/*
Class which represent 3D point in space.
 */
public class Point {

    /*
    X axis.
     */
    public double x;
    /*
    Y axis
     */
    public double y;
    /*
    Z axis.
     */
    public double z;

    /*
    Constructor.
     */
    public Point(double xAxis, double yAxis, double zAxis) {
        x = xAxis;
        y = yAxis;
        z = zAxis;
    }

    public Vector PointAsVector(){
        return new Vector(x,y,z);
    }
}
