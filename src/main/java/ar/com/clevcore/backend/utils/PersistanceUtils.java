package ar.com.clevcore.backend.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.Query;

import org.eclipse.persistence.indirection.IndirectList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.clevcore.utils.StringUtils;
import ar.com.clevcore.utils.Utils;

public final class PersistanceUtils {

    private static final Logger LOG = LoggerFactory.getLogger(PersistanceUtils.class);

    public static enum Operator {
        EQUAL("="), LIKE("like");

        private final String name;

        private Operator(String name) {
            this.name = name;
        }
        @Override
        public String toString() {
            return name;
        }

    }

    private PersistanceUtils() {
        throw new AssertionError();
    }

    public static Query getSelectQuery(Object object, Operator operator, boolean onlyId, EntityManager entityManager) {
        List<String> entityPropertyFromObject = getEntityPropertyFromObject((Class<?>) object.getClass(), onlyId);
        Map<String, Object> propertyValuesMap = Utils.getPropertyValue(object, entityPropertyFromObject, true);

        String table = StringUtils.splitLast(((Class<?>) object.getClass()).getName(), "\\.");

        String select = "select " + table;
        String from = " from " + table + " " + table;
        String join = "";
        String where = "";

        for (String property : propertyValuesMap.keySet()) {
            String parentTable;
            String childTable;
            String column;

            if (!property.contains(".")) {
                parentTable = "";
                childTable = table;
                column = property;
            } else {
                String[] subProperty = property.split("\\.");

                if (subProperty.length < 3) {
                    parentTable = table;
                } else {
                    parentTable = table + "_" + subProperty[subProperty.length - 3];
                }

                childTable = subProperty[subProperty.length - 2];
                column = subProperty[subProperty.length - 1];

                String newJoin = " join " + parentTable + "." + childTable + " " + parentTable + "_" + childTable;

                if (!join.contains(newJoin)) {
                    join += newJoin;
                }
            }

            if (where.length() == 0) {
                where += " where ";
            } else {
                where += " and ";
            }

            where += parentTable + (parentTable.length() > 0 ? "_" : "") + childTable + "." + column + " "
                    + operator.toString() + " :" + getParameterName(property);
        }

        Query query = entityManager.createQuery(select + from + join + where);

        if (operator.equals(Operator.EQUAL)) {
            for (String property : propertyValuesMap.keySet()) {
                query.setParameter(getParameterName(property), propertyValuesMap.get(property));
            }
        } else {
            for (String property : propertyValuesMap.keySet()) {
                if (propertyValuesMap.get(property) instanceof String) {
                    query.setParameter(getParameterName(property),
                            setWildcard(propertyValuesMap.get(property).toString()));
                } else {
                    query.setParameter(getParameterName(property), propertyValuesMap.get(property));
                }
            }
        }

        return query;
    }

    public static String getParameterName(String property) {
        return property.replace(".", "");
    }

    public static List<String> getEntityPropertyFromObject(Class<?> clazz, boolean onlyId) {
        List<String> propertyList = new ArrayList<String>();

        for (Field property : clazz.getDeclaredFields()) {
            if (!property.getType().isAnnotationPresent(Entity.class)) {
                if (onlyId && property.isAnnotationPresent(Id.class)) {
                        propertyList.add(property.getName());
                } else if (Utils.isNativeType(property.getType())) {
                        propertyList.add(property.getName());
                }
            } else {
                for (String entityPropertyFromObject : getEntityPropertyFromObject(property.getType(), onlyId)) {
                    propertyList.add(property.getName() + "." + entityPropertyFromObject);
                }
            }
        }

        return propertyList;
    }

    public static String setWildcard(String value) {
        if (value != null) {
            return "%" + value + "%";
        }
        return value;
    }

    public static String setWildcardFirst(String value) {
        if (value != null) {
            return "%" + value;
        }
        return value;
    }

    public static String setWildcardLast(String value) {
        if (value != null) {
            return value + "%";
        }
        return value;
    }

    public static List sortList(List list, String property, boolean ascendingOrder) {
        if (list == null || list.size() < 2) {
            return list;
        }

        try {
            if (list instanceof IndirectList) {
                IndirectList indirectList = (IndirectList) list;
                Object collectionObject = indirectList.getDelegateObject();

                if (collectionObject instanceof List) {
                    Collections.sort((List) collectionObject, Utils.getComparator(property, ascendingOrder));
                }
            } else {
                Collections.sort(list, Utils.getComparator(property, ascendingOrder));
            }
        } catch (Exception e) {
            LOG.error("[E] Exception occurred in [sortList]", e);

        }

        return list;
    }

}
