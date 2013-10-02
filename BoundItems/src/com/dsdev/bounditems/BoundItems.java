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
            
            if (args.length < 2)
            {
                p.sendMessage("§6[§eBoundItems§6]§r Not enough arguments!");
                return true;
            }
            
            if (args[0].equals("set"))
            {
                ItemStack item = p.getItemInHand();
                ItemMeta meta = item.getItemMeta();
                
                if (meta.hasLore())
                {
                    List<String> lore = meta.getLore();
                    for (String l : lore)
                        if (l.startsWith("§8Bound to: "))
                            lore.remove(l);
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
                
                p.sendMessage("§6[§eBoundItems§6]§r Item bindable/unbound.");
            }
            else if (args[0].equals("remove"))
            {
                ItemStack item = p.getItemInHand();
                ItemMeta meta = item.getItemMeta();
                
                if (meta.hasLore())
                {
                    List<String> lore = meta.getLore();
                    for (String l : lore)
                        if (l.startsWith("§8Bound to: ") || l.equals("§8<Not Bound>"))
                            lore.remove(l);
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    p.sendMessage("§6[§eBoundItems§6]§r Item binding removed.");
                }
                else
                {
                    p.sendMessage("§6[§eBoundItems§6]§r Item was not bound.");
                }
            }
            
            return true;
            
        } else {
            return false;
        }
    }
    
    
    @EventHandler(priority=EventPriority.LOW)
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        
    }
}
