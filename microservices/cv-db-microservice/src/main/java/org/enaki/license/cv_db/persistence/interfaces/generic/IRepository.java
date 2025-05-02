package org.enaki.license.cv_db.persistence.interfaces.generic;

public interface IRepository<T> extends IReadRepository<T>{
    /**
     * [add] method adds a new record to the database.
     * @param item the new record to be introduced in the database.
     * @return a [String?] indicating the new record's unique id.
     */
    Long add(T item);

    /**
     * [delete] method deletes a record from the database by the specified [id].
     * @param id the id record to be deleted in the database.
     * @return a [Boolean] value that indicates whether the operation was successful or not.
     */
    Boolean delete(Long id);

    /**
     * [update] method updates a record from the database by the specified [id] with the new data from [item].
     * @param id the id record to be updated in the database.
     * @param item the entity with updated values.
     * @return a [Boolean] value that indicates whether the operation was successful or not.
     */
    Boolean update(Long id,T item);
}
