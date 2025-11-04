package io.absolutwarehouse.manager;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientManager {

    public static ClientManager instance;

    private ClientManager() {}

    public static ClientManager getInstance() {
        if (instance == null) instance = new ClientManager();
        return instance;
    }


    public static void handleMessage(Socket socket, String message){
        if(message == null) return;
        if(socket.isClosed()) return;

        if(message.startsWith("Bonjour") ){
            ClientManager.getInstance().basicAnswer(socket, "Bonjour client !");
        }

        if(message.startsWith("Test") ){
            ClientManager.getInstance().basicAnswer(socket, "micro 1,2,3 !");
        }


    }


    public void basicAnswer(Socket socket, String message) {
        if(socket == null || socket.isClosed()) return;

        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            out.write(message);
            out.newLine(); // équivalent de println()
            out.flush();   // très important pour envoyer les données
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
