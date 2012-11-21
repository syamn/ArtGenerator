/**
 * ArtGenerator - Package: syam.artgenerator.generator
 * Created: 2012/11/21 20:10:59
 */
package syam.artgenerator.generator;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import syam.artgenerator.ArtGenerator;
import syam.artgenerator.exception.StateException;
import syam.artgenerator.util.Actions;

/**
 * GeneratorTask (GeneratorTask.java)
 * @author syam(syamn)
 */
public class GeneratorTask implements Runnable{
	// Logger
    private static final Logger log = ArtGenerator.log;
    private static final String logPrefix = ArtGenerator.logPrefix;
    private static final String msgPrefix = ArtGenerator.msgPrefix;

    private final ArtGenerator plugin;

    private String senderName;
    private Location senderLocation;
    private Direction dir;

    private boolean isURL = false;
    private boolean isFile = false;
    private URL url = null;
    private File file = null;

    BufferedImage img = null;

    public GeneratorTask(final ArtGenerator plugin, final CommandSender sender, final Direction direction){
    	this.plugin = plugin;

    	this.senderName = (sender instanceof Player) ? sender.getName() : null;
    	this.senderLocation = (sender instanceof Player) ? ((Player) sender).getLocation() : null;
    	this.dir = direction;
    }

    public void setSource(final URL url){
    	this.isURL = true;
    	this.url = url;
    }
    public void setSource(final File file){
    	this.isFile = true;
    	this.file = file;
    }


    @Override
    public void run(){
    	plugin.debug("== Start GeneratorTask by " + ((senderName == null) ? "Console" : senderName) + " ==");

    	if (!isURL && !isFile) {
    		throw new StateException("Invalid state! Not source specified!");
    	}

    	// TODO: just only support URL
    	if (!isURL){
    		sendMessage("Just support only URL format!");
    		return;
    	}

    	// get image
    	try{
    		img = getImage();
    	}catch (IOException ex){
    		sendMessage("Could not read source image!");
    		if (plugin.getConfigs().isDebug()){
    			ex.printStackTrace();
    		}
    		return;
    	}

    	final int width = img.getWidth();
    	final int height = img.getHeight();

    	plugin.debug("Finish read the source image: width=" + width + ", height=" + height);

    	// loop image bits
    	plugin.debug("Start image convert loop");
    	BlockData[][] blocks = new BlockData[width][height];
    	for (int x = 0; x <= img.getWidth() - 1; x++){
    		for (int y = 0; y <= img.getHeight() - 1; y++){
    			Color closest = getClosestMatch(x, y);

    			String[] blockStr = ColorData.getBlockStr(closest).split(":");
    			final BlockData block = new BlockData(
    					Integer.parseInt(blockStr[0]),
    					(blockStr.length > 1) ? Byte.parseByte(blockStr[1]) : (byte) 0);

    			// put data
    			blocks[x][y] = block;
    		}
    	}
    	plugin.debug("Finish image convert loop");

    	// build
    	BuildingTask task = new BuildingTask(plugin, senderName, senderLocation, dir);
    	task.putBlockData(blocks, width, height);
    	int taskID = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, task, 0L);
    	plugin.debug("Started BuildingTask (TaskID:" + taskID + ")");

    	plugin.debug("== Finish GeneratorTask ==");
    }

    /**
     * Get image
     * @return
     * @throws IOException
     */
    private BufferedImage getImage() throws IOException{
    	if (isURL){
    		plugin.debug("Getting image from uri: " + url.getPath());
    		return ImageIO.read(url);
    	}
    	else if (isFile){
    		plugin.debug("Getting image from file: " + file.getPath());
    		return ImageIO.read(file);
    	}
    	return null;
    }

    /**
     * プラグインに登録済みの中から一番近い色を返す
     * @param x
     * @param y
     * @return
     */
    private Color getClosestMatch(final int x, final int y){
    	if (img == null || (img.getRGB(x, y) >> 24 & 0xFF) < 10){
    		return null;
    	}

    	int prevMin = 765; //255 * 3; // possibly max value
    	Color closestColor = null;

    	for (Color col : ColorData.getColorMap().keySet()){
    		int diff = getColorDiff(new Color(img.getRGB(x, y)), col);
    		if (diff >= prevMin) continue;

    		prevMin = diff;
    		closestColor = col;
    	}

    	return closestColor;
    }
    /**
     * 色と色の差をintで返す
     * @param c1
     * @param c2
     * @return
     */
    private int getColorDiff(final Color c1, final Color c2){
    	int diff = 0;
    	diff += Math.abs(c1.getRed() - c2.getRed());
    	diff += Math.abs(c1.getGreen() - c2.getGreen());
    	diff += Math.abs(c1.getBlue() - c2.getBlue());
    	return diff;
    }

    private void sendMessage(String msg){
		Actions.sendMessage(senderName, msg);
	}
}
