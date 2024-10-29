package kr.cosine.loudspeaker.service

import kr.cosine.loudspeaker.enums.Message
import kr.cosine.loudspeaker.netty.packet.LoudSpeakerPacket
import kr.cosine.loudspeaker.registry.SettingRegistry
import kr.hqservice.framework.global.core.component.Service
import kr.hqservice.framework.netty.api.PacketSender
import org.bukkit.Server
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

@Service
class LoudSpeakerService(
    private val packetSender: PacketSender,
    private val server: Server,
    private val settingRegistry: SettingRegistry,
    private val proxyService: ProxyService,
    private val loudSpeakerCooldownService: LoudSpeakerCooldownService
) {
    fun isLoudSpeaker(itemStack: ItemStack): Boolean {
        return settingRegistry.isLoudSpeaker(itemStack)
    }

    fun useLoudSpeaker(player: Player, itemStack: ItemStack, message: String) {
        val playerUniqueId = player.uniqueId
        val cooldown = loudSpeakerCooldownService.getCooldown(playerUniqueId)
        val currentTime = System.currentTimeMillis()
        if (cooldown > currentTime) {
            val leftCooldown = ((cooldown - currentTime) / 1000) + 1
            Message.APPLIED_COOLDOWN.sendMessage(player) {
                it.replace("%cooldown%", "$leftCooldown")
            }
            return
        }
        loudSpeakerCooldownService.applyCooldown(playerUniqueId)
        itemStack.amount--
        val playerName = player.name
        val playerDisplayName = player.displayName
        if (proxyService.isProxyConnected()) {
            val loudSpeakerPacket = LoudSpeakerPacket(playerName, playerDisplayName, message)
            packetSender.sendPacketAll(loudSpeakerPacket)
        } else {
            broadcast(playerName, playerDisplayName, message)
        }
    }

    fun broadcast(playerName: String, playerDisplayName: String, message: String) {
        val loudSpeakerMessages = settingRegistry.getLoudSpeakerMessages {
            it.replace("%player%", playerName)
                .replace("%player_display%", playerDisplayName)
                .replace("%message%", message)
        }.joinToString("\n")
        server.broadcastMessage(loudSpeakerMessages)
    }

    fun giveLoudSpeaker(player: Player): Boolean {
        val loudSpeakerItemStack = settingRegistry.findLoudSpeaker() ?: return false
        player.inventory.addItem(loudSpeakerItemStack)
        return true
    }
}