import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class TestMain {
//    public static void main(String[] args) throws Exception {
//
//       // 切换控制台编码为 GBK
//        String property = System.getProperty("os.name").toLowerCase().contains("windows") ? "linux" : "mac";
//        System.out.println(property);
//        Runtime.getRuntime().exec("cmd /c chip 936");
//        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
//
//        // 现在打印的内容会使用 GBK 编码
//        System.out.println("这是一条GBK编码的日志输出");
//    }

    public static void main(String[] args) {
        System.setProperty("file.encoding", "GBK");
        System.out.println("hello world");
    }

}