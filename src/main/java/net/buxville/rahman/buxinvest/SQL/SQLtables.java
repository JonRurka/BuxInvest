package net.buxville.rahman.buxinvest.SQL;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.buxville.rahman.buxinvest.BuxInvest;

public class SQLtables {

	public static void createTables() {
		try {
			PreparedStatement ps = BuxInvest.getConnection().prepareStatement(
					"CREATE TABLE IF NOT EXISTS `stocks` ("
							+ "`companyid` int(6) NOT NULL AUTO_INCREMENT,"
							+ "`index` varchar(6) NOT NULL,"
							+ "`stockName` varchar(255) NOT NULL,"
							+ "PRIMARY KEY (`companyid`)"
							+ ")ENGINE=InnoDB  DEFAULT CHARSET=utf8 "
							+ "COLLATE=utf8_unicode_ci AUTO_INCREMENT=1;");
			ps.execute();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			PreparedStatement ps2 = BuxInvest.getConnection().prepareStatement(
					"CREATE TABLE IF NOT EXISTS `stock_value` ("
							+ "`valueid` int(6) NOT NULL AUTO_INCREMENT,"
							+ "`index` varchar(6) NOT NULL,"
							+ "`value` int(10) NOT NULL,"
							+ "`change` int(10) NOT NULL,"
							+ "`amount` int(10) NOT NULL,"
							+ "`owned` int(10) NOT NULL,"
							+ "PRIMARY KEY (`valueid`)"
							+ ") ENGINE=InnoDB  DEFAULT CHARSET=utf8 "
							+ "COLLATE=utf8_unicode_ci AUTO_INCREMENT=1;");
			ps2.execute();
			ps2.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			PreparedStatement ps3 = BuxInvest.getConnection().prepareStatement(
					"CREATE TABLE IF NOT EXISTS `stock_changes` ("
							+ "`changeid` int(6) NOT NULL AUTO_INCREMENT,"
							+ "`index` varchar(6) NOT NULL,"
							+ "`upper` int(6) NOT NULL,"
							+ "`lower` int(6) NOT NULL,"
							+ "PRIMARY KEY (`changeid`)"
							+ ") ENGINE=InnoDB  DEFAULT CHARSET=utf8 "
							+ "COLLATE=utf8_unicode_ci AUTO_INCREMENT=1;");
			ps3.execute();
			ps3.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			PreparedStatement ps4 = BuxInvest.getConnection().prepareStatement(
					"CREATE TABLE IF NOT EXISTS `stock_history` ("
							+ "`historyid` int(6) NOT NULL AUTO_INCREMENT,"
							+ "`index` varchar(6) NOT NULL,"
							+ "`timestamp` DATETIME NOT NULL,"
							+ "`value` DECIMAL NOT NULL,"
							+ "PRIMARY KEY (`historyid`)"
							+ ") ENGINE=InnoDB  DEFAULT CHARSET=utf8 "
							+ "COLLATE=utf8_unicode_ci AUTO_INCREMENT=1;");
			ps4.execute();
			ps4.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			PreparedStatement ps5 = BuxInvest.getConnection().prepareStatement(
					"CREATE TABLE IF NOT EXISTS `player_stocks` ("
							+ "`playerstockid` int(6) NOT NULL AUTO_INCREMENT,"
							+ "`username` varchar(20) NOT NULL,"
							+ "`uuid` varchar(60) NOT NULL,"
							+ "`index` varchar(6) NOT NULL,"
							+ "`amount` int(10) NOT NULL,"
							+ "PRIMARY KEY (`playerstockid`)"
							+ ") ENGINE=InnoDB  DEFAULT CHARSET=utf8 "
							+ "COLLATE=utf8_unicode_ci AUTO_INCREMENT=1;");
			ps5.execute();
			ps5.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
