package net.buxville.rahman.buxinvest.SQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.buxville.rahman.buxinvest.BuxInvest;

public class SQLportfolio {
	public static void getStocks(Player p) {
		try {
			PreparedStatement pslist = BuxInvest.getConnection()
					.prepareStatement(
							"SELECT `index`,`amount` FROM "
									+ "`player_stocks` WHERE `uuid`=?;");
			pslist.setString(1, p.getUniqueId().toString());
			ResultSet rslist = pslist.executeQuery();
			while (rslist.next()) {
				String ind = rslist.getString("index");
				int am = rslist.getInt("amount");
				p.sendMessage(ChatColor.GOLD + ind + "| " + am);
			}

			rslist.close();
			pslist.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return;

	}
}
