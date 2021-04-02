package DataObjects.Surfaces;

import DataObjects.Point;

/*
Sphere class.
 */
public class Sphere extends Surface {

    public Point Position;      /*Position of the sphere center*/
    public float Radius;        /*sphere's radius*/

    /*
    Constructor.
     */
    public Sphere(Point position, float radius, int materialIndex){
        Position = position;
        Radius = radius;
        MaterialIndex = materialIndex;
    }
}
