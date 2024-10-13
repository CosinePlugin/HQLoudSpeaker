package kr.cosine.loudspeaker.registry

import kr.hqservice.framework.global.core.component.Bean
import org.bukkit.inventory.ItemStack

@Bean
class SettingRegistry {
    private var loudSpeaker: ItemStack? = null

    private var loudSpeakerMessages = emptyList<String>()

    fun isLoudSpeaker(itemStack: ItemStack): Boolean {
        return loudSpeaker?.isSimilar(itemStack) == true
    }

    fun findLoudSpeaker(): ItemStack? {
        return loudSpeaker?.clone()
    }

    fun setLoudSpeaker(loudSpeaker: ItemStack) {
        this.loudSpeaker = loudSpeaker
    }

    fun getLoudSpeakerMessages(replace: (String) -> String): List<String> {
        return loudSpeakerMessages.map(replace)
    }

    fun setLoudSpeakerMessages(loudSpeakerMessages: List<String>) {
        this.loudSpeakerMessages = loudSpeakerMessages
    }

    fun clear() {
        loudSpeaker = null
        loudSpeakerMessages = emptyList()
    }
}