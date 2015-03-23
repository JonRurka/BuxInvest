package net.buxville.rahman.buxinvest.commands;

import net.buxville.rahman.buxinvest.BuxInvest;
import net.buxville.rahman.buxinvest.SQL.SQLchecks;
import net.buxville.rahman.buxinvest.SQL.SQLsell;
import net.buxville.rahman.buxinvest.SQL.SQLvaluechange;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SellStocks {

	public static void sellStocks(Player p, String[] args) {
		if (!p.hasPermission("buxinvest.sell")) {
			p.sendMessage(ChatColor.RED + "You do not have permission.");
			return;
		}

		if (args.length < 3) {
			p.sendMessage(ChatColor.RED
					+ "Not enough arguments! Use /invest sell [index] [amount]");
			return;
		}

		// Retrieve data from command
		String index = args[1].toUpperCase();

		if (!SQLchecks.playerstocksContains(index, p)) {
			p.sendMessage(ChatColor.RED + "You do not own any of \"" + index
					+ "\" stocks.");
			return;
		}

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

		// Find out how many stocks player has
		int currentamount = SQLsell.getAmount(p, index);
		if (currentamount == -1) {
			p.sendMessage(ChatColor.RED + "Sell error: 1");
			Bukkit.getLogger().severe("SQL buy error: 1");
			return;
		}

		// Compare how many they tried to sell
		if (amount > currentamount) {
			p.sendMessage(ChatColor.RED + "You tried to sell more than the "
					+ currentamount + " you own.");
			return;
		}

		int newvalue = currentamount - amount;

		// Check if player has no stocks left
		if (newvalue == 0) {
			if (SQLsell.removeStocks(index, p) == false) {
				p.sendMessage(ChatColor.RED + "Sell error: 2");
				Bukkit.getLogger().severe("SQL buy error: 2");
				return;
			}
		} else {
			if (SQLsell.updateAmount(index, p, newvalue) == false) {
				p.sendMessage(ChatColor.RED + "Sell error: 3");
				Bukkit.getLogger().severe("SQL buy error: 3");
				return;
			}
		}

		// Reduction in owned stocks
		if (SQLsell.updateOwned(index, amount) == false) {
			p.sendMessage(ChatColor.RED + "Sell error: 4");
			Bukkit.getLogger().severe("SQL buy error: 4");
			return;
		}
		// Stock value
		int value = SQLvaluechange.getValue(index);

		// Tax value
		float tax = 100 - BuxInvest.getSellTax();
		float multiplier = tax / 100;
		float finalvalue = Math.round(multiplier * amount * value);

		// Deduct money
		BuxInvest.getEconomy().depositPlayer(p, finalvalue);
		if (BuxInvest.getSellTax() == 0) {
			p.sendMessage(ChatColor.GREEN + "You have sold " + amount
					+ " of \"" + index + "\" stocks for " + (finalvalue)
					+ BuxInvest.getEconomy().currencyNamePlural() + ".");
		} else {
			p.sendMessage(ChatColor.GREEN + "You have sold " + amount
					+ " of \"" + index + "\" stocks for " + (finalvalue)
					+ BuxInvest.getEconomy().currencyNamePlural()
					+ ", after tax deduction.");
		}
	}
}
