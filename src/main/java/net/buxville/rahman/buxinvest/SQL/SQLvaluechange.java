package net.buxville.rahman.buxinvest.SQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.buxville.rahman.buxinvest.BuxInvest;

public class SQLvaluechange {

	// Add stock value
	public static void addEntry(String index, Integer value, Integer amount) {
		try {
			PreparedStatement ps = BuxInvest
					.getConnection()
					.prepareStatement(
							"INSERT INTO stock_value (`index`,`value`,`change`,`amount`,`owned`) VALUES (?,?,?,?,?);");
			ps.setString(1, index);
			ps.setInt(2, value);
			ps.setInt(3, 0);
			ps.setInt(4, amount);
			ps.setInt(5, 0);
			ps.execute();
			ps.close();
		} catch (SQLException e) {
			System.out.println("Error with value - BuxInvest");
			e.printStackTrace();
			return;
		}
	}

	// Update stock value
	public static void updateEntry(String index, Integer value) {
		try {
			PreparedStatement ps = BuxInvest.getConnection().prepareStatement(
					"UPDATE stock_value SET `value`=? WHERE `index` = ?;");
			ps.setInt(1, value);
			ps.setString(2, index);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			System.out.println("Error with value - BuxInvest");
			e.printStackTrace();
			return;
		}
	}

	// Update stock amount
	public static void updateAmount(String index, Integer amount) {
		try {
			PreparedStatement ps = BuxInvest.getConnection().prepareStatement(
					"UPDATE stock_value SET `amount`=? WHERE `index` = ?;");
			ps.setInt(1, amount);
			ps.setString(2, index);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			System.out.println("Error with value - BuxInvest");
			e.printStackTrace();
			return;
		}
	}

	// Add change amount
	public static void addChange(String index, Integer lower, Integer upper) {
		try {
			PreparedStatement ps = BuxInvest
					.getConnection()
					.prepareStatement(
							"INSERT INTO stock_changes (`index`, `lower`, `upper`) VALUES (?,?,?);");
			ps.setString(1, index);
			ps.setInt(2, 0 - lower);
			ps.setInt(3, upper);
			ps.execute();
			ps.close();
		} catch (SQLException e) {
			System.out.println("Error with value - BuxInvest");
			e.printStackTrace();
			return;
		}
	}

	public static void updateChange(String index, Integer lower, Integer upper) {
		try {
			PreparedStatement ps = BuxInvest
					.getConnection()
					.prepareStatement(
							"UPDATE stock_changes SET `lower`=?,`upper`=? WHERE `index` = ?;");
			ps.setInt(1, 0 - lower);
			ps.setInt(2, upper);
			ps.setString(3, index);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			System.out.println("Error with value - BuxInvest");
			e.printStackTrace();
			return;
		}
	}

	public static Integer getValue(String index) {
		int value = -1;
		try {
			PreparedStatement ps = BuxInvest.getConnection().prepareStatement(
					"SELECT `value` FROM stock_value WHERE `index`=?;");
			ps.setString(1, index);
			ResultSet rs = ps.executeQuery();
			rs.next();
			value = rs.getInt("value");

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println("Error with value - BuxInvest");
			e.printStackTrace();
			return null;
		}
		return value;
	}
}
