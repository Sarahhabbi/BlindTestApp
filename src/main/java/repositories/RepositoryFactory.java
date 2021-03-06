package repositories;

import database.Database;
import models.MyImage;
import models.Audio;

public class RepositoryFactory {

    private static final Database DATABASE = Database.getInstance("jdbc:mysql://localhost:3306/blind_test", "root", "Siyani=17");

    public static CompositeRepository<MyImage> images() {
        return new CompositeRepository<>(MyImageRepository.getInstance(DATABASE.getConnection()));
    }

    public static  CompositeRepository<Audio> audios() {
        return new CompositeRepository<>(AudioRepository.getInstance(DATABASE.getConnection()));
    }
    
}




