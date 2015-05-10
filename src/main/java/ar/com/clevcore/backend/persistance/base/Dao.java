package ar.com.clevcore.backend.persistance.base;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

public interface Dao<E extends Serializable> {

    E selectOne(EntityManager entityManager) throws Exception;

    E selectIdOne(E entity, EntityManager entityManager) throws Exception;
    
    E selectEqualOne(E entity, EntityManager entityManager) throws Exception;

    E selectLikeOne(E entity, EntityManager entityManager) throws Exception;

    
    List<E> selectAll(EntityManager entityManager) throws Exception;
    
    List<E> selectIdAll(E entity, EntityManager entityManager) throws Exception;

    List<E> selectEqualAll(E entity, EntityManager entityManager) throws Exception;

    List<E> selectLikeAll(E entity, EntityManager entityManager) throws Exception;

    
    void insert(E entity, EntityManager entityManager) throws Exception;

    void update(E entity, EntityManager entityManager) throws Exception;

    void delete(E entity, EntityManager entityManager) throws Exception;

}
