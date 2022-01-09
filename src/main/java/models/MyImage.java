package models;

import database.Database;
import javafx.scene.image.Image;

public class MyImage implements HasId{
    private String answer;
    private String url;
    public MyImage(String url, String answer) {
        this.url=url;
        this.answer = answer;
    }


    @Override
    public String getId(){
        return this.url;
    }

    public String getAnswer() {
        return answer;
    }
}