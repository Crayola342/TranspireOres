package com.deserialize.transpireores.commands;

import com.deserialize.transpireores.TranspireOres;
import com.deserialize.transpireores.Util;
import com.deserialize.transpireores.objects.RegenerateOre;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class OreCommand implements CommandExecutor {
    TranspireOres main;

    public OreCommand(TranspireOres main) {
        this.main = main;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("ores") && sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 4) {
                if (args[0].equalsIgnoreCase("give")) {
                    if (player.hasPermission("transpireores.admin")) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target != null) {
                            if (this.main.getAllRegenerateOres().containsKey(args[2])) {
                                RegenerateOre ore = (RegenerateOre) this.main.getAllRegenerateOres().get(args[2]);
                                int amount = 1;

                                try {
                                    amount = Integer.parseInt(args[3]);
                                } catch (NumberFormatException var10) {
                                    player.sendMessage(Util.toColor(this.main.getConfig().getString("messages.invalid-number").replace("%number%", args[3])));
                                }

                                for (int i = 0; i < amount; ++i) {
                                    player.getInventory().addItem(new ItemStack[]{ore.getPlaceableItem()});
                                }

                                player.sendMessage(Util.toColor(this.main.getConfig().getString("messages.ore-give").replace("%ore%", args[2]).replace("%amount%", args[3])));
                            } else {
                                player.sendMessage(Util.toColor(this.main.getConfig().getString("messages.invalid-ore").replace("%ore%", args[2])));
                            }
                        } else {
                            player.sendMessage(Util.toColor(this.main.getConfig().getString("messages.offline-player").replace("%player%", args[1])));
                        }
                    } else {
                        player.sendMessage(Util.toColor(this.main.getConfig().getString("messages.no-permission")));
                    }
                }
            } else {
                this.runHelp(player);
            }
        }

        return false;
    }

    private void runHelp(Player player) {
        this.main.getConfig().getStringList("messages.help").forEach((m) -> {
            player.sendMessage(Util.toColor(m));
        });
    }
}
