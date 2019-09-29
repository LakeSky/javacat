package com.kzh.sys.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PostRemove;

public class EntityRemoveListener {

    public static final Logger logger = LoggerFactory.getLogger(EntityRemoveListener.class);

    @PostRemove
    public void postRemove(Object entity) {
        String entityName = entity.getClass().getSimpleName();
        logger.debug("remove system {} : {}", entityName, entity.toString());
    }
}
