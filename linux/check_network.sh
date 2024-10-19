#!/bin/sh

# 检查网络连通性
check_network() {
    echo "正在检查网络状态中... Checking network connectivity..."
    if ping -c 1 -W 2 "www.baidu.com" >/dev/null; then
        echo "Network is working"
        return 0
    else
        echo "Network is unreachable, attempting to re-login..."
        return 1
    fi
}

# 调用登录脚本
login_if_needed() {
    /path/to/login_script.sh >> /path/to/login.log 2>&1
}

# 主逻辑
if ! check_network; then
    echo "$(date +"%Y-%m-%d %H:%M:%S")"
    login_if_needed
fi