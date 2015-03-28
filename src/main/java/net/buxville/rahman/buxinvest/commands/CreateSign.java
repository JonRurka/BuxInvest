package net.buxville.rahman.buxinvest.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;


public class CreateSign {
	
	private static List<UUID> ReadyToPlace;
	
	public static void PlaceSignCommand(Player p)
	{
		if (ReadyToPlace == null)
			ReadyToPlace = new ArrayList<UUID>();
		
		if (!ReadyToPlace.contains(p.getUniqueId()))
		{
			ReadyToPlace.add(p.getUniqueId());
		}
	}
	
	public static boolean IsRead(Player p)
	{
		return (ReadyToPlace != null && ReadyToPlace.contains(p.getUniqueId()));
	}
	
	public static void Remove(Player p)
	{
		ReadyToPlace.remove(p.getUniqueId());
	}
	
	public static void Clear()
	{
		if (ReadyToPlace != null)
			ReadyToPlace.clear();
	}
}
