package models;

import javafx.scene.image.Image;
import repositories.RepositoryFactory;
import java.util.concurrent.atomic.AtomicLong;

public class MyImage extends Image implements HasId{

    private static final int nb = RepositoryFactory.images().count();
    private final AtomicLong ID_GENERATOR = new AtomicLong(nb);
    private final String id;

    private String answer;
    public MyImage(String url, String answer) {
        super(url);
        this.id = Long.toString(ID_GENERATOR.incrementAndGet()) ;
        this.answer = answer;
    }

    public MyImage(String id, String url, String answer) {
        super(url);
        this.id =Long.toString(ID_GENERATOR.incrementAndGet()) ;
        this.answer = answer;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public String getAnswer() {
        return answer;
    }
}