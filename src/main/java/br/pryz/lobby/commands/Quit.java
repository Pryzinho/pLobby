package br.pryz.lobby.commands;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import br.pryz.lobby.utils.PvP;

public class Quit implements CommandExecutor {
    public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
        if (!(s instanceof Player)) {
            s.sendMessage(color("&cSomente jogadores podem utilizar este comando."));
            return true;
        }
        Player p = (Player)s;
        if (!PvP.isInPvp(p)) {
            p.sendMessage(color("&cVocê não está batalhando."));
            return true;
        }
        if (args.length != 0) {
            s.sendMessage(color("&cUse: /sair"));
            return true;
        }
        PvP.removePlayer(p);
        p.sendMessage(color("&cVocê saiu da batalha."));
        return true;
    }

    private String color(String txt){
        return ChatColor.translateAlternateColorCodes('&', txt);
    }
}
