package ar.com.clevcore.backend.persistance.base;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.criteria.CriteriaQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.clevcore.backend.utils.PersistanceUtils;
import ar.com.clevcore.backend.utils.PersistanceUtils.Operator;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DaoImpl<E extends Serializable> implements Dao<E> {

    private static final Logger LOG = LoggerFactory.getLogger(DaoImpl.class);

    private Class<?> clazz;

    public DaoImpl() {
        clazz = (Class<?>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public E selectOne(EntityManager entityManager) {
        try {
            CriteriaQuery criteriaQuery = entityManager.getCriteriaBuilder().createQuery(clazz);
            criteriaQuery.select(criteriaQuery.from(clazz));
            return (E) entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            LOG.error("[E] No Result or No Unique Exception occurred in [selectOne]", e);
            return null;
        }
    }

    @Override
    public E selectIdOne(E entity, EntityManager entityManager) {
        try {
            return (E) PersistanceUtils.getSelectQuery(entity, Operator.EQUAL, true, entityManager).getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            LOG.error("[E] No Result or No Unique Exception occurred in [selectIdOne]", e);
            return null;
        }
    }

    @Override
    public E selectLikeOne(E entity, EntityManager entityManager) {
        try {
            return (E) PersistanceUtils.getSelectQuery(entity, Operator.LIKE, false, entityManager).getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            LOG.error("[E] No Result or No Unique Exception occurred in [selectLikeOne]", e);
            return null;
        }
    }

    @Override
    public E selectEqualOne(E entity, EntityManager entityManager) {
        try {
            return (E) PersistanceUtils.getSelectQuery(entity, Operator.EQUAL, false, entityManager).getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            LOG.error("[E] No Result or No Unique Exception  occurred in [selectEqualOne]", e);
            return null;
        }
    }

    @Override
    public List<E> selectAll(EntityManager entityManager) {
        CriteriaQuery criteriaQuery = entityManager.getCriteriaBuilder().createQuery(clazz);
        criteriaQuery.select(criteriaQuery.from(clazz));
        return (List<E>) entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<E> selectIdAll(E entity, EntityManager entityManager) {
        return (List<E>) PersistanceUtils.getSelectQuery(entity, Operator.EQUAL, true, entityManager).getResultList();
    }

    @Override
    public List<E> selectEqualAll(E entity, EntityManager entityManager) {
        return (List<E>) PersistanceUtils.getSelectQuery(entity, Operator.EQUAL, false, entityManager).getResultList();
    }

    @Override
    public List<E> selectLikeAll(E entity, EntityManager entityManager) {
        return (List<E>) PersistanceUtils.getSelectQuery(entity, Operator.LIKE, false, entityManager).getResultList();
    }

    @Override
    public void insert(E entity, EntityManager entityManager) {
        entityManager.persist(entity);
    }

    @Override
    public void update(E entity, EntityManager entityManager) {
        entityManager.merge(entity);
    }

    @Override
    public void delete(E entity, EntityManager entityManager) {
        E mergedEntity = entityManager.merge(entity);
        entityManager.remove(mergedEntity);
    }

}
