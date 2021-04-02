package DataObjects;

import java.awt.*;

/*
Scene class. Represents the scene we are working on.
 */
public class Scene {

    /*
    This color is used when a ray does not hit any surface.
     */
    public Color BackroundColor;
    /*
    This number controls the amount of rays which are used for every light to compute soft shadows.
    Between 1-10.
     */
    public int NumberOfShadowRays;
    /*
    Maximum ray recursion level. Usually around 10.
     */
    public int MaximumRecursionLevel;
    /*
    Option of adding a fisheye lens that will perform a fisheye effect on the image.
     */
    public boolean FishEyeLens;

    /*
    Constructor.
     */
    public Scene(){

    }
}
