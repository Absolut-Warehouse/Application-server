package io.absolutwarehouse.network.listener;

import java.net.Socket;

public interface ClientListener {

    void onClientDisconnected(Socket clientSocket);
    void onClientConnected(Socket clientSocket);
    void onReceived(Socket clientSocket, String message);

}
