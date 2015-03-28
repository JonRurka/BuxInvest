package net.buxville.rahman.buxinvest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.buxville.rahman.buxinvest.commands.CreateSign;
import net.buxville.rahman.buxinvest.commands.StockChange;
import net.buxville.rahman.buxinvest.listeners.SingPlaceListener;

public class BuxInvest extends JavaPlugin {
	private static BuxInvest instance;
	
	String username;
	String password;
	String url;
	int signUpdateInterval;
	int stocksUpdateInterval;
	static Integer selltax = 0;
	static Integer buytax = 0;
	static Connection con;
	static Economy econ;
	static SignController signMnger;
	
	SingPlaceListener signListener;
	
	int scheduleID;

	public void onEnable() {
		// Load Config
		instance = this;
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
		
		signMnger = new SignController(this, signUpdateInterval);
		signListener = new SingPlaceListener(this);
		
		getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
			@Override
			public void run() {
				getLogger().info("Loading stock signs.");
				signMnger.Load();
			}
			
		}, (long)20);
		
		scheduleID = getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				Update();
			}}, 40, stocksUpdateInterval * 20);
	}
	
	private void Update()
	{
		StockChange.updateStocks(null);
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
		signUpdateInterval = this.getConfig().getInt("signinterval");
		stocksUpdateInterval = this.getConfig().getInt("stockinterval");
	}

	public void onDisable() {
		// Clean up MySQL connection
		try {
			HandlerList.unregisterAll(this);
			CreateSign.Clear();
			getServer().getScheduler().cancelTask(scheduleID);
			if (signMnger != null)
				signMnger.Close();
			instance = null;
			signListener = null;
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
	
	public static SignController GetSignController()
	{
		return signMnger;
	}

	public static BuxInvest GetInstance()
	{
		return instance;
	}
}
