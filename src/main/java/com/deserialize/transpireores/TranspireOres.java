package com.deserialize.transpireores;

import com.deserialize.transpireores.commands.OreCommand;
import com.deserialize.transpireores.events.BlockBreak;
import com.deserialize.transpireores.events.BlockPlace;
import com.deserialize.transpireores.objects.Properties;
import com.deserialize.transpireores.objects.Regen;
import com.deserialize.transpireores.objects.RegenerateOre;
import com.deserialize.transpireores.tasks.RegenTask;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;

public final class TranspireOres extends JavaPlugin {
    private HashMap<String, ArrayList<String>> regenerateOreLocations;
    private HashMap<String, RegenerateOre> allRegenerateOres;
    private HashMap<String, RegenerateOre> locationToOre;
    private HashMap<Regen, RegenerateOre> currentRegens;
    private RegenTask regenTask;

    public void onEnable() {
        this.reload();
        this.getServer().getPluginManager().registerEvents(new BlockBreak(this), this);
        this.getServer().getPluginManager().registerEvents(new BlockPlace(this), this);
        this.getCommand("ores").setExecutor(new OreCommand(this));
        this.regenerateOreLocations = new HashMap();
        this.allRegenerateOres = new HashMap();
        this.locationToOre = new HashMap();
        this.currentRegens = new HashMap();
        this.loadOres();
        this.regenTask = new RegenTask(this);
    }

    public void onDisable() {
        Iterator var1 = this.getCurrentRegens().keySet().iterator();

        while(var1.hasNext()) {
            Regen r = (Regen)var1.next();
            RegenerateOre ore = (RegenerateOre)this.getCurrentRegens().get(r);
            r.getBlock().getLocation().getBlock().setType(ore.getOreSpawn());
        }

        var1 = this.getRegenerateOreLocations().keySet().iterator();

        while(var1.hasNext()) {
            String string = (String)var1.next();
            File file = new File(this.getDataFolder(), string + "Data.yml");
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            RegenerateOre ore = (RegenerateOre)this.getAllRegenerateOres().get(string);
            ArrayList<String> locs = (ArrayList)this.getRegenerateOreLocations().get(string);
            ListIterator iterator = locs.listIterator();

            while(iterator.hasNext()) {
                String s = (String)iterator.next();
                iterator.set(s + ":" + ore.getName());
            }

            config.set("locs", locs);

            try {
                config.save(file);
            } catch (IOException var9) {
                var9.printStackTrace();
            }
        }

    }

    public void reload() {
        this.saveDefaultConfig();
    }

    public RegenTask getRegenTask() {
        return this.regenTask;
    }

    public HashMap<Regen, RegenerateOre> getCurrentRegens() {
        return this.currentRegens;
    }

    public HashMap<String, ArrayList<String>> getRegenerateOreLocations() {
        return this.regenerateOreLocations;
    }

    public HashMap<String, RegenerateOre> getLocationToOre() {
        return this.locationToOre;
    }

    public HashMap<String, RegenerateOre> getAllRegenerateOres() {
        return this.allRegenerateOres;
    }

