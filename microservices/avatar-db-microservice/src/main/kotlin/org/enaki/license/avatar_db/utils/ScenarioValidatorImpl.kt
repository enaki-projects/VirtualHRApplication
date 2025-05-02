package org.enaki.license.avatar_db.utils

import org.enaki.license.avatar_db.interfaces.ScenarioValidator
import org.enaki.license.avatar_db.model.Scenario
import org.springframework.stereotype.Service

@Service
class ScenarioValidatorImpl: ScenarioValidator {
    val availableLanguages = listOf("en", "ro")
    val availableResolutions = listOf("720x720", "800x600", "1280x720")
    val availableCategories = listOf("others", "cv", "iq", "manager", "candidate")
    val availableSubcategories = listOf("intro", "first_name", "last_name", "phone_number", "page", "view_page", "personal_info",
    "contact", "nationality", "language", "location", "hobby", "projects", "awards", "educational_experiences", "professional_experiences", "skills"
    , "publications", "analysis", "result", "candidates", "carts")

    override fun validate(scenario: Scenario): Boolean {
        return scenario.language in availableLanguages && scenario.resolution in availableResolutions && scenario.category in availableCategories && scenario.subcategory in availableSubcategories
    }
}
