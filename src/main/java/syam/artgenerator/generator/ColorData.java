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
        putColor("35:4", 177, 166, 39);
        putColor("35:11", 46, 56, 141);
        putColor("35:13", 53, 70, 27);
        putColor("35:15", 0, 0, 0);
        putColor("35:0", 221, 221, 221);
        putColor("35:1", 172, 105, 65);
        putColor("35:9", 46, 110, 137);
        putColor("35:10", 126, 61, 181);
        putColor("35:7", 64, 64, 64);
        putColor("35:3", 106, 138, 201);
        putColor("35:6", 208, 132, 153);
        putColor("35:5", 65, 174, 56);
        putColor("35:2", 179, 80, 188);
        putColor("35:8", 154, 161, 161);
        putColor("35:12", 79, 50, 31);
        putColor("1", 125, 125, 125);
        putColor("3", 134, 96, 67);
        putColor("12", 219, 211, 160);
        putColor("13", 131, 123, 123);
        putColor("82", 158, 164, 176);
        putColor("49", 20, 18, 29);
        putColor("48", 103, 121, 103);
        putColor("86", 165, 110, 44);
        putColor("5", 156, 127, 78);
        putColor("4", 122, 122, 122);
        putColor("24", 218, 210, 158);
        putColor("45", 146, 99, 86);
        putColor("42", 219, 219, 219);
        putColor("41", 249, 236, 78);
        putColor("57", 97, 219, 213);
        putColor("22", 29, 71, 165);
        putColor("25", 100, 67, 50);
        putColor("89", 143, 118, 69);
        putColor("87", 40, 20, 20);
        putColor("88", 84, 64, 51);
        putColor("80", 239, 251, 251);
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
