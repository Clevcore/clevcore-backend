package ar.com.clevcore.backend.service.base;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import ar.com.clevcore.backend.persistance.base.Dao;
import ar.com.clevcore.exceptions.ClevcoreException;
import ar.com.clevcore.utils.ExceptionUtils;

public class ServiceImpl<E extends Serializable, D extends Dao<E>> implements Service<E> {

    protected D dao;
    protected EntityManagerFactory entityManagerFactory;

    public ServiceImpl(D dao, EntityManagerFactory entityManagerFactory) {
        this.dao = dao;
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    @Override
    public E selectOne() throws ClevcoreException {
        try {
            return dao.selectOne(getEntityManager());
        } catch (Exception e) {
            ExceptionUtils.treateException(e, this);
            return null;
        }
    }

    @Override
    public E selectIdOne(E entity) throws ClevcoreException {
        try {
            return dao.selectIdOne(entity, getEntityManager());
        } catch (Exception e) {
            ExceptionUtils.treateException(e, this);
            return null;
        }
    }

    @Override
    public E selectEqualOne(E entity) throws ClevcoreException {
        try {
            return dao.selectEqualOne(entity, getEntityManager());
        } catch (Exception e) {
            ExceptionUtils.treateException(e, this);
            return null;
        }
    }

    @Override
    public E selectLikeOne(E entity) throws ClevcoreException {
        try {
            return dao.selectLikeOne(entity, getEntityManager());
        } catch (Exception e) {
            ExceptionUtils.treateException(e, this);
            return null;
        }
    }

    @Override
    public List<E> selectAll() throws ClevcoreException {
        try {
            return dao.selectAll(getEntityManager());
        } catch (Exception e) {
            ExceptionUtils.treateException(e, this);
            return null;
        }
    }

    @Override
    public List<E> selectIdAll(E entity) throws ClevcoreException {
        try {
            return dao.selectIdAll(entity, getEntityManager());
        } catch (Exception e) {
            ExceptionUtils.treateException(e, this);
            return null;
        }
    }

    @Override
    public List<E> selectEqualAll(E entity) throws ClevcoreException {
        try {
            return dao.selectEqualAll(entity, getEntityManager());
        } catch (Exception e) {
            ExceptionUtils.treateException(e, this);
            return null;
        }
    }

    @Override
    public List<E> selectLikeAll(E entity) throws ClevcoreException {
        try {
            return dao.selectLikeAll(entity, getEntityManager());
        } catch (Exception e) {
            ExceptionUtils.treateException(e, this);
            return null;
        }
    }

    @Override
    public void insert(E entity) throws ClevcoreException {
        EntityManager entityManager = getEntityManager();

        try {
            entityManager.getTransaction().begin();
            dao.insert(entity, entityManager);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            ExceptionUtils.treateException(e, this);
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    @Override
    public void update(E entity) throws ClevcoreException {
        EntityManager entityManager = getEntityManager();

        try {
            entityManager.getTransaction().begin();
            dao.update(entity, entityManager);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            ExceptionUtils.treateException(e, this);
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    @Override
    public void delete(E entity) throws ClevcoreException {
        EntityManager entityManager = getEntityManager();

        try {
            entityManager.getTransaction().begin();
            dao.delete(entity, entityManager);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            ExceptionUtils.treateException(e, this);
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

}
