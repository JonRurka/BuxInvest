package net.buxville.rahman.buxinvest.commands;

import net.buxville.rahman.buxinvest.SQL.SQLchecks;
import net.buxville.rahman.buxinvest.SQL.SQLvaluechange;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SetChanges {

	// Manually set stock changes
	public static void setChanges(Player p, String[] args) {
		if (!p.hasPermission("buxinvest.admin")) {
			p.sendMessage(ChatColor.RED
					+ "You do not have permission for this.");
			return;
		}

		if (args.length < 4) {
			p.sendMessage(ChatColor.RED + "Not enough arguments."
					+ " Use /invest setchange [index] [negative] [positive]");
			return;
		}

		String index = args[1].toUpperCase();
		if (!SQLchecks.stockhistoryContainsIndex(index)) {
			p.sendMessage(ChatColor.RED + "This company doesn't have a value");
			return;
		}

		int Lower;
		int Upper;
		try {
			Lower = Integer.parseInt(args[2]);
			Upper = Integer.parseInt(args[3]);
		} catch (NumberFormatException e) {
			p.sendMessage(ChatColor.RED
					+ "Error with Upper and Lower fluctuations.");
			return;
		}

		if (Lower < 0 || Upper < 0) {
			p.sendMessage(ChatColor.RED + "Upper and lower bounds "
					+ "should both be positive in the command.");
			return;
		}

		if (SQLchecks.stockchangesContainsIndex(index)) {
			SQLvaluechange.updateChange(index, Lower, Upper);
			p.sendMessage(ChatColor.GREEN + "Company \"" + index
					+ "\" changes set to: -" + Lower + " and " + Upper
					+ " percent.");
			return;
		} else {
			SQLvaluechange.addChange(index, Lower, Upper);
			p.sendMessage(ChatColor.GREEN + "Company \"" + index
					+ "\" changes added as: -" + Lower + " and " + Upper
					+ " percent.");
			return;
		}

	}

}