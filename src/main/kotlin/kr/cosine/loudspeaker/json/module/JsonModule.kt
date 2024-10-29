package kr.cosine.loudspeaker.json.module

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kr.cosine.loudspeaker.json.LoudSpeakerCooldownJson
import kr.cosine.loudspeaker.service.ProxyService
import kr.hqservice.framework.bukkit.core.HQBukkitPlugin
import kr.hqservice.framework.bukkit.core.component.module.Module
import kr.hqservice.framework.bukkit.core.component.module.Setup
import kr.hqservice.framework.bukkit.core.component.module.Teardown
import kr.hqservice.framework.bukkit.core.coroutine.bukkitDelay
import kr.hqservice.framework.bukkit.core.coroutine.element.TeardownOptionCoroutineContextElement

@Module
class JsonModule(
    private val plugin: HQBukkitPlugin,
    private val loudSpeakerCooldownJson: LoudSpeakerCooldownJson,
    private val proxyService: ProxyService
) {
    @Setup
    fun setup() {
        if (proxyService.isProxyConnected()) return
        loudSpeakerCooldownJson.load()

        plugin.launch(Dispatchers.IO + TeardownOptionCoroutineContextElement(true)) {
            while (isActive) {
                bukkitDelay(36000)
                loudSpeakerCooldownJson.save()
            }
        }

    }

    @Teardown
    fun teardown() {
        if (proxyService.isProxyConnected()) return
        loudSpeakerCooldownJson.save()
    }
}