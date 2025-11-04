package io.absolutwarehouse.config;

public final class ServerConfig {

    public static String SERVER_NAME = "ABSOLUT SERVER";
    public static String IP = "127.0.0.1";
    public static int PORT = 8888;

    public static int MAX_CONCURRENT_CONNECTIONS = 1;

    public static int MAX_BUFFER_SIZE = 1024 * 16; // 16 Ko

    public static String DB_HOSTNAME = "postgre-...";
    public static String DB_NAME = "absolutwarehouse";
    public static String DB_PASSWORD = "myPassword";
    public static int DB_PORT = 5432;


}
