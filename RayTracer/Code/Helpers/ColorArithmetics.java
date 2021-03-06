package Helpers;

import java.awt.*;

public class ColorArithmetics {
    public static Color multiplyColors(Color a, Color b){
        float red = (a.getRed() / 255.0F) * (b.getRed() / 255.0F);
        float blue = (a.getBlue() / 255.0F) * (b.getBlue() / 255.0F);
        float green = (a.getGreen() / 255.0F) * (b.getGreen() / 255.0F);
        return new Color(Math.min(red,1),Math.min(green,1),Math.min(blue,1));

    }

    public static Color multiplyColors(Color a, float num){
        float red = (a.getRed() / 255.0F) * num;
        float blue = (a.getBlue() / 255.0F) * num;
        float green = (a.getGreen() / 255.0F) * num;
        return new Color(Math.min(red,1),Math.min(green,1),Math.min(blue,1));

    }

    public static Color addColors(Color a, Color b){
        float red = (a.getRed() / 255.0F) + (b.getRed() / 255.0F);
        float blue = (a.getBlue() / 255.0F) + (b.getBlue() / 255.0F);
        float green = (a.getGreen() / 255.0F) + (b.getGreen() / 255.0F);
        return new Color(Math.min(red,1),Math.min(green,1),Math.min(blue,1));
    }
}

