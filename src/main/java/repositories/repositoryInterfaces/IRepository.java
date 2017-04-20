package repositories.repositoryInterfaces;

import java.util.Collection;
import java.util.List;

/**
 * Created by Kaempe on 23-02-2017.
 */
public interface IRepository<T>
{
    Collection<T> getAll();
    T get(int id);
    int create(T model);
    boolean update(T model);
    boolean delete(int id);
    boolean exists(int id);
    void failIfInvalid(T model);
}
