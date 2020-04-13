package Framework.server;

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
    private ArrayList<ServerMessage> serverResponses;

    public InputListener(Socket s){
        this.socket = s;
        this.serverResponses = new ArrayList<ServerMessage>();

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
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                System.out.println("Failed to read from server...");
            }
            if (inputFromServer.length() != 0){
                System.out.println(inputFromServer);
                ServerMessage sMessage = new ServerMessage(inputFromServer, System.currentTimeMillis());

                serverResponses.add(sMessage);
            }
        }
    }

    public ArrayList<ServerMessage> getServerResponses(){
        return serverResponses;
    }

    public ServerMessage getLastResponse() {
        return serverResponses.get(serverResponses.size()-1);
    }
}

