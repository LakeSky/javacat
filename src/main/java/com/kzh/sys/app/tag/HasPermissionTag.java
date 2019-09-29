package com.kzh.sys.app.tag;


public class HasPermissionTag extends AbstractPermissionTag {

    public HasPermissionTag() {
        super();
        init();
    }
    
    private void init() {
        this.setPermissions(null);
    }

    @Override
    public void release() {
        super.release();
        this.setPermissions(null);
    }

}
