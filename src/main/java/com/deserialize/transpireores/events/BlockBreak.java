package com.deserialize.transpireores.events;

import com.deserialize.transpireores.TranspireOres;
import com.deserialize.transpireores.Util;
import com.deserialize.transpireores.objects.RegenerateOre;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class BlockBreak implements Listener {
    TranspireOres main;

    public BlockBreak(TranspireOres main) {
        this.main = main;
    }

    @EventHandler(
            ignoreCancelled = true
    )
    public void onBlockBreak(BlockBreakEvent event) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        String mix = event.getPlayer().getWorld().getName() + "," + event.getBlock().getLocation().getX() + "," + event.getBlock().getLocation().getY() + "," + event.getBlock().getLocation().getZ();
        RegenerateOre ore;
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            if (!event.getPlayer().hasPermission("transpireores.admin")) {
                return;
            }

            if (this.main.getLocationToOre().containsKey(mix)) {
                ore = (RegenerateOre)this.main.getLocationToOre().get(mix);
                ((ArrayList)this.main.getRegenerateOreLocations().get(ore.getName())).remove(mix);
                this.main.getLocationToOre().remove(mix);
                event.getPlayer().sendMessage(Util.toColor(this.main.getConfig().getString("messages.ore-broken").replace("%ore%", ore.getName())));
            }
        } else if (this.main.getLocationToOre().containsKey(mix)) {
            event.setCancelled(true);
            ore = (RegenerateOre)this.main.getLocationToOre().get(mix);
            ore.run(event.getPlayer(), event.getBlock(), this.main, ore);
        }

    }
}