package be.ehb.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public abstract class AbstractJpaStorage {

    private static Logger log = LoggerFactory.getLogger(AbstractJpaStorage.class.getName());

    @PersistenceContext
    protected EntityManager em;

    protected EntityManager getActiveEntityManager() {
        return em;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public <T> T create(T bean) {
        log.trace("create:" + bean.toString());
        em.persist(bean);
        em.flush();
        return bean;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public <T> T update(T bean) {
        log.trace("update:" + bean.toString());
        T result = em.merge(bean);
        em.flush();
        return result;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public <T> void delete(T bean) {
        log.trace("delete:" + bean);
        em.remove(em.merge(bean));
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public <T> T get(Long id, Class<T> type) {
        log.trace("get(long:id):" + id);
        T rval = null;
        rval = em.find(type, id);
        return rval;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public <T> T get(String id, Class<T> type) {
        log.trace("get(string:id):" + id);
        T rval = null;
        rval = em.find(type, id);
        return rval;
    }

}