package sample;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class OutputListener{
    private Socket socket;
    private PrintWriter pw;

    private String command = "";

    public OutputListener(Socket s) {
        this.socket = s;
        try {
            pw = new PrintWriter(socket.getOutputStream());
        } catch (IOException e){
            System.out.println("failed to connect to Server");
        }
    }



    public void sendCommand(String str) {
        command = str;
        pw.println(command);
        pw.flush();
        command = "";
    }
}
