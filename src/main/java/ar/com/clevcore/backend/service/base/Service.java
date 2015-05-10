package ar.com.clevcore.backend.service.base;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import ar.com.clevcore.exceptions.ClevcoreException;

public interface Service<E extends Serializable> {

    EntityManager getEntityManager();

    
    E selectOne() throws ClevcoreException;

    E selectIdOne(E entity) throws ClevcoreException;

    E selectEqualOne(E entity) throws ClevcoreException;

    E selectLikeOne(E entity) throws ClevcoreException;
    
    
    List<E> selectAll() throws ClevcoreException;

    List<E> selectIdAll(E entity) throws ClevcoreException;

    List<E> selectEqualAll(E entity) throws ClevcoreException;

    List<E> selectLikeAll(E entity) throws ClevcoreException;

    
    void insert(E entity) throws ClevcoreException;

    void update(E entity) throws ClevcoreException;

    void delete(E entity) throws ClevcoreException;

}
