package DataObjects;

public class Vector {
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
    public Vector(double xAxis, double yAxis, double zAxis) {
        x = xAxis;
        y = yAxis;
        z = zAxis;
    }

    /*
       Returning new point - addition of this point and another point.
        */
    public Vector VectorsAddition(Vector other){
        return new Vector(x + other.x, y+ other.y, z + other.z);
    }

    /*
    Returning new vector - subtraction of this vector and another vector.
    */
    public Vector VectorsSubtraction(Vector other){
        return new Vector(x - other.x, y - other.y, z - other.z);
    }

    /*
    Returning new vector - multiplication by scalar.
    */
    public Vector VectorsScalarMultiplication(double scalar){
        return new Vector(x * scalar, y * scalar, z * scalar);
    }

    /*
    Returning new vector - cross product of another vector.
    */
    public Vector CrossProduct(Vector other) {
        double x = y * other.z - z * other.y;
        double y = z * other.x - x * other.z;
        double z = x * other.y - y * other.x;
        return new Vector(x, y, z);
    }

    /*
    Dot product of this vector and another vector.
     */
    public double DotProduct(Vector other) {
        return x * other.x + y * other.y + z * other.z;
    }

    /*
    Getting vector normal.
     */
    public double VectorNormal(){
        return Math.sqrt(DotProduct(this));
    }

    /*
    Returning new vector - this vector as normalized vector.
     */
    public Vector NormalizeVector(){
        return VectorsScalarMultiplication(1 / Math.sqrt(DotProduct(this)));
    }

    /*
    Creating vector from 2 points.
     */
    public static Vector CreateVectorFromTwoPoints(Point a, Point b) {
        return new Vector(a.x - b.x, a.y - b.y, a.z - b.z);
    }
}
