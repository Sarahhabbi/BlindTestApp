package multiplayer.network;

import models.Audio;
import models.MyImage;
import models.PlayerAnswer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ComSocket {
    private final Socket socket;
    private final ObjectOutputStream oos ;
    private final ObjectInputStream ois;

    public ComSocket(Socket socket) throws IOException {
        System.out.println("Creation");
        this.socket = socket;
        System.out.println("la socket est chargée");
        this.oos=new ObjectOutputStream(socket.getOutputStream());
        System.out.println("l'output  est chargé");
        this.ois=new ObjectInputStream(socket.getInputStream());
        System.out.println("l'input' est chargé");
    }

    public void write(String s) throws IOException {
        this.oos.writeObject(s);
        this.oos.flush();
    }

    public void writePlayerAnswer(PlayerAnswer p) throws IOException {
        this.oos.writeObject(p);
        this.oos.flush();
    }

    public void writeImage(MyImage i) throws IOException {
        this.oos.writeObject(i);
        this.oos.flush();
    }

    public void writeAudio(Audio a) throws IOException {
        this.oos.writeObject(a);
        this.oos.flush();
    }

    public String read() throws IOException, ClassNotFoundException {
        return (String) this.ois.readObject();
    }

    public PlayerAnswer readPlayerAnswer() throws IOException, ClassNotFoundException {
        return (PlayerAnswer) this.ois.readObject();
    }

    public MyImage readMyImage() throws IOException, ClassNotFoundException {
        return (MyImage) this.ois.readObject();
    }

    public Audio readAudio() throws IOException, ClassNotFoundException {
        return (Audio) this.ois.readObject();
    }

    public void closeEverything() {
        try{
            if(ois != null){ois.close();}
            if(oos != null){oos.close();}
            if(socket != null){socket.close();}
        } catch (Exception e) {e.printStackTrace();}

    }

    public boolean isConnected(){
        return socket.isConnected();
    }


    public Socket getSocket() {
        return socket;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public ObjectInputStream getOis() {
        return ois;
    }
}
