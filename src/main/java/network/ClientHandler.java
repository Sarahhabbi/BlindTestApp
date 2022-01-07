package network;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    private static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;


    private BufferedReader bufferedReader; //read message
    private PrintWriter writer; //broadcast(write) messages to other clients
    private String clientUsername;
    private String joinedChannel;

    //private static UserService userService = new UserService();

    public ClientHandler(Socket socket) {
        try{
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);

            this.clientUsername = bufferedReader.readLine();  // username is sent in sendMessage() method in Client class
            this.clientHandlers.add(this);
            this.joinedChannel = null;

            System.out.println("SERVER: " + clientUsername + " is connected to the server !");
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, writer);
            e.printStackTrace();
        }
    }
    public String getJoinedChannel() {
        return joinedChannel;
    }
    public void setJoinedChannel(String joinedChannel) {
        this.joinedChannel = joinedChannel;
    }

//    everything in this method will be run in a separate thread, we will listen to new messages (blocking operation)
    @Override
    public void run() {
        /* handle client command & send messages*/
        try {
            /*String[] command;
            command = bufferedReader.readLine().split(" ");
            if(command.length >= 2 && command[0].equals("/")){
                handleClientChoice(command[1], command[2]);
                System.out.println(" première commande");
            }*/
            sendMessage();
        } catch (Exception e) {
            closeEverything(socket, bufferedReader, writer);
            e.printStackTrace();
        }

        this.sendMessage();
        closeEverything(socket, bufferedReader, writer);
    }

    public void handleClientChoice(String feature, String arg) throws Exception {
        switch(feature){

            /*case "signUp":
                System.out.println(clientUsername + " want to signUp");
                this.signUp(arg);
                break;

            case "logIn":
                System.out.println(clientUsername + " want to logIn");
                this.logIn(arg);
                break;*/

            case "create":
                System.out.println(clientUsername + " want to create");

//              TODO: channelService.createChannel(arg, username);
                System.out.println("juste pour que Intellij arrete de souler");
                setJoinedChannel(arg);
                broadcastMessage(clientUsername+ " has entered "+ joinedChannel, joinedChannel);
                break;

            case "join":
                setJoinedChannel(arg);
                broadcastMessage( clientUsername+ " has entered "+ joinedChannel, joinedChannel);
                break;

            case "delete":
//              TODO channelService.deleteChannel(arg);
                break;

            case "exit":
                closeEverything(socket, bufferedReader, writer);
                break;
            default:
                break;
        }
    }

    /*public void signUp(String password) {
        try {
            userService.signUp(clientUsername, password);
        } catch (Exception e) {
            writer.println(e.getMessage());
            e.printStackTrace();
        }
    }*/
    /*public void logIn(String password) {
        try {
            userService.connect(clientUsername, password);
        } catch (Exception e) {
            writer.println(e.getMessage());
            e.printStackTrace();
        }
    }*/

   /* public void handleCommand(String[] command){
        try {
            handleClientChoice(command[1], command[2]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    public void sendMessage(){
        String messageFromClient = null;

        do{
            try{
                messageFromClient = bufferedReader.readLine(); // blocking operation but here it's in another thread so no problem
                String[] words = messageFromClient.split(" ");

                if(words[0].equals("/")){
                    handleClientChoice(words[1],words[2] );
                    System.out.println("handling client choice");
                }else if(joinedChannel!=null){
                    broadcastMessage(messageFromClient, joinedChannel);
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while(messageFromClient!=null);  // ICI
    }

    public void broadcastMessage(String messageToSend, String channel) {
        System.out.println(">>> broadcastMessage() "+ messageToSend);

        /* iterate over each client and write in every output stream the message except to the client who sent */
        for(ClientHandler clientHandler : clientHandlers){
            try{
                boolean sameChannel = clientHandler.joinedChannel.equals(this.joinedChannel);
                boolean sameUser = clientHandler.clientUsername.equals(this.clientUsername);
                if(!sameUser && sameChannel){
                    clientHandler.writer.println(messageToSend);
                }
            } catch (Exception e) {
                closeEverything(socket, bufferedReader, writer);
                e.printStackTrace();
            }
        }

    }

    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadcastMessage("SERVER: "+ clientUsername+ " has left "+ joinedChannel, joinedChannel);
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, PrintWriter writer) {
        try{
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(writer != null){
                writer.close();
            }
            if(socket != null){
                socket.close();
            }
            removeClientHandler();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
