package net.buxville.rahman.buxinvest.SQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.bukkit.entity.Player;

import net.buxville.rahman.buxinvest.BuxInvest;

public class SQLchecks {
	// Check for index in stocks
	public synchronized static boolean stockContainsIndex(String index) {
		try {
			PreparedStatement ps = BuxInvest.getConnection().prepareStatement(
					"SELECT * FROM `stocks` WHERE `index`=?;");
			ps.setString(1, index.toUpperCase());
			ResultSet rs = ps.executeQuery();
			boolean containsIndex = rs.next();
			ps.close();
			rs.close();
			return containsIndex;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// Check for index in stock values
	public synchronized static boolean stockhistoryContainsIndex(String index) {
		try {
			PreparedStatement ps = BuxInvest.getConnection().prepareStatement(
					"SELECT * FROM `stock_value` WHERE `index`=?;");
			ps.setString(1, index.toUpperCase());
			ResultSet rs = ps.executeQuery();
			boolean containsIndex = rs.next();
			ps.close();
			rs.close();
			return containsIndex;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// Check for index in stock values
	public synchronized static boolean stockchangesContainsIndex(String index) {
		try {
			PreparedStatement ps = BuxInvest.getConnection().prepareStatement(
					"SELECT * FROM `stock_changes` WHERE `index`=?;");
			ps.setString(1, index.toUpperCase());
			ResultSet rs = ps.executeQuery();
			boolean containsIndex = rs.next();
			ps.close();
			rs.close();
			return containsIndex;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// Check if player owns a stock
	public synchronized static boolean playerstocksContains(String index,
			Player p) {
		try {
			PreparedStatement ps = BuxInvest
					.getConnection()
					.prepareStatement(
							"SELECT * FROM `player_stocks` WHERE `uuid`=? AND `index`=?;");
			ps.setString(1, p.getUniqueId().toString());
			ps.setString(2, index.toUpperCase());
			ResultSet rs = ps.executeQuery();
			boolean containsIndex = rs.next();
			ps.close();
			rs.close();
			return containsIndex;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
