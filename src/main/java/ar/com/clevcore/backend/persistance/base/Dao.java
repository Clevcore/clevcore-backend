package ar.com.clevcore.backend.persistance.base;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

public interface Dao<E extends Serializable> {

    E selectOne(EntityManager entityManager);

    E selectIdOne(E entity, EntityManager entityManager);

    E selectEqualOne(E entity, EntityManager entityManager);

    E selectLikeOne(E entity, EntityManager entityManager);

    List<E> selectAll(EntityManager entityManager);

    List<E> selectIdAll(E entity, EntityManager entityManager);

    List<E> selectEqualAll(E entity, EntityManager entityManager);

    List<E> selectLikeAll(E entity, EntityManager entityManager);

    void insert(E entity, EntityManager entityManager);

    void update(E entity, EntityManager entityManager);

    void delete(E entity, EntityManager entityManager);

}
