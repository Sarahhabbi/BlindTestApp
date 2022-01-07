package repositories;

import models.Audio;

import java.sql.*;
import java.util.ArrayList;


public class AudioRepository implements Repository<Audio> {
    private static AudioRepository instance;
    private final Connection DBConnexion;

    private AudioRepository(Connection DBConnexion){
        this.DBConnexion = DBConnexion;
    }

    public static AudioRepository getInstance(Connection DBConnexion){
        if (AudioRepository.instance == null) {
            AudioRepository.instance = new AudioRepository(DBConnexion);
        }
        return instance;
    }

    @Override
    public Audio save(Audio obj) {
        try (PreparedStatement ps = this.DBConnexion.prepareStatement("INSERT INTO audios (id,answer) VALUES (?, ?)")) {

            ps.setString(1,obj.getId());
            ps.setString(2,obj.getAnswer());
            ps.executeUpdate();

            System.out.println(" successfully added to Audio table !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public ArrayList<Audio> findAll() {
        ArrayList<Audio> audios = new ArrayList<>();
        try{
            PreparedStatement ps = this.DBConnexion.prepareStatement("SELECT * FROM audios");
            ResultSet generatedKeys = ps.executeQuery();

            while(generatedKeys.next()){
                audios.add(new Audio(generatedKeys.getString(1),generatedKeys.getString(2)));
                System.out.println("data "+generatedKeys.getString(1)+" "+generatedKeys.getString(2));
            }
            generatedKeys.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return audios;
    }


    @Override
    public void delete(Audio obj){

        try {
            PreparedStatement ps = this.DBConnexion.prepareStatement("DELETE FROM audios WHERE id=?");
            ps.setString(1, obj.getId());
            ps.executeUpdate();
            System.out.println(" successfully deleted to CHANNEL_USERS table !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Audio find(String id){
        try {
            PreparedStatement ps = this.DBConnexion.prepareStatement("SELECT * FROM audios WHERE id=? ");
            ps.setString(1, id);

            ResultSet res=ps.executeQuery();
            if(res.next()){
                Audio u=new Audio(res.getString(1),res.getString(2));
                System.out.println();
                res.close();
                return u;
            }
            res.close();
            System.out.println("There is no user with this name");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int count (){
        try {
            PreparedStatement ps = this.DBConnexion.prepareStatement("SELECT Count(*) FROM audios");
            ResultSet res=ps.executeQuery();
            if(res.next()){
                //System.out.println("DEBUG COUNT = " + res.getInt(1));
                return res.getInt(1);
            }
            res.close();
            System.out.println("There is no user with this name");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public ArrayList<Audio> saveAll(ArrayList<Audio> Audios) {
        for(Audio element:Audios){
            save(element);
        }
        return null;
    }


    public void deleteAll() {
        try {
            PreparedStatement ps = this.DBConnexion.prepareStatement("DELETE * FROM audios");
            ps.executeUpdate();
            System.out.println(" successfully deleted to CHANNEL_USERS table !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}