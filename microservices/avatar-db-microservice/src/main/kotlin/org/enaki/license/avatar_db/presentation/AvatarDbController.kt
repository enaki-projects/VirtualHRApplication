package org.enaki.license.avatar_db.presentation

import org.enaki.license.avatar_db.interfaces.AvatarService
import org.enaki.license.avatar_db.model.AvatarScene
import org.enaki.license.avatar_db.model.Scenario
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.UrlResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.MediaTypeFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.io.File

@RestController
class AvatarDbController{

    @Autowired
    private lateinit var _avatarService: AvatarService

    @Value("\${avatar.resources-dir}")
    private lateinit var resourcesDir: String

    @RequestMapping("/api/avatars", method = [RequestMethod.GET])
    fun getAvatarScenes(): ResponseEntity<List<AvatarScene>> {
        val avatarScenes = _avatarService.getAvatarScenes()
        val status = if (avatarScenes.isEmpty()) HttpStatus.NO_CONTENT else HttpStatus.OK
        return ResponseEntity(avatarScenes, status)
    }

    @RequestMapping("/api/avatar-info", method = [RequestMethod.GET])
    fun getAvatarInfoScene(scenario: Scenario): ResponseEntity<AvatarScene> {
        val avatarScene = _avatarService.getAvatarScene(scenario)
        val status = if (avatarScene == null) HttpStatus.NOT_FOUND else HttpStatus.OK
        return ResponseEntity.status(status).body(avatarScene)
    }

    @RequestMapping("/api/avatar", method = [RequestMethod.GET])
    fun getAvatarScene(scenario: Scenario): ResponseEntity<UrlResource?> {
        val avatarScene = _avatarService.getAvatarScene(scenario)
        println(avatarScene?.relativePath)
        val resourceUrl = if (avatarScene != null)
            UrlResource(File("$resourcesDir/avatar/${avatarScene.relativePath}").toURI())
        else null
        val status = if (avatarScene == null) HttpStatus.NOT_FOUND else HttpStatus.OK
        return ResponseEntity.status(status).contentType(MediaTypeFactory
                .getMediaType(resourceUrl)
                .orElse(MediaType.APPLICATION_OCTET_STREAM))
                .body(resourceUrl)
    }
}
