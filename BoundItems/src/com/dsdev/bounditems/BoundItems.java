/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dsdev.bounditems;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author LukeSmalley
 */
public class BoundItems extends JavaPlugin implements Listener {
    
    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
        
        getLogger().info("BoundItems has been enabled!");
    }
    
    @Override
    public void onDisable()
    {
        getLogger().info("BoundItems has been disabled!");
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("bind") && sender.hasPermission("bounditems.bind") && sender instanceof Player) {
            
            Player p = (Player)sender;
            
            if (args.length < 1)
            {
                p.sendMessage("§6[§eBoundItems§6]§r Not enough arguments!");
                return true;
            }
            
            if (args[0].equalsIgnoreCase("on"))
            {
                ItemStack item = p.getItemInHand();
                ItemMeta meta = item.getItemMeta();
                
                if (meta.hasLore())
                {
                    List<String> lore = meta.getLore();
                    for (String l : lore)
                        if (l.startsWith("§8Bound to: ") || l.equals("§8<Not Bound>"))
                        {
                            lore.remove(l);
                            break;
                        }
                    lore.add("§8<Not Bound>");
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                }
                else
                {
                    List<String> lore = new ArrayList<>();
                    lore.add("§8<Not Bound>");
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                }
                
                p.sendMessage("§6[§eBoundItems§6]§r Item was made bindable.");
            }
            else if (args[0].equalsIgnoreCase("off"))
            {
                ItemStack item = p.getItemInHand();
                ItemMeta meta = item.getItemMeta();
                
                if (meta.hasLore())
                {
                    List<String> lore = meta.getLore();
                    for (String l : lore)
                        if (l.startsWith("§8Bound to: ") || l.equals("§8<Not Bound>"))
                        {
                            lore.remove(l);
                            break;
                        }
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                }
                
                p.sendMessage("§6[§eBoundItems§6]§r Item binding removed.");
            }
            else if (args[0].equalsIgnoreCase("set"))
            {
                if (args.length < 2)
                {
                    p.sendMessage("§6[§eBoundItems§6]§r Not enough arguments!");
                    return true;
                }
                
                ItemStack item = p.getItemInHand();
                ItemMeta meta = item.getItemMeta();
                
                if (meta.hasLore())
                {
                    List<String> lore = meta.getLore();
                    for (String l : lore)
                        if (l.startsWith("§8Bound to: ") || l.equals("§8<Not Bound>"))
                        {
                            lore.remove(l);
                            break;
                        }
                    lore.add("§8Bound to: " + args[1]);
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                }
                else
                {
                    List<String> lore = new ArrayList<>();
                    lore.add("§8Bound to: " + args[1]);
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                }
                
                p.sendMessage("§6[§eBoundItems§6]§r Item was bound to '" + args[1] + "'.");
            }
            
            return true;
            
        } else {
            return false;
        }
    }
    
    
    @EventHandler(priority=EventPriority.LOW)
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        ItemStack item = event.getPlayer().getItemInHand();
        
        if (item != null)
        {
            ItemMeta meta = item.getItemMeta();
            
            if (meta.hasLore())
            {
                for (String l : meta.getLore())
                {
                    if (l.startsWith("§8Bound to: "))
                    {
                        String playername = l.substring(12);
                        
                        if (!event.getPlayer().getName().equals(playername))
                            event.setCancelled(true);
                        
                        break;
                    }
                    else if (l.equals("§8<Not Bound>"))
                    {
                        List<String> itemlore = meta.getLore();
                        //itemlore.set(itemlore.indexOf(l), "§8Bound to: " + event.getPlayer().getName());
                        itemlore.remove(l);
                        itemlore.add("§8Bound to: " + event.getPlayer().getName());
                        meta.setLore(itemlore);
                        item.setItemMeta(meta);
                        event.getPlayer().setItemInHand(item);
                        break;
                    }
                }
            }
        }
    }
}
