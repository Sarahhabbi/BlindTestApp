package repositories;

import models.HasId;

import java.util.List;

public interface Repository<T extends HasId> {

    T save(T obj);

    void delete(T obj) throws Exception;

    List<T> findAll();

    T find(String id);

    int count();

}
