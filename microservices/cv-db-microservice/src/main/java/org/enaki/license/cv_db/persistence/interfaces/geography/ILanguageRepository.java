package org.enaki.license.cv_db.persistence.interfaces.geography;

import org.enaki.license.cv_db.persistence.entities.geography.Language;
import org.enaki.license.cv_db.persistence.interfaces.generic.IReadRepository;

import java.util.List;

public interface ILanguageRepository extends IReadRepository<Language> {
    List<Language> getLanguagesByName(String name);
}
