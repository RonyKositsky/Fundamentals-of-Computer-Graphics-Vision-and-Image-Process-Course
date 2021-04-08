package RayTracing;

import DataObjects.*;
import DataObjects.Point;
import DataObjects.Surfaces.Box;
import DataObjects.Surfaces.Plane;
import DataObjects.Surfaces.Sphere;
import DataObjects.Surfaces.Surface;

import java.awt.*;
import java.awt.color.*;
import java.awt.image.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import static DataObjects.Vector.CreateVectorFromTwoPoints;

/**
 *  Main class for ray tracing exercise.
 */
public class RayTracer {

	public int imageWidth;
	public int imageHeight;
	public Scene scene;
	public Camera camera;
	List<Material> MaterialsList = new ArrayList<>();
	List<Surface> SurfacesList = new ArrayList<>();
	List<Light> LightsList = new ArrayList<>();

	/**
	 * Runs the ray tracer. Takes scene file, output image file and image size as input.
	 */
	public static void main(String[] args) {

		try {

			RayTracer tracer = new RayTracer();

			// Default values:
			tracer.imageWidth = 500;
			tracer.imageHeight = 500;

			if (args.length < 2)
				throw new RayTracerException("Not enough arguments provided. Please specify an input scene file and an output image file for rendering.");

			String sceneFileName = args[0];
			String outputFileName = args[1];

			if (args.length > 3)
			{
				tracer.imageWidth = Integer.parseInt(args[2]);
				tracer.imageHeight = Integer.parseInt(args[3]);
			}


			// Parse scene file:
			tracer.parseScene(sceneFileName);

			// Render scene:
			tracer.renderScene(outputFileName);

//		} catch (IOException e) {
//			System.out.println(e.getMessage());
		} catch (RayTracerException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}


	}

