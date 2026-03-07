package com.sunohara.announcement;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

/**
 * 公告样式管理器：聊天、ActionBar、标题、BossBar
 */
public class AnnouncementStyleManager {

    private final AnnouncementPlugin plugin;
    private final ConfigManager configManager;
    private BossBar currentBossBar;

    public AnnouncementStyleManager(AnnouncementPlugin plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
    }

    public void broadcastAnnouncement(String message, AnnouncementType type, Integer bossbarDurationSeconds) {
        String prefix = configManager.getAnnouncementPrefix();
        String fullMessage = prefix + message;

        switch (type) {
            case CHAT -> {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendMessage(fullMessage);
                }
            }
            case ACTION_BAR -> {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendActionBar(fullMessage);
                }
            }
            case TITLE -> {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendTitle(message, "", 10, 70, 20);
                }
            }
            case BOSS_BAR -> {
                clearBossBar();
                currentBossBar = Bukkit.createBossBar(message, BarColor.YELLOW, BarStyle.SOLID);
                for (Player p : Bukkit.getOnlinePlayers()) {
                    currentBossBar.addPlayer(p);
                }
                currentBossBar.setVisible(true);
                int duration = bossbarDurationSeconds != null ? bossbarDurationSeconds : configManager.getBossbarDurationSeconds();
                if (duration > 0) {
                    Bukkit.getScheduler().runTaskLater(plugin, this::clearBossBar, duration * 20L);
                }
            }
            case ALL -> {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendMessage(fullMessage);
                    p.sendActionBar(fullMessage);
                    p.sendTitle(message, "", 10, 70, 20);
                }
                clearBossBar();
                currentBossBar = Bukkit.createBossBar(message, BarColor.YELLOW, BarStyle.SOLID);
                for (Player p : Bukkit.getOnlinePlayers()) {
                    currentBossBar.addPlayer(p);
                }
                currentBossBar.setVisible(true);
                int duration = bossbarDurationSeconds != null ? bossbarDurationSeconds : configManager.getBossbarDurationSeconds();
                if (duration > 0) {
                    Bukkit.getScheduler().runTaskLater(plugin, this::clearBossBar, duration * 20L);
                }
            }
        }
    }

    public void clearBossBar() {
        if (currentBossBar != null) {
            currentBossBar.removeAll();
            currentBossBar = null;
        }
    }
}
