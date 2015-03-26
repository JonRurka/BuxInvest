package net.buxville.rahman.buxinvest;

import java.util.ArrayList;
import java.util.List;

import net.buxville.rahman.buxinvest.commands.CreateSign;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.scheduler.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

public class SignController {
	class SignData
	{
		public Location position;
		public String stock;
		
		public SignData(Location pos, String index)
		{
			position = pos;
			stock = index;
		}
	}
	
	List<SignData> signs;
	BuxInvest plugin;
	long delay, period;
	
	public SignController(BuxInvest instance, long seconds)
	{
		delay = 20;
		period = seconds * 20;
		plugin = instance;
		signs = new ArrayList<SignData>();
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run()
			{
				Update();
			}
		} , delay, period);
	}
	
	public void Update(){
		for (int i = 0; i < signs.size(); i++)
		{
			Sign sign = (Sign) signs.get(i).position.getBlock().getState();
			String stockIndex = signs.get(i).stock;
			sign.setLine(2, "Value: " + Database.getValue(stockIndex));
			sign.update();
		}
	}
	
	public void CreateSign(Player p, Location position, SignChangeEvent event)
	{
		if (CreateSign.IsRead(p)){
			Material blockMaterial = position.getBlock().getType();
			if (blockMaterial == Material.WALL_SIGN)
			{
				String stockIndex = event.getLine(0);
				List<String> stocks = Database.stockList();
				if (stocks.contains(stockIndex))
				{
					event.setLine(0, "[" + stockIndex + "]");
					event.setLine(1, Database.GetStockName(stockIndex));
					event.setLine(2, "Value: " + Database.getValue(stockIndex));
					Database.AddSign(stockIndex, position);
					signs.add(new SignData(position, stockIndex));
					CreateSign.Remove(p);
				}
				else
					p.sendMessage(ChatColor.RED + "Could not find stock \"" + stockIndex + "\".");
			}
			else
				p.sendMessage(ChatColor.RED + "Invalid block type. You probably tried to use a picket sign.");
		}
	}
}
