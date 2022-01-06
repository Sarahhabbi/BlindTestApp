package models;

import database.Database;
import javafx.scene.image.Image;
import repositories.MyImageRepository;
import repositories.RepositoryFactory;
import java.util.concurrent.atomic.AtomicLong;

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