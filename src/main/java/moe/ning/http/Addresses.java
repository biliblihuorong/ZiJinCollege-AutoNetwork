package moe.ning.http;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;

public class Addresses {
    public boolean addr() {
        try {
            // 获取所有网络接口
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            for (NetworkInterface networkInterface : Collections.list(interfaces)) {
                // 获取此接口绑定的所有 IP 地址
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();

                for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                    // 仅考虑 IPv4 地址
                    if (inetAddress instanceof Inet4Address) {
                        String ipAddress = inetAddress.getHostAddress();

                        // 如果是以 "172" 开头的 IPv4 地址
                        if (ipAddress.startsWith("172.")) {
                            System.out.println("找到匹配的接口: " + networkInterface.getName());
                            System.out.println("IPv4 地址: " + ipAddress);
                            return false;
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

}
