package net.buxville.rahman.buxinvest.commands;

import net.buxville.rahman.buxinvest.BuxInvest;
import net.buxville.rahman.buxinvest.Database;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SetValue {

	// Set initial stock value and amount
	public static void setValue(Player p, String[] args) {
		if (!p.hasPermission("buxinvest.admin")) {
			p.sendMessage(ChatColor.RED
					+ "You do not have permission for this.");
			return;
		}

		if (args.length < 4) {
			p.sendMessage(ChatColor.RED
					+ "Not enough arguments."
					+ " Use /invest setvalue [index] [value] [amount in market]");
			return;
		}

		String index = args[1].toUpperCase();
		if (!Database.stockContainsIndex(index)) {
			p.sendMessage(ChatColor.RED + "Company with index \"" + index
					+ "\" does not exist.");
			return;
		}

		Integer value;
		Integer amount;
		try {
			value = Integer.parseInt(args[2]);
		} catch (NumberFormatException e) {
			p.sendMessage(ChatColor.RED + "Error with your company value!");
			return;
		}

		try {
			amount = Integer.parseInt(args[3]);
		} catch (NumberFormatException e) {
			p.sendMessage(ChatColor.RED + "Error with your company value!");
			return;
		}

		if (Database.stockhistoryContainsIndex(index)) {
			p.sendMessage(ChatColor.RED
					+ "This operation has already been completed.");
			p.sendMessage(ChatColor.RED
					+ "To update value do /invest updatevalue [index] [value].");
			p.sendMessage(ChatColor.RED
					+ "To update amount of stocks do /invest updateamount [index] [amount].");
			return;
		} else {
			Database.addEntry(index, value, amount);
			p.sendMessage(ChatColor.GREEN + "Company \"" + index
					+ "\" value set at " + value + "Bux.");
			p.sendMessage(ChatColor.GOLD
					+ "Use /invest setchanges [index] [upper] [lower] to set the value.");
		}
	}

	// Manually update stock amount
	public static void updateAmount(Player p, String[] args) {
		if (!p.hasPermission("buxinvest.admin")) {
			p.sendMessage(ChatColor.RED
					+ "You do not have permission for this.");
			return;
		}
		if (args.length < 3) {
			p.sendMessage(ChatColor.RED
					+ "Use /invest updateamount [index] [amount]");
			return;
		}
		String index = args[1].toUpperCase();
		Integer amount = null;
		try {
			amount = Integer.parseInt(args[2]);
		} catch (NumberFormatException e) {
			p.sendMessage(ChatColor.RED + "Error with your company value!");
			return;
		}
		Database.updateAmount(index, amount);
		p.sendMessage(ChatColor.GREEN + "Company \"" + index
				+ "\" value updated to " + amount + " amount of stocks.");
	}

	// Manually update stocks value
	public static void updateValue(Player p, String[] args) {
		if (!p.hasPermission("buxinvest.admin")) {
			p.sendMessage(ChatColor.RED
					+ "You do not have permission for this.");
			return;
		}
		if (args.length < 3) {
			p.sendMessage(ChatColor.RED
					+ "Use /invest updatevalue [index] [value]");
			return;
		}
		String index = args[1].toUpperCase();
		Integer value = null;
		try {
			value = Integer.parseInt(args[2]);
		} catch (NumberFormatException e) {
			p.sendMessage(ChatColor.RED + "Error with your company value!");
			return;
		}
		Database.updateEntry(index, value);
		p.sendMessage(ChatColor.GREEN + "Company \"" + index
				+ "\" value updated to " + value + "Bux.");
	}

	// Get Item value
	public static void getValue(Player p, String[] args) {
		if (!p.hasPermission("buxinvest.value")) {
			p.sendMessage(ChatColor.RED
					+ "You do not have permission for this.");
			return;
		}

		if (args.length < 2) {
			p.sendMessage(ChatColor.RED + "Use /invest value [index]");
			return;
		}

		String index = args[1].toUpperCase();
		if (!Database.stockchangesContainsIndex(index)) {
			p.sendMessage(ChatColor.RED + "The company with index \"" + index
					+ "\" does not exist.");
			return;
		}

		int value = Database.getStockValue(index);
		p.sendMessage(ChatColor.GREEN + "Price of stock with " + "index \""
				+ index + "\" is " + value + BuxInvest.getEconomy().currencyNamePlural()+ ".");
		return;
	}

}
