package Service;

import models.Audio;
import models.MyImage;
import repositories.Repository;
import repositories.RepositoryFactory;

import java.util.ArrayList;


public class GameService {
    private static final Repository<MyImage> myImages= RepositoryFactory.images();
    private static final Repository<Audio> myAudios= RepositoryFactory.audios();

    public GameService() {
    }

    public ArrayList<MyImage> randomList(){
        System.out.println("Choix aleatoire des fichiers");
        ArrayList<Integer> id = new ArrayList<>();
        ArrayList<MyImage> images=myImages.findAll();
        ArrayList<MyImage> selectedImages=new ArrayList<>();
        int size= myImages.count();
        int i=0;
        while(i < 5){
            int e = (int)(Math.random()*size);
            if(! id.contains(e)){
                id.add(e);
                selectedImages.add(images.get(e));
                i++;
            }
        }
        return selectedImages;
    }

    public ArrayList<Audio> randomListaudio(){
        System.out.println("Choix aleatoire des fichiers");
        ArrayList<Integer> id = new ArrayList<>();
        ArrayList<Audio> audios = myAudios.findAll();
        ArrayList<Audio> selectedAudios=new ArrayList<>();
        int size = myAudios.count();
        int i=0;
        while(i < 2){
            int e = (int)(Math.random()*size);
            if(!id.contains(e)){
                id.add(e);
                selectedAudios.add(audios.get(e));
                i++;
            }
        }
        return selectedAudios;
    }
}

