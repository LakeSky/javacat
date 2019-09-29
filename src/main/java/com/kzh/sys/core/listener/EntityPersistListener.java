package com.kzh.sys.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PostPersist;

public class EntityPersistListener {

    public static final Logger logger = LoggerFactory.getLogger(EntityPersistListener.class);

    @PostPersist
    public void postAdd(Object entity) {
        String entityName = entity.getClass().getSimpleName();
        logger.debug("remove system {} : {}", entityName, entity.toString());
    }
}
