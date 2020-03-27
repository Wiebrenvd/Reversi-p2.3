package sample;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServerConnection{

    private int port = 7789;
    private String host = "localhost";
    private Socket socket;

    private String loginName = "";

    private OutputListener outputListener;
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

                outputListener = new OutputListener(socket);
            }
        } catch (IOException e) {
            System.out.println("Connection Failed.");
            e.printStackTrace();
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
        outputListener.sendCommand(str);
    }

    public String showLastResponse(){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e){
            System.out.println("Fail to sleep");
        }
        System.out.println(inputListener.getLastResponse());
        return inputListener.getLastResponse();
    }

    public String[] getDicOrArr(String response){
        String tmp = response.split(" ",3)[2];
        return tmp.substring(1,tmp.length()-1).split(", ");
    }

    public ArrayList<String> getLastResponses(){
        return inputListener.getServerResponses();
    }

    //https://stackoverflow.com/questions/26485964/how-to-convert-string-into-hashmap-in-java
    public Map<String,String> strToMap(String str){
        String tmp = str.substring(1, str.length()-1);           //remove curly brackets
        String[] keyValuePairs = tmp.split(",");              //split the string to creat key-value pairs
        Map<String,String> output = new HashMap<>();

        for(String pair : keyValuePairs)                        //iterate over the pairs
        {
            String[] entry = pair.split("=");                   //split the pairs to get key and value
            output.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
        }

        return output;
    }
}
