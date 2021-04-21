package DataObjects.Surfaces;

import DataObjects.Material;
import DataObjects.Vector;
import java.util.AbstractMap;

/*
Sphere class.
 */
public class Sphere extends Surface {

    public Vector Center;      /*Position of the sphere center*/
    public float Radius;        /*sphere's radius*/

    /*
    Constructor.
     */
    public Sphere(Vector center, float radius, Material material){
        Center = center;
        Radius = radius;
        SurfaceMaterial = material;
    }

    @Override
    public AbstractMap.SimpleEntry<Vector, Double> findIntersection(Vector ray, Vector start) {
        // Taken from this page - https://en.wikipedia.org/wiki/Line%E2%80%93sphere_intersection

        Vector direction = start.VectorSubtraction(Center);
        double temp = -ray.DotProduct(direction); //-(ray*direction)
        double delta = Math.pow(temp,2) -(direction.VectorNormal() - Math.pow(Radius,2));
        if(delta < 0)
            return null;
        else if (delta == 0) {
            Vector hitPoint = start.VectorsAddition(ray.VectorsScalarMultiplication(temp));
            return temp > 0 ? new AbstractMap.SimpleEntry<>(hitPoint, temp) : null;
        }else{
            delta  = Math.sqrt(delta);
            double[] arr = SortArray(new double[]{temp + delta, temp - delta});
            if(arr[0] <0 && arr[1] <0)
                return null;
            double minValue = arr[0] < 0 ? arr[1] : arr[0];
            Vector hitPoint = start.VectorsAddition(ray.VectorsScalarMultiplication(minValue));
            return new AbstractMap.SimpleEntry<>(hitPoint, minValue);
        }
    }

    @Override
    public Vector getNormal(Vector point) {
        return Vector.CreateVectorFromTwoPoints(Center, point).VectorsScalarMultiplication(-1);
    }


    /*
    Sorting array with 2 values.
     */
    private double[] SortArray(double[] arr){
        return (arr[0] < arr[1]) ? arr : new double[]{arr[1], arr[0]};
    }
}
