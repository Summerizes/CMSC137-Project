import java.io.*;
import java.net.*;
import java.util.*;

public class Server extends Thread{
    int i, j;
    Random rand;
    int port;
    int numOfPlayers;
    String[] cards;
    boolean someoneFinished;
    int joinID;
    int finishNumber;
    List<Integer> indices;
    Set<String> memberNames = new HashSet<>();
    Set<Member> members = new HashSet<>();
 
    public Server(int port, int numOfPlayers){
        this.rand = new Random();
        this.port = port;
        this.numOfPlayers = numOfPlayers;
        this.cards = new String[this.numOfPlayers * 4];
        this.indices = new ArrayList<Integer>();
        this.someoneFinished = false;
        this.joinID = 1;
        this.finishNumber = 0;

        for(i = 0; i < numOfPlayers; i++){
            String card = Integer.toString(i + 1);
            for(j = 0; j < 4; j++){
                if(j == 0){
                    card = card + "S";
                }
                else if(j == 1){
                    card = card + "C";
                }
                else if(j == 2){
                    card = card + "H";
                }
                else{
                    card = card + "D";
                }

                cards[i * 4 + j] = card;
                card = Integer.toString(i + 1);
            }
        }
    }
 
    public void run(){
        try(ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Server on port " + port);

            // for(i = 0; i < 4 * this.numOfPlayers; i++){
            //     System.out.println(this.cards[i]);
            // }

            while(members.size() < numOfPlayers){
                Socket socket = serverSocket.accept();

                Member member = new Member(socket, this);
                member.joinID = this.joinID++;
                this.giveCards(member);
                members.add(member);
                member.start();
                // System.out.println(memberNames.size());
            }

            while(memberNames.size() < numOfPlayers){
                Thread.sleep(100);
            }
            this.broadcastConnected();
            this.members.forEach(x -> {
                x.write.println("Your cards:");
                for(i = 0; i < 4; i++){
                    x.write.print(x.cards[i] + " ");
                }
                x.write.println();
            });

            this.members.forEach(x -> {
                x.write.println("Members and ID's:");
                // for(i = 0; i < numOfPlayers; i++){
                //     x.write.print(memberNames.toArray()[i] + "-0" + Integer.toString(i + 1) + " ");
                // }

                for(i = 0; i < numOfPlayers; i++){
                    for(Member y : this.members){
                        if(y.joinID == i + 1){
                            x.write.print(y.memberName + "-0" + y.joinID + " ");
                        }
                    }
                }
                x.write.println();
            });

            this.broadcastToAll("Game starting in...");
            for(i = 3; i > 0; i--){
                this.broadcastToAll(Integer.toString(i) + "...");
                Thread.sleep(1000);
            }
            while(true){
                this.broadcastToAll("Enter pass code: ");

                while(true){
                    if(this.haveAllMembersPassed() == true){
                        break;
                    }
                    Thread.sleep(100);
                }

                if(this.someoneFinished == true){
                    this.conclude();
                    this.broadcastToAll("Thank you for playing.");
                    break;
                }
                else{
                    this.broadcastToAll("All players have passed cards");

                    this.rotateCards();

                    this.members.forEach(x -> {
                        x.write.println("Your cards:");
                        for(i = 0; i < 4; i++){
                            x.write.print(x.cards[i] + " ");
                        }
                        x.write.println();
                    });
                }

            }
        }catch(Exception e){}
    }
 
    public static void main(String[] args){
        try{
            int port = Integer.parseInt(args[0]);
            int numOfPlayers = Integer.parseInt(args[1]);
 
            Server server = new Server(port, numOfPlayers);
            server.start();
        }catch(Exception e){
            System.out.println("Usage: java Server <port-no.> <num. of players>\n"+"Make sure to use valid ports (greater than 1023)");
        }
    }

    void removeMember(String memberName, Member member){
        boolean isRemoved = memberNames.remove(memberName);
        if(isRemoved == true){
            members.remove(member);
            System.out.println(memberName + " has left the conversation.");
        }
    }

