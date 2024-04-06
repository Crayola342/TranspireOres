package com.deserialize.transpireores.objects;

import com.deserialize.transpireores.TranspireOres;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RegenerateOre {
    private ItemStack toDrop;
    private String name;
    private ItemStack placeableItem;
    private int regenerationDelay;
    private boolean permissionBased;
    private String permission;
    private Properties allowedProperties;
    private Properties deniedProperties;
    private Material oreSpawn;

    public RegenerateOre(ItemStack toDrop, String name, ItemStack placeableItem, int regenerationDelay, boolean permissionBased, String permission, Properties allowedProperties, Properties deniedProperties, Material oreSpawn) {
        this.toDrop = toDrop;
        this.name = name;
        this.placeableItem = placeableItem;
        this.regenerationDelay = regenerationDelay;
        this.permissionBased = permissionBased;
        this.permission = permission;
        this.allowedProperties = allowedProperties;
        this.deniedProperties = deniedProperties;
        this.oreSpawn = oreSpawn;
    }

    public Material getOreSpawn() {
        return this.oreSpawn;
    }

    private Properties getAllowedProperties() {
        return this.allowedProperties;
    }

    public Properties getDeniedProperties() {
        return this.deniedProperties;
    }

    public void run(Player player, Block block, TranspireOres main, RegenerateOre ore) {
        if (this.isPermissionBased()) {
            if (player.hasPermission(this.getPermission())) {
                this.getAllowedProperties().run(player, block);
                player.getInventory().addItem(new ItemStack[]{ore.getToDrop()});
                block.setType(Material.BEDROCK);
                int delay = ore.getRegenerationDelay() * 1000;
                Regen regen = new Regen(block, ore, System.currentTimeMillis(), System.currentTimeMillis() + (long)delay);
                main.getCurrentRegens().put(regen, ore);
            } else {
                this.getDeniedProperties().run(player, block);
            }
        }

    }

    public String getPermission() {
        return this.permission;
    }

    public int getRegenerationDelay() {
        return this.regenerationDelay;
    }

    public boolean isPermissionBased() {
        return this.permissionBased;
    }

    public ItemStack getToDrop() {
        return this.toDrop;
    }

    public String getName() {
        return this.name;
    }

    public ItemStack getPlaceableItem() {
        return this.placeableItem;
    }
}