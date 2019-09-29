package com.kzh.sys.pojo.jqx;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * Created by gang on 2019/7/10.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JqxColumn {
    private String text;
    private String datafield;
    private Integer minwidth;
    private Integer maxwidth;
    private Integer width;
    private String filtertype;
    private String cellsalign;
    private String cellsrenderer;
}
