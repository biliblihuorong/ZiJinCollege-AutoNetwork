# 自动登录网络程序使用说明

此仓库提供了两种自动登录网络的解决方案，分别针对 **Windows** 和 **Linux** 用户。请根据您的操作系统选择对应的使用指南：

- [Windows 客户端使用指南](#windows-客户端使用指南)
- [Linux 客户端使用指南](#linux-客户端使用指南)

---

## Windows 客户端使用指南

### 功能简介

- 自动使用存储的凭据登录到您的网络服务。
- 可在系统启动时后台自动运行，无需用户交互。

### 安装步骤

#### 第一步：下载程序

1. 前往 [Releases](#) 页面。
2. 下载适用于您操作系统版本的 `.exe` 文件。

#### 第二步：创建配置文件

1. 打开您的 Windows 用户目录。默认路径通常为：
   ```
   C:\Users\您的用户名\
   ```

2. 在用户目录下新建一个名为 `autoLoginNetwork.ini` 的文件。

3. 打开该文件，并按照以下格式输入您的网络登录信息：

   ```ini
   userId=您的用户名
   password=您的密码
   service=您的运营商
   ```

    - `userId`: 您的网络登录用户名。
    - `password`: 您的网络登录密码。
    - `service`: 您的网络服务提供商的名称（例如，校园网、电信、联通、移动）。

#### 第三步：将程序添加到启动项

1. 将下载好的 `.exe` 文件移动到 Windows 系统的启动文件夹中。按下 `Win + R` 打开“运行”对话框，然后输入以下命令并按回车：
   ```
   shell:startup
   ```

2. 在弹出的启动文件夹中，将下载的 `.exe` 文件粘贴进去。这样，程序将在每次计算机启动时自动运行。

#### 第四步：运行程序

每次系统启动时，程序会自动运行，并使用 `autoLoginNetwork.ini` 文件中的凭据登录到网络服务。

### 常见问题解答

#### 问题：如何更新登录凭据？
答：您只需打开 `autoLoginNetwork.ini` 文件，修改其中的 `userId`、`password` 或 `service` 字段，保存后程序将使用更新的凭据登录。

#### 问题：如何手动运行程序而不重启电脑？
答：可以直接双击 `.exe` 文件手动运行程序。

---

## Linux 客户端使用指南

本文将指导您在 **Linux 系统** 上安装并配置自动登录网络的脚本。该脚本将从仓库的 `linux` 文件夹中下载两个文件，设置为可执行文件，并通过 `cron` 服务定时执行。

### 使用步骤

#### 第一步：下载脚本文件

1. `check_network.sh` - 用于检查网络连接的脚本。
2. `login_script.sh` - 用于登录网络的脚本。

您可以使用以下命令下载这两个文件：

```bash
# 创建存放脚本的文件夹（假设路径为 /path/to/，可以自定义路径）
mkdir -p /path/to/

# 下载脚本文件
wget -O /path/to/check_network.sh https://your-repo-url/linux/check_network.sh
wget -O /path/to/login_script.sh https://your-repo-url/linux/login_script.sh
```

> 请将 `your-repo-url` 替换为仓库的实际 URL。

#### 第二步：填写登录信息

在下载完成后，您需要在 `login_script.sh` 中填写网络的登录信息：

1. 打开 `login_script.sh` 文件进行编辑：
   ```bash
   nano /path/to/login_script.sh
   ```

2. 找到以下部分：
   ```bash
   # 登录信息（根据需求修改）
   USER_ID="" # 账号
   PASSWORD="" # 密码
   SERVICE="电信"  # 可以选择 "校园网", "移动", "联通", "电信"
   ```

3. 将 `USER_ID` 替换为您的登录账号，`PASSWORD` 替换为您的密码，`SERVICE` 替换为您的服务运营商（如 "校园网"、"移动"、"联通" 或 "电信"）。例如：
   ```bash
   USER_ID="yourUsername"
   PASSWORD="yourPassword"
   SERVICE="电信"
   ```

4. 保存并退出编辑器。按 `Ctrl + O` 保存，按 `Ctrl + X` 退出。

#### 第三步：设置脚本为可执行文件

使用 `chmod` 命令将下载的脚本文件设置为可执行文件：

```bash
chmod +x /path/to/check_network.sh
chmod +x /path/to/login_script.sh
```

#### 第四步：配置 `cron` 定时任务

您需要通过 `cron` 配置定时任务，以定期运行这两个脚本。

1. 打开 `cron` 编辑器：
   ```bash
   crontab -e
   ```

2. 在 `cron` 文件中添加以下内容：

   ```bash
   # 每天早上 6 点运行登录脚本，输出日志到 /path/to/login.log
   0 6 * * * /path/to/login_script.sh >> /path/to/login.log 2>&1

   # 每天晚上 11 点至早上 5 点，每 10 分钟检查一次网络状态，输出日志到 /path/to/network_check.log
   */10 23-5 * * * /path/to/check_network.sh >> /path/to/network_check.log 2>&1
   ```

    - **`0 6 * * *`**：表示每天早上 6 点运行 `login_script.sh` 并将日志写入 `login.log`。
    - **`*/10 23-5 * * *`**：表示每天晚上 11 点至早上 5 点，每 10 分钟运行 `check_network.sh`，并将日志写入 `network_check.log`。

#### 第五步：保存并退出

保存并退出 `cron` 编辑器后，`cron` 将自动加载配置，脚本会根据设定时间运行。

### 检查 `cron` 配置

您可以通过以下命令检查当前的 `cron` 配置：

```bash
crontab -l
```

### 日志查看

- 登录脚本的日志将保存到 `/path/to/login.log`。
- 网络检查脚本的日志将保存到 `/path/to/network_check.log`。

如果脚本没有按预期运行，可以通过日志文件进行故障排查。

---

这样，您就完成了在 Linux 系统上自动登录网络的脚本配置。