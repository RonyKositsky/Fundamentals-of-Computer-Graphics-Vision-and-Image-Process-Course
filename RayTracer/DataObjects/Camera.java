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
    public double ScreenDistance;
    /*
    The width of the camera's screen.
     */
    public double ScreenWidth;
    /*
    Option of adding a fisheye lens that will perform a fisheye effect on the image.
    */
    public boolean FishEyeLens;
    /*
    Fish eye k value.
     */
    public float KValue;

    /*
    Constructor.
     */
    public Camera(Point cameraPosition, Point lookAtPoint, Vector upVector, double screenDistance,
                  double screenWidth, boolean fishEye, float kValue){
        CameraPosition = cameraPosition;
        LookAtPoint = lookAtPoint;
        UpVector = upVector;
        ScreenDistance = screenDistance;
        ScreenWidth = screenWidth;
        FishEyeLens = fishEye;
        KValue = kValue;
    }


}
