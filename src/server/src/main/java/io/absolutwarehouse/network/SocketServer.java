package io.absolutwarehouse.network;

import io.absolutwarehouse.config.ServerConfig;
import io.absolutwarehouse.network.listener.ClientListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer implements Runnable {

    private final int port;
    private final InetAddress address;
    private final ClientListener listener;
    private volatile boolean running = false;
    private ServerSocket serverSocket;

    public SocketServer(int port, InetAddress address, ClientListener listener) {
        this.port = port;
        this.address = address;
        this.listener = listener;
    }

    public void run() {
        try {
            serverSocket = new ServerSocket(port, ServerConfig.MAX_CONCURRENT_CONNECTIONS, address);
            serverSocket.setReceiveBufferSize(ServerConfig.MAX_BUFFER_SIZE);



            running = true;
            System.out.println("Serveur listening on " + address.getHostAddress() + ":" + port);

            while (running) {
                try {
                    Socket client = serverSocket.accept();
                    if (listener != null) listener.onClientConnected(client);
                    new Thread(() -> handleClient(client)).start();
                } catch (IOException e) {
                    if (running && listener != null) listener.onError(e);
                }
            }

        } catch (IOException e) {
            if (listener != null) listener.onError(e);
        } finally {
            closeServerSocket();
        }
    }

    /** Démarre le serveur dans un thread séparé */
    public void start() {
        printServerInfo();
        new Thread(this, "SocketServer-MainListenerThread").start();
    }

    /** Arrête proprement le serveur */
    public void stop() {
        running = false;
        closeServerSocket();
        System.out.println("Serveur stopped.");
    }

    private void closeServerSocket() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException ignored) {}
    }

    private void handleClient(Socket client) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
            String msg;
            while ((msg = in.readLine()) != null) {
                if (listener != null) listener.onReceived(client, msg);
            }
        } catch (java.net.SocketException e) {
            if ("Connection reset".equals(e.getMessage())) {
                System.out.println("Client " + client.getInetAddress() + " has closed brutally the connection !");
            } else {
                if (listener != null) listener.onError(e);
            }
        } catch (IOException e) {
            if (listener != null) listener.onError(e);
        } finally {
            // Toujours notifier le listener et fermer le socket
            if (listener != null) listener.onClientDisconnected(client);
            try {
                client.close();
            } catch (IOException ignored) {}
        }
    }



    private void printServerInfo() {
        System.out.println(String.format(
                """
                ============
                ServerName : %s
                ===========
                IP : %s
                Port: %d
                MaxConcurrentConnections : %d
                MaxBufferSize : %d Ko
                ============""",
                ServerConfig.SERVER_NAME,
                ServerConfig.IP,
                ServerConfig.PORT,
                ServerConfig.MAX_CONCURRENT_CONNECTIONS,
                ServerConfig.MAX_BUFFER_SIZE
                )
        );

    }


}
