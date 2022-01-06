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
        System.out.println("Choix aleatoire des fichiers");
        ArrayList<Integer> id = new ArrayList<>();
        ArrayList<MyImage> images=myImages.findAll();
        ArrayList<MyImage> selectedImages=new ArrayList<>();
        int size= myImages.count();
        int i=0;
        while(i < 1){
            int e = (int)(Math.random()*size);
            if(! id.contains(e)){
                id.add(e);
                selectedImages.add(images.get(e));
                i++;
            }
        }
        return images;
    }
}

