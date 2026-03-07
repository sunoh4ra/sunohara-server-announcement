# Sunohara Server Announcement

基于 Paper 1.21.11 的 Minecraft 服务器公告插件。

## 功能特性

- 💬 向所有在线玩家发送公告
- 📺 **多样化显示方式**（聊天框、屏幕中央、大标题、进度条）
- 🎨 支持 Minecraft 颜色代码和简单英文关键字
- 🔧 简单易用的命令系统
- 📝 完整的权限管理系统
- ⚙️ 灵活的配置文件管理
- 📋 详细的日志记录

## 安装

1. 确保服务器运行 Paper 1.21.11 或以上版本
2. 将编译后的 JAR 文件放入 `plugins/` 文件夹
3. 重启服务器
4. 查看服务器日志确认插件加载成功
5. 编辑 `plugins/SunoharaServerAnnouncement/config.yml` 自定义配置

## 使用

### 命令

#### 发送公告
```
/announce [类型] [颜色] [格式] <消息>
```
**说明：** 类型、颜色和格式都是可选的，可以随意组合使用。
**别名:** `/ann <message>` 或 `/notice <message>`

**简单示例：**
```
/announce 欢迎来到我们的服务器！
/announce red 这是一条红色消息
/announce blue bold 这是蓝色加粗的消息
/announce actionbar green 屏幕中央绿色消息
/announce title gold bold 【服务器维护】
/announce bossbar yellow 正在扫描病毒...
/announce bossbar 60 正在维护（60秒后自动消失）
/announce bossbar clear          # 移除当前 BossBar
/announce all red bold 【紧急通知】
```

#### 查看帮助
```
/ahelp
```
查看可用命令和权限级别

#### 重新加载配置
```
/areload
```
重新加载配置文件而无需重启服务器（需要管理员权限）

### 公告类型

| 类型 | 关键字 | 显示方式 | 说明 |
|------|-------|--------|------|
| 聊天框 | `chat` | 聊天框消息 | 普通聊天消息（默认） |
| 屏幕中央 | `actionbar` | 屏幕中央上方 | 黄色高可见文字 |
| 大标题 | `title` | 屏幕中央大标题 | 带淡入淡出效果 |
| 进度条 | `bossbar` | 屏幕顶部进度条 | 类似 Boss 血条，支持 `[秒数]` 指定时长、`clear` 移除 |
| 全部 | `all` | 所有方式 | 同时发送所有类型 |

**BossBar 高级用法：**
- `/announce bossbar <消息>` - 使用配置默认时长（`bossbar-duration-seconds`）
- `/announce bossbar <秒数> <消息>` - 指定显示时长，0 表示不自动移除
- `/announce bossbar clear` 或 `remove` - 立即移除当前显示的 BossBar

## 权限系统

完整的权限层级系统：

| 权限 | 权限级别 | 描述 |
|------|--------|------|
| `sunohara.*` | 4 | 所有权限（相当于 OP） |
| `sunohara.announce` | 2 | 发送服务器公告 |
| `sunohara.announce.admin` | 3 | 管理员功能 |
| `sunohara.config.reload` | 3 | 重新加载配置文件 |
| `sunohara.broadcast` | 2 | 广播消息 |
| `sunohara.help` | 1 | 查看帮助信息 |

**默认权限：** OP 拥有所有权限，所有人可使用 `/ahelp` 和 `/announce` 命令。

## 配置文件

插件会自动在首次运行时创建默认配置文件于 `plugins/SunoharaServerAnnouncement/config.yml`

**主要配置项：**

```yaml
# 公告显示前缀
announcement-prefix: "§6[系统公告]§r "

# 是否记录公告到日志
log-announcements: true

# 最大公告长度
max-announcement-length: 256

# BossBar 显示时长（秒），0 表示不自动移除
bossbar-duration-seconds: 10

# 权限系统是否启用
permissions:
  enabled: true
```

详见 [config.yml](src/main/resources/config.yml) 文件了解所有配置选项

## 高级配置

### 颜色和格式系统

为了让玩家更方便地使用颜色和格式，插件提供了简单的英文关键字系统，无需记忆复杂的 `§` 符号。

#### 颜色关键字

| 颜色 | 关键字 | 说明 |
|------|-------|------|
| 黑色 | `black` | §0黑色 |
| 深蓝色 | `dark_blue` | §1深蓝色 |
| 深绿色 | `dark_green` | §2深绿色 |
| 深青色 | `dark_cyan` | §3深青色 |
| 深红色 | `dark_red` | §4深红色 |
| 紫色 | `dark_purple` | §5紫色 |
| 金色 | `gold` | §6金色 |
| 浅灰色/灰色 | `gray` 或 `grey` | §7浅灰色 |
| 深灰色 | `dark_gray` 或 `dark_grey` | §8深灰色 |
| 蓝色 | `blue` | §9蓝色 |
| 绿色 | `green` | §a绿色 |
| 青色 | `cyan` | §b青色 |
| 红色 | `red` | §c红色 |
| 品红色 | `magenta` | §d品红色 |
| 黄色 | `yellow` | §e黄色 |
| 白色 | `white` | §f白色 |

