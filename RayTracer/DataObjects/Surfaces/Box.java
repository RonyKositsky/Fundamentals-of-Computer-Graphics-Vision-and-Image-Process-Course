package DataObjects.Surfaces;

import DataObjects.Material;
import DataObjects.Vector;

import java.util.AbstractMap;

/*
Cube class.
 */
public class Box extends Surface {

    /*
    Center of the cube.
     */
    Vector Center;
    /*
    Edge length.
     */
    double EdgeLength;

    /*
    Constructor.
     */
    public Box(Vector center, double edgeLength, Material material){
        Center = center;
        EdgeLength = edgeLength;
        SurfaceMaterial = material;
    }

    @Override
    public AbstractMap.SimpleEntry<Vector, Double> findIntersection(Vector ray, Vector start) {
        return null;
    }

    @Override
    public Vector getNormal(Vector point) {
        return null;
    }
}
