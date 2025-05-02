CREATE TABLE "roles" (
	"role_id"	INTEGER NOT NULL,
	"role_name"	TEXT UNIQUE,
	"description" TEXT
);

CREATE TABLE "users" (
                         "uid"	INTEGER PRIMARY KEY,
                         "email" TEXT UNIQUE,
                         "password" INTEGER
);

CREATE TABLE "user_roles" (
	"uid"	INTEGER NOT NULL,
	"role_id"	INTEGER NOT NULL,
	CONSTRAINT "user_roles_uk" UNIQUE("uid","role_id")
);

CREATE TABLE "cv" (
	"uid"	INTEGER NOT NULL,
	"first_name" TEXT NOT NULL,
	"last_name"	TEXT NOT NULL,
	"email"	TEXT NOT NULL,
	"gender" TEXT NOT NULL,
	"birthdate" DATE NOT NULL,
	"about_me" TEXT,
	"motto" TEXT,
	"image_name" TEXT,
	"iq_score" INTEGER,
	"can_take_the_iq_test" BOOLEAN DEFAULT True,
	"requested_retake_of_iq_test" BOOLEAN DEFAULT False,
	CONSTRAINT "cv_uid_pk" PRIMARY KEY(uid),
	CONSTRAINT "cv_uid_fk" FOREIGN KEY(uid) REFERENCES "users"("uid")
);

CREATE TABLE "countries" (
	"country_id"	INTEGER NOT NULL,
	"country_name"	TEXT NOT NULL,
	"iso3"	CHARACTER(3),
	"iso2"	CHARACTER(2),
	"phone_code" TEXT,
	"capital" TEXT,
	"currency" TEXT,
	CONSTRAINT "countries_country_id_pk" PRIMARY KEY(country_id)
);

CREATE TABLE "regions" (
	"region_id"	INTEGER NOT NULL,
	"country_id"	INTEGER NOT NULL,
	"region_name"	TEXT,
	"region_code" TEXT,
	
	CONSTRAINT "regions_region_id_pk" PRIMARY KEY("region_id"),
	CONSTRAINT "regions_country_id_fk" FOREIGN KEY(country_id) REFERENCES "countries"("country_id")
);

CREATE TABLE "cities" (
	"city_id"	INTEGER NOT NULL,
	"region_id"	INTEGER NOT NULL,
	"city_name"	TEXT,
	"latitude"	REAL,
	"longitude"	REAL,
	
	CONSTRAINT "cities_city_id_pk" PRIMARY KEY("city_id"),
	CONSTRAINT "cities_region_id_fk" FOREIGN KEY(region_id) REFERENCES "regions"("region_id")	
);

CREATE TABLE "locations" (
	"location_id"	INTEGER NOT NULL,
	"uid"	INTEGER NOT NULL,
	"city_id"	INTEGER NOT NULL,
	"type"	TEXT,
	"address"	TEXT,
	CONSTRAINT "locations_uk" UNIQUE("uid","city_id","type","address"),
	CONSTRAINT "locations_location_id_pk" PRIMARY KEY("location_id"),
	CONSTRAINT "locations_uid_fk" FOREIGN KEY(uid) REFERENCES "users"("uid"),
	CONSTRAINT "locations_city_id_fk" FOREIGN KEY(city_id) REFERENCES "cities"("city_id")
);

CREATE TABLE "organisations" (
	"organisation_id"	INTEGER NOT NULL,
	"city_id"	INTEGER NOT NULL,
	"organisation_name"	TEXT NOT NULL,
	"address"	TEXT,
	CONSTRAINT "organisations_uk" UNIQUE("city_id","organisation_name"),
	CONSTRAINT "organisations_id_pk" PRIMARY KEY("organisation_id"),
	CONSTRAINT "organisations_city_id_fk" FOREIGN KEY(city_id) REFERENCES "cities"("city_id")
);

