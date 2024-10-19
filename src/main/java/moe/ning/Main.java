package moe.ning;


import moe.ning.domain.QueryString;
import moe.ning.domain.User;
import moe.ning.http.Addresses;
import moe.ning.http.EportalLogin;
import moe.ning.io.Config;


import java.io.PrintStream;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        // 设置 System.out 输出流的编码为 GBK
        try {
            System.setOut(new PrintStream(System.out, true, "GBK"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Addresses addresses = new Addresses();
        if (addresses.addr()) {
            System.out.println("不在校园或者没有连接到校园网");
            waitForExit();
            return;
        }
        EportalLogin eportalLogin = new EportalLogin();
        if (eportalLogin.getLoginStatus()){
            System.out.println("已经登陆");
            waitForExit();
            return;
        }
        Config config = new Config();
        User user = config.readIniFile();
        System.out.println(user.toString());
        String queryString = eportalLogin.getQueryString();
        System.out.println(queryString);
        QueryString queryString1 = eportalLogin.extractWlanuseripData(queryString);
        System.out.println(queryString1);
        user.setQueryString(queryString1);
        String login = eportalLogin.login(user);
        System.out.println(login);
        waitForExit();

    }

    // 用于等待用户输入以便保持窗口打开
    private static void waitForExit() {
        System.out.println("按下回车键退出...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }
}