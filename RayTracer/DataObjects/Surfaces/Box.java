package DataObjects.Surfaces;

import DataObjects.Material;
import DataObjects.Point;
import DataObjects.Vector;

import java.util.AbstractMap;

/*
Cube class.
 */
public class Box extends Surface {

    /*
    Center of the cube.
     */
    Point Center;
    /*
    Edge length.
     */
    double EdgeLength;

    /*
    Constructor.
     */
    public Box(Point center, double edgeLength, Material material){
        Center = center;
        EdgeLength = edgeLength;
        SurfaceMaterial = material;
    }

    @Override
    public AbstractMap.SimpleEntry<Point, Double> FindIntersection(Vector ray, Vector start) {
        return null;
    }
}
