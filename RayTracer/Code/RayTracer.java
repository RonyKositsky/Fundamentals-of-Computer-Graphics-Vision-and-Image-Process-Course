package RayTracing;

import DataObjects.*;
import DataObjects.Surfaces.Box;
import DataObjects.Surfaces.Plane;
import DataObjects.Surfaces.Sphere;
import DataObjects.Surfaces.Surface;
import Helpers.ColorArithmetics;
import Helpers.Utils;

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
					Vector cameraPosition = new Vector(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
							Double.parseDouble(params[2]));
					Vector lookAtPoint = new Vector(Double.parseDouble(params[3]), Double.parseDouble(params[4]),
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
					Vector point = new Vector(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
							Double.parseDouble(params[2]));
					double edgeLength = Double.parseDouble(params[3]);

					SurfacesList.add(new Box(point, edgeLength, MaterialsList.get(Integer.parseInt(params[4])-1)));
					System.out.println(String.format("Parsed box (line %d)", lineNum));
				}
				else if (code.equals("lgt")) {
					Vector point = new Vector(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
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

		//Calculating global vectors we are going to use.
		Vector ForwardVector = CreateVectorFromTwoPoints(camera.CameraPosition, camera.LookAtPoint).NormalizeVector();
		Vector RightVector = ForwardVector.CrossProduct(camera.UpVector).NormalizeVector();
		Vector ScreenCenter = ForwardVector.VectorsScalarMultiplication(camera.ScreenDistance)
				.VectorsAddition(camera.CameraPosition);
		Vector vp = ForwardVector.CrossProduct(RightVector).NormalizeVector();

		double pixelWidth = camera.ScreenWidth / imageWidth;
		double pixelHeight = imageWidth / imageHeight * pixelWidth;

		//Iterating over all the pixels.
		for(int h = 0; h< imageHeight; h++){
			for(int w =0; w < imageWidth; w++){
				double xPixel = w + 0.5;
				double yPixel = h + 0.5;

				double aspectRatio = imageWidth / imageHeight;
				double upDistance = (yPixel - imageHeight / 2) * pixelHeight;
				double rightDistance = (xPixel - imageWidth / 2) * pixelWidth;

				Vector upMovement = vp.VectorsScalarMultiplication(upDistance);
				Vector rightMovement = RightVector.VectorsScalarMultiplication(rightDistance);
				Vector pixelCenter = ScreenCenter.VectorsAddition(upMovement).VectorsAddition(rightMovement);
				Vector ray = Vector.CreateVectorFromTwoPoints(camera.CameraPosition, pixelCenter).NormalizeVector();
				//FishEye calculation
				if(camera.FishEyeLens){
					Vector centerToCurrent = CreateVectorFromTwoPoints(ScreenCenter, pixelCenter);
					double pixelRadius = Math.sqrt(centerToCurrent.DotProduct(centerToCurrent));
					double theta = calcThetaRadians(camera.KValue, pixelRadius, camera.ScreenDistance);
					if(theta > (Math.PI / 2.0)) {
						getColor(rgbData, 3 * (w + h * imageWidth), null, null, null, true);
						continue;
					}
					double radiusSize = camera.ScreenDistance*Math.tan(theta);

					Vector radiusVector = Vector.CreateVectorFromTwoPoints(pixelCenter,ScreenCenter).NormalizeVector();
					radiusVector = radiusVector.VectorsScalarMultiplication(radiusSize).VectorsAddition(ScreenCenter);
					ray = camera.CameraPosition.VectorSubtraction(radiusVector).NormalizeVector();
				}


				AbstractMap.SimpleEntry<Surface, Vector> entry =
						Utils.GetFirstIntersection(camera.CameraPosition, ray, null, SurfacesList);
				Surface surface = entry.getKey();
				Vector hitPoint = entry.getValue();

				getColor(rgbData, 3 * (w + h * imageWidth), surface, ray, hitPoint, false);
			}
		}

		long endTime = System.currentTimeMillis();
		Long renderTime = endTime - startTime;

		System.out.println("Finished rendering scene in " + renderTime.toString() + " milliseconds.");

		// This is already implemented, and should work without adding any code.
		saveImage(this.imageWidth, rgbData, outputFileName);

		System.out.println("Saved file " + outputFileName);
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

	/*
	Get pixel color.
	 */
	private void getColor(byte[] data, int index, Surface surface, Vector ray, Vector hitPoint, boolean setBlack){
		Color pixelColor;
		if(!setBlack) {
			pixelColor = calculateColor(hitPoint, surface);
			pixelColor = getTransparency(hitPoint, ray, surface, pixelColor);
			Color reflection = getReflection(ray, hitPoint, surface, scene.MaximumRecursionLevel);
			pixelColor = ColorArithmetics.addColors(pixelColor, reflection);
		}
		else{
			pixelColor = Color.BLACK;
		}

		data[index] = (byte)pixelColor.getRed();	//(byte)surface.GetSurfaceMaterial().DiffuseColor.getRed();
		data[index + 1] = (byte)pixelColor.getGreen();
		data[index + 2] = (byte)pixelColor.getBlue();
	}

	/*
	Calculating the diffuse and specular color for all the lights.
	 */
	private Color calculateColor(Vector hitPoint, Surface surface){
		if(surface == null)
			return scene.BackgroundColor;

		Color colorTotal = Color.BLACK;

		//Iterating over all lights to see if they hit the given hitPoint directly
		for(Light light : LightsList){
			Vector lightRay = Vector.CreateVectorFromTwoPoints(light.Position, hitPoint).NormalizeVector();

			double lightIntensity = calculateLightIntensity(light, hitPoint, lightRay, surface);
			colorTotal = ColorArithmetics.addColors(colorTotal,
					getDiffuseLight(lightRay, surface, hitPoint, light, lightIntensity));
			colorTotal = ColorArithmetics.addColors(colorTotal,
					getSpecularLight(lightRay, surface, hitPoint, light, lightIntensity));
		}
		return colorTotal;
	}

	/*
	Calculating the specular color.
	 */
	private Color getSpecularLight(Vector lightRay, Surface surface, Vector hitPoint, Light light, double lightIntensity){
		Material material = surface.getSurfaceMaterial();
		//R = (2L*N)N - L
		Vector normal = surface.getNormal(hitPoint).NormalizeVector();
		double dotProduct = lightRay.DotProduct(normal);
		dotProduct = dotProduct * 2;
		Vector R = normal.VectorsScalarMultiplication(dotProduct).VectorSubtraction(lightRay);

		//Ispec=Ks Ipcosn(??)=Ks Ip(R???V)n * light intensity
		double specularity = Math.pow(Math.max(R.DotProduct(lightRay),0),material.PhongSpecularityCoefficient) * lightIntensity;
		return ColorArithmetics.multiplyColors(ColorArithmetics.multiplyColors(material.SpecularColor,
				(float)specularity*light.SpecularIntensity),light.LightColor);
	}

	/*
	Calculating the diffuse color.
	 */
	private Color getDiffuseLight(Vector lightRay, Surface surface, Vector hitPoint, Light light, double lightIntensity){
		double nDotL;
		Vector normal = surface.getNormal(hitPoint).NormalizeVector();
		Color resColor = ColorArithmetics.multiplyColors(surface.getSurfaceMaterial().DiffuseColor, light.LightColor);
		resColor = ColorArithmetics.multiplyColors(resColor, (float) lightIntensity);
		nDotL = normal.DotProduct(lightRay);	//performing N dot L
		resColor = ColorArithmetics.multiplyColors(resColor, Math.max((float)nDotL, 0));
		return resColor;
	}

	/*
	Calculation the reflections. Recursive function, with maximum depth which is defined in the txt file.
	 */
	private Color getReflection(Vector ray, Vector hitPoint, Surface surface, int recursion){
		if (surface == null || recursion == 0)
			return Color.BLACK;

		Vector normal = surface.getNormal(hitPoint).NormalizeVector();
		double dotProduct = 2.0 * ray.DotProduct(normal);
		Vector normalDotProduct = normal.VectorsScalarMultiplication(dotProduct);
		Vector reflectionRay = ray.VectorSubtraction(normalDotProduct);

		AbstractMap.SimpleEntry<Surface, Vector> entry = Utils.GetFirstIntersection(hitPoint, reflectionRay,
				surface, SurfacesList);
		Surface hitSurface = entry.getKey();
		Vector hit = entry.getValue();

		Color color = calculateColor(hit, hitSurface);
		color = getTransparency(hit, reflectionRay, hitSurface, color);

		Color reflection = getReflection(reflectionRay, hit, hitSurface, recursion -1);
		color = ColorArithmetics.addColors(color, reflection);
		color = ColorArithmetics.multiplyColors(color, surface.getSurfaceMaterial().ReflectionColor);

		return color;
	}

	/*
	Calculating light intensity by the soft shadows algorithm.
	light intensity = (1 - shadow_intensity) * 1 + shadow_intensity *
		(%of rays that hit the points from the light source)
	 */
	private double calculateLightIntensity(Light light, Vector hitPoint, Vector lightRay, Surface surface){
		int n = scene.NumberOfShadowRays;
		Vector[] axis = Utils.FindPerpendicularPlane(light, lightRay);
		ShadowGrid grid = new ShadowGrid(light, axis[0], axis[1], scene.NumberOfShadowRays);
		double hitPercentage = grid.calculateNumberOfLightRayHits(hitPoint, surface, SurfacesList) / Math.pow(n , 2);
		return (1 - light.ShadowIntensity) + light.ShadowIntensity * hitPercentage;
	}

	/*
	Calculating the surface transparency color. We will loop until we hit not transparent surface or the background.
	 */
	private Color getTransparency(Vector hitPoint, Vector direction, Surface workSurface, Color color){

		if(hitPoint == null || workSurface.getSurfaceMaterial().Transparency == 0){
			return color;
		}

		AbstractMap.SimpleEntry<Surface, Vector> rayAfter = Utils.GetFirstIntersection(hitPoint, direction,
				workSurface, SurfacesList);
		Surface rayAfterSurface = rayAfter.getKey();
		Vector rayAfterVector = rayAfter.getValue();
		float transparency = workSurface.getSurfaceMaterial().Transparency;
		color = ColorArithmetics.multiplyColors(color, (1-transparency));		//color of current transparent surface
		Color temp = ColorArithmetics.multiplyColors(calculateColor(rayAfterVector, rayAfterSurface), transparency); //color of surface behind
		color = ColorArithmetics.addColors(color, temp);

		while(rayAfter.getKey() != null){
			rayAfterSurface = rayAfter.getKey();
			rayAfterVector = rayAfter.getValue();
			transparency = workSurface.getSurfaceMaterial().Transparency;
			if(rayAfterVector == null || rayAfterSurface.getSurfaceMaterial().Transparency == 0){
				return color;
			}
			if(rayAfterSurface.equals(workSurface)){
				rayAfter = Utils.GetFirstIntersection(rayAfterVector, direction, workSurface, SurfacesList);
				if(rayAfter.getKey() != null){
					workSurface = rayAfter.getKey();
				}
				continue;
			}

			//Surface has a measure of transparency
			color = ColorArithmetics.multiplyColors(color, (1-transparency));		//color of current transparent surface
			temp = ColorArithmetics.multiplyColors(calculateColor(rayAfterVector, rayAfterSurface), transparency); //color of surface behind
			color = ColorArithmetics.addColors(color, temp);
			rayAfter = Utils.GetFirstIntersection(rayAfterVector, direction, rayAfterSurface, SurfacesList);
		}
		return color;
	}

	private double calcThetaRadians(double k, double radius, double screenDistance){
		if(k > 0 && k<= 1){
			return Math.atan((radius*k)/screenDistance)/k;
		}
		if(k == 0){
			return radius/screenDistance;
		}
		else {
			return Math.asin((radius*k)/screenDistance)/k;
		}
	}
}