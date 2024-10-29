package kr.cosine.loudspeaker.service

import kr.cosine.loudspeaker.config.SettingConfig
import kr.cosine.loudspeaker.database.repository.LoudSpeakerCooldownRepository
import kr.cosine.loudspeaker.registry.LoudSpeakerCooldownRegistry
import kr.hqservice.framework.global.core.component.Service
import java.util.*

@Service
class LoudSpeakerCooldownService(
    private val settingConfig: SettingConfig,
    private val loudSpeakerCooldownRepository: LoudSpeakerCooldownRepository,
    private val loudSpeakerCooldownRegistry: LoudSpeakerCooldownRegistry,
    private val proxyService: ProxyService
) {
    fun getCooldown(uniqueId: UUID): Long {
        return if (proxyService.isProxyConnected()) {
            loudSpeakerCooldownRepository[uniqueId]
        } else {
            loudSpeakerCooldownRegistry[uniqueId]
        }
    }

    fun applyCooldown(uniqueId: UUID) {
        val cooldown = System.currentTimeMillis() + settingConfig.loudSpeakerCooldown
        if (proxyService.isProxyConnected()) {
            loudSpeakerCooldownRepository[uniqueId] = cooldown
        } else {
            loudSpeakerCooldownRegistry[uniqueId] = cooldown
        }
    }
}