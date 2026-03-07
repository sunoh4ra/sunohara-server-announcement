package com.sunohara.announcement;

import java.util.HashMap;
import java.util.Map;

/**
 * 颜色/格式关键字转换器
 */
public final class ColorConverter {

    private static final Map<String, String> COLOR_MAP = new HashMap<>();
    private static final Map<String, String> FORMAT_MAP = new HashMap<>();
    private static final String[] COLOR_KEYWORDS;
    private static final String[] FORMAT_KEYWORDS;

    static {
        COLOR_MAP.put("black", "§0");
        COLOR_MAP.put("dark_blue", "§1");
        COLOR_MAP.put("dark_green", "§2");
        COLOR_MAP.put("dark_aqua", "§3");
        COLOR_MAP.put("dark_red", "§4");
        COLOR_MAP.put("dark_purple", "§5");
        COLOR_MAP.put("gold", "§6");
        COLOR_MAP.put("gray", "§7");
        COLOR_MAP.put("grey", "§7");
        COLOR_MAP.put("dark_gray", "§8");
        COLOR_MAP.put("dark_grey", "§8");
        COLOR_MAP.put("blue", "§9");
        COLOR_MAP.put("green", "§a");
        COLOR_MAP.put("aqua", "§b");
        COLOR_MAP.put("cyan", "§b");
        COLOR_MAP.put("red", "§c");
        COLOR_MAP.put("light_purple", "§d");
        COLOR_MAP.put("magenta", "§d");
        COLOR_MAP.put("yellow", "§e");
        COLOR_MAP.put("white", "§f");

        FORMAT_MAP.put("bold", "§l");
        FORMAT_MAP.put("strikethrough", "§m");
        FORMAT_MAP.put("strike", "§m");
        FORMAT_MAP.put("underline", "§n");
        FORMAT_MAP.put("italic", "§o");
        FORMAT_MAP.put("reset", "§r");

        COLOR_KEYWORDS = COLOR_MAP.keySet().toArray(new String[0]);
        FORMAT_KEYWORDS = FORMAT_MAP.keySet().toArray(new String[0]);
    }

    public static final class ParseResult {
        public final boolean success;
        public final String message;

        public ParseResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }

    public static String[] getColorKeywords() {
        return COLOR_KEYWORDS.clone();
    }

    public static String[] getFormatKeywords() {
        return FORMAT_KEYWORDS.clone();
    }

    public static ParseResult parseCommand(String[] args) {
        if (args == null || args.length == 0) {
            return new ParseResult(false, "");
        }

        StringBuilder message = new StringBuilder();
        int i = 0;

        while (i < args.length) {
            String token = args[i].toLowerCase();
            if (COLOR_MAP.containsKey(token)) {
                message.append(COLOR_MAP.get(token));
                i++;
            } else if (FORMAT_MAP.containsKey(token)) {
                message.append(FORMAT_MAP.get(token));
                i++;
            } else {
                break;
            }
        }

        if (i >= args.length) {
            return new ParseResult(false, "");
        }

        for (int j = i; j < args.length; j++) {
            if (j > i) message.append(" ");
            message.append(args[j]);
        }

        String result = message.toString().replace("&", "§");
        return new ParseResult(true, result);
    }
}
