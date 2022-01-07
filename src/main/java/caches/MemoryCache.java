package caches;

import models.HasId;
import repositories.Repository;

import java.util.*;

public class MemoryCache<T extends HasId> implements Repository<T> {

    //public final LinkedHashMap<String, T> cache = new LinkedHashMap<>();
    public final ArrayList<T> liste=new ArrayList<>();
    @Override
    public T save(T obj) {
        //cache.put(obj.getId(), obj);
        liste.add(obj);
        return obj;
    }


    /*public Map<String, T> getCache() {
        return cache;
    }*/

    @Override
    public void delete(T obj) {
        liste.remove(obj);
        //cache.remove(obj.getId());
    }

    @Override
    public ArrayList<T> findAll() {
        return liste;
    }

    @Override
    public T find(String id) {
        return null;
        // return cache.get(id);
    }

    @Override
    public int count() {
        return liste.size();
    }

    public T get(int i){
        return liste.get(i);
    }
}
