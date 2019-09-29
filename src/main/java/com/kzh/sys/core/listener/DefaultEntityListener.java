package com.kzh.sys.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class DefaultEntityListener {

    public static final Logger logger = LoggerFactory.getLogger(DefaultEntityListener.class);
    
    @PostPersist
    public void postAdd(Object entity) {
        String entityName = entity.getClass().getSimpleName();
        logger.debug("add system {} : {}", entityName, entity.toString());
    }

    @PostLoad
    public void postLoad(Object entity) {
        if (entity instanceof EntityUpdateRecordable) {
            ((EntityUpdateRecordable) entity).setPreUpdateStatus(entity.toString());
        }
    }

    @PostUpdate
    public void postUpdate(Object entity) {
        if (entity instanceof EntityUpdateRecordable) {
            EntityUpdateRecordable recordable = (EntityUpdateRecordable) entity;
            String entityName = entity.getClass().getSimpleName();
            logger.debug("update system {} {} to {}", new Object[]{entityName, recordable.getPreUpdateStatus(), entity.toString()});
        }
    }


    @PostRemove
    public void postRemove(Object entity) {
        String entityName = entity.getClass().getSimpleName();
        logger.debug("remove system {} : {}", entityName, entity.toString());
    }

}