package models;

import database.Database;
import javafx.scene.image.Image;

public class MyImage extends Image implements HasId{
    private String answer;

    public MyImage(String url, String answer) {
        super(url);
        this.answer = answer;
    }


    @Override
    public String getId(){
        return getUrl();
    }

    public String getAnswer() {
        return answer;
    }
}