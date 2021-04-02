package DataObjects;

/*
Camera class. Each scene will have one camera.
 */
public class Camera {

    /*
    Camera position.
     */
    public Point CameraPosition;
    /*
    A vector specifying the point the camera is looking at.
     */
    public Point LookAtPoint;
    /*
    The up vector of the camera defines the direction the camera is looking up at.
     */
    public Vector UpVector;
    /*
    The distance of the camera's screen from the camera.
     */
    public float ScreenDistance;
    /*
    The width of the camera's screen.
     */
    public float ScreenWidth;

    /*
    Constructor.
     */
    public Camera(){

    }
}