	/**
	 * Parses the scene file and creates the scene. Change this function so it generates the required objects.
	 */
	public void parseScene(String sceneFileName) throws IOException, RayTracerException {
		FileReader fr = new FileReader(sceneFileName);

		BufferedReader r = new BufferedReader(fr);
		String line = null;
		int lineNum = 0;
		System.out.println("Started parsing scene file " + sceneFileName);


		while ((line = r.readLine()) != null) {
			line = line.trim();
			++lineNum;

			if (line.isEmpty() || (line.charAt(0) == '#')) {  // This line in the scene file is a comment
				continue;
			}
			else {
				String code = line.substring(0, 3).toLowerCase();
				// Split according to white space characters:
				String[] params = line.substring(3).trim().toLowerCase().split("\\s+");

				if (code.equals("cam")) {
					Point cameraPosition = new Point(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
							Double.parseDouble(params[2]));
					Point lookAtPoint = new Point(Double.parseDouble(params[3]), Double.parseDouble(params[4]),
							Double.parseDouble(params[5]));
					Vector upVector = new Vector(Double.parseDouble(params[6]), Double.parseDouble(params[7]),
							Double.parseDouble(params[8]));
					double screeDistance = Double.parseDouble(params[9]);
					double screenWidth = Double.parseDouble(params[10]);
					boolean fishEye = params.length != 11 && Boolean.parseBoolean(params[11]);
					float kValue = params.length < 13 ? 0.5f : Float.parseFloat(params[12]);

					camera = new Camera(cameraPosition, lookAtPoint, upVector, screeDistance, screenWidth,
							fishEye, kValue);

					System.out.println(String.format("Parsed camera parameters (line %d)", lineNum));
				}
				else if (code.equals("set")) {
					Color bg = new Color(Float.parseFloat(params[0]), Float.parseFloat(params[1]),
							Float.parseFloat(params[2]));
					int numOfShadows = Integer.parseInt(params[3]);
					int maxRecursions = Integer.parseInt(params[4]);

					scene = new Scene(bg, numOfShadows, maxRecursions);

					System.out.println(String.format("Parsed general settings (line %d)", lineNum));
				}
				else if (code.equals("mtl")) {
					Color diffColor = new Color(Float.parseFloat(params[0]), Float.parseFloat(params[1]),
							Float.parseFloat(params[2]));
					Color specColor = new Color(Float.parseFloat(params[3]), Float.parseFloat(params[4]),
							Float.parseFloat(params[5]));
					Color reflectColor = new Color(Float.parseFloat(params[6]), Float.parseFloat(params[7]),
							Float.parseFloat(params[8]));
					float phong = Float.parseFloat(params[9]);
					float transparency = Float.parseFloat(params[10]);

					MaterialsList.add(new Material(diffColor, specColor, reflectColor, phong, transparency));
					System.out.println(String.format("Parsed material (line %d)", lineNum));
				}
				else if (code.equals("sph")) {
					Vector position = new Vector(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
							Double.parseDouble(params[2]));
					float radius = Float.parseFloat(params[3]);

					SurfacesList.add(new Sphere(position, radius, MaterialsList.get(Integer.parseInt(params[4])-1)));
					System.out.println(String.format("Parsed sphere (line %d)", lineNum));
				}
				else if (code.equals("pln")) {
					Vector vector = new Vector(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
							Double.parseDouble(params[2]));
					float off = Float.parseFloat(params[3]);

					SurfacesList.add(new Plane(vector, off, MaterialsList.get(Integer.parseInt(params[4])-1)));
					System.out.println(String.format("Parsed plane (line %d)", lineNum));
				}
				else if (code.equals("box")) {
					Point point = new Point(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
							Double.parseDouble(params[2]));
					double edgeLength = Double.parseDouble(params[8]);

					SurfacesList.add(new Box(point, edgeLength, MaterialsList.get(Integer.parseInt(params[4])-1)));
					System.out.println(String.format("Parsed box (line %d)", lineNum));
				}
				else if (code.equals("lgt")) {
					Point point = new Point(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
							Double.parseDouble(params[2]));
					Color lgColor = new Color(Float.parseFloat(params[3]), Float.parseFloat(params[4]),
							Float.parseFloat(params[5]));
					float specIntensity = Float.parseFloat(params[6]);
					float shadowIntensity = Float.parseFloat(params[7]);
					float radius = Float.parseFloat(params[8]);

					LightsList.add(new Light(point, lgColor, specIntensity, shadowIntensity, radius));
					System.out.println(String.format("Parsed light (line %d)", lineNum));
				}
				else {
					System.out.println(String.format("ERROR: Did not recognize object: %s (line %d)", code, lineNum));
				}
			}
		}
		System.out.println("Finished parsing scene file " + sceneFileName);
	}

