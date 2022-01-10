package Service;

import models.Audio;
import models.MyImage;
import repositories.CompositeRepository;
import repositories.Repository;
import repositories.RepositoryFactory;

import java.util.ArrayList;


public class GameService {
    private static final CompositeRepository<MyImage> myImages= RepositoryFactory.images();
    private static final CompositeRepository<Audio> myAudios= RepositoryFactory.audios();

    public GameService() {
    }

    public ArrayList<MyImage> randomList(int round){
        System.out.println("Choix aleatoire des fichiers");
        ArrayList<Integer> id = new ArrayList<>();
        ArrayList<MyImage> selectedImages=new ArrayList<>();
        int size= myImages.count();
        int i=0;
        while(i < round){
            int e = (int)(Math.random()*size);
            if(! id.contains(e)){
                id.add(e);
                selectedImages.add(myImages.get(e));
                i++;
            }
        }
        return selectedImages;
    }

    public ArrayList<Audio> randomListaudio(int round){
        System.out.println("Choix aleatoire des fichiers");
        ArrayList<Integer> id = new ArrayList<>();
        ArrayList<Audio> selectedAudios=new ArrayList<>();
        int size = myAudios.count();
        int i=0;
        while(i < round){
            int e = (int)(Math.random()*size);
            System.out.println(e);
            if(!id.contains(e)){
                id.add(e);
                selectedAudios.add(myAudios.get(e));
                i++;
            }
        }
        return selectedAudios;
    }
}

