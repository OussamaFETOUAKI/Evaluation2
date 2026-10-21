package ma.projet.dao;

import java.util.List;

/**
 * Interface générique pour la couche d'accès aux données (DAO).
 * @param <T> Le type d'entité manipulé.
 */
public interface IDao<T> {
    boolean create(T o);
    boolean update(T o);
    boolean delete(T o);
    T getById(int id);
    List<T> getAll();
}
