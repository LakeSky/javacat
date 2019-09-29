package com.kzh.sys.pojo.tree;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gang on 2017/2/1.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ZTreeNode {
    private String id;
    private String pId;
    private String name;
    private Boolean checked;
    private Boolean open;
    private String url;
    private String icon;
    private String iconSkin;
    
    private List<ZTreeNode> children = new ArrayList<>();

    public ZTreeNode() {
    }

    public ZTreeNode(String id, String pId, String name, Boolean checked, Boolean open, String url) {
        this.id = id;
        this.pId = pId;
        this.name = name;
        this.checked = checked;
        this.open = open;
        this.url = url;
    }

    public String getIconSkin() {
        return iconSkin;
    }

    public void setIconSkin(String iconSkin) {
        this.iconSkin = iconSkin;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }
}
