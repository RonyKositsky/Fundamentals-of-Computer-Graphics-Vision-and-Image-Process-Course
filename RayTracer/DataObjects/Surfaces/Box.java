package DataObjects.Surfaces;

import DataObjects.Material;
import DataObjects.Vector;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

/*
Cube class.
 */
public class Box extends Surface {

    /*
    Center of the cube.
     */
    public Vector Center;
    /*
    Edge length.
     */
    public double EdgeLength;
    /*
    Property which store the maximum offset value from the box center.
     */
    private double step;
    /*
    For each axis we have two plane to construct the box.
     */
    private int numberOfDoublePlanes = 3;
    /*
    Array of 3D unit vectors.
     */
    private Vector[] unitVectors;
    /*
    List of planes that construct the box.
     */
    private List<Plane> planesList;

    /*
    Constructor.
     */
    public Box(Vector center, double edgeLength, Material material){
        Center = center;
        EdgeLength = edgeLength;
        SurfaceMaterial = material;
        step = edgeLength / 2;
        unitVectors = new Vector[]{
                new Vector(1, 0,0),
                new Vector(0, 1,0),
                new Vector(0, 0,1),
        };

        planesList = new ArrayList<>();
        for (int i = 0; i < numberOfDoublePlanes; i++){
            Vector normal = unitVectors[i];
            double axis = getRelevantSegment(i);
            planesList.add(new Plane(normal, axis + step, material));
            planesList.add(new Plane(normal.VectorsScalarMultiplication(-1), -(axis - step), material));
        }
    }

    @Override
    public AbstractMap.SimpleEntry<Vector, Double> findIntersection(Vector ray, Vector start) {
        double closest = Double.MAX_VALUE;
        AbstractMap.SimpleEntry<Vector, Double> entry = null;
        for (Plane plane: planesList) {
            AbstractMap.SimpleEntry<Vector, Double> intersection = plane.findIntersection(ray, start);
            if (intersection == null) continue;;
            Vector point = intersection.getKey();
            double t = intersection.getValue();
            if(pointInsideBox(point) && t < closest ){
                entry = new AbstractMap.SimpleEntry<>(point, t);
                closest = t;
            }
        }
        return entry;
    }

    @Override
    public Vector getNormal(Vector point) {
        for (Plane plane: planesList) {
            Vector normal = plane.getNormal(point);
            double dot = normal.DotProduct(point);
            //We use small tolerance because we can't compare double numbers because of the precision.
            if (Math.abs(dot - plane.Offset) < 0.0001) {
                return normal;
            }
        };

        return null;
    }

    /*
    Returning the relevant segment of the Center point we are searching for.
     */
    private double getRelevantSegment(int i){
        switch (i){
            case 0:
                return Center.x;
            case 1:
                return Center.y;
            case 2:
                return Center.z;
            default:
                return -1;
        }
    }

    /*
    Check whether the point is on the box surface.
     */
    private boolean pointInsideBox(Vector point){
        double a = Math.abs(Center.x - point.x);
        double b = Math.abs(Center.y - point.y);
        double c = Math.abs(Center.z - point.z);
        return (a <= step) && (b <= step) && (c <= step);
    }
}
