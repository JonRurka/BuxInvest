package net.buxville.rahman.buxinvest.commands;

import net.buxville.rahman.buxinvest.Database;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class RemoveCompany {

	public static void deleteCompany(Player p, String[] args) {
		if (!p.hasPermission("buxinvest.admin")) {
			p.sendMessage(ChatColor.RED
					+ "You do not have permission for this.");
			return;
		}

		if (args.length < 2) {
			p.sendMessage(ChatColor.RED
					+ "Not enough arguments. Use /invest removecompany [index]");
			return;
		}

		// Remove company from data base
		String index = args[1].toUpperCase();
		
		Database.removePlayerStocks(index);
		
		if (Database.stockchangesContainsIndex(index)) {
			Database.removeChanges(index);
		}

		if (Database.stockhistoryContainsIndex(index)) {
			Database.removeHistory(index);
		}

		if (Database.stockContainsIndex(index)) {
			Database.removeStock(index);
			p.sendMessage(ChatColor.GREEN + "Company removed from Database.");
		}
	}

}
