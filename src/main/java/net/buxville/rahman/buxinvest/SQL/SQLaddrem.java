package net.buxville.rahman.buxinvest.SQL;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.buxville.rahman.buxinvest.BuxInvest;

public class SQLaddrem {

	public static void removeChanges(String index) {
		// DELETE FROM table_name WHERE some_column=some_value;
		try {
			PreparedStatement ps = BuxInvest.getConnection().prepareStatement(
					"DELETE FROM `stock_changes` WHERE `index`=?;");
			ps.setString(1, index);
			ps.execute();
			ps.close();
		} catch (SQLException e) {
			System.out.println("Error with value - BuxInvest");
			e.printStackTrace();
			return;
		}
		return;
	}

	public static void removeHistory(String index) {
		try {
			PreparedStatement ps = BuxInvest.getConnection().prepareStatement(
					"DELETE FROM `stock_value` WHERE `index`=?;");
			ps.setString(1, index);
			ps.execute();
			ps.close();
		} catch (SQLException e) {
			System.out.println("Error with value - BuxInvest");
			e.printStackTrace();
			return;
		}
		return;
	}

	public static void removeStock(String index) {
		try {
			PreparedStatement ps = BuxInvest.getConnection().prepareStatement(
					"DELETE FROM `stocks` WHERE `index`=?;");
			ps.setString(1, index);
			ps.execute();
			ps.close();
		} catch (SQLException e) {
			System.out.println("Error with value - BuxInvest");
			e.printStackTrace();
			return;
		}
		return;
	}

	public static void addCompany(String index, String companyname, Player p) {
		try {
			PreparedStatement ps = BuxInvest.getConnection().prepareStatement(
					"INSERT INTO stocks (`index`, `stockName`) VALUES (?,?);");
			ps.setString(1, index);
			ps.setString(2, companyname);
			ps.execute();
			ps.close();
		} catch (SQLException e) {
			p.sendMessage(ChatColor.RED + "Error.");
			e.printStackTrace();
			return;
		}
	}

}
