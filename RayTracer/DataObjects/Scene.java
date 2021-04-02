package DataObjects;

import java.awt.*;

/*
Scene class. Represents the scene we are working on.
 */
public class Scene {

    /*
    This color is used when a ray does not hit any surface.
     */
    public Color BackgroundColor;
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
    Constructor.
     */
    public Scene(Color backgroundColor, int numberOfShadowRays, int maximumRecursionLevel){
        BackgroundColor = backgroundColor;
        NumberOfShadowRays = numberOfShadowRays;
        MaximumRecursionLevel = maximumRecursionLevel;
    }
}
