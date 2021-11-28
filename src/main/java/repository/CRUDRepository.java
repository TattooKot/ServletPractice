package repository;

import java.util.List;

public interface CRUDRepository<T, ID>{
    List<T> getAll();
    T getById(ID id);
    T create(T t);
    T update(T t);
    void delete(ID id);

}
