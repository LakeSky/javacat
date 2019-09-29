package com.kzh.sys.core.dao;

import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * 条件构造器
 * 用于创建条件表达式
 *
 * @Class Name Restrictions
 */
public class Restrictions {
    public static SimpleExpression isNull(String fieldName) {
        return new SimpleExpression(fieldName, null, SimpleExpression.Operator.IS);
    }

    public static SimpleExpression isEmpty(String fieldName) {
        return new SimpleExpression(fieldName, null, SimpleExpression.Operator.IS_EMPTY);
    }
    
    public static SimpleExpression isNotEmpty(String fieldName) {
        return new SimpleExpression(fieldName, null, SimpleExpression.Operator.IS_NOT_EMPTY);
    }

    public static SimpleExpression is(String fieldName, Object value, boolean ignoreNull) {
        if (StringUtils.isEmpty(value)) return null;
        return new SimpleExpression(fieldName, value, SimpleExpression.Operator.IS);
    }
    
    public static SimpleExpression eq(String fieldName, Object value, boolean ignoreNull) {
        if (!ignoreNull) {
            if (value == null || StringUtils.isEmpty(value.toString())) {
                return new SimpleExpression(fieldName, value, SimpleExpression.Operator.IS_EMPTY);
            }
        }
        if (StringUtils.isEmpty(value)) return null;
        if (value instanceof String || value instanceof Enum) {
            return new SimpleExpression(fieldName, "'" + value.toString().trim() + "'", SimpleExpression.Operator.EQ);
        } else {
            return new SimpleExpression(fieldName, value, SimpleExpression.Operator.EQ);
        }
    }
    
    public static SimpleExpression ne(String fieldName, Object value, boolean ignoreNull) {
        if (StringUtils.isEmpty(value)) return null;
        if (value instanceof String || value instanceof Enum) {
            return new SimpleExpression(fieldName, "'" + value.toString().trim() + "'", SimpleExpression.Operator.NE);
        } else {
            return new SimpleExpression(fieldName, value, SimpleExpression.Operator.NE);
        }
    }
    
    public static SimpleExpression like(String fieldName, String value, boolean ignoreNull) {
        if (StringUtils.isEmpty(value)) return null;
        return new SimpleExpression(fieldName, "'%" + value.toString().trim() + "%'", SimpleExpression.Operator.LIKE);
    }
    
    public static SimpleExpression gt(String fieldName, Object value, boolean ignoreNull) {
        if (StringUtils.isEmpty(value)) return null;
        if (value instanceof String || value instanceof Enum) {
            return new SimpleExpression(fieldName, "'" + value.toString().trim() + "'", SimpleExpression.Operator.GT);
        } else {
            return new SimpleExpression(fieldName, value, SimpleExpression.Operator.GT);
        }
    }
    
    public static SimpleExpression lt(String fieldName, Object value, boolean ignoreNull) {
        if (StringUtils.isEmpty(value)) return null;
        if (value instanceof String || value instanceof Enum) {
            return new SimpleExpression(fieldName, "'" + value.toString().trim() + "'", SimpleExpression.Operator.LT);
        } else {
            return new SimpleExpression(fieldName, value, SimpleExpression.Operator.LT);
        }
    }

    public static SimpleExpression gte(String fieldName, Object value, boolean ignoreNull) {
        if (StringUtils.isEmpty(value)) return null;
        if (value instanceof String || value instanceof Enum) {
            return new SimpleExpression(fieldName, "'" + value + "'", SimpleExpression.Operator.GTE);
        } else {
            return new SimpleExpression(fieldName, value, SimpleExpression.Operator.GTE);
        }
    }
    
    public static SimpleExpression lte(String fieldName, Object value, boolean ignoreNull) {
        if (StringUtils.isEmpty(value)) return null;
        if (value instanceof String || value instanceof Enum) {
            return new SimpleExpression(fieldName, "'" + value.toString().trim() + "'", SimpleExpression.Operator.LTE);
        } else {
            return new SimpleExpression(fieldName, value, SimpleExpression.Operator.LTE);
        }
    }

    public static SimpleExpression in(String fieldName, Collection value, boolean ignoreNull) {
        if (!ignoreNull) {
            if (value == null || value.isEmpty()) {
                return new SimpleExpression(fieldName, value, SimpleExpression.Operator.IS_EMPTY);
            }
        }
        if (ignoreNull && (value == null || value.isEmpty())) {
            return null;
        }
        StringBuffer newValue = new StringBuffer("(");
        for (Object obj : value) {
            if (obj instanceof String || obj instanceof Enum) {
                newValue.append("'").append(obj).append("',");
            } else {
                newValue.append(obj).append(",");
            }
        }
        newValue.deleteCharAt(newValue.length() - 1).append(")");
        return new SimpleExpression(fieldName, newValue, SimpleExpression.Operator.IN);
    }
    
    public static SimpleExpression subQueryIN(String fieldName, String value, boolean ignoreNull) {
        if (ignoreNull && (StringUtils.isEmpty(value))) {
            return null;
        }
        return new SimpleExpression(fieldName, "(" + value + ")", SimpleExpression.Operator.SUB_QUERY_IN);
    }

    public static SimpleExpression subQueryNotIN(String fieldName, String value, boolean ignoreNull) {
        if (ignoreNull && (StringUtils.isEmpty(value))) {
            return null;
        }
        return new SimpleExpression(fieldName, "(" + value.toString().trim() + ")", SimpleExpression.Operator.SUB_QUERY_NOT_IN);
    }

    public static SimpleExpression hql(String hql, boolean ignoreNull) {
        if (ignoreNull && StringUtils.isEmpty(hql)) {
            return null;
        }
        return new SimpleExpression(hql, null, SimpleExpression.Operator.HQL);
    }
}

