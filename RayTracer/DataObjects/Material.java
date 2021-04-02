package DataObjects;

import javafx.scene.paint.PhongMaterial;

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
    public Material(Color diffuseColor, Color specularColor, Color reflectionColor,
                    float phongSpecularityCoefficient, float transparency){
        DiffuseColor = diffuseColor;
        SpecularColor = specularColor;
        ReflectionColor = reflectionColor;
        PhongSpecularityCoefficient = phongSpecularityCoefficient;
        Transparency = transparency;

    }
}
