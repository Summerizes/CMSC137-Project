import java.net.*;
import java.io.*;

public class Client extends Thread{
    String ipAddr;
    int port;
    String memberName;
 
    public Client(String ipAddr, int port){
        this.ipAddr = ipAddr;
        this.port = port;
    }
 
    public void run(){
        try{
            Socket socket = new Socket(ipAddr, port);
  
            new Receive(socket, this).start();
            new Send(socket, this).start();
 
        }catch(Exception e){}
    }
  
    public static void main(String[] args){
        try{
            String ipAddr = args[0];
            int port = Integer.parseInt(args[1]);
     
            Client client = new Client(ipAddr, port);
            client.start();
        }catch(Exception e){
            System.out.println("Usage: java Client <server-ip> <port-no.>");
        }
    }
}