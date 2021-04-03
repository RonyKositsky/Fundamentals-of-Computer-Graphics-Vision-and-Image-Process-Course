package DataObjects.Surfaces;

import DataObjects.Material;
import DataObjects.Point;
import DataObjects.Vector;

import java.util.AbstractMap;

/*
Sphere class.
 */
public class Sphere extends Surface {

    public Point Position;      /*Position of the sphere center*/
    public float Radius;        /*sphere's radius*/

    /*
    Constructor.
     */
    public Sphere(Point position, float radius, Material material){
        Position = position;
        Radius = radius;
        SurfaceMaterial = material;
    }

    @Override
    public AbstractMap.SimpleEntry<Point, Double> FindIntersection(Vector ray, Vector start) {
        return null;
    }
}
