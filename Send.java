import java.io.*;
import java.net.*;

public class Send extends Thread{
    PrintWriter write;
    Socket socket;
    Client client;
 
    public Send(Socket socket, Client client){
        this.socket = socket;
        this.client = client;
 
        try{
            OutputStream out = socket.getOutputStream();
            write = new PrintWriter(out, true);
        }catch(Exception e){}
    }
 
    public void run(){
        Console console = System.console();
        System.out.println("**********************************");
        System.out.println("*           WELCOME TO           *");
        System.out.println("*         1-2-3 PASS GAME        *");
        System.out.println("**********************************");
        System.out.println("            TUTORIALS:            ");
        System.out.println("1. You will be given 4 cards. The Cards are labeled according to number and suite. Kindly refer to the documentation file for more details.");
        System.out.println("2. You will be asked to enter a code for the card you want to pass. The code is 2 digits, which is the label of the card itself.");
        System.out.println("3. After all have passed a card, you will be prompted with a new set of cards.");
        System.out.println("4. Once your card is all of the same number, you have to pass the word “finish”.");
        System.out.println("5. If another player has finished, you will be prompted and you have to pass the word “hand”, even if you already passed a card. The last player to do so, will be the loser.");
        System.out.println("6. To exit, simply pass the word “exit”.");

        String memberName = console.readLine("Enter your name: ");
        client.memberName = memberName;
        write.println(memberName);
 
        String message;
 
        do{
            message = console.readLine("");
            write.println(message);
 
        }while(!message.equals("exit"));
 
        try{
            socket.close();
        } catch (Exception e){}
    }
}