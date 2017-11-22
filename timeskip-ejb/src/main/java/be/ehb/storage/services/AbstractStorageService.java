package be.ehb.storage.services;

import be.ehb.storage.EntityListRetriever;
import be.ehb.storage.EntityRetriever;
import be.ehb.storage.EntitySetRetriever;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceException;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public abstract class AbstractStorageService<T> implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(AbstractStorageService.class);

    private Class<T> entityClass;

    public AbstractStorageService(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    protected T create(T bean) {
        getEntityManager().persist(bean);
        getEntityManager().flush();
        log("create");
        return bean;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    protected T update(T bean) {
        T rVal = getEntityManager().merge(bean);
        getEntityManager().flush();
        log("update");
        return rVal;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    protected void delete(T bean) {
        log("delete");
        getEntityManager().remove(getEntityManager().merge(bean));
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    protected T get(Long id) {
        log("get(long:id)");
        return getEntityManager().find(entityClass, id);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    protected T get(String id) {
        log("get(string:id)");
        return getEntityManager().find(entityClass, id);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    protected T get(Enum id) {
        log("get(enum:id)");
        return getEntityManager().find(entityClass, id);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    protected Optional<T> findOrEmpty(EntityRetriever<T> retriever) {
        try {
            return Optional.ofNullable(retriever.retrieve());
        }
        catch (NoResultException ex) {
            log("No result found");
            return Optional.empty();
        }
        catch (NonUniqueResultException ex) {
            log("Multiple results found");
            return Optional.empty();
        }
        catch (PersistenceException ex) {
            log(ex.getMessage());
            return Optional.empty();
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    protected Optional<List<T>> listOrEmpty(EntityListRetriever<T> retriever) {
        try {
            return Optional.ofNullable(retriever.retrieve());
        }
        catch (PersistenceException ex) {
            log(ex.getMessage());
            return Optional.empty();
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    protected Optional<Set<T>> setOrEmpty(EntitySetRetriever<T> retriever) {
        try {
            return Optional.ofNullable(retriever.retrieve());
        }
        catch (PersistenceException ex) {
            log(ex.getMessage());
            return Optional.empty();
        }
    }

    private void log(String logMessage) {
        log.debug("{}: {}", this.entityClass.getSimpleName(), logMessage);
    }

}