package ar.com.clevcore.backend.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;

import org.eclipse.persistence.indirection.IndirectList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.clevcore.utils.StringUtils;
import ar.com.clevcore.utils.Utils;

public final class PersistenceUtils {

    private static final Logger LOG = LoggerFactory.getLogger(PersistenceUtils.class);

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

    public static enum Condition {
        AND("and"), OR("or");

        private final String name;

        private Condition(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

    }

    private PersistenceUtils() {
        throw new AssertionError();
    }

    public static TypedQuery<?> getSelectQuery(Object object, Operator operator, boolean onlyId,
            EntityManager entityManager) {
        Class<?> clazz = (Class<?>) object.getClass();
        List<String> entityPropertyFromObject = getEntityPropertyFromObject(clazz, onlyId);
        Map<String, Object> propertyValuesMap = Utils.getPropertyValue(object, entityPropertyFromObject, true);

        String table = StringUtils.splitLast(clazz.getName(), "\\.");

        String select = "select " + table;
        String from = " from " + table + " " + table;
        String join = "";
        String where = "";

        for (String property : propertyValuesMap.keySet()) {
            if (!property.contains(".")) {
                where = prepareWhere(where);
                where += table + "." + property + " " + operator.toString() + " :" + getParameterName(property);
            } else {
                String parentTable;
                String childTable;
                String column;
                String newJoin;

                Field field;

                String[] subProperty = property.split("\\.");

                if (subProperty.length < 3) {
                    parentTable = table;
                } else {
                    parentTable = getTableName(table, subProperty[subProperty.length - 3]);
                }

                childTable = subProperty[subProperty.length - 2];
                column = subProperty[subProperty.length - 1];

                field = getFieldFromProperty(clazz, property.substring(0, property.lastIndexOf(".")));
                if (!field.isAnnotationPresent(EmbeddedId.class)) {
                    newJoin = " join " + parentTable + "." + childTable + " " + getTableName(parentTable, childTable);

                    if (!join.contains(newJoin)) {
                        join += newJoin;
                    }

                    where = prepareWhere(where);
                    where += getTableName(parentTable, childTable) + "." + column + " " + operator.toString() + " :"
                            + getParameterName(property);
                } else {
                    where = prepareWhere(where);
                    where += parentTable + "." + childTable + "." + column + " " + operator.toString() + " :"
                            + getParameterName(property);
                }
            }
        }

        TypedQuery<?> query = entityManager.createQuery(select + from + join + where, clazz);

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

    private static String getTableName(String value1, String value2) {
        return value1 + "_" + value2;
    }

    private static String getParameterName(String property) {
        return property.replace(".", "");
    }

    public static String prepareWhere(String value) {
        if (value.length() == 0) {
            value += " where ";
        } else {
            value += " and ";
        }

        return value;
    }

    public static List<String> getEntityPropertyFromObject(Class<?> clazz, boolean onlyId) {
        List<String> propertyList = new ArrayList<String>();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType().isAnnotationPresent(Entity.class) || field.isAnnotationPresent(EmbeddedId.class)) {
                if (!field.isAnnotationPresent(Transient.class)) {
                    for (String entityPropertyFromObject : getEntityPropertyFromObject(field.getType(), onlyId)) {
                        propertyList.add(field.getName() + "." + entityPropertyFromObject);
                    }
                }
            } else {
                if (onlyId) {
                    if (field.isAnnotationPresent(Id.class)) {
                        propertyList.add(field.getName());
                    }
                } else if (Utils.isNativeType(field.getType())) {
                    propertyList.add(field.getName());
                }
            }
        }

        return propertyList;
    }

    public static Field getFieldFromProperty(Class<?> clazz, String property) {
        Field field = null;

        String[] propertyArray = property.split("\\.");
        for (int i = 0; i < propertyArray.length; i++) {
            try {
                field = clazz.getDeclaredField(propertyArray[i]);
                clazz = field.getType();
            } catch (NoSuchFieldException | SecurityException e) {
            }
        }

        return field;
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

    public static String setWildcardAll(String value) {
        if (value != null) {
            return "%" + value.replaceAll(" ", "%") + "%";
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    public static List<?> sortList(List<?> list, String property, boolean ascendingOrder) {
        if (list == null || list.size() < 2) {
            return list;
        }

        try {
            if (list instanceof IndirectList) {
                IndirectList<?> indirectList = (IndirectList<?>) list;
                Object collectionObject = indirectList.getDelegateObject();

                if (collectionObject instanceof List) {
                    Collections.sort((List<?>) collectionObject, Utils.getComparator(property, ascendingOrder));
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
