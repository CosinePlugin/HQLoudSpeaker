package kr.cosine.loudspeaker.command.module

import kr.cosine.loudspeaker.command.LoudSpeakerUserCommand
import kr.hqservice.framework.bukkit.core.HQBukkitPlugin
import kr.hqservice.framework.bukkit.core.component.module.Module
import kr.hqservice.framework.bukkit.core.component.module.Setup

@Module
class CommandModule(
    private val plugin: HQBukkitPlugin,
    private val loudSpeakerUserCommand: LoudSpeakerUserCommand
) {
    @Setup
    fun setup() {
        plugin.getCommand("확성기")?.setExecutor(loudSpeakerUserCommand)
    }
}