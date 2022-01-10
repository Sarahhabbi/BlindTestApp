package models;

import database.Database;
import javafx.scene.image.Image;

import java.io.Serializable;

public class MyImage implements HasId, Serializable{
    private String answer;
    private String url;

    public MyImage(String url, String answer) {
        this.url=url;
        this.answer = answer;
    }


    public String getId(){
        return this.url;
    }

    public String getAnswer() {
        return answer;
    }
}