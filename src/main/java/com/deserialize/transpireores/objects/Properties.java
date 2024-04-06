package com.deserialize.transpireores.objects;

import com.deserialize.transpireores.Util;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Properties {
    private boolean enabledEffect;
    private String effectName;
    private boolean enabledSound;
    private String soundName;
    private boolean enabledMessage;
    private String message;
    private String chatType;
    private boolean enabledSubtitle;
    private String subtitle;

    public Properties(boolean enabledEffect, String effectName, boolean enabledSound, String soundName, boolean enabledMessage, String message, String chatType, boolean enabledSubtitle, String subtitle) {
        this.enabledEffect = enabledEffect;
        this.effectName = effectName;
        this.enabledSound = enabledSound;
        this.soundName = soundName;
        this.enabledMessage = enabledMessage;
        this.message = message;
        this.chatType = chatType;
        this.enabledSubtitle = enabledSubtitle;
        this.subtitle = subtitle;
    }

    public Sound getSound() {
        return Sound.valueOf(this.getSoundName());
    }

    public Effect getEffect() {
        return Effect.valueOf(this.effectName);
    }

    public boolean isEnabledEffect() {
        return this.enabledEffect;
    }

    public String getEffectName() {
        return this.effectName;
    }

    public boolean isEnabledSound() {
        return this.enabledSound;
    }

    public String getSoundName() {
        return this.soundName;
    }

    public boolean isEnabledMessage() {
        return this.enabledMessage;
    }

    public String getMessage() {
        return this.message;
    }

    public String getChatType() {
        return this.chatType;
    }

    public boolean isEnabledSubtitle() {
        return this.enabledSubtitle;
    }

    public String getSubtitle() {
        return this.subtitle;
    }

    public void run(Player player, Block block) {
        if (this.isEnabledEffect()) {
            player.getWorld().playEffect(block.getLocation(), this.getEffect(), 0);
        }

        if (this.isEnabledSound()) {
            player.getWorld().playSound(player.getLocation(), this.getSound(), 100.0F, 0.0F);
        }

        if (this.isEnabledMessage()) {
            if (this.getChatType().equals("CHAT")) {
                player.sendMessage(Util.toColor(this.getMessage()));
            } else if (this.getChatType().equals("ACTION_BAR")) {
                Util.sendActionbar(player, Util.toColor(this.getMessage()));
            } else if (this.getChatType().equals("TITLE")) {
                if (this.isEnabledSubtitle()) {
                    Util.sendTitle(player, 10, 10, 10, Util.toColor(this.getMessage()), Util.toColor(this.getSubtitle()));
                } else {
                    Util.sendTitle(player, 10, 10, 10, Util.toColor(this.getMessage()), (String)null);
                }
            }
        }

    }
}