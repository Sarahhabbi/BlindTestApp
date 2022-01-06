package repositories;

import database.Database;
import models.MyImage;

public class RepositoryFactory {

    private static final Database DATABASE = Database.getInstance("jdbc:mysql://localhost:3306/blind_test", "root", "poudebs91");

    public static Repository<MyImage> images() {
        return new CompositeRepository<>(MyImageRepository.getInstance(DATABASE.getConnection()));
    }
    
}




