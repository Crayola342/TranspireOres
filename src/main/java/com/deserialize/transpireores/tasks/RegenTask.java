package com.deserialize.transpireores.tasks;

import com.deserialize.transpireores.TranspireOres;
import com.deserialize.transpireores.objects.Regen;
import com.deserialize.transpireores.objects.RegenerateOre;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Iterator;

public class RegenTask extends BukkitRunnable {
    TranspireOres main;

    public RegenTask(TranspireOres main) {
        this.main = main;
        this.runTaskTimer(main, 0L, 5L);
    }

    public void run() {
        HashMap<Regen, RegenerateOre> toRemove = new HashMap();
        Iterator var2 = this.main.getCurrentRegens().keySet().iterator();

        Regen regen;
        RegenerateOre ore;
        while(var2.hasNext()) {
            regen = (Regen)var2.next();
            ore = regen.getOre();
            if (System.currentTimeMillis() >= regen.getEnd()) {
                regen.getBlock().setType(ore.getOreSpawn());
                toRemove.put(regen, ore);
            }
        }

        var2 = toRemove.keySet().iterator();

        while(var2.hasNext()) {
            regen = (Regen)var2.next();
            ore = (RegenerateOre)toRemove.get(regen);
            this.main.getCurrentRegens().remove(regen, ore);
        }

    }
}
