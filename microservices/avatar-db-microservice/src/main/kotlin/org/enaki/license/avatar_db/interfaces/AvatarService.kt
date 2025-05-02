package org.enaki.license.avatar_db.interfaces

import org.enaki.license.avatar_db.model.AvatarScene
import org.enaki.license.avatar_db.model.Scenario

interface AvatarService {
    fun getAvatarScene(scenario: Scenario): AvatarScene?
    fun getAvatarScenes(): List<AvatarScene>
}