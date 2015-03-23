package net.buxville.rahman.buxinvest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class BuxInvest extends JavaPlugin {
	String username;
	String password;
	String url;
	static Integer selltax = 0;
	static Integer buytax = 0;
	static Connection con;
	static Economy econ;

	public void onEnable() {
		// Load Config
		saveDefaultConfig();
		getLogger().info("Config Loaded.");

		if (!setupEconomy()) {
			getLogger().severe("Could not connect to Economy. Shutting down.");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		// Load SQL data
		username = getConfig().getString("username");
		password = getConfig().getString("password");
		url = getConfig().getString("url");
		reloadConfigValues();

		// Attempt MySQL connection
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			con = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Generate SQL Databases
		Database.createTables();

		// Command Executor
		this.getCommand("invest").setExecutor(
				new BuxInvestCommandExecutor(this));
		this.getCommand("portfolio").setExecutor(
				new BuxInvestCommandExecutor(this));

	}

	private boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = getServer()
				.getServicesManager().getRegistration(
						net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			econ = economyProvider.getProvider();
		}

		return (econ != null);
	}

	private void reloadConfigValues() {
		buytax = this.getConfig().getInt("buytax");
		selltax = this.getConfig().getInt("selltax");
	}

	public void onDisable() {
		// Clean up MySQL connection
		try {
			if (con != null && !con.isClosed()) {
				con.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		return con;
	}

	public static Economy getEconomy() {
		return econ;
	}

	public static Integer getBuyTax() {
		return buytax;
	}

	public static Integer getSellTax() {
		return selltax;
	}

}
