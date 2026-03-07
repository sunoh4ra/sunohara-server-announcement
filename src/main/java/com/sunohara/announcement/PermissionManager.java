package com.sunohara.announcement;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * 公告权限管理器
 */
public class PermissionManager {

    private final AnnouncementPlugin plugin;

    // 权限常量
    public static final String PERMISSION_ANNOUNCE = "sunohara.announce";
    public static final String PERMISSION_ANNOUNCE_ADMIN = "sunohara.announce.admin";
    public static final String PERMISSION_CONFIG_RELOAD = "sunohara.config.reload";
    public static final String PERMISSION_BROADCAST = "sunohara.broadcast";
    public static final String PERMISSION_HELP = "sunohara.help";

    public PermissionManager(AnnouncementPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * 检查发送者是否有公告权限
     */
    public boolean hasAnnouncePermission(CommandSender sender) {
        return sender.hasPermission(PERMISSION_ANNOUNCE) || sender.isOp();
    }

    /**
     * 检查发送者是否有管理员权限
     */
    public boolean hasAdminPermission(CommandSender sender) {
        return sender.hasPermission(PERMISSION_ANNOUNCE_ADMIN) || sender.isOp();
    }

    /**
     * 检查发送者是否有配置重载权限
     */
    public boolean hasReloadPermission(CommandSender sender) {
        return sender.hasPermission(PERMISSION_CONFIG_RELOAD) || sender.isOp();
    }

    /**
     * 检查发送者是否有广播权限
     */
    public boolean hasBroadcastPermission(CommandSender sender) {
        return sender.hasPermission(PERMISSION_BROADCAST) || sender.isOp();
    }

    /**
     * 检查发送者是否有帮助权限
     */
    public boolean hasHelpPermission(CommandSender sender) {
        return sender.hasPermission(PERMISSION_HELP);
    }

    /**
     * 获取权限级别（数字越高权限越高）
     */
    public int getPermissionLevel(CommandSender sender) {
        if (sender.isOp()) {
            return 4; // 最高权限
        }

        if (sender.hasPermission(PERMISSION_ANNOUNCE_ADMIN)) {
            return 3; // 管理员
        }

        if (sender.hasPermission(PERMISSION_ANNOUNCE)) {
            return 2; // 一般权限
        }

        if (sender.hasPermission(PERMISSION_HELP)) {
            return 1; // 仅查看帮助
        }

        return 0; // 无权限
    }

    /**
     * 检查权限并返回友好的错误消息
     */
    public boolean checkPermission(CommandSender sender, String permission) {
        if (sender.hasPermission(permission) || sender.isOp()) {
            return true;
        }

        sender.sendMessage("§c你没有权限使用此命令！");
        if (sender instanceof Player) {
            plugin.getLogger().warning("玩家 " + ((Player) sender).getName() + " 尝试使用无权限命令");
        }
        return false;
    }

    /**
     * 获取权限级别的名称
     */
    public String getPermissionLevelName(int level) {
        return switch (level) {
            case 4 -> "§c运营者";
            case 3 -> "§e管理员";
            case 2 -> "§a主持人";
            case 1 -> "§7用户";
            default -> "§8无权限";
        };
    }
}
