package com.kzh.sys.pojo.echart;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gang on 2019/7/24.
 */
@Data
public class Series {
    private String name;
    private String symbol = "circle";
    private Boolean showSymbol = false;
    private String type;
    private List<Object> data = new ArrayList<>();
}
