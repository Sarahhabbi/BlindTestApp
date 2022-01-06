package models;

import javafx.scene.image.Image;

public class MyImage extends Image{
    private final String goodAnswer;

    public MyImage(String url, String goodAnswer) {
        super(url);
        this.goodAnswer = goodAnswer;
    }

    public String getGoodAnswer() {
        return goodAnswer;
    }
}
