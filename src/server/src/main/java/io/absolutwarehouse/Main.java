package io.absolutwarehouse;

import io.absolutwarehouse.config.ServerConfig;
import io.absolutwarehouse.network.SocketServer;
import io.absolutwarehouse.network.listener.MyListener;

import java.net.InetAddress;

public class Main {


    public static void main(String[] args) {

        try {
            InetAddress address = InetAddress.getByName(ServerConfig.IP);
            SocketServer socketServer = new SocketServer(ServerConfig.PORT, address, new MyListener());
            socketServer.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}