package kr.cosine.loudspeaker.json

import com.google.gson.GsonBuilder
import kr.cosine.loudspeaker.registry.LoudSpeakerCooldownRegistry
import kr.hqservice.framework.bukkit.core.HQBukkitPlugin
import kr.hqservice.framework.global.core.component.Bean
import org.bukkit.plugin.Plugin
import java.io.File

@Bean
class LoudSpeakerCooldownJson(
    plugin: HQBukkitPlugin,
    private val loudSpeakerCooldownRegistry: LoudSpeakerCooldownRegistry
) {
    private val gson = GsonBuilder().setPrettyPrinting().create()

    private val file = File(plugin.dataFolder, "loud-speaker-cooldown.json")

    fun load() {
        if (!file.exists()) return
        val loudSpeakerCooldownRegistry = gson.fromJson(file.bufferedReader(), LoudSpeakerCooldownRegistry::class.java)
        this.loudSpeakerCooldownRegistry.restore(loudSpeakerCooldownRegistry)
    }

    fun save() {
        if (loudSpeakerCooldownRegistry.isChanged) {
            loudSpeakerCooldownRegistry.isChanged = false
            val json = gson.toJson(loudSpeakerCooldownRegistry)
            file.bufferedWriter().use {
                it.appendLine(json)
                it.flush()
            }
        }
    }
}