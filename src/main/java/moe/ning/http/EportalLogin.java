package moe.ning.http;

import moe.ning.domain.QueryString;
import moe.ning.domain.User;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EportalLogin {

    /**
     * 登录请求，使用Java自带的HttpURLConnection替换Unirest
     * @param user
     * @return 响应结果
     */
    public String login(User user) throws Exception {
        String service = switch (user.getService()) {
            case "校园网" -> "%E6%A0%A1%E5%9B%AD%E7%BD%91";
            case "移动" -> "%E7%A7%BB%E5%8A%A8";
            case "联通" -> "%E8%81%94%E9%80%9A";
            case "电信" -> "%E7%94%B5%E4%BF%A1";
            default -> throw new IllegalArgumentException("服务错误");
        };

        // 构建URL
        URL url = new URL("http://172.21.2.10:8080/eportal/InterFace.do?method=login");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // 设置请求方法和请求头
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setConnectTimeout(60000); // 设置连接超时
        connection.setReadTimeout(60000); // 设置读取超时
        connection.setDoOutput(true); // 允许写出

        // 构建请求参数
        String urlParameters = "userId=" + URLEncoder.encode(user.getUserId(), "UTF-8") +
                "&password=" + URLEncoder.encode(user.getPassword(), "UTF-8") +
                "&service=" + URLEncoder.encode(service, "UTF-8") +
                "&wlanacname=" + URLEncoder.encode(user.getQueryString().getWlanacname(), "UTF-8") +
                "&ssid=" + URLEncoder.encode(user.getQueryString().getSsid(), "UTF-8") +
                "&nasip=" + URLEncoder.encode(user.getQueryString().getNasip(), "UTF-8") +
                "&queryString=" + URLEncoder.encode(user.getQueryString().getWlanuserip(), "UTF-8") +
                "&apmac=" + "" + // 可以为空
                "&mac=" + URLEncoder.encode(user.getQueryString().getMac(), "UTF-8") +
                "&t=" + URLEncoder.encode(user.getQueryString().getT(), "UTF-8") +
                "&url=" + URLEncoder.encode(user.getQueryString().getUrl(), "UTF-8") +
                "&apmac=" + URLEncoder.encode(user.getQueryString().getApmac(), "UTF-8") +
                "&nasid=" + URLEncoder.encode(user.getQueryString().getNasid(), "UTF-8") +
                "&vid=" + URLEncoder.encode(user.getQueryString().getVid(), "UTF-8") +
                "&port=" + URLEncoder.encode(user.getQueryString().getPort(), "UTF-8") +
                "&nasportid=" + URLEncoder.encode(user.getQueryString().getNasportid(), "UTF-8") +
                "&operatorPwd=" + "" + // 可以为空
                "&operatorUserId=" + "" + // 可以为空
                "&validcode=" + "" + // 可以为空
                "&passwordEncrypt=" + user.getQueryString().isPasswordEncrypt();

        // 发送POST请求
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = urlParameters.getBytes("UTF-8");
            os.write(input, 0, input.length);
        }

        // 读取响应
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
        StringBuilder response = new StringBuilder();
        String responseLine;
        while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
        }
        br.close();

        return response.toString();
    }

    /**
     * 获取QueryString信息，使用Java自带的HttpURLConnection替换Unirest
     * @return 响应结果
     */
    public String getQueryString() throws Exception {
        // 构建URL
        URL url = new URL("http://172.21.2.10:8080/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // 设置请求方法
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(60000); // 设置连接超时
        connection.setReadTimeout(60000); // 设置读取超时

        // 读取响应
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
        StringBuilder response = new StringBuilder();
        String responseLine;
        while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
        }
        br.close();

        System.out.println(response.toString());
        return response.toString();
    }

    /**
     * 从输入字符串中提取 wlanuserip 到结尾的数据，并将其转换为 QueryString 对象
     * @param queryString
     * @return QueryString对象
     */
    public QueryString extractWlanuseripData(String queryString) {
        String regex = "top\\.self\\.location\\.href='(.*?)'";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(queryString);

        if (matcher.find()) {
            // 提取到的完整 URL
            String fullUrl = matcher.group(1);

            // 查找 "wlanuserip" 开始的位置并返回后续字符串
            int wlanuseripIndex = fullUrl.indexOf("wlanuserip");
            if (wlanuseripIndex != -1) {
                String extractedParams = fullUrl.substring(wlanuseripIndex);

                // 将参数解析为 QueryString 对象
                return parseQueryString(extractedParams);
            }
        }

        System.out.println("获取 queryString错误");
        return null;
    }

    /**
     * 将字符串解析为 QueryString 对象
     * @param query
     * @return QueryString对象
     */
    private QueryString parseQueryString(String query) {
        QueryString queryStringObj = new QueryString();
        String[] params = query.split("&");

        for (String param : params) {
            String[] keyValue = param.split("=", 2);
            String key = keyValue[0];
            String value = keyValue.length > 1 ? keyValue[1] : "";

            // 根据 key 赋值到对应的 QueryString 字段
            switch (key) {
                case "wlanuserip" -> queryStringObj.setWlanuserip(value);
                case "wlanacname" -> queryStringObj.setWlanacname(value);
                case "ssid" -> queryStringObj.setSsid(value);
                case "nasip" -> queryStringObj.setNasip(value);
                case "snmpagentip" -> queryStringObj.setSnmpagentip(value);
                case "mac" -> queryStringObj.setMac(value);
                case "t" -> queryStringObj.setT(value);
                case "url" -> queryStringObj.setUrl(value);
                case "apmac" -> queryStringObj.setApmac(value);
                case "nasid" -> queryStringObj.setNasid(value);
                case "vid" -> queryStringObj.setVid(value);
                case "port" -> queryStringObj.setPort(value);
                case "nasportid" -> queryStringObj.setNasportid(value);
                case "operatorPwd" -> queryStringObj.setOperatorPwd(value);
                case "operatorUserId" -> queryStringObj.setOperatorUserId(value);
                case "validcode" -> queryStringObj.setValidcode(value);
                case "passwordEncrypt" -> queryStringObj.setPasswordEncrypt("true".equals(value));
                default -> System.out.println("Unknown key: " + key); // 不匹配的键
            }
        }

        return queryStringObj;
    }

    public Boolean getLoginStatus() throws Exception {
        // 构建URL
        URL url = new URL("http://172.21.2.10:8080/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // 设置请求方法
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(60000); // 设置连接超时
        connection.setReadTimeout(60000); // 设置读取超时

        // 读取响应
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder response = new StringBuilder();
        String responseLine;
        while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
        }
        br.close();

        // 打印完整的响应内容
        String fullResponse = response.toString();

        // 检查是否包含"登录成功"
        if (fullResponse.contains("��¼�ɹ�")) {
            System.out.println("检测到登录成功！");
            return true;
        } else {
            System.out.println("登录失败或其他情况");
            return false;
        }
    }
}
