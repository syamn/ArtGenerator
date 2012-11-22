/**
 * ArtGenerator - Package: syam.artgenerator.command
 * Created: 2012/11/21 19:26:15
 */
package syam.artgenerator.command;

import syam.artgenerator.Perms;
import syam.artgenerator.generator.Timer;
import syam.artgenerator.util.Actions;

/**
 * ReloadCommand (ReloadCommand.java)
 * @author syam(syamn)
 */
public class ReloadCommand extends BaseCommand{
    public ReloadCommand(){
        bePlayer = false;
        name = "reload";
        argLength = 0;
        usage = "<- reload config.yml";
    }

    @Override
    public void execute() {
        Timer.stopAll();
        plugin.getServer().getScheduler().cancelTasks(plugin);

        try{
            plugin.getConfigs().loadConfig(false);
        }catch (Exception ex){
            log.warning(logPrefix+"an error occured while trying to load the config file.");
            ex.printStackTrace();
            return;
        }
        Actions.message(sender, "&aConfiguration reloaded!");
    }

    @Override
    public boolean permission() {
        return Perms.RELOAD.has(sender);
    }
}