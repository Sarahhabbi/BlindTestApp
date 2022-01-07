package caches;

import models.HasId;
import repositories.Repository;

import java.util.*;

public class MemoryCache<T extends HasId> implements Repository<T> {

    public final LinkedHashMap<String, T> cache = new LinkedHashMap<>();

    @Override
    public T save(T obj) {
        cache.put(obj.getId(), obj);
        System.out.println("after");
        List<T> c=new ArrayList<>(cache.values());
        for(T b:c){
            System.out.println(b.toString());
        }
        return obj;
    }


    public Map<String, T> getCache() {
        return cache;
    }

    @Override
    public void delete(T obj) {
        cache.remove(obj.getId());
    }

    @Override
    public ArrayList<T> findAll() {
        return new ArrayList<>(cache.values());
    }

    @Override
    public T find(String id) {
        return cache.get(id);
    }

    @Override
    public int count() {
        return cache.size();
    }
}
