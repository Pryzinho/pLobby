package br.pryz.lobby.utils.profile;

import br.pryz.lobby.utils.easydatabase.EasyDatabaseException;
import br.pryz.lobby.utils.easydatabase.EasyDatabaseMysql;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProfileManager {
    private List<Profile> profiles = new ArrayList<>();
    private final EasyDatabaseMysql sql = new EasyDatabaseMysql("127.0.0.1", "root", "", "plobby");


    public Profile initProfile(Player p) {
        try {
            Connection con = sql.getConnection();
			String query = "SELECT * FROM `profiles` WHERE name='" + p.getName() +"'";
            PreparedStatement ps = con.prepareStatement(query);
            ps.executeQuery();
            ResultSet rs = ps.getResultSet();
            if (!rs.wasNull()) {
                Profile pf = new Profile(p,
                        rs.getString(2),
                        rs.getString(3),
                        rs.getLong(4),
                        rs.getLong(5));
                if (profiles.contains(p))profiles.remove(p);
                    profiles.add(pf);
                ps.close();
				con.close();
                return pf;
            } else {
                Profile pf = new Profile(p,
                        "Ugh, e-mail?",
                        "MEU, LINKA ISSO LOGO",
                        p.getFirstPlayed(),
                        System.currentTimeMillis());
                profiles.add(pf);
                String values = "(" + p.getName() + ", 'Ugh, e-mail?', 'Meu, linka isso logo!', " + p.getFirstPlayed() + ", " + System.currentTimeMillis() +");";
                PreparedStatement ps2 = con.prepareStatement("INSERT INTO `profiles` (`name`, `email`, `discord`, `firstTimeOnline`, `lastTimeOnline`) VALUES" + values);
                ps2.executeQuery();
				ps2.close();
				con.close();
                return pf;

            }
        } catch (EasyDatabaseException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
	
    public Profile getProfile(Player p) {
		for (int i = 0; i < profiles.size(); i++){
			Profile pf = profiles.get(i);
			if (pf.getPlayer().getName() == p.getName()){
				return pf;
			}
		}
		return null;
	}
	
    public EasyDatabaseMysql getSQL(){
        return sql;
    }


}
