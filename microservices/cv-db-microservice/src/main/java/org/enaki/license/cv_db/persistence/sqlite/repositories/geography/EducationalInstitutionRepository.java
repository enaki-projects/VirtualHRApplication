package org.enaki.license.cv_db.persistence.sqlite.repositories.geography;

import org.enaki.license.cv_db.persistence.entities.geography.EducationalInstitution;
import org.enaki.license.cv_db.persistence.interfaces.geography.IEducationalInstitutionRepository;
import org.enaki.license.cv_db.persistence.sqlite.mappers.geography.EducationalInstitutionRowMapper;
import org.enaki.license.cv_db.persistence.sqlite.repositories.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EducationalInstitutionRepository extends GenericRepository<EducationalInstitution> implements IEducationalInstitutionRepository {
    public EducationalInstitutionRepository() {
        _tableName = "educational_institutions";
        _rowMapper = new EducationalInstitutionRowMapper();
    }


    @Override
    public List<EducationalInstitution> getAll() {
        String script = String.format("SELECT * FROM %s", _tableName);
        return _jdbcTemplate.query(script, _rowMapper);
    }

    @Override
    public List<EducationalInstitution> getEducationalInstitutionsByCountryId(Long id) {
        String script = String.format("SELECT * FROM %s WHERE country_id=? ORDER BY institution_name", _tableName);
        return _jdbcTemplate.query(script, _rowMapper, id);
    }

    @Override
    public EducationalInstitution get(Long id) {
        String script = String.format("SELECT * FROM %s WHERE educational_institution_id = ?", _tableName);
        List<EducationalInstitution> educationalInstitutionList = _jdbcTemplate.query(script, _rowMapper, id);
        return educationalInstitutionList.isEmpty() ? null : educationalInstitutionList.get(0);
    }

    @Override
    public List<EducationalInstitution> getIEducationalInstitutionsByName(String name) {
        String script = String.format("SELECT * FROM %s WHERE instr(lower(institution_name), ?) > 0 OR instr(lower(alt_institution_name), ?) > 0", _tableName);
        return _jdbcTemplate.query(script, _rowMapper, name.toLowerCase(), name.toLowerCase());
    }
}
