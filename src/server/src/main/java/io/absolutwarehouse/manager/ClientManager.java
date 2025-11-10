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

        if (message.startsWith("DISCONNECT")) {
            System.out.println("Client " + socket.getInetAddress() + " demande une deconnexion.");
            ClientManager.getInstance().resetEtape();
            return;
        }

        List<String> parsedMessage = parseMessage(message);
        System.out.println(parsedMessage.get(0));
        switch (Etape) {
            case "Connexion":
                System.out.println("Traitement connexion");
                initMessageAction(parsedMessage,socket);
                break;
            case "Lire Package":
                System.out.println("Traitement lecture package");
                searchPackageAction(parsedMessage,socket);
                break;
            case "Inserer Package":
                System.out.println("Traitement insertion package");
                addPackageAction(parsedMessage,socket);
                break;
            case "Modifier Package":
                System.out.println("Traitement modifier package");
                modifyPackageAction(parsedMessage,socket);
                break;
            case "Supprimer Package":
                System.out.println("Traitement Supprimer package");
                deletePackageAction(parsedMessage,socket);
                break;
            default:
                System.out.println(Etape);
                break;
        }

    }

    private static void modifyPackageAction(List<String> parsedMessage, Socket socket) {
        printList(parsedMessage);
        ClientManager.getInstance().basicAnswer(socket, "ALLOWED");
    }


    private static void addPackageAction(List<String> parsedMessage, Socket socket) {
        printList(parsedMessage);
        ClientManager.getInstance().basicAnswer(socket, "ALLOWED");
    }


    private static void deletePackageAction(List<String> parsedMessage, Socket socket) {
        printList(parsedMessage);
        ClientManager.getInstance().basicAnswer(socket, "CONFIRMED");
    }

    private static void searchPackageAction(List<String> parsedMessage, Socket socket) {
        printList(parsedMessage);
        ClientManager.getInstance().basicAnswer(socket, "MY GIVEN DATA TO SEARCH");
    }

    private static void printList(List<String> parsedMessage) {
        for (String s : parsedMessage) {
            System.out.println(s);
        }
    }

    private static boolean initMessageAction(List<String> parsedMessage,Socket socket) {
        String action=parsedMessage.get(0);
        String terminal=parsedMessage.get(1);
        if(!checkPerms(terminal, action)){
            ClientManager.getInstance().basicAnswer(socket, "Action Refusé, vous n'avez pas la permission pour l'action: "+action);
            return false;
            
        }
        switch (action) {
            case "ADD":
                ClientManager.getInstance().basicAnswer(socket, "ALLOWED");
                Etape="Ajouter Package";
                break;
            case "MODIFY":
                ClientManager.getInstance().basicAnswer(socket, "ALLOWED");
                Etape="Modifier Package";
                break;
            case "SEARCH":
                ClientManager.getInstance().basicAnswer(socket, "ALLOWED");
                Etape="Lire Package";
                break;
            case "DELETE":
                ClientManager.getInstance().basicAnswer(socket, "ALLOWED");
                Etape="Supprimer Package";
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
            String perm ="RW"; //DatabaseManager.requestTerminalPermission(terminalID);
            if (action.equals("ADD") || action.equals("MODIFY") || action.equals("DELETE")){
                return (perm.equals("RW"));
            }
            else if(action.equals("SEARCH")){
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
