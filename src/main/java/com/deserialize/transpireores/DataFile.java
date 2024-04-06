package com.deserialize.transpireores;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class DataFile {
    private TranspireOres main;
    private File file;
    protected FileConfiguration config;
    private String fileName;

    public DataFile(TranspireOres main, String name) {
        this.main = main;
        this.file = new File(main.getDataFolder(), name + "Data.yml");
        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            } catch (IOException var5) {
                var5.printStackTrace();
            }
        }

        this.config = YamlConfiguration.loadConfiguration(this.file);
        main.getRegenerateOreLocations().put(name, new ArrayList());
        Iterator var3 = this.config.getStringList("locs").iterator();

        while(var3.hasNext()) {
            String string = (String)var3.next();
            ((ArrayList)main.getRegenerateOreLocations().get(name)).add(string.split(":")[0]);
            main.getLocationToOre().put(string.split(":")[0], main.getAllRegenerateOres().get(string.split(":")[1]));
        }

    }
}