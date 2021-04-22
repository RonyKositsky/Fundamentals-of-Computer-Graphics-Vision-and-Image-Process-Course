package DataObjects.Surfaces;

import DataObjects.Material;
import DataObjects.Vector;

import java.util.AbstractMap;

/*
Surface interface. Represent all the surface we will deal with.
 */
public abstract class Surface {

    /*
    The material index from the list of materials.
     */
    protected Material SurfaceMaterial;

    /*
    Returning material index.
     */
    public Material getSurfaceMaterial(){
        return SurfaceMaterial;
    }

    /*
    Finds if the ray intersect with the surface.
     */
    public abstract AbstractMap.SimpleEntry<Vector, Double> findIntersection(Vector ray, Vector start);

    /*
    Getting the normal on the surface.
     */
    public abstract Vector getNormal(Vector point);
}
