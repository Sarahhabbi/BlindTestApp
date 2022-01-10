package repositories;

import models.HasId;

import java.util.ArrayList;

public interface Repository<T> {

    T save(T obj);

    void delete(T obj) throws Exception;

    ArrayList<T> findAll();

    T find(String id);

    int count();

}
