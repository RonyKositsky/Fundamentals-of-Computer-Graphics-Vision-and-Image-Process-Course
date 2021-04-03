package DataObjects.Surfaces;

import DataObjects.Material;
import DataObjects.Point;
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
    double Offset;

    /*
    Constructor.
     */
    public Plane(Vector normal, double c, Material material){
        Normal = normal.NormalizeVector();
        Offset = c;
        SurfaceMaterial = material;
    }

    @Override
    public AbstractMap.SimpleEntry<Point, Double> FindIntersection(Vector ray, Vector start) {
        double dotProduct = Normal.DotProduct(ray);
        if(dotProduct == 0)
            return null;

        double temp = Normal.DotProduct(start);
        double t = -1 * (Offset + temp) / dotProduct;
        return t < 0 ? null : new AbstractMap.SimpleEntry<>
                (ray.VectorsScalarMultiplication(t).VectorsAddition(start).VectorAsPoint(), t);
    }
}
