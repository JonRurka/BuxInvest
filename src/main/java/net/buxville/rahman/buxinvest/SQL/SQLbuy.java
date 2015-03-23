package net.buxville.rahman.buxinvest.SQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import net.buxville.rahman.buxinvest.BuxInvest;

public class SQLbuy {

	// Add stocks to players portfolio
	public static boolean addStocks(String index, Player p, Integer amount) {
		try {
			PreparedStatement ps = BuxInvest
					.getConnection()
					.prepareStatement(
							"INSERT INTO player_stocks (`uuid`,`index`,`amount`,`username`) VALUES (?,?,?,?);");
			ps.setString(1, p.getUniqueId().toString());
			ps.setString(2, index);
			ps.setInt(3, amount);
			ps.setString(4, p.getName());
			ps.execute();
			ps.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// Get buy data
	public static int[] getValues(String index) {
		int owned = -1;
		int amount = -1;
		try {
			PreparedStatement ps = BuxInvest
					.getConnection()
					.prepareStatement(
							"SELECT `owned`,`amount` FROM `stock_value` WHERE `index`=?;");
			ps.setString(1, index);
			ResultSet rs = ps.executeQuery();
			rs.next();
			owned = rs.getInt("owned");
			amount = rs.getInt("amount");
			ps.close();
		} catch (SQLException e) {
			System.out.println("Error with value - BuxInvest");
			e.printStackTrace();
		}
		return new int[] { owned, amount };

	}

	// Update owned amount
	public static boolean updateValue(String index, Integer owned) {
		try {
			PreparedStatement ps = BuxInvest.getConnection().prepareStatement(
					"UPDATE stock_value SET `owned`=? WHERE `index`=?;");
			ps.setInt(1, owned);
			ps.setString(2, index);
			ps.executeUpdate();
			ps.close();
			return true;
		} catch (SQLException e) {
			System.out.println("Error with value - BuxInvest");
			e.printStackTrace();
			return false;
		}
	}

	// Update amount
	public static boolean updateStocks(String index, Player p, Integer amount) {
		try {
			PreparedStatement ps = BuxInvest
					.getConnection()
					.prepareStatement(
							"SELECT `amount` FROM player_stocks WHERE `index`=? AND `uuid`=?;");
			ps.setString(1, index);
			ps.setString(2, p.getUniqueId().toString());
			ResultSet rs = ps.executeQuery();
			rs.next();
			int currentamount = rs.getInt("amount");

			int newamount = currentamount + amount;
			rs.close();
			ps.close();

			try {
				PreparedStatement ps2 = BuxInvest
						.getConnection()
						.prepareStatement(
								"UPDATE `player_stocks` SET `amount`=? WHERE `index`=? AND `uuid`=?;");
				ps2.setInt(1, newamount);
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
		} catch (SQLException e) {
			System.out.println("Error with value - BuxInvest");
			e.printStackTrace();
			return false;
		}
	}

}
