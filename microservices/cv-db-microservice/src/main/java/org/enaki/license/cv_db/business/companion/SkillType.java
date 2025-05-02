package org.enaki.license.cv_db.business.companion;

public enum SkillType {
    NONE,
    LEADERSHIP {
        @Override
        public String toString() {
            return "leadership_skills";
        }
    },
    ORGANISATIONAL{
        @Override
        public String toString() {
            return "organisational_skills";
        }
    },
    DIGITAL{
        @Override
        public String toString() {
            return "digital_skills";
        }
    },
    INTERPERSONAL{
        @Override
        public String toString() {
            return "interpersonal_skills";
        }
    }
}