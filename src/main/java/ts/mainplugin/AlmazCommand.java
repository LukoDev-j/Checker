package ts.mainplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface AlmazCommand {
    boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings);
}
