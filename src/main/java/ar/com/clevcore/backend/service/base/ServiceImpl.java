package ar.com.clevcore.backend.service.base;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import ar.com.clevcore.backend.persistence.base.Dao;
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
        EntityManager entityManager = getEntityManager();

        try {
            return dao.selectOne(entityManager);
        } catch (Exception e) {
            ExceptionUtils.treateException(e, this);
            return null;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    @Override
    public E selectIdOne(E entity) throws ClevcoreException {
        EntityManager entityManager = getEntityManager();

        try {
            return dao.selectIdOne(entity, entityManager);
        } catch (Exception e) {
            ExceptionUtils.treateException(e, this);
            return null;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    @Override
    public E selectEqualOne(E entity) throws ClevcoreException {
        EntityManager entityManager = getEntityManager();

        try {
            return dao.selectEqualOne(entity, entityManager);
        } catch (Exception e) {
            ExceptionUtils.treateException(e, this);
            return null;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    @Override
    public E selectLikeOne(E entity) throws ClevcoreException {
        EntityManager entityManager = getEntityManager();

        try {
            return dao.selectLikeOne(entity, entityManager);
        } catch (Exception e) {
            ExceptionUtils.treateException(e, this);
            return null;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    @Override
    public List<E> selectAll() throws ClevcoreException {
        EntityManager entityManager = getEntityManager();

        try {
            return dao.selectAll(entityManager);
        } catch (Exception e) {
            ExceptionUtils.treateException(e, this);
            return Collections.emptyList();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    @Override
    public List<E> selectIdAll(E entity) throws ClevcoreException {
        EntityManager entityManager = getEntityManager();

        try {
            return dao.selectIdAll(entity, entityManager);
        } catch (Exception e) {
            ExceptionUtils.treateException(e, this);
            return Collections.emptyList();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    @Override
    public List<E> selectEqualAll(E entity) throws ClevcoreException {
        EntityManager entityManager = getEntityManager();

        try {
            return dao.selectEqualAll(entity, entityManager);
        } catch (Exception e) {
            ExceptionUtils.treateException(e, this);
            return Collections.emptyList();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    @Override
    public List<E> selectLikeAll(E entity) throws ClevcoreException {
        EntityManager entityManager = getEntityManager();

        try {
            return dao.selectLikeAll(entity, entityManager);
        } catch (Exception e) {
            ExceptionUtils.treateException(e, this);
            return Collections.emptyList();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
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
