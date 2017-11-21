package be.ehb.storage.services;

import be.ehb.entities.projects.ActivityBean;
import be.ehb.factories.ExceptionFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@ApplicationScoped
public class ActivityStorageService extends AbstractStorageService<ActivityBean> {

    private static final Logger log = LoggerFactory.getLogger(ActivityStorageService.class);

    @PersistenceContext
    private EntityManager entityManager;

    public ActivityStorageService() {
        super(ActivityBean.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Optional<ActivityBean> get(String organizationId, Long projectId, Long activityId) {
        return findOrEmpty(() -> getEntityManager()
                .createQuery("SELECT a FROM ActivityBean a JOIN a.project p JOIN p.organization o WHERE a.id = :aId AND p.id = :pId AND o.id = :orgId", ActivityBean.class)
                .setParameter("aId", activityId)
                .setParameter("pId", projectId)
                .setParameter("orgId", organizationId)
                .getSingleResult());
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Optional<List<ActivityBean>> list(String organizationId, Long projectId) {
        return listOrEmpty(() -> {
            StringBuilder queryString = new StringBuilder("SELECT a FROM ActivityBean a");

            boolean orgCrit = StringUtils.isNotBlank(organizationId);
            boolean projCrit = projectId != null;

            if (orgCrit || projCrit) {
                queryString.append(" JOIN a.project p JOIN p.organization o WHERE ");
            }
            if (orgCrit) {
                queryString.append("o.id = :oId");
            }
            if (orgCrit && projCrit) {
                queryString.append(" AND ");
            } else if (projCrit) {
                queryString.append("p.id = :pId");
            }

            Query query = getEntityManager().createQuery("SELECT a FROM ActivityBean a", ActivityBean.class);
            if (orgCrit) {
                query.setParameter("oId", organizationId);
            }
            if (projCrit) {
                query.setParameter("pId", projectId);
            }
            return query.getResultList();
        });
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Optional<ActivityBean> findActivityByName(String organizationId, Long projectId, String activityName) {
        return findOrEmpty(() -> getEntityManager()
                .createQuery("SELECT a FROM ActivityBean a JOIN a.project p JOIN p.organization o WHERE o.id = :oId AND p.id = :pId AND a.name = :aName", ActivityBean.class)
                .setParameter("oId", organizationId)
                .setParameter("pId", projectId)
                .setParameter("aName", activityName)
                .getSingleResult());

    }
}