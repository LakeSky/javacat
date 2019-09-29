package com.kzh.sys.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PostLoad;
import javax.persistence.PostUpdate;

public class EntityUpdateListener {

    public static final Logger logger = LoggerFactory.getLogger(EntityUpdateListener.class);

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
}
