/**
 * ArtGenerator - Package: syam.artgenerator.command
 * Created: 2012/11/21 19:41:06
 */
package syam.artgenerator.command;

import java.net.MalformedURLException;
import java.net.URL;

import syam.artgenerator.Perms;
import syam.artgenerator.exception.CommandException;
import syam.artgenerator.generator.Direction;
import syam.artgenerator.generator.GeneratorTask;
import syam.artgenerator.util.Actions;

/**
 * GenerateCommand (GenerateCommand.java)
 * @author syam(syamn)
 */
public class GenerateCommand extends BaseCommand{
	public GenerateCommand(){
		bePlayer = true;
		name = "generate";
		argLength = 1;
		usage = "<URL,file> (up/down) <- generate dot art";
	}

	@Override
	public void execute() throws CommandException {
		// check source
		URL url = null;
		try{
			url = new URL(args.get(0));
		}catch(MalformedURLException ex){
			throw new CommandException(logPrefix+ "Invalid URL!");
		}

		// check direction
		Direction dir = Direction.FACE;
		if (args.size() >= 2){
			String str1 = args.get(1);
			for(Direction d : Direction.values()){
				if (d.name().equalsIgnoreCase(str1)){
					dir = d;
				}
			}
		}

		final GeneratorTask task = new GeneratorTask(plugin, sender, dir);
		task.setSource(url);
		int taskID = plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, task, 0L);

		Actions.message(sender, msgPrefix + "ドットアートの生成を開始しました..");
	}

	@Override
	public boolean permission() {
		return Perms.GENERATE.has(sender);
	}
}