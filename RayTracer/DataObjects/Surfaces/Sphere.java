package DataObjects.Surfaces;

import DataObjects.Material;
import DataObjects.Vector;
import java.util.AbstractMap;

/*
Sphere class.
 */
public class Sphere extends Surface {

    public Vector Position;      /*Position of the sphere center*/
    public float Radius;        /*sphere's radius*/

    /*
    Constructor.
     */
    public Sphere(Vector position, float radius, Material material){
        Position = position;
        Radius = radius;
        SurfaceMaterial = material;
    }

    @Override
    public AbstractMap.SimpleEntry<Vector, Double> findIntersection(Vector ray, Vector start) {
        Vector vec = start.VectorSubtraction(Position);
        double a = ray.DotProduct(ray);
        double b = 2 * ray.DotProduct(vec);
        double c = vec.DotProduct(vec) - Math.pow(Radius,2);
        double disc = Math.pow(b,2) - 4 * a * c;

        if(disc < 0)
            return null;
        else
        {
            double[] arr = FindRoots(a,b,disc);
            if(arr.length == 1){
                if(arr[0]>0){
                    Vector intersection = start.VectorsAddition(ray.VectorsScalarMultiplication(arr[0]));
                    return new AbstractMap.SimpleEntry<>(intersection, arr[0]);
                }
            }else{
                if(arr[0] > 0){
                    Vector intersection = start.VectorsAddition(ray.VectorsScalarMultiplication(arr[0]));
                    return new AbstractMap.SimpleEntry<>(intersection, arr[0]);
                }else if(arr[1] > 0){
                    Vector intersection = start.VectorsAddition(ray.VectorsScalarMultiplication(arr[1]));
                    return new AbstractMap.SimpleEntry<>(intersection, arr[1]);
                }
            }
        }
        return null;
    }

    @Override
    public Vector getNormal(Vector point) {
        return Vector.CreateVectorFromTwoPoints(Position, point).VectorsScalarMultiplication(-1);
    }

    /*
    Finding the roots of quadratic formula.
     */
    private double[] FindRoots(double a, double b, double disc){
        double denominator = 2*a;
        if (disc == 0)
            return new double[]{-b/denominator};
        else
            return SortArray(new double[]{
                    (Math.sqrt(disc) - b)/ denominator,
                    -(b+Math.sqrt(disc))/ denominator
            });
    }

    /*
    Sorting array with 2 values.
     */
    private double[] SortArray(double[] arr){
        return (arr[0] < arr[1]) ? arr : new double[]{arr[1], arr[0]};
    }
}
