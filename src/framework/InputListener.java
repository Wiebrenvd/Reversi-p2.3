package framework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class InputListener implements Runnable {
    private Socket socket;
    private InputStreamReader isr;
    private BufferedReader br;

    private String inputFromServer = "";
    private String lastResponse = "";

    public InputListener(Socket s){
        this.socket = s;
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
                lastResponse = inputFromServer;
                inputFromServer = "";
            }
        }
    }

    public String getLastResponse() {
        return lastResponse;
    }
}
