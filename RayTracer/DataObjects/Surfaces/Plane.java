package DataObjects.Surfaces;

import DataObjects.Material;
import DataObjects.Vector;

import java.util.AbstractMap;

/*
Infinite plane class.
 */
public class Plane extends Surface {

    /*
    The plane normal.
     */
    Vector Normal;
    /*
    The plane offset.
     */
    public double Offset;

    /*
    Constructor.
     */
    public Plane(Vector normal, double c, Material material){
        Normal = normal.NormalizeVector();
        Offset = c;
        SurfaceMaterial = material;
    }

    @Override
    public AbstractMap.SimpleEntry<Vector, Double> findIntersection(Vector ray, Vector start) {
        double dotProduct = Normal.DotProduct(ray);
        if(dotProduct == 0)
            return null;

        Vector temp = Normal.VectorsScalarMultiplication(Offset);
        double t = Normal.DotProduct(temp.VectorSubtraction(start)) / dotProduct;
        return t < 0 ? null : new AbstractMap.SimpleEntry<>
                (ray.VectorsScalarMultiplication(t).VectorsAddition(start), t);
    }

    @Override
    public Vector getNormal(Vector point) {
        return Normal;
    }
}
