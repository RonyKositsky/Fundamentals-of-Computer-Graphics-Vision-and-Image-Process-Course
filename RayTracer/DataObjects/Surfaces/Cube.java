package DataObjects.Surfaces;

import DataObjects.Point;

/*
Cube class.
 */
public class Cube extends Surface {

    /*
    Center of the cube.
     */
    Point Center;
    /*
    Edge length.
     */
    double EdgeLength;

    /*
    Constructor.
     */
    public Cube(Point center, double edgeLength, int materialId){
        Center = center;
        EdgeLength = edgeLength;
        MaterialIndex = materialId;
    }
}
