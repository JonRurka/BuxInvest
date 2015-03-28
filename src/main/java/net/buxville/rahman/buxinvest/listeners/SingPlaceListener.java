package net.buxville.rahman.buxinvest.listeners;

import java.util.List;

import net.buxville.rahman.buxinvest.*;

import org.bukkit.*;
import org.bukkit.material.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.*;

import net.buxville.rahman.buxinvest.commands.CreateSign;

public class SingPlaceListener implements Listener {
	BuxInvest plugin;
	
	public SingPlaceListener(BuxInvest instance)
	{
		plugin = instance;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void OnSignChanged(SignChangeEvent event)
	{
		Player p = event.getPlayer();
		if (p.hasPermission("buxinvest.admin"))
		{
			BuxInvest.GetSignController().CreateSign(p, event.getBlock().getLocation(), event);
		}
		
	}
	
	@EventHandler
	public void OnSignBroke(BlockBreakEvent event)
	{
		Player p = event.getPlayer();
		if (p.hasPermission("buxinvest.admin") && event.getBlock().getType() == Material.WALL_SIGN)
		{
			BuxInvest.GetSignController().DeleteSign(p, event.getBlock().getLocation());
		}
	}
}
