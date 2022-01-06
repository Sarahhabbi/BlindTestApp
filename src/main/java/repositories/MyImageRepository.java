package repositories;

import models.MyImage;

import java.sql.*;
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
        String req = "INSERT INTO MyImage (image_path,answer) VALUES (?, ?)";
        try (PreparedStatement ps = this.DBConnexion.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1,obj.getUrl());
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
        String req="SELECT * FROM MyImage";
        ArrayList<MyImage> channels = new ArrayList<>();
        try{
            PreparedStatement ps = this.DBConnexion.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);
            ResultSet generatedKeys=ps.executeQuery(req);
            while(generatedKeys.next()){
                channels.add(new MyImage(generatedKeys.getString(1),generatedKeys.getString(2),generatedKeys.getString(2)));
                System.out.println(generatedKeys.getString(1)+ " "+generatedKeys.getString(2)+" "+generatedKeys.getString(3));
            }
            generatedKeys.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return channels;
    }


    @Override
    public void delete(MyImage obj){

        try {
            PreparedStatement ps = this.DBConnexion.prepareStatement("DELETE FROM MyImage WHERE image_path=?");
            ps.setString(1, obj.getUrl());
            ps.executeUpdate();
            System.out.println(" successfully deleted to CHANNEL_USERS table !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public MyImage find(String id){
        try {
            PreparedStatement ps = this.DBConnexion.prepareStatement("SELECT * FROM MyImage WHERE id=? ");
            ps.setString(1, id);

            ResultSet res=ps.executeQuery();
            if(res.next()){
                MyImage u=new MyImage(id,res.getString(2),res.getString(3));
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
            PreparedStatement ps = this.DBConnexion.prepareStatement("SELECT Count(*) FROM MyImage");
            ResultSet res=ps.executeQuery();
            if(res.next()){
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
            PreparedStatement ps = this.DBConnexion.prepareStatement("DELETE * FROM MyImage");
            ps.executeUpdate();
            System.out.println(" successfully deleted to CHANNEL_USERS table !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}