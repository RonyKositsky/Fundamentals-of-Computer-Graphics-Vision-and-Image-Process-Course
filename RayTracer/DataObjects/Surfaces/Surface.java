package DataObjects.Surfaces;

/*
Surface interface. Represent all the surface we will deal with.
 */
public abstract class Surface {

    /*
    The material index from the list of materials.
     */
    protected int MaterialIndex;

    /*
    Returning material index.
     */
    public int GetSurfaceMaterial(){
        return MaterialIndex;
    }
}
