package Framework.server;

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
        outputSender.sendCommand(str);
    }

    /**
     * @return the last response from the server as a String with a 500ms delay
     **/
    public ServerMessage showLastResponse(){
        try {
            Thread.sleep(500);
        } catch (InterruptedException e){
            System.out.println("Fail to sleep");
        }
        return inputListener.getLastResponse();
    }

    /**
     * @return An Arraylist of all responses from the Server with 500ms delay
     **/
    public ArrayList<ServerMessage> getLastResponses(){
        try {
            Thread.sleep(500);
        } catch (InterruptedException e){
            System.out.println("Fail to sleep");
        }
        return inputListener.getServerResponses();
    }

    /**
    * Looking for response if it match with the key(MessageType) and returns response
     * @param mType the MessageType that match with a response
     * @return the String response if it has found a match with the key, otherwise null
    **/
    public ServerMessage lastRespContains(MessageType mType){
        return this.lastRespContains(mType.name());
    }


    /**
     * Looking for response if it match with the key(String) and returns response
     * @param key the String that match with a response
     * @return the ServerMessage response if it has found a match with the key, otherwise null
     **/
    public ServerMessage lastRespContains(String key){
        ArrayList<ServerMessage> sList = getLastResponses();

        for(int s = sList.size()-1; s >= sList.size()-2; s--){
            ServerMessage sRes = sList.get(s);

            if(sRes.getType() != null && sRes.getType().toString().contains(key))
                return sRes;
        }

        return null;
    }
}