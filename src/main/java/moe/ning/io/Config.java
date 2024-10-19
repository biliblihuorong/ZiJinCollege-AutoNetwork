package moe.ning.io;

import moe.ning.domain.User;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

public class Config {
    // 获取用户目录下的 ini 文件路径
    public  String getIniFilePath() {
        // 获取当前用户目录
        String userHome = System.getProperty("user.home");
        // 拼接出 ini 文件路径
        return Paths.get(userHome, "autoLoginNatwork.ini").toString();
    }

    // 读取 ini 文件，并解析为 User 对象
    public  User readIniFile() {
        String iniFilePath = getIniFilePath();
        Properties properties = new Properties();

        try (BufferedReader reader = new BufferedReader(new FileReader(iniFilePath))) {
            // 读取 ini 文件中的属性
            properties.load(reader);

            // 获取 userId, password, service
            String userId = properties.getProperty("userId");
            String password = properties.getProperty("password");
            String service = properties.getProperty("service");

            // 返回解析出的 User 对象
            return new User(userId, password, service);

        } catch (FileNotFoundException e) {
            System.out.println("文件未找到: " + iniFilePath);
        } catch (IOException e) {
            System.out.println("读取文件时出错: " + e.getMessage());
        }
        return null;
    }

}
