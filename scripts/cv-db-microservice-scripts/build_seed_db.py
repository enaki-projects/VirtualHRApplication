#!/usr/bin/env python3
"""Build the full cv-db SQLite seed database offline.

The original generator (cv_db_script_and_data_generator/main.py) downloads its
location data from URLs that no longer exist, so this script rebuilds the same
database from the SQL files the generator already produced (output/*.sql) plus
the local JSON in data/. It needs only the Python standard library.

Usage: python3 build_seed_db.py <output.db>
"""
import os
import re
import sqlite3
import sys
import time

HERE = os.path.dirname(os.path.abspath(__file__))
GEN = os.path.join(HERE, "cv_db_script_and_data_generator")
OUT = os.path.join(GEN, "output")

# Order matters because of foreign keys.
SQL_FILES = [
    "insert_country.sql",
    "insert_region.sql",
    "insert_city.sql",
    "insert_nationality.sql",  # despite the name, this file fills the "languages" table
    "insert_leadership_skills.sql",
    "insert_organisational_skills.sql",
    "insert_digital_skills.sql",
    "insert_interpersonal_skills.sql",
    "insert_roles.sql",
    "insert_educational_institutions.sql",
]

LINE_RE = re.compile(r'^(INSERT (?:OR REPLACE )?INTO "\w+" \([^)]*\) VALUES )\((.*)\);\s*$')


def fix_line(line):
    """Repair known defects in the generated SQL."""
    # The universities generator filtered on a column that is called country_name.
    line = line.replace("from countries where name=", "from countries where country_name=")
    return line


def repair_quotes(cur, line):
    """Re-run a row whose double-quoted values contain unescaped double quotes."""
    m = LINE_RE.match(line)
    if not m:
        raise ValueError("unparseable line: " + line[:120])
    prefix, body = m.groups()
    if not (body.startswith('"') and body.endswith('"')):
        raise ValueError("unexpected value list: " + line[:120])
    values = body[1:-1].split('", "')
    placeholders = ", ".join("?" for _ in values)
    cur.execute(prefix + "(" + placeholders + ")", values)


def run_sql_file(conn, path):
    cur = conn.cursor()
    with open(path, encoding="utf-8") as f:
        lines = [fix_line(l) for l in f if l.strip()]
    try:
        cur.execute("BEGIN")
        for line in lines:
            try:
                cur.execute(line)
            except sqlite3.OperationalError:
                repair_quotes(cur, line)
        conn.commit()
    except Exception:
        conn.rollback()
        raise
    return len(lines)


def main(db_path):
    if os.path.exists(db_path):
        os.remove(db_path)
    start = time.time()
    conn = sqlite3.connect(db_path, isolation_level=None)
    conn.execute("PRAGMA journal_mode=OFF")
    conn.execute("PRAGMA synchronous=OFF")
    with open(os.path.join(HERE, "create_tables.sql"), encoding="utf-8") as f:
        conn.executescript(f.read())

    for name in SQL_FILES:
        n = run_sql_file(conn, os.path.join(OUT, name))
        print(f"{name}: {n} rows")

    # The JSON-driven inserts reuse the original generator modules (no network access).
    sys.path.insert(0, GEN)
    sys.argv = sys.argv[:1]  # the generator modules parse argv at import time
    conn.close()

    import json
    with open(os.path.join(GEN, "data", "nationalities.json"), encoding="utf-8") as f:
        nationalities = json.load(f)
    conn = sqlite3.connect(db_path)
    conn.executemany('INSERT OR REPLACE INTO "nationalities" ("nationality") VALUES (?)',
                     [(n,) for n in nationalities])
    conn.commit()
    conn.close()
    print(f"nationalities: {len(nationalities)} rows")

    cwd = os.getcwd()
    os.chdir(GEN)
    try:
        import organisations
        import users
        import cv
        organisations.insert_organisations(database_path=db_path, datapath="data/organisations.json")
        users.insert_users(database_path=db_path, datapath="data/users.json")
        cv.insert_cvs(database_path=db_path, datapath="data/users.json")
    finally:
        os.chdir(cwd)

    conn = sqlite3.connect(db_path)
    conn.execute("VACUUM")
    for t in ["countries", "regions", "cities", "languages", "nationalities", "roles",
              "educational_institutions", "organisations", "users", "cv", "user_roles"]:
        print(f"  {t}: {conn.execute(f'SELECT COUNT(*) FROM {t}').fetchone()[0]}")
    conn.close()
    print(f"built {db_path} in {time.time() - start:.1f}s")


if __name__ == "__main__":
    if len(sys.argv) != 2:
        sys.exit("usage: build_seed_db.py <output.db>")
    main(os.path.abspath(sys.argv[1]))
