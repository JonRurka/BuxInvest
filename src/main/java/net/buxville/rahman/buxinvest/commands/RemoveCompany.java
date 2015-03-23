package net.buxville.rahman.buxinvest.commands;

import net.buxville.rahman.buxinvest.SQL.SQLchecks;
import net.buxville.rahman.buxinvest.SQL.SQLaddrem;

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
		if (SQLchecks.stockchangesContainsIndex(index)) {
			SQLaddrem.removeChanges(index);
		}

		if (SQLchecks.stockhistoryContainsIndex(index)) {
			SQLaddrem.removeHistory(index);
		}

		if (SQLchecks.stockContainsIndex(index)) {
			SQLaddrem.removeStock(index);
			p.sendMessage(ChatColor.GREEN + "Company removed from Database.");
		}
	}

}
