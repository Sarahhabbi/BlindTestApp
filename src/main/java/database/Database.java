package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static Database instance;

    private final String url;
    private final String user;
    private final String password;
    private Connection connection;

    private Database(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.initConnection();
    }

    public static Database getInstance(String url, String user, String password){
        if (Database.instance == null) {
            Database.instance = new Database(url, user, password);
        }
        return instance;
    }

    public Connection getConnection() {
        return this.connection;
    }

    private void initConnection() {
        try {
            this.connection = DriverManager.getConnection(this.url, this.user, this.password);
            System.out.println("Connection established !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
