package multiplayer.network;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) throws IOException {
        System.out.println("Creating a server on port "+ serverSocket.getInetAddress().getHostAddress());
        this.serverSocket = serverSocket;
    }

    public void closeServerSocket() {
        try{
            if(serverSocket != null){
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startServer() {
        try{
            while(!serverSocket.isClosed()) {
                System.out.println("Waiting a connection ...");
                Socket socket = serverSocket.accept();  //blocking operation

                System.out.println("A new client is connected from "+ socket.getInetAddress().getHostAddress());

                ClientHandler clientHandler = new ClientHandler(socket);

                // create a new thread object
                //Thread thread = new Thread(clientHandler);
                //System.out.println("A new thread was assigned to this client");
            }
        }catch(IOException e){
            this.closeServerSocket();
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server;

        try (ServerSocket serverSocket = new ServerSocket(3000)) {
            server = new Server(serverSocket);
            server.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}