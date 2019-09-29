package com.kzh.sys.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kzh.sys.app.utils.SessionUtil;
import com.kzh.sys.core.dao.Restrictions;
import com.kzh.sys.core.dao.SimpleExpression;
import com.kzh.sys.pojo.Result;
import com.kzh.sys.pojo.WorldPage;
import com.kzh.sys.service.generate.auto.Action;
import com.kzh.sys.service.generate.auto.QField;
import com.kzh.sys.service.generate.auto.QFieldInfo;
import com.kzh.sys.service.generate.auto.QFieldQueryType;
import com.kzh.sys.util.CollectionUtil;
import com.kzh.sys.util.DateUtil;
import com.kzh.sys.util.SysUtil;
import lombok.Data;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 基础类 继承它的注意数据库字段
 */
@MappedSuperclass
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = -707273279554634993L;
    public static final Long VERSION_DEFAULT = 1L;
    public static Set<String> baseEntityFields = new HashSet<>();

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @Column(length = 50)
    @QField(name = "id")
    private String id;

    @Column(name = "shop_id", length = 50)
    @QField(name = "所属店铺", actions = {Action.query})
    private String shopId;

    @Column(name = "creator", updatable = false, nullable = false, length = 50)
    @QField(name = "创建人", actions = {Action.query})
    private String creator;

    @QField(name = "修改人")
    @Column(name = "modifier", nullable = false, length = 50)
    private String modifier;

    @Column(name = "create_time", updatable = false, nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @QField(name = "创建时间")
    private Date createTime;

    @Column(name = "modify_time", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @QField(name = "修改时间")
    private Date modifyTime;

    @Column(name = "del_flag", nullable = false)
    @QField(name = "删除标记")
    private Boolean delFlag = false;// 是否合法数据，用于软删除

    @Version
    @Column(name = "version")
    @QField(name = "版本号")
    @JsonIgnore
    private Long _version;

    @Transient
    @QField(name = "创建时间文本", actions = {Action.export})
    private String createTimeStr;

    @Transient
    @QField(name = "修改时间文本", actions = {Action.export})
    private String modifyTimeStr;

    @Transient
    private WorldPage worldPage;

    public Result validateField() throws Exception {
        return validateField(null);
    }

    public Result validateField(String excludeField) throws Exception {
        Map<String, QFieldInfo> notNullsMap = getNotNullFields(this.getClass());
        if (CollectionUtil.isNotEmpty(notNullsMap.keySet())) {
            for (String notNull : notNullsMap.keySet()) {
                if (SysUtil.isEmpty(excludeField) || !excludeField.equals(notNull)) {
                    Method method = this.getClass().getDeclaredMethod("get" + StringUtils.capitalize(notNull));
                    Object value = method.invoke(this);
                    if (value == null || SysUtil.isEmpty(value.toString())) {
                        QFieldInfo qFieldInfo = notNullsMap.get(notNull);
                        return new Result(false, qFieldInfo.getNickname() + " 不能为空");
                    }
                }
            }
        }
        return new Result(true);
    }

    @JsonIgnore
    public static Map<String, QFieldInfo> getNotNullFields(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        Map<String, QFieldInfo> qFieldInfoNotNull = new HashMap<>();
        for (Field field : fields) {
            QField qField = field.getAnnotation(QField.class);
            if (qField != null) {
                QFieldInfo fieldInfo = new QFieldInfo();
                fieldInfo.fromQField(field, qField);
                if (!fieldInfo.getNullable()) {
                    qFieldInfoNotNull.put(fieldInfo.getName(), fieldInfo);
                }
            }
        }
        return qFieldInfoNotNull;
    }

    @JsonIgnore
    public List<SimpleExpression> initExpressions() throws Exception {
        Class clazz = this.getClass();
        List<SimpleExpression> expressions = new ArrayList<>();
        Map<String, Object> paramMap = new HashMap<>();
        for (Field field : this.getClass().getDeclaredFields()) {
            Method method = this.getClass().getDeclaredMethod("get" + StringUtils.capitalize(field.getName()));
            if (method.getName().startsWith("get")) {
                Object value = method.invoke(this);
                if (!(value == null || SysUtil.isEmpty(value.toString()))) {
                    if (value instanceof Enum) {
                        paramMap.put(field.getName(), ((Enum) value).name());
                    } else if (value instanceof Double) {
                        paramMap.put(field.getName(), value.toString());
                    } else if (value instanceof String) {
                        paramMap.put(field.getName(), value);
                    }
                }
            }
        }

        if (MapUtils.isNotEmpty(paramMap)) {
            for (Object o : paramMap.entrySet()) {
                Map.Entry entry = (Map.Entry) o;
                String queryField = (String) entry.getKey();
                if (!queryField.startsWith("_")) {//以下划线开头的参数属于特殊的查询参数，需要手动进行处理
                    if (queryField.endsWith("_start")) {
                        String field = queryField.replace("_start", "");
                        expressions.add(Restrictions.gte(field, entry.getValue(), true));
                    } else if (queryField.endsWith("_end")) {
                        String field = queryField.replace("_end", "");
                        expressions.add(Restrictions.lte(field, entry.getValue(), true));
                    } else {
                        if (BaseEntity.baseEntityFields.contains(queryField)) {
                            expressions.add(Restrictions.eq(queryField, entry.getValue(), true));
                        } else {
                            Field field = clazz.getDeclaredField(queryField);
                            QField qField = field.getAnnotation(QField.class);
                            if (qField != null) {
                                if (field.getType().isEnum()) {
                                    expressions.add(Restrictions.eq(queryField, entry.getValue(), true));
                                } else {
                                    if (QFieldQueryType.eq.equals(qField.queryType())) {
                                        expressions.add(Restrictions.eq(queryField, entry.getValue(), true));
                                    } else if (QFieldQueryType.like.equals(qField.queryType())) {
                                        if (String.class.equals(field.getType())) {
                                            expressions.add(Restrictions.like(queryField, (String) entry.getValue(), true));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return expressions;
    }

    public String getCreateTimeStr() {
        return DateUtil.format("yyyy-MM-dd HH:mm:ss", this.getCreateTime());
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public String getModifyTimeStr() {
        return DateUtil.format("yyyy-MM-dd HH:mm:ss", this.getModifyTime());
    }

    public void setModifyTimeStr(String modifyTimeStr) {
        this.modifyTimeStr = modifyTimeStr;
    }

    @PrePersist
    public void initCreation() {
        if (getCreateTime() == null) {
            setCreateTime(new Date());
        }
        if (getCreator() == null) {
            if (SysUtil.isNotEmpty(SessionUtil.getUserName())) {
                setCreator(SessionUtil.getUserName());
            } else {
                setCreator("system");
            }
        }
        if (getModifyTime() == null) {
            setModifyTime(new Date());
        }
        if (getModifier() == null) {
            if (SysUtil.isNotEmpty(SessionUtil.getUserName())) {
                setModifier(SessionUtil.getUserName());
            } else {
                setModifier("system");
            }
        }
        User user = SessionUtil.getUser();
        if (user != null) {
            this.setShopId(user.getShopId());
        }
    }

    @PreUpdate
    public void initModify() {
        setModifyTime(new Date());
        if (SysUtil.isNotEmpty(SessionUtil.getUserName())) {
            setModifier(SessionUtil.getUserName());
        } else {
            setModifier("system");
        }
    }

    public void initModifyFields(Object obj) throws Exception {
        BeanUtils.copyProperties(obj, this, getNullPropertyNames(obj));
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null || !isBaseType(srcValue)) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static boolean isBaseType(Object object) {
        Class className = object.getClass();
        if (className.equals(java.lang.Integer.class) ||
                className.equals(java.lang.String.class) ||
                className.equals(java.lang.Byte.class) ||
                className.equals(java.lang.Long.class) ||
                className.equals(java.lang.Double.class) ||
                className.equals(java.lang.Float.class) ||
                className.equals(java.lang.Character.class) ||
                className.equals(java.lang.Short.class) ||
                className.equals(java.lang.Boolean.class) ||
                object instanceof Enum) {
            return true;
        }
        return false;
    }

    public static Set<String> initBaseFields() {
        Set<String> baseEntityFields = new HashSet<>();
        Field[] fields = BaseEntity.class.getDeclaredFields();
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                baseEntityFields.add(field.getName());
            }
        }
        return baseEntityFields;
    }

    static {
        baseEntityFields = initBaseFields();
    }
}