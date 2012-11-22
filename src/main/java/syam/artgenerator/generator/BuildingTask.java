/**
 * ArtGenerator - Package: syam.artgenerator.generator
 * Created: 2012/11/21 23:50:18
 */
package syam.artgenerator.generator;

import org.bukkit.Location;
import org.bukkit.block.Block;

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
            sendMessage("&c斜めには作成できません！");
            return;
        }
        plugin.debug("Using FaceDirection: " + face);

        int ax = 0, ay = 0, az = 0;
        BlockData putData;
        for (int x = 0; x <= width - 1; x++){
            for (int y = 0; y <= height - 1; y++){
                // first, set image-x to add variable block-x
                if (face == 0) ax = -x;
                else if (face == 1) az = -x;
                else if (face == 2) ax = x;
                else if (face == 3) az = x;

                // next, switch generate direction
                switch (dir){
                    case UP: // up side
                        if (face == 0) az = -y;
                        else if (face == 1) ax = y;
                        else if (face == 2) az = y;
                        else if (face == 3) ax = -y;
                        ay = 0;
                        break;

                    case DOWN: // down side
                        if (face == 0) az = y;
                        else if (face == 1) ax = -y;
                        else if (face == 2) az = -y;
                        else if (face == 3) ax = y;
                        ay = 0;
                        break;

                    case FACE: // player's face side
                        if (face == 0 || face == 2) az = 0;
                        else if (face == 1 || face == 3) ax = 0;
                        //ay = -y; // 左上起点
                        ay = -y + height - 1;
                        break;
                    default: throw new StateException("Undefined Direction!");
                }
                //target = loc.clone().add(ax, ay, az).getBlock();
                putData = blocks[x][y];
                loc.clone().add(ax, ay, az).getBlock().setTypeIdAndData(putData.getID(), putData.getData(), true);
                //target.setTypeId(block.getID());
                //target.setData(block.getData());
                //plugin.debug("put block: " + block.getID() + ":" + block.getData() + ", To: " + Actions.getBlockLocationString(target.getLocation()) + " ["+x+","+y+"]");
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
            return 3;
        }
        else if (247.5 <= rotation && rotation < 292.5){
            return 0;
        }
        else if (337.5 <= rotation && rotation < 360.0){
            return 1;
        }
        else if (67.5 <= rotation && rotation < 112.5){
            return 2;
        }

        return -1;
    }

    private void sendMessage(String msg){
        Actions.sendMessage(senderName, msg);
    }
}
