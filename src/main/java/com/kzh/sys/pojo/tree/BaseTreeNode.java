package com.kzh.sys.pojo.tree;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class BaseTreeNode {
    private String name;
    private String id;
    private String url;
    private Boolean checked = false;//是否被选中
    private Boolean open = true;//是否展开
    private List<BaseTreeNode> children = new ArrayList<>();
    private String icon;
    
    public void addChild(BaseTreeNode treeNode) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(treeNode);
    }
}
