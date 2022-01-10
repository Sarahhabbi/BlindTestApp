package repositories;

import database.Database;
import models.Audio;
import models.MyImage;

public class RepositoryFactory {

    private static final Database DATABASE = Database.getInstance("jdbc:mysql://localhost:3306/blind_test", "root", "poudebs91");

    public static CompositeRepository<MyImage> images() {
        return new CompositeRepository<>(MyImageRepository.getInstance(DATABASE.getConnection()));
    }

    public static  CompositeRepository<Audio> audios() {
        return new CompositeRepository<>(AudioRepository.getInstance(DATABASE.getConnection()));
    }

}




