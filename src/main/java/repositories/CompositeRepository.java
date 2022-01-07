package repositories;

import caches.MemoryCache;
import models.HasId;

import java.util.ArrayList;

public class CompositeRepository<T extends HasId> implements Repository<T> {

    //private final MemoryCache memory;
    private ArrayList<T> liste=new ArrayList<>();
    private final Repository<T> file;

    public CompositeRepository(Repository<T> file) {
        ArrayList<T> r = file.findAll();
        System.out.println("taille au debut "+r.size());
        for (T element : r) {
            liste.add(element);
            System.out.println("Element ajoute "+ element.getId());
        }
        this.file = file;
    }

    @Override
    public T save(T obj) {
        liste.add(obj);
        return file.save(obj);
    }

    @Override
    public void delete(T obj) throws Exception{
       liste.remove(obj);
        file.delete(obj);
    }

    @Override
    public ArrayList<T> findAll(){
        return liste;
    }

    @Override
    public T find(String id) {
        for (T element : this.liste) {
            if(id.equals(element.getId())){
                return element;
            }
        }
        return null;
    }

    @Override
    public int count() {
        return liste.size();
    }

    public T get(int i){
        return liste.get(i);
    }
}
