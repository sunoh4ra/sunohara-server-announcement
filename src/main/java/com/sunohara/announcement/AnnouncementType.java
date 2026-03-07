package com.sunohara.announcement;

/**
 * 公告类型枚举
 */
public enum AnnouncementType {
    CHAT("chat", "聊天框"),
    ACTION_BAR("actionbar", "屏幕中央"),
    TITLE("title", "大标题"),
    BOSS_BAR("bossbar", "进度条"),
    ALL("all", "全部");

    private final String configName;
    private final String displayName;

    AnnouncementType(String configName, String displayName) {
        this.configName = configName;
        this.displayName = displayName;
    }

    public String getConfigName() {
        return configName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