#### 格式关键字

| 格式 | 关键字 | 说明 |
|------|-------|------|
| 粗体 | `bold` | 文字变粗 |
| 删除线 | `strikethrough` 或 `strike` | 文字添加删除线 |
| 下划线 | `underline` | 文字添加下划线 |
| 斜体 | `italic` | 文字变斜体 |
| 重置 | `reset` | 重置所有格式 |

#### 使用示例

**基础使用：**
```
/announce 欢迎来到服务器
/announce red 这是一条红色消息
/announce blue 这是一条蓝色消息
```

**组合使用（颜色 + 格式）：**
```
/announce red bold 这是红色加粗的消息
/announce gold underline 这是金色下划线的消息
/announce blue italic 这是蓝色斜体的消息
/announce gold bold 【重要】服务器将在10分钟后维护
```

### 颜色代码速查表（§ 符号）

如果你已经熟悉 Minecraft 的颜色代码，也可以继续使用 `§` 符号：

**颜色代码：**
- `§0` - 黑色 | `§1` - 深蓝色 | `§2` - 深绿色 | `§3` - 深青色
- `§4` - 深红色 | `§5` - 紫色 | `§6` - 金色 | `§7` - 浅灰色
- `§8` - 深灰色 | `§9` - 蓝色 | `§a` - 绿色 | `§b` - 青色
- `§c` - 红色 | `§d` - 品红色 | `§e` - 黄色 | `§f` - 白色

**格式代码：**
- `§l` - 粗体 | `§m` - 删除线 | `§n` - 下划线 | `§o` - 斜体 | `§r` - 重置格式

### 权限配置示例

#### 使用 LuckPerms

```
# 给 moderator 组添加权限
/lp group moderator permission set sunohara.announce

# 给特定玩家添加权限
/lp user PlayerName permission set sunohara.announce
```

#### 使用默认权限配置

编辑 `permissions.yml` 文件，添加你的权限配置：

```yaml
group:
  moderator:
    permissions:
      sunohara.announce: true
```

## 项目结构

```
sunohara-server-announcement/
├── src/main/java/com/sunohara/announcement/
│   ├── AnnouncementPlugin.java        # 主插件类
│   ├── AnnouncementCommand.java       # 命令处理
│   ├── PermissionManager.java         # 权限管理
│   ├── ConfigManager.java             # 配置管理
│   ├── ColorConverter.java            # 颜色/格式转换
│   ├── AnnouncementType.java          # 公告类型枚举
│   └── AnnouncementStyleManager.java  # 样式管理
├── src/main/resources/
│   ├── plugin.yml
│   └── config.yml
├── pom.xml
└── README.md
```

## 项目架构

- **AnnouncementPlugin** - 主插件类，初始化与事件管理
- **AnnouncementCommand** - 命令处理
- **PermissionManager** - 权限检查与级别
- **ConfigManager** - 配置加载与保存
- **ColorConverter** - 英文关键字转 Minecraft 颜色代码
- **AnnouncementType** - 公告类型枚举
- **AnnouncementStyleManager** - 聊天/ActionBar/标题/BossBar 发送

## 构建

```bash
mvn clean package
```

编译后的 JAR 文件将生成在 `target/` 目录下。

## 开发指南

可以根据需要添加以下功能：

1. **定时公告** - 定期自动发送公告
2. **数据库支持** - 存储公告历史
3. **公告队列** - 管理多个公告的发送顺序
4. **音效/标题** - 结合 ActionBar 和 Title 显示
5. **事件系统** - 监听玩家加入事件自动发送欢迎公告
6. **公告模板** - 预设公告模板库

### 添加新权限

在 [plugin.yml](src/main/resources/plugin.yml) 中添加新权限定义：

```yaml
permissions:
  sunohara.newfeature:
    description: 新功能描述
    default: op
```

在代码中使用权限检查：

```java
if (sender.hasPermission("sunohara.newfeature")) {
    // 执行操作
}
```

## 使用场景举例

### 场景 1：欢迎新玩家

使用标题显示方式发送欢迎消息：

```
/announce title gold bold 欢迎来到我们的服务器！
```

### 场景 2：服务器维护通知

使用 ActionBar 显示方式进行紧急通知：

```
/announce actionbar red bold 【服务器维护】即将于5分钟后重启，请保存进度！
```

### 场景 3：重要活动公告

使用 BossBar 显示方式进行长期显示，可指定显示时长：

```
/announce bossbar 120 yellow 【活动】现在正在进行方块赛，所有玩家都可以参加！
```

活动结束后手动移除：

```
/announce bossbar clear
```

### 场景 4：全服公告

使用 ALL 显示所有方式进行最重要的公告：

```
/announce all gold bold 【紧急】发现恶意破坏，管理员已介入处理！
```

## 更新日志

### v1.0.0
- **新增：** 初始发布
- **新增：** 基础公告功能
- **新增：** 完整权限系统
- **新增：** 配置文件管理
- **新增：** 多命令支持
- **新增：** 颜色/格式关键字系统

## 许可证

MIT License

## 支持

如有问题或建议，欢迎提出 Issue 或 Pull Request。
