package net.buxville.rahman.buxinvest;

import net.buxville.rahman.buxinvest.commands.AddCompany;
import net.buxville.rahman.buxinvest.commands.BuyStocks;
import net.buxville.rahman.buxinvest.commands.Portfolio;
import net.buxville.rahman.buxinvest.commands.RemoveCompany;
import net.buxville.rahman.buxinvest.commands.SellStocks;
import net.buxville.rahman.buxinvest.commands.SetChanges;
import net.buxville.rahman.buxinvest.commands.SetValue;
import net.buxville.rahman.buxinvest.commands.StockChange;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class BuxInvestCommandExecutor implements CommandExecutor {

	BuxInvest plugin;

	public BuxInvestCommandExecutor(BuxInvest instance) {
		plugin = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("invest")) {
			// TODO no args text and deal with console
			if (args.length == 0) {
				if ((Player) sender instanceof Player) {
					sender.sendMessage("Error!");
				}
				return false;
			}

			// Stock changes
			if (args[0].equalsIgnoreCase("stockchange")) {
				if (sender instanceof Player) {
					StockChange.changeStock((Player) sender, args);
					return false;
				} else {
					StockChange
							.changeStock((ConsoleCommandSender) sender, args);
				}
			}

			// Console check
			if (!(sender instanceof Player)) {
				return false;
			}
			Player p = (Player) sender;

			// Add company
			if (args[0].equalsIgnoreCase("addcompany")) {
				AddCompany.newCompany(p, args);
				return false;
			}

			// Add company
			if (args[0].equalsIgnoreCase("buy")) {
				BuyStocks.buyStocks(p, args);
				return false;
			}

			// Remove company
			if (args[0].equalsIgnoreCase("removecompany")) {
				RemoveCompany.deleteCompany(p, args);
				return false;
			}

			// Set company changes
			if (args[0].equalsIgnoreCase("sell")) {
				SellStocks.sellStocks(p, args);
				return false;
			}

			// Set company changes
			if (args[0].equalsIgnoreCase("setchanges")) {
				SetChanges.setChanges(p, args);
				return false;
			}

			// Set company value
			if (args[0].equalsIgnoreCase("setvalue")) {
				SetValue.setValue(p, args);
				return false;
			}

			// Set company value
			if (args[0].equalsIgnoreCase("taxes")) {
				if (!p.hasPermission("buxinvest.taxes")) {
					p.sendMessage(ChatColor.RED
							+ "You do not have permission for this.");
					return false;
				}
				p.sendMessage(ChatColor.GOLD + "Buy tax: "
						+ BuxInvest.getBuyTax() + "%");
				p.sendMessage(ChatColor.GOLD + "Sell tax: "
						+ BuxInvest.getSellTax() + "%");
				return false;
			}

			// Set company amount
			if (args[0].equalsIgnoreCase("updateamount")) {
				SetValue.updateAmount(p, args);
				return false;
			}

			// Set company value
			if (args[0].equalsIgnoreCase("updatevalue")) {
				SetValue.updateValue(p, args);
				return false;
			}

			// Set company value
			if (args[0].equalsIgnoreCase("value")) {
				SetValue.getValue(p, args);
				return false;
			}

			else {
				// TODO fix this
				p.sendMessage("Invalid Command.");
			}
		}

		if (cmd.getName().equalsIgnoreCase("portfolio")) {
			// Console check
			if (!(sender instanceof Player)) {
				return false;
			}
			Player p = (Player) sender;
			Portfolio.playerStocks(p, args);
		}
		return false;
	}

}
