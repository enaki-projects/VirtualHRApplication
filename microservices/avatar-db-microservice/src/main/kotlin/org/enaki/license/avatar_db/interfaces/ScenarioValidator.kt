package org.enaki.license.avatar_db.interfaces

import org.enaki.license.avatar_db.model.Scenario

interface ScenarioValidator {
    fun validate(scenario: Scenario): Boolean
}
