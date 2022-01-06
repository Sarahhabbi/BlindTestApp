package repositories;

import caches.MemoryCache;
import models.HasId;

import java.util.List;

public class CompositeRepository<T extends HasId> implements Repository<T> {

    private final MemoryCache memory;
    private final Repository<T> file;

    public CompositeRepository(Repository<T> file) {
        this.memory= new MemoryCache<>();
        List<T> r = file.findAll();
        for (T element : r) {
            this.memory.save(element);
            System.out.println("Element ajoute "+ element.getId());
        }
        this.file = file;
    }

    @Override
    public T save(T obj) {
        memory.save(obj);
        return file.save(obj);
    }

    @Override
    public void delete(T obj) throws Exception{
        memory.delete(obj);
        file.delete(obj);
    }

    @Override
    public List<T> findAll(){
        return memory.findAll();
    }

    @Override
    public T find(String id) {
        return (T)memory.find(id);
    }

    @Override
    public int count() {
        return memory.count();
    }


}
