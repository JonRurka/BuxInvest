package net.buxville.rahman.buxinvest.commands;

import net.buxville.rahman.buxinvest.Database;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class AddCompany {

	public static void newCompany(Player p, String[] args) {
		if (!p.hasPermission("buxinvest.admin")) {
			p.sendMessage(ChatColor.RED
					+ "You do not have permission for this.");
			return;
		}

		if (args.length < 3) {
			p.sendMessage(ChatColor.RED
					+ "Not enough arguments. Use /invest addcompany [index] [name]");
			return;
		}

		StringBuilder sb = new StringBuilder();
		for (Integer i = 2; i < args.length; i++) {
			sb.append(args[i]);
			sb.append(" ");
		}
		String companyname = sb.toString().substring(0,
				sb.toString().length() - 1);
		String index = args[1].toUpperCase();

		if (index.length() > 5) {
			p.sendMessage(ChatColor.RED + "Index is too long. 5 is the max.");
			return;
		}

		if (Database.stockContainsIndex(index)) {
			p.sendMessage(ChatColor.RED + "Company already exists.");
			return;
		}

		// Add company
		Database.addCompany(index, companyname, p);

		p.sendMessage(ChatColor.GREEN + "Company \"" + companyname
				+ "\" created with index \"" + index + "\".");
		p.sendMessage(ChatColor.GOLD
				+ "Use /invest setvalue [index] [value] [number of stocks] to set the value.");
		return;
	}

}
