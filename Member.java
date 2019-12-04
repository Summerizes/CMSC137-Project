import java.io.*;
import java.net.*;
import java.util.*;

public class Member extends Thread{
    Socket socket;
    Server server;
    PrintWriter write;
    String[] cards;
    boolean passed;
    String clientMessage;
    int finishNumber;
    int joinID;
    String memberName;
 
    public Member(Socket socket, Server server){
        this.socket = socket;
        this.server = server;
        this.cards = new String[4];
        this.passed = false;
    }
 
    public void run(){
        try{
            InputStream in = socket.getInputStream();
            BufferedReader read = new BufferedReader(new InputStreamReader(in));
 
            OutputStream out = socket.getOutputStream();
            write = new PrintWriter(out, true);
  
            memberName = read.readLine();
            server.memberNames.add(memberName);
 
            String serverMessage = memberName + " has joined the conversation.";
            server.broadcast(serverMessage, this);
 
            do{
                clientMessage = read.readLine();
                // serverMessage = memberName + ": " + clientMessage;
                // server.broadcast(serverMessage, this);
                if(clientMessage != "exit"){
                    if(clientMessage.equals("finish")){
                        if(this.checkCards() == true){
                            this.finishNumber = this.server.finishNumber++;
                            // write.println("FN: " + finishNumber);
                            this.server.broadcast("Someone is finished! Type 'hand' to put hand in the table!", this);
                            this.server.someoneFinished = true;

                            for(Member x : this.server.members){
                                if(x != this){
                                    x.passed = false;
                                }
                            }
                            this.passed = true;
                        }else{
                            write.println("Error: Your cards are not matching");
                        }
                    }
                    else if(clientMessage.equals("hand")){
                        if(this.server.someoneFinished == true){
                            this.finishNumber = this.server.finishNumber++;
                            // write.println("FN: " + finishNumber);
                            this.passed = true;
                        }
                    }
                    else{
                        if(this.checkValidityMessage(clientMessage) == true){
                            this.passed = true;
                            write.println("You passed: " + clientMessage);
                        }else{
                            if(!clientMessage.equals("exit")){
                                write.println("Error: You do not have that card");
                            }
                        }
                    }
                }
 
            }while(!clientMessage.equals("exit"));
 
            server.removeMember(memberName, this);
            socket.close();
 
            serverMessage = memberName + " has left the conversation.";
            server.broadcast(serverMessage, this);
 
        }catch(Exception e){}
    }

    public boolean checkCards(){
        if(cards[0].charAt(0) == cards[1].charAt(0) && cards[1].charAt(0) == cards[2].charAt(0) && cards[2].charAt(0) == cards[3].charAt(0)){
            return true;
        }

        return false;
    }

    public boolean checkValidityMessage(String message){
        int i;

        if(message.length() == 6){
            String cardFromMessage = message.substring(4, 6);
            for(i = 0; i < 4; i++){
                if(cards[i].equals(cardFromMessage)){
                    break;
                }
            }
            if(i < 4){
                return true;
            }
        }

        return false;
    }
}