package com.deserialize.transpireores.events;

import com.deserialize.transpireores.TranspireOres;
import com.deserialize.transpireores.Util;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;

public class BlockPlace implements Listener {
    TranspireOres main;

    public BlockPlace(TranspireOres main) {
        this.main = main;
    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.LOWEST
    )
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand() != null && !player.getItemInHand().getType().equals(Material.AIR)) {
            NBTItem item = new NBTItem(player.getItemInHand());
            if (item.hasKey("ores")) {
                String mix = player.getWorld().getName() + "," + event.getBlock().getLocation().getX() + "," + event.getBlock().getLocation().getY() + "," + event.getBlock().getLocation().getZ();
                ((ArrayList)this.main.getRegenerateOreLocations().get(item.getString("ores"))).add(mix);
                this.main.getLocationToOre().put(mix, this.main.getAllRegenerateOres().get(item.getString("ores")));
                player.sendMessage(Util.toColor(this.main.getConfig().getString("messages.ore-placed").replace("%ore%", item.getString("ores"))));
            }
        }
    }
}