CREATE TABLE "educational_institutions" (
	"educational_institution_id"	INTEGER NOT NULL,
	"country_id" INTEGER,
	"institution_name"	TEXT NOT NULL,
	"alt_institution_name"	TEXT,
	"address"	TEXT,
	"telephone"	TEXT,
	"website"	TEXT,
	"email"	TEXT,
	"funding"	TEXT,
	"accrediting_agency" TEXT,
	CONSTRAINT "educational_institutions_educational_institution_id_pk" PRIMARY KEY("educational_institution_id"),
	CONSTRAINT "educational_institutions_country_id_fk" FOREIGN KEY(country_id) REFERENCES "countries"("country_id")
);

CREATE TABLE "professional_experiences" (
	"professional_experience_id" INTEGER NOT NULL,
	"organisation_id" INTEGER NOT NULL,
	"uid" INTEGER NOT NULL,
	"job_name"	TEXT NOT NULL,
	"description"	TEXT,
	"from_date"	DATE NOT NULL,
	"to_date"	DATE,
	CONSTRAINT "professional_experiences_uk" UNIQUE("uid","job_name","from_date","organisation_id"),
	CONSTRAINT "professional_experiences_id_pk" PRIMARY KEY("professional_experience_id"),
	CONSTRAINT "professional_experiences_uid_fk" FOREIGN KEY(uid) REFERENCES "users"("uid"),
	CONSTRAINT "professional_experiences_company_location_id_fk" FOREIGN KEY(organisation_id) REFERENCES "organisations"("organisation_id")
);

CREATE TABLE "educational_experiences" (
	"educational_experience_id" INTEGER NOT NULL,
	"educational_institution_id" INTEGER NOT NULL,
	"uid" INTEGER NOT NULL,
	"specialty"	TEXT NOT NULL,
	"qualification_title" TEXT NOT NULL,
	"from_date"	DATE NOT NULL,
	"to_date"	DATE,
	CONSTRAINT "educational_experiences_uk" UNIQUE("educational_institution_id", "uid","qualification_title","specialty","from_date"),
	CONSTRAINT "educational_experiences_id_pk" PRIMARY KEY("educational_experience_id"),
	CONSTRAINT "educational_experiences_uid_fk" FOREIGN KEY(uid) REFERENCES "users"("uid"),
	CONSTRAINT "educational_experiences_educational_institution_id_fk" FOREIGN KEY(educational_institution_id) REFERENCES "educational_institutions"("educational_institution_id")
);

CREATE TABLE "nationalities" (
	"nationality_id" INTEGER NOT NULL,
	"nationality" TEXT NOT NULL UNIQUE,
	CONSTRAINT "nationalities_id_pk" PRIMARY KEY("nationality_id"),
	CONSTRAINT "citizenships_nationality_id_fk" FOREIGN KEY(nationality_id) REFERENCES "nationalities"("nationality_id")
);

CREATE TABLE "citizenships" (
	"uid" INTEGER NOT NULL,
	"nationality_id" INTEGER NOT NULL,
	CONSTRAINT "citizenships_uid_fk" FOREIGN KEY(uid) REFERENCES "users"("uid"),
	CONSTRAINT "citizenships_nationality_id_fk" FOREIGN KEY(nationality_id) REFERENCES "nationalities"("nationality_id"),
	CONSTRAINT "citizenships_uk" UNIQUE("uid","nationality_id")
);

CREATE TABLE "awards" (
	"award_id" INTEGER NOT NULL,
	"uid" INTEGER NOT NULL,
	"title" TEXT NOT NULL,
	"awarding_institution" TEXT NOT NULL,
	"date_of_award" DATE NOT NULL,
	"description" TEXT,
	CONSTRAINT "award_id_pk" PRIMARY KEY("award_id"),
	CONSTRAINT "awards_uid_fk" FOREIGN KEY(uid) REFERENCES "users"("uid"),
	CONSTRAINT "awards_uk" UNIQUE("uid","title","awarding_institution","date_of_award")
);

CREATE TABLE "hobbies" (
	"hobby_id" INTEGER NOT NULL,
	"uid" INTEGER NOT NULL,
	"name" TEXT NOT NULL,
	"description" TEXT,
	CONSTRAINT "hobbies_id_pk" PRIMARY KEY("hobby_id"),
	CONSTRAINT "hobbies_uk" UNIQUE("uid", "name"),
	CONSTRAINT "hobbies_uid_fk" FOREIGN KEY(uid) REFERENCES "users"("uid")
);

