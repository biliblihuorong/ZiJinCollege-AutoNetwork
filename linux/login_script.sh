#!/bin/sh

# 登录信息（根据需求修改）
USER_ID="" # 账号
PASSWORD="" # 密码
SERVICE="电信"  # 可以选择 "校园网", "移动", "联通", "电信"

# 认证接口的基础URL
AUTH_URL="http://172.21.2.10:8080/eportal/InterFace.do?method=login"

# 获取queryString信息
get_query_string() {
    echo "Fetching queryString..."

    QUERY_STRING=$(curl -s -L "http://172.21.2.10:8080/")

    REDIRECT_URL=$(echo "$QUERY_STRING" | grep -o "top.self.location.href='[^']*" | sed "s/top.self.location.href='//")

    if [ -n "$REDIRECT_URL" ]; then
        echo "Redirect URL: $REDIRECT_URL"
    else
        echo "Failed to extract redirect URL"
        exit 1
    fi

    WLANUSERIP=$(echo "$REDIRECT_URL" | grep -o 'wlanuserip=[^&]*' | sed 's/wlanuserip=//')
    WLANACNAME=$(echo "$REDIRECT_URL" | grep -o 'wlanacname=[^&]*' | sed 's/wlanacname=//')
    NASIP=$(echo "$REDIRECT_URL" | grep -o 'nasip=[^&]*' | sed 's/nasip=//')
    MAC=$(echo "$REDIRECT_URL" | grep -o 'mac=[^&]*' | sed 's/mac=//')
    URL=$(echo "$REDIRECT_URL" | grep -o 'url=[^&]*' | sed 's/url=//')
    NASID=$(echo "$REDIRECT_URL" | grep -o 'nasid=[^&]*' | sed 's/nasid=//')
    VID=$(echo "$REDIRECT_URL" | grep -o 'vid=[^&]*' | sed 's/vid=//')
    PORT=$(echo "$REDIRECT_URL" | grep -o 'port=[^&]*' | sed 's/port=//')
    NASPORTID=$(echo "$REDIRECT_URL" | grep -o 'nasportid=[^&]*' | sed 's/nasportid=//')

    if [ -z "$WLANUSERIP" ] || [ -z "$WLANACNAME" ] || [ -z "$NASIP" ] || [ -z "$MAC" ] || [ -z "$URL" ]; then
        echo "Failed to extract necessary parameters from redirect URL"
        exit 1
    fi
}

# 登录函数
login() {
    case "$SERVICE" in
        "校园网") ENCODED_SERVICE="%E6%A0%A1%E5%9B%AD%E7%BD%91" ;;
        "移动") ENCODED_SERVICE="%E7%A7%BB%E5%8A%A8" ;;
        "联通") ENCODED_SERVICE="%E8%81%94%E9%80%9A" ;;
        "电信") ENCODED_SERVICE="%E7%94%B5%E4%BF%A1" ;;
        *) echo "Invalid service option" && exit 1 ;;
    esac

    echo "Sending login request..."
    RESPONSE=$(curl -s -X POST "$AUTH_URL" \
        --header 'Content-Type: application/x-www-form-urlencoded' \
        --data-urlencode "userId=$USER_ID" \
        --data-urlencode "password=$PASSWORD" \
        --data-urlencode "service=$ENCODED_SERVICE" \
        --data-urlencode "queryString=wlanuserip=$WLANUSERIP" \
        --data-urlencode "wlanacname=$WLANACNAME" \
        --data-urlencode "ssid=" \
        --data-urlencode "nasip=$NASIP" \
        --data-urlencode "snmpagentip=" \
        --data-urlencode "mac=$MAC" \
        --data-urlencode "t=wireless-v2" \
        --data-urlencode "url=$URL" \
        --data-urlencode "apmac=" \
        --data-urlencode "nasid=$NASID" \
        --data-urlencode "vid=$VID" \
        --data-urlencode "port=$PORT" \
        --data-urlencode "nasportid=$NASPORTID" \
        --data-urlencode "operatorPwd=" \
        --data-urlencode "operatorUserId=" \
        --data-urlencode "validcode=" \
        --data-urlencode "passwordEncrypt=false")

    echo "Login response: $RESPONSE"

    if echo "$RESPONSE" | grep -q "登录成功"; then
        echo "Login successful!"
    else
        echo "Login failed!"
    fi
}

# 主逻辑
echo "开始自动认证... Starting a certified network ...."
echo "$(date +"%Y-%m-%d %H:%M:%S")"
get_query_string
login