    private void loadOres() {
        Iterator var1 = this.getConfig().getConfigurationSection("ores").getKeys(false).iterator();

        while(var1.hasNext()) {
            String oreName = (String)var1.next();
            ItemStack drop = new ItemStack(Material.valueOf(this.getConfig().getString("ores." + oreName + ".drop.material")), this.getConfig().getInt("ores." + oreName + ".drop.amount"), (short)((byte)this.getConfig().getInt("ores." + oreName + ".drop.damage")));
            ItemMeta meta = drop.getItemMeta();
            meta.setDisplayName(Util.toColor(this.getConfig().getString("ores." + oreName + ".drop.name")));
            ArrayList<String> lore = new ArrayList();
            this.getConfig().getStringList("ores." + oreName + ".drop.lore").forEach((l) -> {
                lore.add(Util.toColor(l));
            });
            meta.setLore(lore);
            drop.setItemMeta(meta);
            boolean persmissionBased = this.getConfig().getBoolean("ores." + oreName + ".permission-based");
            String permission = this.getConfig().getString("ores." + oreName + ".permission");
            int regenerationDelay = this.getConfig().getInt("ores." + oreName + ".regeneration-delay");
            ItemStack placeableItem = new ItemStack(Material.valueOf(this.getConfig().getString("ores." + oreName + ".placeable-item.material")), 1, (short)((byte)this.getConfig().getInt("ores." + oreName + ".placeable-item.damage")));
            ItemMeta placeableItemMeta = placeableItem.getItemMeta();
            placeableItemMeta.setDisplayName(Util.toColor(this.getConfig().getString("ores." + oreName + ".placeable-item.name")));
            ArrayList<String> placeableItemLore = new ArrayList();
            this.getConfig().getStringList("ores." + oreName + ".placeable-item.lore").forEach((l) -> {
                placeableItemLore.add(Util.toColor(l));
            });
            placeableItemMeta.setLore(placeableItemLore);
            placeableItem.setItemMeta(placeableItemMeta);
            placeableItem = this.setNBT(placeableItem, "ores", oreName);
            String path = "ores." + oreName + ".onBreak";
            boolean deniedSendMessage = this.getConfig().getBoolean(path + ".denied-break.send-message");
            String deniedMessageType = this.getConfig().getString(path + ".denied-break.message-type");
            String deniedMessage = this.getConfig().getString(path + ".denied-break.message");
            boolean deniedPlayEffect = this.getConfig().getBoolean(path + ".denied-break.play-effect");
            String deniedEffect = this.getConfig().getString(path + ".denied-break.effect");
            boolean deniedPlaySound = this.getConfig().getBoolean(path + ".denied-break.send-message");
            String deniedSound = this.getConfig().getString(path + ".denied-break.sound");
            boolean deniedSendSubtitle = this.getConfig().getBoolean(path + ".denied-break.send-subtitle");
            String deniedSubtitle = this.getConfig().getString(path + ".denied-break.subtitle");
            boolean allowedSendMessage = this.getConfig().getBoolean(path + ".allowed-break.send-message");
            String allowedMessageType = this.getConfig().getString(path + ".allowed-break.message-type");
            String allowedMessage = this.getConfig().getString(path + ".allowed-break.message");
            boolean allowedPlayEffect = this.getConfig().getBoolean(path + ".allowed-break.play-effect");
            String allowedEffect = this.getConfig().getString(path + ".allowed-break.effect");
            boolean allowedPlaySound = this.getConfig().getBoolean(path + ".allowed-break.send-message");
            String allowedSound = this.getConfig().getString(path + ".allowed-break.sound");
            boolean allowedSendSubtitle = this.getConfig().getBoolean(path + ".allowed-break.send-subtitle");
            String allowedSubtitle = this.getConfig().getString(path + ".allowed-break.subtitle");
            Properties allowedProperites = new Properties(allowedPlayEffect, allowedEffect, allowedPlaySound, allowedSound, allowedSendMessage, allowedMessage, allowedMessageType, allowedSendSubtitle, allowedSubtitle);
            Properties deniedProperties = new Properties(deniedPlayEffect, deniedEffect, deniedPlaySound, deniedSound, deniedSendMessage, deniedMessage, deniedMessageType, deniedSendSubtitle, deniedSubtitle);
            Material oreSpawn = Material.valueOf(this.getConfig().getString("ores." + oreName + ".ore-spawn"));
            RegenerateOre ore = new RegenerateOre(drop, oreName, placeableItem, regenerationDelay, persmissionBased, permission, allowedProperites, deniedProperties, oreSpawn);
            this.getAllRegenerateOres().put(oreName, ore);
            new DataFile(this, oreName);
        }

    }

    private ItemStack setNBT(ItemStack item, String key, String value) {
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setString(key, value);
        return nbtItem.getItem();
    }
}
