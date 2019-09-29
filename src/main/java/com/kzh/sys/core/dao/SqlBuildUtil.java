package com.kzh.sys.core.dao;

import java.util.Collections;
import java.util.List;

/**
 * Created by peter.zhu on 2015/11/21.
 */
public class SqlBuildUtil {

    public static String buildWhereQuery(List<SimpleExpression> expressions) {
        StringBuilder whereQueryHql = new StringBuilder();
        if (expressions != null) {
            expressions.removeAll(Collections.singleton(null));
            if (expressions.size() > 0) {
                for (SimpleExpression expression : expressions) {
                    whereQueryHql.append(" AND ");
                    switch (expression.getOperator()) {
                        case IS:
                            whereQueryHql.append("o.").append(expression.getFieldName());
                            whereQueryHql.append(" IS ");
                            whereQueryHql.append(expression.getValue());
                            break;
                        case EQ:
                            whereQueryHql.append("o.").append(expression.getFieldName());
                            whereQueryHql.append(" = ");
                            whereQueryHql.append(expression.getValue());
                            break;
                        case NE:
                            whereQueryHql.append("(o.").append(expression.getFieldName());
                            whereQueryHql.append(" <> ");
                            whereQueryHql.append(expression.getValue());
                            whereQueryHql.append(" OR o.").append(expression.getFieldName());
                            whereQueryHql.append(" IS NULL) ");
                            break;
                        case LIKE:
                            whereQueryHql.append("o.").append(expression.getFieldName());
                            whereQueryHql.append(" like ");
                            whereQueryHql.append(expression.getValue());
                            break;
                        case LT:
                            whereQueryHql.append("o.").append(expression.getFieldName());
                            whereQueryHql.append(" < ");
                            whereQueryHql.append(expression.getValue());
                            break;
                        case GT:
                            whereQueryHql.append("o.").append(expression.getFieldName());
                            whereQueryHql.append(" > ");
                            whereQueryHql.append(expression.getValue());
                            break;
                        case LTE:
                            whereQueryHql.append("o.").append(expression.getFieldName());
                            whereQueryHql.append(" <= ");
                            whereQueryHql.append(expression.getValue());
                            break;
                        case GTE:
                            whereQueryHql.append("o.").append(expression.getFieldName());
                            whereQueryHql.append(" >= ");
                            whereQueryHql.append(expression.getValue());
                            break;
                        case IN:
                            whereQueryHql.append("o.").append(expression.getFieldName());
                            whereQueryHql.append(" in ");
                            whereQueryHql.append(expression.getValue());
                            break;
                        case SUB_QUERY_IN:
                            whereQueryHql.append("o.").append(expression.getFieldName());
                            whereQueryHql.append(" in ");
                            whereQueryHql.append(expression.getValue());
                            break;
                        case SUB_QUERY_NOT_IN:
                            whereQueryHql.append("o.").append(expression.getFieldName());
                            whereQueryHql.append(" not in ");
                            whereQueryHql.append(expression.getValue());
                            break;
                        case IS_EMPTY:
                            whereQueryHql.append("(o.").append(expression.getFieldName());
                            whereQueryHql.append(" is null or ");
                            whereQueryHql.append("o.").append(expression.getFieldName());
                            whereQueryHql.append(" = '') ");
                            break;
                        case IS_NOT_EMPTY:
                            whereQueryHql.append("o.").append(expression.getFieldName());
                            whereQueryHql.append(" is not null and ");
                            whereQueryHql.append("o.").append(expression.getFieldName());
                            whereQueryHql.append(" <> '' ");
                            break;
                        case HQL:
                            whereQueryHql.append(expression.getFieldName());
                            break;
                    }
                }
            }
        }

        return whereQueryHql.toString();
    }
}
