package com.sunohara.announcement;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 公告插件主类
 */
public class AnnouncementPlugin extends JavaPlugin {

    private PermissionManager permissionManager;
    private ConfigManager configManager;
    private AnnouncementStyleManager styleManager;

    @Override
    public void onEnable() {
        // 初始化配置管理器
        configManager = new ConfigManager(this);
        configManager.loadConfig();

        // 初始化权限与样式管理器
        permissionManager = new PermissionManager(this);
        styleManager = new AnnouncementStyleManager(this);

        // 注册命令
        AnnouncementCommand commandExecutor = new AnnouncementCommand(this);
        getCommand("announce").setExecutor(commandExecutor);
        getCommand("announce").setTabCompleter(commandExecutor);
        getCommand("ahelp").setExecutor(commandExecutor);
        getCommand("areload").setExecutor(commandExecutor);

        getLogger().info("================================");
        getLogger().info("  Sunohara Server Announcement（公告）");
        getLogger().info("  版本: " + getDescription().getVersion());
        getLogger().info("  权限: " + (configManager.isPermissionsEnabled() ? "已启用" : "已禁用"));
        getLogger().info("  已启用");
        getLogger().info("================================");
    }

    @Override
    public void onDisable() {
        styleManager.clearBossBar();
        getLogger().info("Sunohara Server Announcement 已禁用");
    }

    public void broadcastAnnouncement(String message) {
        broadcastAnnouncement(message, AnnouncementType.CHAT);
    }

    public void broadcastAnnouncement(String message, AnnouncementType type) {
        broadcastAnnouncement(message, type, null);
    }

    public void broadcastAnnouncement(String message, AnnouncementType type, Integer bossbarDurationSeconds) {
        styleManager.broadcastAnnouncement(message, type, bossbarDurationSeconds);

        if (configManager.shouldLogAnnouncements()) {
            getLogger().info("公告已发送 [" + type.getDisplayName() + "]: " + message);
        }
    }

    public void clearBossBar() {
        styleManager.clearBossBar();
    }

    /**
     * 获取样式管理器
     */
    public AnnouncementStyleManager getStyleManager() {
        return styleManager;
    }

    /**
     * 获取权限管理器
     */
    public PermissionManager getPermissionManager() {
        return permissionManager;
    }

    /**
     * 获取配置管理器
     */
    public ConfigManager getConfigManager() {
        return configManager;
    }

    /**
     * 获取日志前缀
     */
    public String getLogPrefix() {
        return "§6[" + this.getName() + "§6]§r ";
    }
}
