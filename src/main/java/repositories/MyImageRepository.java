package repositories;

import models.MyImage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class MyImageRepository implements Repository<MyImage> {
    private static MyImageRepository instance;
    private final Connection DBConnexion;

    private MyImageRepository(Connection DBConnexion){
        this.DBConnexion = DBConnexion;
    }

    public static MyImageRepository getInstance(Connection DBConnexion){
        if (MyImageRepository.instance == null) {
            MyImageRepository.instance = new MyImageRepository(DBConnexion);
        }
        return instance;
    }

    @Override
    public MyImage save(MyImage obj) {
        try (PreparedStatement ps = this.DBConnexion.prepareStatement("INSERT INTO images (id,answer) VALUES (?, ?)")) {

            ps.setString(1,obj.getId());
            ps.setString(2,obj.getAnswer());
            ps.executeUpdate();
            System.out.println(" successfully added to MyImage table !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public ArrayList<MyImage> findAll() {
        ArrayList<MyImage> images = new ArrayList<>();
        try{
            PreparedStatement ps = this.DBConnexion.prepareStatement("SELECT * FROM images");
            ResultSet generatedKeys=ps.executeQuery();
            while(generatedKeys.next()){
                images.add(new MyImage(generatedKeys.getString(1),generatedKeys.getString(2)));
                System.out.println("data "+generatedKeys.getString(1)+" "+generatedKeys.getString(2));
            }
            generatedKeys.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return images;
    }


    @Override
    public void delete(MyImage obj){

        try {
            PreparedStatement ps = this.DBConnexion.prepareStatement("DELETE FROM images WHERE id=?");
            ps.setString(1, obj.getId());
            ps.executeUpdate();
            System.out.println(" successfully deleted to CHANNEL_USERS table !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public MyImage find(String id){
        try {
            PreparedStatement ps = this.DBConnexion.prepareStatement("SELECT * FROM images WHERE id=? ");
            ps.setString(1, id);

            ResultSet res=ps.executeQuery();
            if(res.next()){
                MyImage u=new MyImage(res.getString(1),res.getString(2));
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
            PreparedStatement ps = this.DBConnexion.prepareStatement("SELECT Count(*) FROM images");
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
    public ArrayList<MyImage> saveAll(ArrayList<MyImage> MyImages) {
        for(MyImage element:MyImages){
            save(element);
        }
        return null;
    }


    public void deleteAll() {
        try {
            PreparedStatement ps = this.DBConnexion.prepareStatement("DELETE * FROM images");
            ps.executeUpdate();
            System.out.println(" successfully deleted to CHANNEL_USERS table !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}