package io.absolutwarehouse.network.listener;

import io.absolutwarehouse.manager.ClientManager;

import java.net.Socket;

public class MyListener implements ClientListener {

    @Override
    public void onClientConnected(Socket clientSocket) {
        System.out.println("Client connected : " + clientSocket.getInetAddress());
    }

    @Override
    public void onReceived(Socket clientSocket, String message) {
        System.out.println("Message received from " + clientSocket.getInetAddress() + " : " + message);
        ClientManager.handleMessage(clientSocket, message);
    }

    @Override
    public void onClientDisconnected(Socket clientSocket) {
        System.out.println("Client disconnected : " + clientSocket.getInetAddress());
        ClientManager.getInstance().resetEtape();
    }

    @Override
    public void onError(Exception e) {
        System.err.println("Error : " + e.getMessage());
        ClientManager.getInstance().resetEtape();
        e.printStackTrace();
    }
}