CREATE TABLE "projects" (
	"project_id" INTEGER NOT NULL,
	"uid" INTEGER NOT NULL,
	"name" TEXT NOT NULL,
	"url" TEXT,
	"description" TEXT,
	"from_date"	DATE,
	"to_date"	DATE,
	CONSTRAINT "projects_id_pk" PRIMARY KEY("project_id"),
	CONSTRAINT "projects_uid_fk" FOREIGN KEY(uid) REFERENCES "users"("uid"),
	CONSTRAINT "projects_uk" UNIQUE("uid","name","from_date")
);

CREATE TABLE "phone_numbers" (
	"phone_number_id" INTEGER NOT NULL,
	"uid" INTEGER NOT NULL,
	"country_id" INTEGER NOT NULL,
	"number" TEXT NOT NULL,
	CONSTRAINT "phone_numbers_id_pk" PRIMARY KEY("phone_number_id"),
	CONSTRAINT "phone_numbers_uid_fk" FOREIGN KEY(uid) REFERENCES "users"("uid"),
	CONSTRAINT "phone_numbers_country_id_fk" FOREIGN KEY(country_id) REFERENCES "countries"("country_id"),
	CONSTRAINT "projects_uk" UNIQUE("uid","number","country_id")
);

CREATE TABLE "publications" (
	"publication_id" INTEGER NOT NULL,
	"uid" INTEGER NOT NULL,
	"title" TEXT NOT NULL,
	"description" TEXT,
	"year"	DATE NOT NULL,
	"url"	TEXT,
	"reference"	TEXT,
	CONSTRAINT "publications_id_pk" PRIMARY KEY("publication_id"),
	CONSTRAINT "publications_uid_fk" FOREIGN KEY(uid) REFERENCES "users"("uid"),
	CONSTRAINT "publications_uk" UNIQUE("uid","title","year")
);

CREATE TABLE "iq_tests" (
	"iq_test_id" INTEGER NOT NULL,
	"uid" INTEGER NOT NULL,
	"test_name" TEXT NOT NULL,
	"exact_iq"	NUMBER,
	"iq"	NUMBER NOT NULL,
	"iq_adjusted_by_age"	NUMBER,
	"time_taken_in_seconds"	NUMBER NOT NULL,
	"submitted_on"	DATETIME NOT NULL,
	"diagnostic" TEXT,
	"user_answers" TEXT NOT NULL,
	"description" TEXT,
	CONSTRAINT "iq_tests_id_pk" PRIMARY KEY("iq_test_id"),
	CONSTRAINT "iq_tests_uid_fk" FOREIGN KEY(uid) REFERENCES "users"("uid"),
	CONSTRAINT "iq_tests_uk" UNIQUE("uid", "test_name", "submitted_on")
);

CREATE TABLE "languages" (
	"language_id" INTEGER NOT NULL,
	"name" TEXT NOT NULL UNIQUE,
	"native_name" TEXT,
	"code" CHARACTER(2) NOT NULL UNIQUE,
	CONSTRAINT "languages_id_pk" PRIMARY KEY("language_id")
);

CREATE TABLE "user_languages" (
	"user_language_id" INTEGER NOT NULL,
	"uid" INTEGER NOT NULL,
	"language_id" INTEGER NOT NULL,
	"listening" CHARACTER(2) NOT NULL CHECK(listening in ("A1", "A2", "B1", "B2", "C1", "C2")),
	"reading" CHARACTER(2) NOT NULL CHECK(listening in ("A1", "A2", "B1", "B2", "C1", "C2")),
	"spoken_interaction" CHARACTER(2) NOT NULL CHECK(listening in ("A1", "A2", "B1", "B2", "C1", "C2")),
	"spoken_production" CHARACTER(2) NOT NULL CHECK(listening in ("A1", "A2", "B1", "B2", "C1", "C2")),
	"writing" CHARACTER(2) NOT NULL CHECK(listening in ("A1", "A2", "B1", "B2", "C1", "C2")),
	CONSTRAINT "user_languages_id_pk" PRIMARY KEY("user_language_id"),
	CONSTRAINT "user_languages_uid_fk" FOREIGN KEY(language_id) REFERENCES "languages"("language_id"),
	CONSTRAINT "user_languages_uid_fk" FOREIGN KEY(uid) REFERENCES "users"("uid"),
	CONSTRAINT "user_languages_uk" UNIQUE("uid", "language_id")
);

