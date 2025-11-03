package io.absolutwarehouse.config;

public final class ServerExampleConfig {

    public static String ip = "127.0.0.1";
    public static int PORT = 8080;

    public static int MAX_CONCURRENT_CONNECTIONS = 8;

    public static int MAX_PACKET_SIZE = 1024; // in Bytes

    public static String DB_HOSTNAME = "postgre-...";
    public static String DB_NAME = "absolutwarehouse";
    public static String DB_PASSWORD = "myPassword";
    public static int DB_PORT = 5432;

}
