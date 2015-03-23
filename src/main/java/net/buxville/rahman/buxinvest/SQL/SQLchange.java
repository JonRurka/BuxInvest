package net.buxville.rahman.buxinvest.SQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.buxville.rahman.buxinvest.BuxInvest;

public class SQLchange {

	public static List<String> stockList() {
		ArrayList<String> indexs = new ArrayList<String>();
		try {
			PreparedStatement pslist = BuxInvest.getConnection()
					.prepareStatement("SELECT `index` FROM `stock_changes`;");
			ResultSet rslist = pslist.executeQuery();

			while (rslist.next()) {
				String i = rslist.getString("index");
				indexs.add(i);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return indexs;
	}

	public static int[] getBoundaries(String index) {
		// Get upper and lowe variations
		int upper = 0;
		int lower = 0;
		try {
			PreparedStatement ps = BuxInvest
					.getConnection()
					.prepareStatement(
							"SELECT `upper`,`lower` FROM `stock_changes` WHERE `index`=?;");
			ps.setString(1, index);
			ResultSet rs = ps.executeQuery();
			rs.next();
			upper = rs.getInt("upper");
			lower = rs.getInt("lower");

			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new int[] { upper, lower };
	}

	public static int getValue(String index) {
		Integer value = null;
		try {
			PreparedStatement ps = BuxInvest.getConnection().prepareStatement(
					"SELECT `value` FROM `stock_value` WHERE `index`=?;");
			ps.setString(1, index);
			ResultSet rs = ps.executeQuery();
			rs.next();
			value = rs.getInt("value");

			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return value;
	}

	public static void updateValue(String index, int newvalue) {
		try {
			PreparedStatement ps = BuxInvest.getConnection().prepareStatement(
					"UPDATE stock_value SET `value`=? WHERE `index`=?");
			ps.setInt(1, newvalue);
			ps.setString(2, index);
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void updateHistory(String index, int newvalue) {
		try {
			PreparedStatement ps = BuxInvest
					.getConnection()
					.prepareStatement(
							"INSERT INTO stock_history (`index`,`timestamp`,`value`) VALUES (?,NOW(),?);");
			ps.setString(1, index);
			ps.setInt(2, newvalue);
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void updateChange(String index, int change) {
		try {
			PreparedStatement ps = BuxInvest.getConnection().prepareStatement(
					"UPDATE stock_value SET `change`=? WHERE `index`=?");
			ps.setInt(1, change);
			ps.setString(2, index);
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
