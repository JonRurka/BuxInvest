package net.buxville.rahman.buxinvest.commands;

import net.buxville.rahman.buxinvest.SQL.SQLportfolio;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Portfolio {

	public static void playerStocks(Player p, String[] args) {
		if (!p.hasPermission("buxinvest.portfolio")) {
			p.sendMessage(ChatColor.RED + "You do not have permission.");
			return;
		}
		if (args.length > 0) {
			if (!p.hasPermission("buxinvest.admin")) {
				p.sendMessage(ChatColor.RED
						+ "You do not have permission for this.");
				return;
			}
		}

		// Get list
		p.sendMessage(ChatColor.GOLD + p.getName() + "'s Portfolio");
		p.sendMessage("===================");
		SQLportfolio.getStocks(p);
	}

}
