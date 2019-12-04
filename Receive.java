import java.io.*;
import java.net.*;

public class Receive extends Thread{
    BufferedReader read;
    Socket socket;
    Client client;
 
    public Receive(Socket socket, Client client){
        this.socket = socket;
        this.client = client;
 
        try{
            InputStream in = socket.getInputStream();
            read = new BufferedReader(new InputStreamReader(in));
        }catch(Exception e){}
    }
 
    public void run(){
        while(true){
            try{
                String reply = read.readLine();
                System.out.println("\n" + reply);
 
                if(client.memberName != null){
                    System.out.print(client.memberName + ": ");
                }
            }catch(Exception e){
                break;
            }
        }
    }
}