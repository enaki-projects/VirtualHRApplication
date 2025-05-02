package org.enaki.license.manager_db.persistence.interfaces.generic

interface IRepository<T>: IReadRepository<T> {
    /**
     * [add] method adds a new record to the database.
     * @param item the new record to be introduced in the database.
     * @return a [String?] indicating the new record's unique id.
     */
    fun add(item: T): Long?

    /**
     * [delete] method deletes a record from the database by the specified [id].
     * @param id the id record to be deleted in the database.
     * @return a [Boolean] value that indicates whether the operation was successful or not.
     */
    fun delete(id: Long): Boolean

    /**
     * [update] method updates a record from the database by the specified [id] with the new data from [item].
     * @param id the id record to be updated in the database.
     * @param item the entity with updated values.
     * @return a [Boolean] value that indicates whether the operation was successful or not.
     */
    fun update(id: Long, item: T): Boolean

    /**
     * [addWithId] method adds a new record to the database with specified id.
     * @param item the new record to be introduced in the database.
     * @return a [Boolean] indicating the success of the operation.
     */
    fun addWithId(item: T): Boolean

}