CREATE TABLE "leadership_skills"(
	"skill_id" INTEGER NOT NULL,
	"name" TEXT NOT NULL UNIQUE,
	"description" TEXT,
	CONSTRAINT "leadership_skills_id_pk" PRIMARY KEY("skill_id")
);

CREATE TABLE "user_leadership_skills"(
	"skill_id" INTEGER NOT NULL,
	"uid" INTEGER NOT NULL,
	CONSTRAINT "user_leadership_skills_id_fk" FOREIGN KEY(skill_id) REFERENCES "leadership_skills"("skill_id"),
	CONSTRAINT "user_leadership_skills_uid_fk" FOREIGN KEY(uid) REFERENCES "users"("uid"),
	CONSTRAINT "user_leadership_skills_uk" UNIQUE("uid", "skill_id")
);

CREATE TABLE "organisational_skills"(
	"skill_id" INTEGER NOT NULL,
	"name" TEXT NOT NULL UNIQUE,
	"description" TEXT,
	CONSTRAINT "organisational_skills_id_pk" PRIMARY KEY("skill_id")
);

CREATE TABLE "user_organisational_skills"(
	"skill_id" INTEGER NOT NULL,
	"uid" INTEGER NOT NULL,
	CONSTRAINT "user_organisational_skills_id_fk" FOREIGN KEY(skill_id) REFERENCES "organisational_skills"("skill_id"),
	CONSTRAINT "user_organisational_skills_uid_fk" FOREIGN KEY(uid) REFERENCES "users"("uid"),
	CONSTRAINT "user_organisational_skills_uk" UNIQUE("uid", "skill_id")
);

CREATE TABLE "digital_skills"(
	"skill_id" INTEGER NOT NULL,
	"name" TEXT NOT NULL UNIQUE,
	"description" TEXT,
	CONSTRAINT "digital_skills_id_pk" PRIMARY KEY("skill_id")
);

CREATE TABLE "user_digital_skills"(
	"skill_id" INTEGER NOT NULL,
	"uid" INTEGER NOT NULL,
	CONSTRAINT "user_digital_skills_id_fk" FOREIGN KEY(skill_id) REFERENCES "digital_skills"("skill_id"),
	CONSTRAINT "user_digital_skills_uid_fk" FOREIGN KEY(uid) REFERENCES "users"("uid"),
	CONSTRAINT "user_digital_skills_uk" UNIQUE("uid", "skill_id")
);

CREATE TABLE "interpersonal_skills"(
	"skill_id" INTEGER NOT NULL,
	"name" TEXT NOT NULL UNIQUE,
	"description" TEXT,
	CONSTRAINT "interpersonal_skills_id_pk" PRIMARY KEY("skill_id")
);

CREATE TABLE "user_interpersonal_skills"(
	"skill_id" INTEGER NOT NULL,
	"uid" INTEGER NOT NULL,
	CONSTRAINT "user_interpersonal_skills_id_fk" FOREIGN KEY(skill_id) REFERENCES "interpersonal_skills"("skill_id"),
	CONSTRAINT "user_interpersonal_skills_uid_fk" FOREIGN KEY(uid) REFERENCES "users"("uid"),
	CONSTRAINT "user_interpersonal_skills_uk" UNIQUE("uid", "skill_id")
);

CREATE UNIQUE INDEX cities_city_id_index ON cities(city_id);
CREATE INDEX cities_region_id_index ON cities(region_id);

CREATE UNIQUE INDEX regions_region_id_index ON regions(region_id);
CREATE INDEX regions_country_id_index ON regions(country_id);

CREATE UNIQUE INDEX countries_country_id_index ON countries(country_id);
