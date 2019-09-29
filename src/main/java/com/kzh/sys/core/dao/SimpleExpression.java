package com.kzh.sys.core.dao;

public class SimpleExpression {
    public enum Operator {
        EQ, NE, LIKE, GT, LT, GTE, LTE, IN, IS, IS_EMPTY, IS_NOT_EMPTY, SUB_QUERY_IN, SUB_QUERY_NOT_IN, HQL
    }

    private String fieldName;       //属性名
    private Object value;           //对应值
    private Operator operator;      //计算符

    public SimpleExpression(String fieldName, Object value, Operator operator) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getValue() {
        return value;
    }

    public Operator getOperator() {
        return operator;
    }
}