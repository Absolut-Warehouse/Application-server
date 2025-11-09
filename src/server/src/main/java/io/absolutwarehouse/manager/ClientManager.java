package io.absolutwarehouse.manager;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientManager {

    public static ClientManager instance;
    private String Action;
    private static String Etape="Connexion";

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
        List<String> parsedMessage = parseMessage(message);
        System.out.println(parsedMessage.get(0));
        switch (Etape) {
            case "Connexion":
                initMessageAction(parsedMessage,socket);
                break;
            case "Lire Package":
                readPackageAction(parsedMessage,socket);
                break;
            case "Inserer Package":
                break;
            case "Modifier Package":
                break;
            default:
                System.out.println(Etape);
                break;
        }



    }

    private static void readPackageAction(List<String> parsedMessage, Socket socket) {
        String package_code=parsedMessage.get(0);
        //requête database
        ClientManager.getInstance().basicAnswer(socket, "String de la requête DB formatter correctement");
    }

    private static boolean initMessageAction(List<String> parsedMessage,Socket socket) {
        String action=parsedMessage.get(0);
        String terminal=parsedMessage.get(1);
        if(!checkPerms(terminal, action)){
            ClientManager.getInstance().basicAnswer(socket, "Action Refusé, vous n'avez pas la permission pour l'action: "+action);
            return false;
            
        }
        switch (action) {
            case "Inserer":
                ClientManager.getInstance().basicAnswer(socket, "ALLOWED");
                Etape="Ajouter Package";
                break;
            case "Modifier":
                ClientManager.getInstance().basicAnswer(socket, "ALLOWED");
                Etape="Modifier Package";
                break;
            case "Lire":
                ClientManager.getInstance().basicAnswer(socket, "ALLOWED");
                Etape="Lire Package";
                break;
            default:
                ClientManager.getInstance().basicAnswer(socket, "Action non reconnue");
                break;
        }
        return true;
    }

    private static void traiterMessageModif(List<String> parsedMessage,Socket socket) {
        String action = parsedMessage.get(1);
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
        public static List<String> parseMessage(String message){
            List<String> tokens = new ArrayList<>();
            Matcher m = Pattern.compile("\"([^\"]*)\"|(\\S+)").matcher(message);

            while (m.find()) {
                if (m.group(1) != null) {
                    tokens.add(m.group(1)); 
                } else {
                    tokens.add(m.group(2));
                }
            }

                
            return tokens;
        }

    private static boolean checkPerms(String terminalID,String action) {
        try {
            String perm ="RW";//DatabaseManager.requestTerminalPermission(terminalID);
            if (action.equals("Inserer") || action.equals("Modifier") || action.equals("Supprimer")){
                return (perm.equals("RW"));
            }
            else if(action.equals("Lire")){
                return (perm.equals("RW")||perm.equals("R"));
            }
            else{
                return false;
            }
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
    }
    public void resetEtape(){
        Etape="Connexion";
    }



}