	/**
	 * Renders the loaded scene and saves it to the specified file location.
	 */
	public void renderScene(String outputFileName) {
		long startTime = System.currentTimeMillis();

		// Create a byte array to hold the pixel data:
		byte[] rgbData = new byte[imageWidth * imageHeight * 3];

		Vector ForwardVector = CreateVectorFromTwoPoints(camera.LookAtPoint, camera.CameraPosition).NormalizeVector();
		Vector RightVector = camera.UpVector.CrossProduct(ForwardVector).NormalizeVector();

		double pixelSize = camera.ScreenWidth / imageWidth;
		Vector dx = RightVector.VectorsScalarMultiplication(pixelSize);
		Vector dy = camera.UpVector.VectorsScalarMultiplication(pixelSize);

		//Screen Center = Camera Position + Screen Distance * Forward Vector
		Vector ScreenCenter = camera.CameraPosition.PointAsVector().VectorsAddition(
				ForwardVector.VectorsScalarMultiplication(camera.ScreenDistance));

		//tmp = (width - pixel size) * Right Vector + (height - pixel size) * Up Vector
		Vector temp = RightVector.VectorsScalarMultiplication(camera.ScreenWidth - pixelSize).
				VectorsAddition(camera.UpVector.VectorsScalarMultiplication(imageHeight / 500 - pixelSize));

		//BottomLeftPixel = Screen Center - 0.5 * tmp
		Vector BottomLeftPixel = ScreenCenter.VectorSubtraction(temp.VectorsScalarMultiplication(0.5));

		for(int row = 0; row < imageHeight; row++){
			for(int col = 0; col < imageWidth; col ++){

				//shift = dx * col + dy * row
				Vector shift = dx.VectorsScalarMultiplication(col).
						VectorsAddition(dy.VectorsScalarMultiplication(row));

				//current pixel center = shift + bottom left pixel.
				Vector currentPixelCenter = BottomLeftPixel.VectorsAddition(shift);

				//ray = current pixel - camera position (normalize)
				Vector currentCenterRay = currentPixelCenter.
						VectorSubtraction(camera.CameraPosition.PointAsVector()).NormalizeVector();

				Surface surface = GetFirstIntersection(currentPixelCenter, currentCenterRay);
				GetColor(rgbData, 3*(col + row * imageWidth), surface);
			}
		}



		long endTime = System.currentTimeMillis();
		Long renderTime = endTime - startTime;

		System.out.println("Finished rendering scene in " + renderTime.toString() + " milliseconds.");

                // This is already implemented, and should work without adding any code.
		saveImage(this.imageWidth, rgbData, outputFileName);

		System.out.println("Saved file " + outputFileName);
	}

	private Surface GetFirstIntersection(Vector start, Vector ray){
		double first = Double.POSITIVE_INFINITY;
		Surface surface = null;
		Vector vec =null;
		for(Surface sur : SurfacesList){
			AbstractMap.SimpleEntry<Vector, Double> entry = sur.FindIntersection(ray, start);
			if(entry != null){
				double t = entry.getValue();
				if(entry.getValue() < first){
					surface = sur;
					first = t;
					vec = entry.getKey();
				}
			}
		}

		return surface;
	}

	//////////////////////// FUNCTIONS TO SAVE IMAGES IN PNG FORMAT //////////////////////////////////////////

	/*
	 * Saves RGB data as an image in png format to the specified location.
	 */
	public static void saveImage(int width, byte[] rgbData, String fileName) {
		try {

			BufferedImage image = bytes2RGB(width, rgbData);
			ImageIO.write(image, "png", new File(fileName));

		} catch (IOException e) {
			System.out.println("ERROR SAVING FILE: " + e.getMessage());
		}

	}

	/*
	 * Producing a BufferedImage that can be saved as png from a byte array of RGB values.
	 */
	public static BufferedImage bytes2RGB(int width, byte[] buffer) {
	    int height = buffer.length / width / 3;
	    ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
	    ColorModel cm = new ComponentColorModel(cs, false, false,
	            Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
	    SampleModel sm = cm.createCompatibleSampleModel(width, height);
	    DataBufferByte db = new DataBufferByte(buffer, width * height);
	    WritableRaster raster = Raster.createWritableRaster(sm, db, null);
	    BufferedImage result = new BufferedImage(cm, raster, false, null);

	    return result;
	}

	public static class RayTracerException extends Exception {
		public RayTracerException(String msg) {  super(msg); }
	}

	private void GetColor(byte[] data, int index, Surface surface){
		if(surface == null){
			data[index] = (byte)scene.BackgroundColor.getRed();
			data[index +1] = (byte)scene.BackgroundColor.getGreen();
			data[index] = (byte)scene.BackgroundColor.getBlue();
		}else{
			data[index] = (byte)surface.GetSurfaceMaterial().DiffuseColor.getRed();
			data[index + 1] = (byte)surface.GetSurfaceMaterial().DiffuseColor.getGreen();
			data[index + 2] = (byte)surface.GetSurfaceMaterial().DiffuseColor.getBlue();
		}
	}
}
