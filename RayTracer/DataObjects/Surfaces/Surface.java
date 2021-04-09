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
    public Material GetSurfaceMaterial(){
        return SurfaceMaterial;
    }

    /*
    Finds if the ray intersect with the surface.
     */
    public abstract AbstractMap.SimpleEntry<Vector, Double> FindIntersection(Vector ray, Vector start);

    public abstract Vector GetNormal(Vector point);
}
