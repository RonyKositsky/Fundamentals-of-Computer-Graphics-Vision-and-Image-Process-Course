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

    /*
    Returning new point - addition of this point and another point.
     */
    public Point PointsAddition(Point other){
        return new Point(x + other.x, y+ other.y, z + other.z);
    }

    /*
    Returning new point - subtraction of this point and another point.
    */
    public Point PointsSubtraction(Point other){
        return new Point(x - other.x, y - other.y, z - other.z);
    }

    public Vector PointAsVector(){
        return new Vector(x,y,z);
    }
}
