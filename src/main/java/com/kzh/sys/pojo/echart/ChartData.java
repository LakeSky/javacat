package com.kzh.sys.pojo.echart;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gang on 2019/7/22.
 */
@Data
public class ChartData {
    private String xunit;
    private String yunit;
    private String yAxisName;
    private Double startFreq;
    private Double endFreq;
    private List<Series> series = new ArrayList<>();
}
