package br.pryz.lobby.utils;


import java.io.File;
import java.io.IOException;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class PryConfig {
    private JavaPlugin plugin;
    private String name;
    private File file;
    private YamlConfiguration config;

    public PryConfig(JavaPlugin plugin, String nome) {
        this.plugin = plugin;
        this.setName(nome);
        this.reloadConfig();
    }

    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    public void setPlugin(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getFile() {
        return this.file;
    }

    public YamlConfiguration getConfig() {
        return this.config;
    }

    public void saveConfig() {
        try {
            this.getConfig().save(this.getFile());
            return;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveDefault() {
        this.getConfig().options().copyDefaults(true);
    }

    public void saveDefaultConfig() {
        if (this.existeConfig()) {
            this.saveConfig();
            return;
        }
        this.getPlugin().saveResource(this.getName(), false);
    }

    public void reloadConfig() {
        this.file = new File(this.getPlugin().getDataFolder(), this.getName());
        this.config = YamlConfiguration.loadConfiguration((File)this.getFile());
    }

    public void deleteConfig() {
        this.getFile().delete();
    }

    public boolean existeConfig() {
        return this.getFile().exists();
    }

    public String getString(String path) {
        return this.getConfig().getString(path);
    }

    public boolean getBoolean(String path) {
        return this.getConfig().getBoolean(path);
    }

    public double getDouble(String path) {
        return this.getConfig().getDouble(path);
    }

    public List<?> getList(String path) {
        return this.getConfig().getList(path);
    }

    public boolean contains(String path) {
        return this.getConfig().contains(path);
    }

    public void set(String path, Object value) {
        this.getConfig().set(path, value);
    }
}

