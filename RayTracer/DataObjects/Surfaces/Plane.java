package DataObjects.Surfaces;

import DataObjects.Vector;

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
    public Plane(Vector normal, double c, int materialId){
        Normal = normal;
        Offset = c;
        MaterialIndex = materialId;
    }
}
