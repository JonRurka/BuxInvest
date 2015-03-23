package net.buxville.rahman.buxinvest.commands;

import net.buxville.rahman.buxinvest.BuxInvest;
import net.buxville.rahman.buxinvest.SQL.SQLbuy;
import net.buxville.rahman.buxinvest.SQL.SQLchecks;
import net.buxville.rahman.buxinvest.SQL.SQLvaluechange;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class BuyStocks {
	// Buy stocks
	public static void buyStocks(Player p, String[] args) {
		if (!p.hasPermission("buxinvest.buy")) {
			p.sendMessage(ChatColor.RED + "You do not have permission.");
			return;
		}

		if (args.length < 3) {
			p.sendMessage(ChatColor.RED
					+ "Not enough arguments! Use /invest buy [index] [amount]");
			return;
		}

		String index = args[1].toUpperCase();

		Integer amount = null;
		try {
			amount = Integer.parseInt(args[2]);
		} catch (NumberFormatException e) {
			p.sendMessage(ChatColor.RED + "Error with your stock amount!");
			return;
		}

		if (amount < 1) {
			p.sendMessage(ChatColor.RED + "Invalid amount!");
			return;
		}

		// Get players account data
		double balance = BuxInvest.getEconomy().getBalance(p);

		// Get stock value
		int value = SQLvaluechange.getValue(index);
		if (value == -1) {
			p.sendMessage(ChatColor.RED + "Money error.");
			return;
		}

		// Check to see if company setup is complete then update amount
		if (!SQLchecks.stockchangesContainsIndex(index)) {
			p.sendMessage(ChatColor.RED + "Invalid company with index \""
					+ index + "\" .");
			return;
		}

		// Get data
		int[] stockdata = SQLbuy.getValues(index);

		if (stockdata[0] == -1 || stockdata[1] == -1) {
			p.sendMessage(ChatColor.RED + "Buy error: 1");
			Bukkit.getLogger().severe("SQL buy error: 1");
			return;
		}

		// Amount already in circulation
		int alreadyowned = stockdata[0];

		// Max amount in system
		int maxamount = stockdata[1];

		int newamount = alreadyowned + amount;
		if (newamount > maxamount) {
			p.sendMessage(ChatColor.RED
					+ "You tried to purchase too many stocks.");
			p.sendMessage(ChatColor.RED
					+ "The current maximum amount you can purchase is "
					+ (maxamount - alreadyowned) + ".");
			return;
		}

		// Tax value
		float tax = 100 + BuxInvest.getSellTax();
		float multiplier = tax / 100;
		double finalvalue = Math.round(multiplier * amount * value);

		if (finalvalue > balance) {
			p.sendMessage(ChatColor.RED + "You do not have enough to buy "
					+ amount + " of \"" + index + "\" stocks.");
			return;
		}

		// Deduct money
		BuxInvest.getEconomy().withdrawPlayer(p, finalvalue);
		if (BuxInvest.getBuyTax() == 0) {
			p.sendMessage(ChatColor.GREEN + "You have purchased " + amount
					+ " of \"" + index + "\" stocks for " + (finalvalue)
					+ BuxInvest.getEconomy().currencyNamePlural() + ".");
		} else {
			p.sendMessage(ChatColor.GREEN + "You have purchased " + amount
					+ " of \"" + index + "\" stocks for " + (finalvalue)
					+ BuxInvest.getEconomy().currencyNamePlural()
					+ ", including tax.");
		}

		if (SQLchecks.playerstocksContains(index, p)) {
			if (SQLbuy.updateStocks(index, p, amount) == false) {
				p.sendMessage(ChatColor.RED + "Buy error: 4");
				Bukkit.getLogger().severe("SQL buy error: 4");
				return;
			}
			SQLbuy.updateValue(index, newamount);
			return;
		} else {
			// Add stocks to database
			if (SQLbuy.addStocks(index, p, amount) == false) {
				p.sendMessage(ChatColor.RED + "Buy error: 2");
				Bukkit.getLogger().severe("SQL buy error: 2");
				return;
			}
			// Change to boolean
			if (SQLbuy.updateValue(index, newamount) == false) {
				p.sendMessage(ChatColor.RED + "Buy error: 3");
				Bukkit.getLogger().severe("SQL buy error: 3");
				return;
			}
		}
	}

}
