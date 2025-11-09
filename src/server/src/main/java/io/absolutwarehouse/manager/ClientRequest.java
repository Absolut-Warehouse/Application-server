package io.absolutwarehouse.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientRequest {
    private String RequestType;
    private Map<String, String> params = new HashMap<>();
    public ClientRequest(List<String> tokens){
        for (String token : tokens) {
            if (token.contains("=")) { //actuellement contient une faille si le message est mal formatté mais que un = est dans le string
                String[] parts = token.split("=", 2); //prend uniquement le premier
                String key = parts[0].trim();
                String value = parts.length > 1 ? parts[1].trim() : "";
                storeExpectedValues(key, value);
            }
            //else throw erreur de formattage
        }
        if(!params.containsKey("Type")){
            RequestType=params.get("Type");
        }
        else{
            //throw exception
        }

    }
    private void storeExpectedValues(String key, String value) { //throw des exceptions a catch dans ClientManager
        switch (key) {
            case "Action":
                params.put(key, value);
                break;
        
            case "Package":
                params.put(key, value);
                break;
        
            case "": //reste a remplir
                params.put(key, value);
                break;

            default:
                params.put(key, value);
                break;
        }
        
    }

    private void handleValuesForAction(){ //aura un switch case/une methode différent pour chaque type d'action

    }
    public String getType() {
        return this.RequestType;
    }
    public String getParam(String key) {
        return params.get(key);
    }
}
