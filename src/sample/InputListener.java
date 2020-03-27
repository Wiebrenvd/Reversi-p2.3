package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class InputListener implements Runnable {
    private Socket socket;
    private InputStreamReader isr;
    private BufferedReader br;

    private String inputFromServer = "";
    private String lastResponse = "";
    private ArrayList<String> serverResponses;

    public InputListener(Socket s){
        this.socket = s;
        this.serverResponses = new ArrayList<>();

        try {
            isr = new InputStreamReader(socket.getInputStream());
            br = new BufferedReader(isr);
        } catch (IOException e){
            System.out.println("InputListener failed to create...");
        }
    }

    @Override
    public void run() {
        while (true){
            try {
                inputFromServer = br.readLine();
            } catch (IOException e) {
                System.out.println("Failed to read from server...");
            }
            if (inputFromServer.length() != 0){
                System.out.println(inputFromServer);
                serverResponses.add(inputFromServer);
                inputFromServer = "";
            }
        }
    }

    public ArrayList<String> getServerResponses(){
        return serverResponses;
    }

    public String getLastResponse() {
        return serverResponses.get(serverResponses.size()-1);
    }
}
