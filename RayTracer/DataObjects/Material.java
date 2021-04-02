package DataObjects;

import java.awt.*;

/*
Material class.
 */
public class Material {

    /*
    Material diffuse color.
     */
    public Color DiffuseColor;
    /*
    Material specular color.
     */
    public Color SpecularColor;
    /*
    Material phong specularity coefficient. For phong shading model calculation.
     */
    public float PhongSpecularityCoefficient;
    /*
    Material reflection color.
     */
    public Color ReflectionColor;
    /*
    Material transparency value. in range between 0-1.
     */
    public float Transparency;

    /*
    Constructor.
     */
    public Material(){

    }
}
