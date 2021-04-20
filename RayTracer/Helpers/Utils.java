package Helpers;

import DataObjects.Camera;
import DataObjects.Light;
import DataObjects.Surfaces.Surface;
import DataObjects.Vector;

import java.util.AbstractMap;
import java.util.List;

public class Utils {

    /*
    Finding perpendicular plane to light source for specific object.
     */
    public static Vector[] FindPerpendicularPlane(Light light, Vector lightRay){
        double dot = light.Position.DotProduct(lightRay);
        Vector U = new Vector(1, 0, (-lightRay.x + dot) / lightRay.z);
        Vector V = new Vector(1, 0, (-lightRay.y + dot) / lightRay.z);

        return new Vector[]{
                U.NormalizeVector(), V.NormalizeVector()
        };
    }

    /*
    Finding perpendicular plane to light source for specific object.
     */
    public static Vector[] FindPerpendicularPlane(Camera light, Vector lightRay){
        double dot = light.CameraPosition.DotProduct(lightRay);
        Vector U = new Vector(1, 0, (-lightRay.x + dot) / lightRay.z);
        Vector V = new Vector(1, 0, (-lightRay.y + dot) / lightRay.z);

        return new Vector[]{
                U.NormalizeVector(), V.NormalizeVector()
        };
    }

    public static Vector GetPerpendicularPlaneNormal(Camera camera, Vector lightRay){
        Vector[] axis = FindPerpendicularPlane(camera, lightRay);
        return axis[0].CrossProduct(axis[1]).NormalizeVector();
    }

    /*
    Finding the first we intersect by the starting point and the direction.
    Returning tuple of the surface found and the hit point. If we hit the background we returning null.
     */
    public static AbstractMap.SimpleEntry<Surface, Vector> GetFirstIntersection(Vector start, Vector ray,
                                                                          Surface ignore, List<Surface> surfaceList){
        double first = Double.POSITIVE_INFINITY;
        Surface surface = null;
        Vector vec =null;
        for(Surface sur : surfaceList){
            if(sur.equals(ignore)) continue;
            AbstractMap.SimpleEntry<Vector, Double> entry = sur.findIntersection(ray, start);
            if(entry != null){
                double t = entry.getValue();
                if(entry.getValue() < first){
                    surface = sur;
                    first = t;
                    vec = entry.getKey();
                }
            }
        }

        return new AbstractMap.SimpleEntry<>(surface, vec);
    }

}
