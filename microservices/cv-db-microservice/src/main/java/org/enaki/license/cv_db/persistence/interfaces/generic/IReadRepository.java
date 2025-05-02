package org.enaki.license.cv_db.persistence.interfaces.generic;

import java.util.List;

public interface IReadRepository<T> {
    /**
     * [getAll] method queries the database and gets all the existing records for a given table.
     * @return a [List] that contains all the records for the given table in the database.
     */
    List<T> getAll();

    /**
     * [get] method gets a specific record from the database by its [id].
     * @param id the id to be searched in the database.
     * @return a valid entity if the specified [id] was found or null otherwise.
     */
    T get(Long id);
}