    void broadcast(String message, Member broadcaster){
        members.forEach(x -> {
            if (x != broadcaster) {
                x.write.println(message);
            }
        });
    }

    void broadcastToAll(String message){
        members.forEach(x -> {
            x.write.println(message);
        });
    }

    void broadcastConnected(){
        members.forEach(x -> {
            x.write.println("Connected players: " + memberNames);
        });
    }

    void newUserConnected(){
        try{
            System.out.println("User has connected.\nConnected users: " + memberNames);            
        }
        catch(Exception e){}
    }

    void giveCards(Member member){
        int index;

        for(i = 0; i < 4; i++){
            do{
                index = this.rand.nextInt(4 * numOfPlayers);
            }while(this.indices.contains(index));

            String card = this.cards[index];            
            member.cards[i] = card;

            this.indices.add(index);
        }
    }

    boolean haveAllMembersPassed(){
        boolean returnVal = true;

        // members.forEach(x -> {
        //     if(x.passed == false){
        //         returnVal = false;
        //     }
        // });

        for(Member x : this.members){
            if(x.passed == false){
                returnVal = false;
            }
        }

        return returnVal;
    }

    void conclude(){
        // int i = 0, winnerIndex = 0, loserIndex = 0;

        // this.broadcastToAll("Winner's cards: ");
        for(Member x : this.members){
            if(x.finishNumber == 0){
                // winnerIndex = i;
                this.broadcastToAll("Winner: " + x.memberName);
                this.members.forEach(y -> {
                    y.write.println("Winning card combination:");
                    for(i = 0; i < 4; i++){
                        y.write.print(x.cards[i] + " ");
                    }
                    y.write.println();
                });
            }
            else if(x.finishNumber == this.numOfPlayers - 1){
                // loserIndex = i;
                this.broadcastToAll("Loser: " + x.memberName);
            }
            // i++;
        }

        // this.broadcastToAll("Winner: " + this.memberNames.toArray()[winnerIndex]);
        // this.broadcastToAll("Loser: " + this.memberNames.toArray()[loserIndex]);
    }

    void rotateCards(){
        // Set<String> passMessages = new HashSet<>();
        // Set<String[]> userSetOfCards = new HashSet<>();

        String[] passMessages = new String[numOfPlayers];
        String[][] userSetOfCards = new String[numOfPlayers][4];
        int c, d;

        // c = 0;
        // for(Member x : this.members){
        //     passMessages[c] = x.clientMessage;
        //     for(d = 0; d < 4; d++){
        //         userSetOfCards[c][d] = x.cards[d];
        //     }

        //     c++;
        // }

        for(c = 0; c < numOfPlayers; c++){
            for(Member x : this.members){
                if(x.joinID == c + 1){
                    passMessages[c] = x.clientMessage;
                    for(d = 0; d < 4; d++){
                        userSetOfCards[c][d] = x.cards[d];
                    }
                }
            }
        }

        for(c = 0; c < numOfPlayers; c++){
            String cardToReplace = passMessages[c].substring(4, 6);
            String replacementCard;
            // int index;

            if(c == 0){
                replacementCard = passMessages[numOfPlayers - 1].substring(4, 6);
            }
            else{
                replacementCard = passMessages[c - 1].substring(4, 6);
            }

            for(d = 0; d < 4; d++){
                if(userSetOfCards[c][d].equals(cardToReplace)){
                    userSetOfCards[c][d] = replacementCard;
                }
            }
        }

        // c = 0;
        // for(Member x : this.members){
        //     for(d = 0; d < 4; d++){
        //         x.cards[d] = userSetOfCards[c][d];
        //     }

        //     c++;
        // }

        for(c = 0; c < numOfPlayers; c++){
            for(Member x : this.members){
                if(x.joinID == c + 1){
                    for(d = 0; d < 4; d++){
                        x.cards[d] = userSetOfCards[c][d];
                    }
                    x.passed = false;
                }
            }
        }

        // for(Member x : this.members){
        //     x.passed = false;
        // }
    }
}