package net.buxville.rahman.buxinvest.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.buxville.rahman.buxinvest.Database;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class StockChange {

	public static void changeStock(Player p, String[] args) {
		// Player command
		if (!p.hasPermission("buxinvest.admin")) {
			p.sendMessage(ChatColor.RED
					+ "You do not have permission for this.");
			return;
		}
		updateStocks(args);
		p.sendMessage(ChatColor.GREEN + "Stocks updated.");
		return;
	}

	public static void changeStock(ConsoleCommandSender sender, String[] args) {
		// Console command
		updateStocks(args);
		return;
	}

	private static void updateStocks(String[] args) {
		List<String> indexs = Database.stockList();

		// Update values of stock loop
		for (String index : indexs) {
			int[] variations = Database.getBoundaries(index);
			int upper = variations[0];
			int lower = variations[1];

			// Generate potential fluctuations
			ArrayList<Float> variation = new ArrayList<Float>();
			for (float i = lower * 10; i <= upper * 10; i++) {
				variation.add(i / 1000);
			}

			// Choose new variation value
			int random = new Random().nextInt(variation.size());
			Float choice = (1 + variation.get(random));

			// Get stock value
			int value = Database.getValue(index);

			// New stock value
			int newvalue = Math.round(choice * value);

			if (newvalue < 1) {
				newvalue = 1;
			}

			int change = Math.round((choice - 1) * 100);

			Database.updateValue(index, newvalue);
			Database.updateChange(index, change);
			Database.updateHistory(index, newvalue);
		}

		System.out.println("Stocks updated.");
		return;

	}
}
