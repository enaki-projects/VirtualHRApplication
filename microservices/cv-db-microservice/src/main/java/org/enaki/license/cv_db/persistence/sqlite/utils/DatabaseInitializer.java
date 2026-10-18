package org.enaki.license.cv_db.persistence.sqlite.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Creates and seeds the SQLite database on startup when it is missing or empty.
 *
 * <p>"Empty" is detected by the absence of the {@code roles} table (one of the base
 * schema tables). If the schema is already present nothing is done, so existing data
 * is never overwritten. The schema/seed scripts are bundled on the classpath under
 * {@code db/} (copied from scripts/cv-db-microservice-scripts, with the known
 * {@code password_hash} -> {@code password} seed-column fix applied).</p>
 */
@Component
public class DatabaseInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DatabaseInitializer.class);

    private static final String SCHEMA_SCRIPT = "db/create_tables.sql";
    private static final String SEED_SCRIPT = "db/populate_tables.sql";

    private final DataSource dataSource;

    public DatabaseInitializer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            if (schemaExists(connection)) {
                log.info("cv-db schema already present; skipping initialization.");
                return;
            }
            log.info("cv-db schema not found; creating schema and seeding from {} + {}.",
                    SCHEMA_SCRIPT, SEED_SCRIPT);
            long start = System.currentTimeMillis();
            ScriptUtils.executeSqlScript(connection, new ClassPathResource(SCHEMA_SCRIPT));
            ScriptUtils.executeSqlScript(connection, new ClassPathResource(SEED_SCRIPT));
            log.info("cv-db initialized in {} ms.", System.currentTimeMillis() - start);
        }
    }

    private boolean schemaExists(Connection connection) throws Exception {
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(
                     "SELECT name FROM sqlite_master WHERE type='table' AND name='roles'")) {
            return rs.next();
        }
    }
}
