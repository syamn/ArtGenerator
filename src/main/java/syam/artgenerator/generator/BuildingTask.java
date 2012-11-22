/**
 * ArtGenerator - Package: syam.artgenerator.generator
 * Created: 2012/11/21 23:50:18
 */
package syam.artgenerator.generator;

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

    private long generator_Taked = 0;

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
            sendMessage("&cInvalid face Direction!");
            Timer.removeData(senderName);
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
                    default:
                        Timer.removeData(senderName);
                        throw new StateException("Undefined Direction! Please report this!");
                }
                //target = loc.clone().add(ax, ay, az).getBlock();
                putData = blocks[x][y];
                loc.clone().add(ax, ay, az).getBlock().setTypeIdAndData(putData.getID(), putData.getData(), true);
                //target.setTypeId(block.getID());
                //target.setData(block.getData());
                //plugin.debug("put block: " + block.getID() + ":" + block.getData() + ", To: " + Actions.getBlockLocationString(target.getLocation()) + " ["+x+","+y+"]");
            }
        }
        plugin.debug("== Finish BuildingTask ==");

        long building_Taked = Timer.getDiffMillis(senderName);
        long total_Taked = this.generator_Taked + building_Taked;

        sendMessage("&aGenerated " + width + "x" + height + " block art!");
        sendMessage("&7Total " + total_Taked + "ms (background " + generator_Taked + "ms + building &c" + building_Taked + "ms&7)");
    }

    private int getPlayerDirection(){
        if (loc == null) return -1;
        double rotation = (loc.getYaw() - 90) % 360;
        if (rotation < 0) rotation += 360.0;
        // valid 0 - 360 rotate

        if (240 <= rotation && rotation <= 300){ // 270: 240 - 300
            return 0;
        } //315
        else if (330 <= rotation || rotation <= 30){ // 0: -30(330) - 30
            return 1;
        } //45
        else if (60 <= rotation && rotation <= 120){ // 90: 60.0 - 120
            return 2;
        } //135
        else if (150 <= rotation && rotation <= 210){ // 180: 150 - 210
            return 3;
        } //225

        return -1;
    }

    private void sendMessage(String msg){
        Actions.sendMessage(senderName, msg);
    }

    public void putGenTakedtime(final long taked){
        this.generator_Taked = taked;
    }
}
