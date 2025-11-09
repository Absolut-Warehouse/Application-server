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
    private ClientManager() {}
    private static ClientRequest request;

    private static int etape=0;
    private static String action;


    public static ClientManager getInstance() { //Faudra m'expliquer
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
        try {
            request= parseMessage(message);
            String ReqType=request.getType();
            handleMessageOfType(request, ReqType,socket);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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

    public static String getMessageType(String message){ //on pourra remplacer par des ints
        //et avoir un tableau de correspondance pour la clarté
        return"Inconnu";
    }
    
    public static void handleMessageOfType(ClientRequest req, String type,Socket socket){
        switch (type) {
            case "Connexion":
                if(!(etape==0)){
                    return;
                }
                if(!checkPerms(req.getParam("id_terminal"),req.getParam("Action"))){
                    ClientManager.getInstance().basicAnswer(socket, "problème de permission");
                    return;
                }
                //DatabaseManager;
                //etape="Chercher Package";
                //etape="Demander Package";
                break;
            case "Package":
                break;
            case "Lire Package":
                
                break;
            case "Supprimer":
                break;
            default:
                break;
        }
    }

    private static boolean checkPerms(String terminalID,String action) {
        try {
            String perm =DatabaseManager.requestTerminalPermission(terminalID);
            if (action.equals("Ajouter") || action.equals("Modifier") || action.equals("Supprimer")){
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


    public static ClientRequest parseMessage(String message){
        List<String> tokens = new ArrayList<>();
        Matcher m = Pattern.compile("\"([^\"]*)\"|(\\S+)").matcher(message);

        while (m.find()) {
            if (m.group(1) != null) {
                tokens.add(m.group(1)); 
            } else {
                tokens.add(m.group(2));
            }
        }

            
        return new ClientRequest(tokens);
    }

    public void resetStep(){

    }



}
