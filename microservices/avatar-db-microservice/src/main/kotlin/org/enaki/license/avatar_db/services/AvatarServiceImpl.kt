package org.enaki.license.avatar_db.services

import org.enaki.license.avatar_db.interfaces.AvatarService
import org.enaki.license.avatar_db.interfaces.ScenarioValidator
import org.enaki.license.avatar_db.model.AvatarScene
import org.enaki.license.avatar_db.model.Scenario
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory


@Service
class AvatarServiceImpl : AvatarService{

    @Autowired
    private lateinit var _scenarioValidator: ScenarioValidator

    override fun getAvatarScene(scenario: Scenario): AvatarScene? {
        if (_scenarioValidator.validate(scenario).not()) return null

        val docBuilderFactory = DocumentBuilderFactory.newInstance()
        val docBuilder = docBuilderFactory.newDocumentBuilder()
        val doc: Document = docBuilder.parse(File("src/main/resources/available_languages/${scenario.language}_avatar.xml"))

        val nList: NodeList = doc.getElementsByTagName("scene")
        println(nList)
        for (temp in 0 until nList.length) {
            val nNode: Node = nList.item(temp)
            if (nNode.nodeType === Node.ELEMENT_NODE) {
                val eElement: Element = nNode as Element

                val name = eElement.getElementsByTagName("name").item(0).textContent
                if (name != scenario.name) continue

                val category = eElement.getElementsByTagName("category").item(0).textContent
                if (category != scenario.category) continue

                val subcategory = eElement.getElementsByTagName("subcategory").item(0).textContent
                if (subcategory != scenario.subcategory) continue

                val language = eElement.getElementsByTagName("language").item(0).textContent
                if (language != scenario.language) continue

                val videos = eElement.getElementsByTagName("video")
                for (index in 0 until videos.length){
                    val videoElement = videos.item(index) as Element

                    val resolution = videoElement.getElementsByTagName("resolution").item(0).textContent
                    if (resolution != scenario.resolution) continue

                    val path = videoElement.getElementsByTagName("path").item(0).textContent
                    val textToSpeech = eElement.getElementsByTagName("text-to-speech").item(0).textContent
                    val videoLink = videoElement.getElementsByTagName("video-link").item(0).textContent

                    println("Video Res: $resolution")
                    println("Video Path: $path")

                    val currentScenario = Scenario(name, category, subcategory, resolution, language)
                    return AvatarScene(currentScenario, textToSpeech, path, videoLink)
                }
            }
        }
        return null
    }

    override fun getAvatarScenes(): List<AvatarScene> {
        val avatarScenes = mutableListOf<AvatarScene>()

        val docBuilderFactory = DocumentBuilderFactory.newInstance()
        val docBuilder = docBuilderFactory.newDocumentBuilder()
        val doc: Document = docBuilder.parse(File("src/main/resources/avatar.xml"))

        val nList: NodeList = doc.getElementsByTagName("scene")
        println(nList)
        for (temp in 0 until nList.length) {
            val nNode: Node = nList.item(temp)
            if (nNode.nodeType === Node.ELEMENT_NODE) {
                val eElement: Element = nNode as Element

                val name = eElement.getElementsByTagName("name").item(0).textContent
                val category = eElement.getElementsByTagName("category").item(0).textContent
                val subcategory = eElement.getElementsByTagName("subcategory").item(0).textContent
                val textToSpeech = eElement.getElementsByTagName("text-to-speech").item(0).textContent
                val language = eElement.getElementsByTagName("language").item(0).textContent

                println("Name : $name")
                println("Category : $category")
                println("SubCategory : $subcategory")
                println("Language : $language")
                println("Text To Speech : $textToSpeech")

                val videos = eElement.getElementsByTagName("video")
                for (index in 0 until videos.length){
                    val videoElement = videos.item(index) as Element

                    val resolution = videoElement.getElementsByTagName("resolution").item(0).textContent
                    val path = videoElement.getElementsByTagName("path").item(0).textContent
                    val videoLink = videoElement.getElementsByTagName("video-link").item(0).textContent

                    val currentScenario = Scenario(name, category, subcategory, resolution, language)
                    val avatarScene = AvatarScene(currentScenario, textToSpeech, path, videoLink)
                    avatarScenes.add(avatarScene)
                }
            }
        }
        return avatarScenes
    }

}