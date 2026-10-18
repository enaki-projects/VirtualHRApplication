package org.enaki.license.cv_db;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.zip.GZIPInputStream;

@SpringBootApplication
public class CvDbApplication {

    public static void main(String[] args) throws IOException {
        seedDatabaseIfMissing();
        seedImagesIfMissing();
        SpringApplication.run(CvDbApplication.class, args);
    }

    /** Copy the demo users' photos (SEED_IMAGES_DIR) into IMAGES_DIR the first time only. */
    private static void seedImagesIfMissing() throws IOException {
        String seed = System.getenv("SEED_IMAGES_DIR");
        String dir = System.getenv("IMAGES_DIR");
        if (seed == null || dir == null) return;
        Path source = Path.of(seed), target = Path.of(dir);
        if (Files.exists(target) || !Files.isDirectory(source)) return;
        try (var paths = Files.walk(source)) {
            for (Path p : (Iterable<Path>) paths::iterator) {
                Path dest = target.resolve(source.relativize(p).toString());
                if (Files.isDirectory(p)) Files.createDirectories(dest);
                else Files.copy(p, dest, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        System.out.println("[cv-db] Seeded user images into " + target);
    }

    /**
     * If DB_PATH does not exist yet and SEED_DB_PATH points at a (optionally gzipped) SQLite file,
     * copy it into place before Spring opens the DataSource. The container image ships the full
     * reference data (countries, regions, cities, institutions, demo users) as
     * /app/seed/cv-seed.db.gz. Existing data is never overwritten. Without a seed the
     * DatabaseInitializer still creates the bare schema.
     */
    private static void seedDatabaseIfMissing() throws IOException {
        String seed = System.getenv("SEED_DB_PATH");
        if (seed == null || seed.isBlank()) return;
        Path target = Path.of(System.getenv().getOrDefault("DB_PATH", "./cv.db"));
        Path seedPath = Path.of(seed.trim());
        if (Files.exists(target) && Files.size(target) > 0) {
            System.out.println("[cv-db] Using existing database at " + target + " (seed skipped)");
            return;
        }
        if (!Files.exists(seedPath)) {
            System.out.println("[cv-db] WARNING: seed " + seedPath + " not found; starting with an empty schema");
            return;
        }
        if (target.toAbsolutePath().getParent() != null) {
            Files.createDirectories(target.toAbsolutePath().getParent());
        }
        Path tmp = target.resolveSibling(target.getFileName() + ".seeding");
        try (InputStream raw = Files.newInputStream(seedPath);
             InputStream in = seedPath.toString().endsWith(".gz") ? new GZIPInputStream(raw) : raw) {
            Files.copy(in, tmp, StandardCopyOption.REPLACE_EXISTING);
        }
        Files.move(tmp, target, StandardCopyOption.ATOMIC_MOVE);
        System.out.println("[cv-db] Seeded database " + target + " from " + seedPath);
    }
}
