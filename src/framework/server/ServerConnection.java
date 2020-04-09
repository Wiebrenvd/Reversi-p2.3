package framework.server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServerConnection{

    private int port = 7789;
    private String host = "localhost";
    private Socket socket;

    private String loginName = "";

    private OutputSender outputSender;
    private InputListener inputListener;        //InputListener is een Runnable


    public boolean startConnection(String ip_host, int prt){
        this.host = ip_host;
        this.port = prt;

        try {
            socket = new Socket(host, port);
            if (socket != null) {
                inputListener = new InputListener(socket);
                Thread responseThrd = new Thread(inputListener);
                responseThrd.start();

                outputSender = new OutputSender(socket);
            }
        } catch (IOException e) {
            System.out.println("Connection Failed.");
//            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginName(){
        return this.loginName;
    }

    public void sendCommand(String str){
        System.out.println("Output: " + str);
        outputSender.sendCommand(str);
    }

    /**
     * @return the last response from the server as a String with a 500ms delay
     **/
    public String showLastResponse(){
        try {
            Thread.sleep(500);
        } catch (InterruptedException e){
            System.out.println("Fail to sleep");
        }
        return inputListener.getLastResponse();
    }

    /**
     * Converts a response(String) into an Array
     * @param response This String will be converted into an Array(Example: SVR GAMELIST ["Reversi", "Tic-tac-toe"])
     * @return The array that is found.
     **/
    public String[] getArr(String response){
        System.out.println("Response: " + response);
        String tmp = response.split(" ",3)[2];
        return tmp.substring(1,tmp.length()-1).split(", ");
    }

    /**
     * @return An Arraylist of all responses from the Server with 500ms delay
     **/
    public ArrayList<String> getLastResponses(){
        try {
            Thread.sleep(500);
        } catch (InterruptedException e){
            System.out.println("Fail to sleep");
        }
        return inputListener.getServerResponses();
    }

    // https://stackoverflow.com/questions/26485964/how-to-convert-string-into-hashmap-in-java
    public Map<String,String> getMap(String response){
        String str = response.split(" ",4)[3];
        String tmp = str.substring(1, str.length()-1);           //remove curly brackets
        String[] keyValuePairs = tmp.split(",");              //split the string to creat key-value pairs
        Map<String,String> output = new HashMap<>();

        for(String pair : keyValuePairs)                        //iterate over the pairs
        {
            String[] entry = pair.split(": ");                   //split the pairs to get key and value
            output.put(entry[0].trim(), entry[1].trim().substring(1,entry[1].length()-1));          //add them to the hashmap and trim whitespaces
        }

        return output;
    }

    /**
    * Looking for response if it match with the key(String) and returns response
     * @param key the String that match with a response
     * @return the String response if it has found a match with the key, otherwise null
    **/
    public String lastRespContains(String key){
        ArrayList<String> tmp = getLastResponses();
        if (tmp.size() >= 3) {
            if (tmp.get(tmp.size()-1).contains(key)) return tmp.get(tmp.size()-1);
            if (tmp.get(tmp.size()-2).contains(key)) return tmp.get(tmp.size()-2);
            if (tmp.get(tmp.size()-3).contains(key)) return tmp.get(tmp.size()-3);
        }
        else if (tmp.size() == 1 && tmp.get(0).contains(key)) {
            return tmp.get(0);
        } else if (tmp.size() == 2 && tmp.get(1).contains(key)) {
            return tmp.get(1);
        }
        return null;
    }
}