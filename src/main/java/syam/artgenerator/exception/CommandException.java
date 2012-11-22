/**
 * ArtGenerator - Package: syam.artgenerator.exception
 * Created: 2012/11/21 19:16:09
 */
package syam.artgenerator.exception;

/**
 * CommandException (CommandException.java)
 * @author syam(syamn)
 */
public class CommandException extends Exception{
    private static final long serialVersionUID = 1413660793408779965L;

    public CommandException(String message){
        super(message);
    }

    public CommandException(Throwable cause){
        super(cause);
    }

    public CommandException(String message, Throwable cause){
        super(message, cause);
    }
}
