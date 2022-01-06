package Service;

import models.MyImage;
import repositories.Repository;
import repositories.RepositoryFactory;

import java.util.ArrayList;


public class GameService {
    private static final Repository<MyImage> myImages= RepositoryFactory.images();

    public GameService() {
    }

    public ArrayList<MyImage> randomList(){
        ArrayList<Integer> id = new ArrayList<>();
        ArrayList<MyImage> images = new ArrayList<>();
        int size = myImages.count();    // nb element in DB
        int i = 0;
        while(i < 10){
            int e = (int)(Math.random()*size);
            if(!id.contains(e)){
                id.add(e);
                MyImage image = myImages.find(String.valueOf(e));
                if(image != null){
                    images.add(myImages.find(String.valueOf(e)));
                    System.out.println("Added " + myImages.find(String.valueOf(e)).getAnswer()+ "to random list");
                }
            }
            i++;
        }
        return images;
    }
}

