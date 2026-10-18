package org.enaki.license.manager_db

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

@SpringBootApplication
class ManagerDbApplication

/**
 * Resolve the SQLite file location (DB_PATH env, default ./manager.db) and, if it does not yet
 * exist, seed it from SEED_DB_PATH (used by the container image, which ships a read-only seed copy
 * under /app/seed). Existing data is never overwritten. This runs before Spring opens the
 * DataSource so the file is in place when connection pooling starts.
 */
private fun seedDatabaseIfMissing() {
    val dbPath = (System.getenv("DB_PATH") ?: "./manager.db").trim()
    val seedPath = System.getenv("SEED_DB_PATH")?.trim().orEmpty()
    if (seedPath.isEmpty()) return

    val target = Path.of(dbPath)
    val seed = Path.of(seedPath)
    if (Files.exists(target)) {
        println("[manager-db] Using existing database at $dbPath (seed skipped)")
        return
    }
    if (!Files.exists(seed)) {
        println("[manager-db] WARNING: seed database $seedPath not found; starting with an empty database at $dbPath")
        return
    }
    target.parent?.let { Files.createDirectories(it) }
    Files.copy(seed, target, StandardCopyOption.COPY_ATTRIBUTES)
    println("[manager-db] Seeded database $dbPath from $seedPath")
}

fun main(args: Array<String>) {
    seedDatabaseIfMissing()
    runApplication<ManagerDbApplication>(*args)
}
