package ghost;

import java.io.*;
import java.net.*;

public class Server {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void start(int port){
        try{
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();
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