package DataObjects;

public class Sphere implements ISurface{

    public Point Position;      /*Position of the sphere center*/
    public float Radius;        /*sphere's radius*/
    public int MaterialIndex;   /*Index of the material*/

    public Sphere(Point position, float radius, int materialIndex){
        Position = position;
        Radius = radius;
        MaterialIndex = materialIndex;
    }
}
