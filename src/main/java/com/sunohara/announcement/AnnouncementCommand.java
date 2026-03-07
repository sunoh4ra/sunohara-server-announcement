package com.sunohara.announcement;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 公告命令执行器
 */
public class AnnouncementCommand implements CommandExecutor, TabCompleter {

    private final AnnouncementPlugin plugin;
    private final PermissionManager permissionManager;
    private final ConfigManager configManager;

    public AnnouncementCommand(AnnouncementPlugin plugin) {
        this.plugin = plugin;
        this.permissionManager = plugin.getPermissionManager();
        this.configManager = plugin.getConfigManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String cmd = command.getName().toLowerCase();

        switch (cmd) {
            case "announce" -> {
                return handleAnnounce(sender, args);
            }
            case "ahelp" -> {
                return handleHelp(sender);
            }
            case "areload" -> {
                return handleReload(sender);
            }
            default -> {
                return false;
            }
        }
    }

    private boolean handleAnnounce(CommandSender sender, String[] args) {
        if (!permissionManager.hasAnnouncePermission(sender)) {
            sender.sendMessage("§c你没有权限使用此命令！");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("§e用法: /announce [类型] [颜色] [格式] <消息>");
            sender.sendMessage("§e类型: chat, actionbar, title, bossbar, all (默认: chat)");
            sender.sendMessage("§eBossBar: /announce bossbar [秒数] <消息>，/announce bossbar clear 移除");
            return true;
        }

        AnnouncementType announcementType = AnnouncementType.CHAT;
        int startIndex = 0;

        String firstArg = args[0].toLowerCase();
        for (AnnouncementType type : AnnouncementType.values()) {
            if (type.getConfigName().equalsIgnoreCase(firstArg)) {
                announcementType = type;
                startIndex = 1;
                break;
            }
        }

        if (startIndex >= args.length) {
            sender.sendMessage("§c请输入消息内容！");
            return true;
        }

        if (announcementType == AnnouncementType.BOSS_BAR && args.length == 2
                && (args[1].equalsIgnoreCase("clear") || args[1].equalsIgnoreCase("remove"))) {
            plugin.clearBossBar();
            sender.sendMessage("§aBossBar 已移除");
            return true;
        }

        String[] messageArgs = new String[args.length - startIndex];
        System.arraycopy(args, startIndex, messageArgs, 0, messageArgs.length);

        Integer bossbarDuration = null;
        if (announcementType == AnnouncementType.BOSS_BAR && messageArgs.length >= 2) {
            try {
                int duration = Integer.parseInt(messageArgs[0]);
                if (duration >= 0) {
                    bossbarDuration = duration;
                    String[] rest = new String[messageArgs.length - 1];
                    System.arraycopy(messageArgs, 1, rest, 0, rest.length);
                    messageArgs = rest;
                }
            } catch (NumberFormatException ignored) {
            }
        }

        ColorConverter.ParseResult parseResult = ColorConverter.parseCommand(messageArgs);

        if (!parseResult.success) {
            sender.sendMessage("§c请输入消息内容！");
            sender.sendMessage("§e用法: /announce [类型] [颜色] [格式] <消息>");
            return true;
        }

        String messageStr = parseResult.message;

        int maxLength = configManager.getMaxAnnouncementLength();
        if (messageStr.length() > maxLength) {
            sender.sendMessage("§c公告长度超过限制 (最大 " + maxLength + " 字符)");
            return true;
        }

        if (announcementType == AnnouncementType.BOSS_BAR && bossbarDuration != null) {
            plugin.broadcastAnnouncement(messageStr, announcementType, bossbarDuration);
        } else {
            plugin.broadcastAnnouncement(messageStr, announcementType);
        }
        String durationHint = (announcementType == AnnouncementType.BOSS_BAR && bossbarDuration != null)
                ? " §7(" + (bossbarDuration == 0 ? "不自动移除" : bossbarDuration + "秒") + ")"
                : "";
        sender.sendMessage("§a公告已发送! §7[" + announcementType.getDisplayName() + "]" + durationHint);

        if (sender instanceof Player p) {
            plugin.getLogger().info(p.getName() + " 发送了公告 [" + announcementType.getDisplayName() + "]: " + messageStr);
        }

        return true;
    }

    private boolean handleHelp(CommandSender sender) {
        if (!permissionManager.hasHelpPermission(sender)) {
            sender.sendMessage("§c你没有权限使用此命令！");
            return true;
        }

        sender.sendMessage("§6================== 公告系统帮助 ==================");
        sender.sendMessage("§e命令用法：");
        sender.sendMessage("  §a/announce [类型] [颜色] [格式] <消息>");
        sender.sendMessage("");
        sender.sendMessage("§e公告类型：");
        sender.sendMessage("  §a chat §7- 聊天框消息（默认）");
        sender.sendMessage("  §a actionbar §7- 屏幕中央消息");
        sender.sendMessage("  §a title §7- 大标题消息");
        sender.sendMessage("  §a bossbar [秒数] §7- 进度条消息，clear 移除");
        sender.sendMessage("  §a all §7- 同时发送所有类型");
        sender.sendMessage("");
        sender.sendMessage("§e可用颜色：");
        sender.sendMessage("  §7" + String.join(" ", ColorConverter.getColorKeywords()));
        sender.sendMessage("§e可用格式：");
        sender.sendMessage("  §7" + String.join(" ", ColorConverter.getFormatKeywords()));
        sender.sendMessage("");
        sender.sendMessage("§e权限级别: " + permissionManager.getPermissionLevelName(permissionManager.getPermissionLevel(sender)));
        if (permissionManager.hasReloadPermission(sender)) {
            sender.sendMessage("  §a/areload §f- 重新加载配置");
        }
        sender.sendMessage("§6================================================");

        return true;
    }

    private boolean handleReload(CommandSender sender) {
        if (!permissionManager.hasReloadPermission(sender)) {
            sender.sendMessage("§c你没有权限使用此命令！");
            return true;
        }

        try {
            configManager.reloadConfig();
            sender.sendMessage("§a配置已重新加载！");
            plugin.getLogger().info("配置已重新加载");
        } catch (Exception e) {
            sender.sendMessage("§c配置加载失败: " + e.getMessage());
            plugin.getLogger().warning("配置加载失败: " + e.getMessage());
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command,
                                      String alias, String[] args) {
        if (!command.getName().equalsIgnoreCase("announce")) {
            return List.of();
        }
        if (!permissionManager.hasAnnouncePermission(sender)) {
            return List.of();
        }
        if (args.length == 1) {
            return filterCompletions(
                    Arrays.stream(AnnouncementType.values()).map(AnnouncementType::getConfigName).toList(),
                    args[0]
            );
        }
        if (args.length == 2) {
            String type = args[0].toLowerCase();
            List<String> options = new ArrayList<>(Arrays.asList(ColorConverter.getColorKeywords()));
            if (type.equals("bossbar")) {
                options.add("clear");
                options.add("remove");
                options.addAll(Arrays.asList("0", "10", "30", "60", "120", "300"));
            }
            return filterCompletions(options, args[1]);
        }
        if (args.length == 3) {
            return filterCompletions(Arrays.asList(ColorConverter.getFormatKeywords()), args[2]);
        }
        return List.of();
    }

    private List<String> filterCompletions(List<String> options, String prefix) {
        if (prefix.isEmpty()) return options;
        String lower = prefix.toLowerCase();
        return options.stream()
                .filter(s -> s.toLowerCase().startsWith(lower))
                .collect(Collectors.toList());
    }
}
