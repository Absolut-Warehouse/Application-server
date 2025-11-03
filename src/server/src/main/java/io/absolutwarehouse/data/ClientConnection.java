package io.absolutwarehouse.data;

import java.net.Socket;

public class ClientConnection {

    private final Socket socket;
    private final String id;

    public ClientConnection(final Socket socket, final String id) {
        this.socket = socket;
        this.id = id;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getAddress() {
        return socket.getInetAddress().getHostAddress();
    }

    public String getId() {
        return id;
    }
}
