package br.pryz.lobby.commands;
import br.pryz.lobby.utils.PryColor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import br.pryz.lobby.utils.PvP;

public class Quit implements CommandExecutor {
    public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
        if (!(s instanceof Player)) {
            s.sendMessage(PryColor.color("&cSomente jogadores podem utilizar este comando."));
            return true;
        }
        Player p = (Player)s;
        if (!PvP.isInPvp(p)) {
            p.sendMessage(PryColor.color("&cVocê não está batalhando."));
            return true;
        }
        if (args.length != 0) {
            s.sendMessage(PryColor.color("&cUse: /sair"));
            return true;
        }
        PvP.removePlayer(p);
        p.sendMessage(PryColor.color("&cVocê saiu da batalha."));
        return true;
    }

}
