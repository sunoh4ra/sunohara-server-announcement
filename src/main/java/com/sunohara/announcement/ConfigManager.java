package com.sunohara.announcement;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 配置管理器
 */
public class ConfigManager {

    private final AnnouncementPlugin plugin;
    private FileConfiguration config;
    private File configFile;

    public ConfigManager(AnnouncementPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        configFile = new File(plugin.getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            try {
                plugin.saveResource("config.yml", false);
                plugin.getLogger().info("已创建默认配置文件");
            } catch (Exception e) {
                plugin.getLogger().warning("配置创建失败: " + e.getMessage());
            }
        }

        config = YamlConfiguration.loadConfiguration(configFile);
        mergeWithDefaults();
    }

    private void mergeWithDefaults() {
        try (InputStream defaultStream = plugin.getResource("config.yml")) {
            if (defaultStream == null) return;
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(
                    new InputStreamReader(defaultStream, StandardCharsets.UTF_8));
            boolean modified = false;
            for (String key : defaultConfig.getKeys(true)) {
                if (defaultConfig.isConfigurationSection(key)) continue;
                if (!config.contains(key)) {
                    config.set(key, defaultConfig.get(key));
                    modified = true;
                }
            }
            if (modified) {
                config.save(configFile);
                plugin.getLogger().info("配置文件已更新，已添加新版本配置项");
            }
        } catch (Exception e) {
            plugin.getLogger().warning("配置合并失败: " + e.getMessage());
        }
    }

    public void reloadConfig() {
        try {
            config = YamlConfiguration.loadConfiguration(configFile);
            mergeWithDefaults();
        } catch (Exception e) {
            plugin.getLogger().warning("配置加载失败: " + e.getMessage());
        }
    }

    public String getAnnouncementPrefix() {
        return config.getString("announcement-prefix", "§6[公告]§r ");
    }

    public int getMaxAnnouncementLength() {
        return config.getInt("max-announcement-length", 256);
    }

    public int getBossbarDurationSeconds() {
        return config.getInt("bossbar-duration-seconds", 10);
    }

    public boolean shouldLogAnnouncements() {
        return config.getBoolean("log-announcements", true);
    }

    public boolean isPermissionsEnabled() {
        return config.getBoolean("permissions.enabled", true);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().warning("配置保存失败: " + e.getMessage());
        }
    }
}
