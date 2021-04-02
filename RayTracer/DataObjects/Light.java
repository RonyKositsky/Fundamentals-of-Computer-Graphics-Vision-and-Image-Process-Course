package DataObjects;

import java.awt.*;

/*
Light class. Represent light source in the scene.
 */
public class Light {

    /*
    Light position.
     */
    public Point Position;
    /*
    Light color.
     */
    public Color LightColor;
    /*
    Light specular intensity.
     */
    public float SpecularIntensity;
    /*
    Light shadow intensity. The light received by a surface which is hidden from the light is multiplied
    by (1-shadow intensity).
     */
    public float ShadowIntensity;
    /*
    Light radius.
     */
    public float LightRadius;

    /*
    Constructor.
     */
    public Light(){

    }
}
