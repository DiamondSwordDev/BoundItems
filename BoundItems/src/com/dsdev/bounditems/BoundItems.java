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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
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
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info("BoundItems has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("BoundItems has been disabled!");
    }

    
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("bind") && sender.hasPermission("bounditems.bind") && sender instanceof Player) {

            Player p = (Player) sender;

            if (args.length < 1) {
                p.sendMessage("§6[§eBoundItems§6]§r Commands:");
                p.sendMessage("§r§" + GetPermissionColor(sender, "bounditems.bind.enable") + "/bind enable§r  --  Makes an item bindable or clears its current binding.");
                p.sendMessage("§r§" + GetPermissionColor(sender, "bounditems.bind.clear") + "/bind clear§r  --  Removes an item's bindability.");
                p.sendMessage("§r§" + GetPermissionColor(sender, "bounditems.bind.set") + "/bind set <name>§r  --  Binds an item to <name>.");
                p.sendMessage("§r§" + GetPermissionColor(sender, "bounditems.bind.set") + "/bind help§r  --  Displays this list of commands.");
                return true;
            }

            if (args[0].equalsIgnoreCase("enable")) {
                p.setItemInHand(RemoveBinding(p.getItemInHand()));
                p.setItemInHand(AddBinding(p.getItemInHand(), null));
                p.sendMessage("§6[§eBoundItems§6]§r Item was made bindable.");
            } else if (args[0].equalsIgnoreCase("clear")) {
                p.setItemInHand(RemoveBinding(p.getItemInHand()));
                p.sendMessage("§6[§eBoundItems§6]§r Item bindings cleared.");
            } else if (args[0].equalsIgnoreCase("set")) {
                if (args.length < 2) {
                    p.sendMessage("§6[§eBoundItems§6]§r Item was bound to '" + args[1] + "'.");
                    return true;
                }
                p.setItemInHand(RemoveBinding(p.getItemInHand()));
                p.setItemInHand(AddBinding(p.getItemInHand(), args[1]));
                p.sendMessage("§6[§eBoundItems§6]§r Item was bound to '" + args[1] + "'.");
            } else if (args[0].equalsIgnoreCase("help")) {
                p.sendMessage("§6[§eBoundItems§6]§r Commands:");
                p.sendMessage("§r§" + GetPermissionColor(sender, "bounditems.bind.enable") + "/bind enable§r  --  Makes an item bindable or clears its current binding.");
                p.sendMessage("§r§" + GetPermissionColor(sender, "bounditems.bind.clear") + "/bind clear§r  --  Removes an item's bindability.");
                p.sendMessage("§r§" + GetPermissionColor(sender, "bounditems.bind.set") + "/bind set <name>§r  --  Binds an item to <name>.");
                p.sendMessage("§r§" + GetPermissionColor(sender, "bounditems.bind.set") + "/bind help§r  --  Displays this list of commands.");
            } else {
                p.sendMessage("§6[§eBoundItems§6]§r Commands:");
                p.sendMessage("§r§" + GetPermissionColor(sender, "bounditems.bind.enable") + "/bind enable§r  --  Makes an item bindable or clears its current binding.");
                p.sendMessage("§r§" + GetPermissionColor(sender, "bounditems.bind.clear") + "/bind clear§r  --  Removes an item's bindability.");
                p.sendMessage("§r§" + GetPermissionColor(sender, "bounditems.bind.set") + "/bind set <name>§r  --  Binds an item to <name>.");
                p.sendMessage("§r§" + GetPermissionColor(sender, "bounditems.bind.set") + "/bind help§r  --  Displays this list of commands.");
            }

            return true;

        } else {
            return false;
        }
    }

    public String GetPermissionColor(CommandSender sender, String perm) {
        if (sender.hasPermission(perm)) {
            return "5";
        } else {
            return "8";
        }
    }

    
    
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getPlayer().getItemInHand();

        if (item != null) {
            if (IsBound(item)) {
                if (HasBinding(item, event.getPlayer().getName())) {
                    //continue
                } else {
                    event.setCancelled(true);
                }
            } else if (IsBindable(item)) {
                RemoveBinding(item);
                AddBinding(item, event.getPlayer().getName());
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onEntityDamage(EntityDamageByEntityEvent event) {

        if (event.getDamager() instanceof Player) {
            Player p = (Player) event.getDamager();
            ItemStack item = p.getItemInHand();

            if (item != null) {
                if (IsBound(item)) {
                    if (HasBinding(item, p.getName())) {
                        //continue
                    } else {
                        event.setCancelled(true);
                    }
                } else if (IsBindable(item)) {
                    RemoveBinding(item);
                    AddBinding(item, p.getName());
                }
            }
        }

    }

    
    
    public ItemStack RemoveBinding(ItemStack i) {
        ItemStack item = i;
        ItemMeta meta = item.getItemMeta();
        if (meta.hasLore()) {
            List<String> lore = meta.getLore();
            for (String l : lore) {
                if (l.startsWith("§8Bound to: ")) {
                    lore.remove(l);
                    break;
                } else if (l.equals("§8<Not Bound>")) {
                    lore.remove(l);
                    break;
                }
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    public ItemStack AddBinding(ItemStack i, String binding) {
        ItemStack item = i;
        ItemMeta meta = item.getItemMeta();
        if (binding == null) {
            if (meta.hasLore()) {
                List<String> lore = meta.getLore();
                lore.add("§8<Not Bound>");
                meta.setLore(lore);
                item.setItemMeta(meta);
            } else {
                List<String> lore = new ArrayList<>();
                lore.add("§8<Not Bound>");
                meta.setLore(lore);
                item.setItemMeta(meta);
            }
        } else {
            if (meta.hasLore()) {
                List<String> lore = meta.getLore();
                lore.add("§8Bound to: " + binding);
                meta.setLore(lore);
                item.setItemMeta(meta);
            } else {
                List<String> lore = new ArrayList<>();
                lore.add("§8Bound to: " + binding);
                meta.setLore(lore);
                item.setItemMeta(meta);
            }
        }

        return item;
    }

    public boolean IsBound(ItemStack i) {
        ItemMeta meta = i.getItemMeta();

        if (meta.hasLore()) {
            for (String l : meta.getLore()) {
                if (l.startsWith("§8Bound to: ")) {
                    return true;
                } else if (l.equals("§8<Not Bound>")) {
                    return false;
                }
            }
        }

        return false;
    }

    public boolean IsBindable(ItemStack i) {
        ItemMeta meta = i.getItemMeta();

        if (meta.hasLore()) {
            for (String l : meta.getLore()) {
                if (l.startsWith("§8Bound to: ")) {
                    return false;
                } else if (l.equals("§8<Not Bound>")) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean HasBinding(ItemStack i, String binding) {
        ItemMeta meta = i.getItemMeta();

        if (meta.hasLore()) {
            for (String l : meta.getLore()) {
                if (l.equals("§8Bound to: " + binding)) {
                    return true;
                }
            }
        }

        return false;
    }
}
