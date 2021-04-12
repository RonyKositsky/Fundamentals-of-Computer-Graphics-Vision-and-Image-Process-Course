package DataObjects;

import DataObjects.Surfaces.Surface;
import Helpers.Utils;

import java.util.List;
import java.util.Random;

/*
Shadow grid class for calculating soft shadow.
 */
public class ShadowGrid {

    public Light GridLight;
    private double CellSize;
    private Vector BottomLeftPosition;
    private int NumberOfCells;
    private Vector du;
    private Vector dv;

    public ShadowGrid(Light light, Vector uAxis, Vector vAxis, int numberOfCells){
        GridLight = light;
        NumberOfCells = numberOfCells;
        CellSize = light.LightRadius / NumberOfCells;
        du = uAxis.VectorsScalarMultiplication(CellSize);
        dv = vAxis.VectorsScalarMultiplication(CellSize);

        // U * radius + V * radius
        Vector temp = uAxis.VectorsScalarMultiplication(light.LightRadius).VectorsAddition(
                vAxis.VectorsScalarMultiplication(light.LightRadius));

        //Bottom left position = Light Center - 0.5 * temp
        BottomLeftPosition = GridLight.Position.VectorSubtraction(temp.VectorsScalarMultiplication(0.5));
    }

    /*
    Calculating the light number of hits in a specific surface for a soft shadows calculation.
     */
    public int calculateNumberOfLightRayHits(Vector point, Surface surface, List<Surface> surfaceList){
        int NumberOfHits = 0;
        Random rand = new Random();

        for(int row = 0; row < NumberOfCells; row ++){
            for(int col = 0; col < NumberOfCells; col++){
                //shift = du * row + dv * col
                Vector shift = du.VectorsScalarMultiplication(row).
                        VectorsAddition(dv.VectorsScalarMultiplication(col));
                Vector currentPoint = BottomLeftPosition.VectorsAddition(shift);

                double randomX = rand.nextDouble() * CellSize;
                double randomY = rand.nextDouble() * CellSize;
                Vector currentRandomPoint = currentPoint.VectorsAddition(new Vector(randomX, randomY, 0));
                Vector ray = Vector.CreateVectorFromTwoPoints(point, currentRandomPoint).NormalizeVector();
                var hit = Utils.GetFirstIntersection(currentRandomPoint, ray,
                        null, surfaceList);
                if(hit.getKey() != null && hit.getKey().equals(surface))
                    NumberOfHits++;
            }
        }
        return NumberOfHits;
    }
}
