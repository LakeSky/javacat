package com.kzh.sys.core.dao;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class DefaultRepositoryFactory extends JpaRepositoryFactory {

    private final EntityManager entityManager;

    public DefaultRepositoryFactory(EntityManager entityManager) {
        super(entityManager);
        Assert.notNull(entityManager);
        this.entityManager = entityManager;

    }

    @Override
    protected <T, ID extends Serializable> SimpleJpaRepository<?, ?> getTargetRepository(RepositoryMetadata metadata, EntityManager entityManager) {
        JpaEntityInformation<?, Serializable> entityInformation = getEntityInformation(metadata.getDomainType());
        return new GenericRepositoryImpl(entityInformation, entityManager); // custom implementation
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        return GenericRepositoryImpl.class;
    }
}
