/**
 * ArtGenerator - Package: syam.artgenerator.generator
 * Created: 2012/11/21 20:37:28
 */
package syam.artgenerator.generator;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * DataMap (DataMap.java)
 * @author syam(syamn)
 */
public class ColorData {
	//private static Map<Integer, String> colorMap = new HashMap<Integer, String>();
	private static Map<Color, String> colorMap = new HashMap<Color, String>();

	public static void init(){
		colorMap.clear();

		// put colors
		putColor("35:14", 150, 52, 48);

	}

	private static void putColor(final String block, final int r, final int g, final int b){
		colorMap.put(new Color(r, g, b), block.trim());
	}

	public static String getBlockStr(final Color color){
		//if (intRGB < 0) return "0";
		if (color == null) return "0";
		return (colorMap.containsKey(color)) ? colorMap.get(color) : "0";
	}

	public static Map<Color, String> getColorMap(){
		return colorMap;
	}
}
