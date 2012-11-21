/**
 * ArtGenerator - Package: syam.artgenerator.generator
 * Created: 2012/11/21 23:50:18
 */
package syam.artgenerator.generator;

import java.awt.image.BufferedImage;

import org.bukkit.Location;

import syam.artgenerator.ArtGenerator;
import syam.artgenerator.exception.StateException;
import syam.artgenerator.util.Actions;

/**
 * BuildingTask (BuildingTask.java)
 * @author syam(syamn)
 */
public class BuildingTask implements Runnable{
	private final ArtGenerator plugin;
	private final String senderName;
	private final Location loc;
	private final Direction dir;

	private BlockData[][] blocks;
	private int width, height;

	public BuildingTask(final ArtGenerator plugin, final String senderName, final Location playerLocation, final Direction dir){
		this.plugin = plugin;

		this.senderName = senderName;
		this.loc = playerLocation;
		this.dir = dir;
	}

	public void putBlockData(final BlockData[][] blocks, final int width, final int height){
		this.blocks = blocks;
		this.width = width;
		this.height = height;
	}

	@Override
	public void run(){
		plugin.debug("== Start BuildingTask ==");

		final int face = getPlayerDirection();
    	if (face == -1){
    		sendMessage("斜めには作成できません！");
    		return;
    	}

    	int ax = 0, ay = 0, az = 0;
    	for (int x = 0; x <= width - 1; x++){
    		for (int y = 0; y <= height - 1; y++){
    			final BlockData block = blocks[x][y];
    			switch (dir){
    				case UP: // 上向き
    				case DOWN: // 下向き
    					sendMessage("現在この向きでの作成は対応していません");
    					break;
    				// プレイヤーの向き
    				case FACE:
    					if (face == 0){
	    					ax = -x;
	    					az = 0;
    					}
    					else if (face == 1){
    						ax = 0;
    						az = -x;
    					}
    					else if (face == 2){
    						ax = x;
    						az = 0;
    					}
    					else if (face == 3){
    						ax = 0;
    						az = -x;
    					}
    					ay = (height - 1) - y;
    					break;
    				// 例外
    				default:
    					throw new StateException("Undefined Direction!");
    			}
    			loc.add(ax, ay, az).getBlock().setTypeIdAndData(block.getID(), block.getData(), true);
    		}
    	}

    	sendMessage("&a" + width + "x" + height + "のドットアートを作成しました！");
    	plugin.debug("== Finish BuildingTask ==");
	}

	private int getPlayerDirection(){
    	if (loc == null) return -1;
    	double rotation = (loc.getYaw() - 90) % 360;
        if (rotation < 0) rotation += 360.0;

        if (157.5 <= rotation && rotation < 202.5){
        	return 0;
        }
        else if (247.5 <= rotation && rotation < 292.5){
        	return 1;
        }
        else if (337.5 <= rotation && rotation < 360.0){
        	return 2;
        }
        else if (67.5 <= rotation && rotation < 112.5){
        	return 3;
        }

        return -1;
    }

	private void sendMessage(String msg){
		Actions.sendMessage(senderName, msg);
	}
}
