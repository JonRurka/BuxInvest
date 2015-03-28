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
	int scheduleID;
	
	public SignController(BuxInvest instance, long seconds)
	{
		delay = 20;
		period = seconds * 20;
		plugin = instance;
		signs = new ArrayList<SignData>();
		scheduleID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
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
			try {
				Location pos = signs.get(i).position;
				World world = pos.getWorld();
				Block signBlock = world.getBlockAt(pos);
				BlockState state = signBlock.getState();
				if (state instanceof Sign){
					Sign sign = (Sign) state;
					String stockIndex = signs.get(i).stock;
					sign.setLine(2, "Value: " + Database.getValue(stockIndex));
					sign.update();
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void Load(){
		List<String> stocks = Database.stockList();
		for (int i = 0; i < stocks.size(); i++)
		{
			try {
				List<Location> signLocations;
				signLocations = Database.GetSignLocation(stocks.get(i));
				for (int j = 0; j < signLocations.size(); j++)
				{
					signs.add(new SignData(signLocations.get(j), stocks.get(i)));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	
	public void DeleteSign(Player p, Location position)
	{
		for (int i = 0; i < signs.size(); i++)
		{
			if (position.distance(signs.get(i).position) == 0)
			{
				signs.remove(i);
				Database.RemoveSign(position);
				p.sendMessage("Sign removed.");
				break;
			}
		}
	}
	
	public void Close()
	{
		signs.clear();
		plugin.getServer().getScheduler().cancelTask(scheduleID);
	}
}
