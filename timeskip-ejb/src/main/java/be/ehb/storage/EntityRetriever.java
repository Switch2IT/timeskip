package be.ehb.storage;

import javax.persistence.PersistenceException;
import java.util.Set;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 *
 * Based on tutorial at https://stackoverflow.com/questions/28249874/best-practice-for-jpa-with-java8s-optional-return
 */
@FunctionalInterface
public interface EntityRetriever<T> {
    T retrieve() throws PersistenceException;
}
