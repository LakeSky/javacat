package com.kzh.sys.pojo;

import com.kzh.sys.util.NumberUtil;
import com.kzh.sys.util.SysUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class WorldPage {
    //最小页码
    public static final Integer PAGE_INDEX_MIN = 1;

    //最小每页条数
    public static final Integer PAGE_SIZE_MIN = 1;

    //默认页码
    public static final Integer PAGE_INDEX_DEFAULT = 1;

    //默认每页条数
    public static final Integer PAGE_SIZE_DEFAULT = 10;

    public final static int PAGE_SIZE_3 = 3;
    public final static int PAGE_SIZE_5 = 5;
    public final static int PAGE_SIZE_10 = 10;
    public final static int PAGE_SIZE_12 = 12;
    public final static int PAGE_SIZE_20 = 20;
    public final static int PAGE_SIZE_30 = 30;
    public final static int PAGE_SIZE_50 = 50;
    public final static int PAGE_SIZE_200 = 200;
    public final static int PAGE_SIZE_1000 = 1000;

    //当前页码
    private Integer pageIndex = PAGE_INDEX_DEFAULT;

    //当前每页条数
    private Integer pageSize = PAGE_SIZE_DEFAULT;

    //排序方向，默认降序
    private Direction direction = Direction.DESC;

    private String order = Direction.DESC.toString();

    //排序字段
    private String properties;

    public Pageable getPageRequest() {
        pageIndex = Math.max(NumberUtil.objectToInt(pageIndex), PAGE_INDEX_MIN);
        pageSize = Math.max(NumberUtil.objectToInt(pageSize), PAGE_SIZE_MIN);
        Pageable pageable = null;
        if (SysUtil.isEmpty(properties) || direction == null) {
            pageable = new PageRequest(pageIndex - 1, pageSize);
        } else {
            pageable = new PageRequest(pageIndex - 1, pageSize, new Sort(direction, properties));
        }
        return pageable;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public static Integer getPageIndexMin() {
        return PAGE_INDEX_MIN;
    }

    public static Integer getPageSizeMin() {
        return PAGE_SIZE_MIN;
    }

    public static Integer getPageIndexDefault() {
        return PAGE_INDEX_DEFAULT;
    }

    public static Integer getPageSizeDefault() {
        return PAGE_SIZE_DEFAULT;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
        this.direction = Direction.fromStringOrNull(order);
    }
}
