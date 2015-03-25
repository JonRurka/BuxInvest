package net.buxville.rahman.buxinvest;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Database {
	// Create tables
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

	// Remove player stocks
	public static void removePlayerStocks(String index) {
		try {
			PreparedStatement ps = BuxInvest.getConnection().prepareStatement(
					"DELETE FROM `player_stocks` WHERE `index`=?;");
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
	
	// Remove stock changes
	public static void removeChanges(String index) {
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

	// Remove stock value
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
				rs.close();
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
		
		// Return stock list
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

		// Get upper and lower variations
		public static int[] getBoundaries(String index) {
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

		// Remove stocks
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

		// Update stock changes
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
		
		// Get stock value
		public static Integer getStockValue(String index) {
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
		
		// DATABASE CHECKS
		
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
