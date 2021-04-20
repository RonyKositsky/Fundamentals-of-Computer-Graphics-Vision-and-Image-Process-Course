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
    Vector Center;
    /*
    Edge length.
     */
    double EdgeLength;

    private static final int numberOfDoublePlanes = 3;

    private Vector[] unitVectors = {
            new Vector(1, 0,0),
            new Vector(0, 1,0),
            new Vector(0, 0,1),
    };
    private List<Plane> planesList;

    /*
    Constructor.
     */
    public Box(Vector center, double edgeLength, Material material){
        Center = center;
        EdgeLength = edgeLength;
        SurfaceMaterial = material;

        planesList = new ArrayList<>();
        double step = 0.5 * edgeLength;
        for (int i = 0; i < numberOfDoublePlanes; i++){
            Vector normal = unitVectors[i];
            double offset = getRelevantSegment(normal, i);
            planesList.add(new Plane(normal, offset + 0.5 * step, material));
            planesList.add(new Plane(normal, offset - 0.5 * step, material));
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
            if(t < closest && pointInsideBox(point)){
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
            if (Math.abs(dot - plane.Offset) < 0.1){
                return normal;
            }
        };

        return null;
    }

    /*
    Returning the relevant segment of the unit vector we are searching for.
     */
    private double getRelevantSegment(Vector unitVector, int i){
        switch (i){
            case 0:
                return unitVector.x;
            case 1:
                return unitVector.y;
            case 2:
                return unitVector.z;
            default:
                return -1;
        }
    }

    /*
    Check whether the point is inside the box.
     */
    private boolean pointInsideBox(Vector point){
        double distance = EdgeLength;
        double x = Math.abs(point.x - Center.x);
        double y = Math.abs(point.y - Center.y);
        double z = Math.abs(point.z - Center.z);
        if(x < distance && y < distance && z< distance)
            return true;
        return false;
    }
}
