package net.buxville.rahman.buxinvest.SQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.buxville.rahman.buxinvest.BuxInvest;

import org.bukkit.entity.Player;

public class SQLsell {

	// Get current amount of stocks
	public static int getAmount(Player p, String index) {
		PreparedStatement ps;
		try {
			ps = BuxInvest
					.getConnection()
					.prepareStatement(
							"SELECT `amount` FROM player_stocks WHERE `index`=? AND `uuid`=?;");
			ps.setString(1, index);
			ps.setString(2, p.getUniqueId().toString());
			ResultSet rs = ps.executeQuery();
			rs.next();
			int currentamount = rs.getInt("amount");

			rs.close();
			ps.close();
			return currentamount;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}

	// Update player stocks
	public static boolean updateAmount(String index, Player p, int newvalue) {
		try {
			PreparedStatement ps2 = BuxInvest
					.getConnection()
					.prepareStatement(
							"UPDATE `player_stocks` SET `amount`=? WHERE `index`=? AND `uuid`=?;");
			ps2.setInt(1, newvalue);
			ps2.setString(2, index);
			ps2.setString(3, p.getUniqueId().toString());
			ps2.executeUpdate();
			ps2.close();
		} catch (SQLException e) {
			System.out.println("Error with value - BuxInvest");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// Update total owned
	public static boolean updateOwned(String index, int owneddiff) {
		try {
			PreparedStatement ps = BuxInvest
					.getConnection()
					.prepareStatement(
							"SELECT `owned`,`amount` FROM `stock_value` WHERE `index`=?;");
			ps.setString(1, index);
			ResultSet rs = ps.executeQuery();
			rs.next();
			int owned = rs.getInt("owned");
			rs.close();
			ps.close();
			int newvalue = owned - owneddiff;
			try {
				PreparedStatement ps2 = BuxInvest
						.getConnection()
						.prepareStatement(
								"UPDATE stock_value SET `owned`=? WHERE `index`=?;");
				ps2.setInt(1, newvalue);
				ps2.setString(2, index);
				ps2.executeUpdate();
				ps2.close();
			} catch (SQLException e) {
				System.out.println("Error with value - BuxInvest");
				e.printStackTrace();
				return false;
			}
		} catch (SQLException e) {
			System.out.println("Error with value - BuxInvest");
			e.printStackTrace();
			return false;
		}
		return true;

	}

	public static boolean removeStocks(String index, Player p) {
		try {
			PreparedStatement ps = BuxInvest
					.getConnection()
					.prepareStatement(
							"DELETE FROM `player_stocks` WHERE `index`=? AND `uuid`=?;");
			ps.setString(1, index);
			ps.setString(2, p.getUniqueId().toString());
			ps.execute();
			ps.close();
			return true;
		} catch (SQLException e) {
			System.out.println("Error with value - BuxInvest");
			e.printStackTrace();
			return false;
		}
	}

}
