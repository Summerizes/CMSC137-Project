# 1 - 2 - 3 Pass Card Game Documentation
## Ma'am Dja Babies
## Members: Elaine Caluag, Earl Palapar, Nicolo Unson, Lance Viray

### Description of the Game:

A famous card game with an objective of getting a four of a kind. A player initially gets 4 cards assuming that no one still has a complete four of a kind, everybody will say 1-2-3 pass then will pass a card. This will run until someone met the objective. When someone finishes he/she will put his/her hand on the table, then everyone will follow. The last one to do so loses. 

### Programming Language & Import:

Java
Socket, serverSocket

### Github Repository:

https://github.com/Summerizes/CMSC137-Project

## I. Basic Game Layout:

1. Players will connect to the server. 

2. The server will wait until the players are enough.

3. Once the players are enought the server will automatically give cards to the players.

4. The server will ask for a code.

5. The players player will now then enter the code of the card.

6. This will continue until a player completes his/her chosen set of cards.

7. If a player completes his/her set of cards he/she will pass the word "finish"

8. The server will now signal that someone has finished completing a set of cards.

9. The server will ask all the remaining players to pass "hand".

10. The last player to pass hand will be the loser.

## II. Communication Protocol (TCP)

### Data Flow Diagram

![alt text](https://github.com/Summerizes/CMSC137-Project/blob/master/Data-Diagram.png "Data Flow Diagram")

### Structure of the Data

|       Names      |   Code  |
|:----------------:|:-------:|
| ID of the Player | 01 - 0N |
| Diamonds         | D       |
| Hearts           | H       |
| Spades           | S       |
| Clubs            | C       |
| Ace              | 1       |
| 2                | 2       |
| 3                | 3       |
| 4                | 4       |
| 5                | 5       |
| 6                | 6       |
| 7                | 7       |
| 8                | 8       |
| 9                | 9       |
| 10               | T       |
| Jack             | J       |
| Queen            | Q       |
| King             | K       |
| Finish           | finish  |
| Exit             | exit    |
