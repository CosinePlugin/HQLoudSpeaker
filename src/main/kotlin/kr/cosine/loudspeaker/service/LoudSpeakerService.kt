package kr.cosine.loudspeaker.service

import kr.cosine.loudspeaker.netty.packet.LoudSpeakerPacket
import kr.cosine.loudspeaker.registry.SettingRegistry
import kr.hqservice.framework.bukkit.core.netty.server.ProxiedNettyServer
import kr.hqservice.framework.global.core.component.Service
import kr.hqservice.framework.netty.api.NettyServer
import kr.hqservice.framework.netty.api.PacketSender
import org.bukkit.Server
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

@Service
class LoudSpeakerService(
    private val packetSender: PacketSender,
    private val nettyServer: NettyServer,
    private val server: Server,
    private val settingRegistry: SettingRegistry
) {
    fun isLoudSpeaker(itemStack: ItemStack): Boolean {
        return settingRegistry.isLoudSpeaker(itemStack)
    }

    fun useLoudSpeaker(player: Player, itemStack: ItemStack, message: String) {
        itemStack.amount--
        val playerName = player.name
        val playerDisplayName = player.displayName
        if (nettyServer is ProxiedNettyServer) {
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