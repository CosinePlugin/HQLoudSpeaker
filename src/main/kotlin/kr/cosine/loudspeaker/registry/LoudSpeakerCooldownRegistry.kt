package kr.cosine.loudspeaker.registry

import kr.hqservice.framework.global.core.component.Bean
import java.util.UUID

@Bean
class LoudSpeakerCooldownRegistry {
    private val cooldownMap = mutableMapOf<UUID, Long>()

    var isChanged = false

    fun restore(loudSpeakerCooldownRegistry: LoudSpeakerCooldownRegistry) {
        cooldownMap.clear()
        cooldownMap.putAll(loudSpeakerCooldownRegistry.cooldownMap)
    }

    operator fun get(uniqueId: UUID): Long {
        return cooldownMap.getOrDefault(uniqueId, 0)
    }

    operator fun set(uniqueId: UUID, cooldown: Long) {
        cooldownMap[uniqueId] = cooldown
        isChanged = true
    }
}