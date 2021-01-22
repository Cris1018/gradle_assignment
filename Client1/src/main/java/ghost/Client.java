package ghost;

import java.io.*;
import java.net.*;

public class Client {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
 
    public void startConnection(String ip, int port) {
        try{
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);            
        } catch (Exception e){

        }

    }

    public String receive(){
        try{
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            return in.readLine();
        } catch (Exception e){
            return null;
        }
    }
 
    public void sendMessage(String msg) {
        out.println(msg);
    }
}