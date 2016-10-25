package ar.com.clevcore.backend.service;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import ar.com.clevcore.backend.exceptions.ClevcoreException;
import ar.com.clevcore.backend.persistence.Persistence;
import ar.com.clevcore.backend.utils.ExceptionUtils;

public class ServiceImpl<E extends Serializable, P extends Persistence<E>> implements Service<E> {

    protected P persistence;
    protected EntityManagerFactory entityManagerFactory;

    public ServiceImpl(P persistence, EntityManagerFactory entityManagerFactory) {
        this.persistence = persistence;
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
            return persistence.selectOne(entityManager);
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
            return persistence.selectIdOne(entity, entityManager);
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
            return persistence.selectEqualOne(entity, entityManager);
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
            return persistence.selectLikeOne(entity, entityManager);
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
            return persistence.selectAll(entityManager);
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
            return persistence.selectIdAll(entity, entityManager);
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
            return persistence.selectEqualAll(entity, entityManager);
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
            return persistence.selectLikeAll(entity, entityManager);
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
            persistence.insert(entity, entityManager);
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
            persistence.update(entity, entityManager);
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
            persistence.delete(entity, entityManager);
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
