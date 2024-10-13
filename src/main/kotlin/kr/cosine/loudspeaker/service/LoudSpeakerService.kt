package kr.cosine.loudspeaker.service

import kr.cosine.loudspeaker.registry.SettingRegistry
import kr.hqservice.framework.bukkit.core.netty.server.ProxiedNettyServer
import kr.hqservice.framework.global.core.component.Service
import kr.hqservice.framework.netty.api.NettyServer
import kr.hqservice.framework.netty.api.PacketSender
import net.md_5.bungee.api.chat.TextComponent
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
        val loudSpeakerMessages = settingRegistry.getLoudSpeakerMessages {
            it.replace("%player%", player.name)
                .replace("%player_display%", player.displayName)
                .replace("%message%", message)
        }.joinToString("\n")
        if (nettyServer is ProxiedNettyServer) {
            packetSender.broadcast(loudSpeakerMessages.run(::TextComponent), false)
        } else {
            server.broadcastMessage(loudSpeakerMessages)
        }
    }

    fun giveLoudSpeaker(player: Player): Boolean {
        val loudSpeakerItemStack = settingRegistry.findLoudSpeaker() ?: return false
        player.inventory.addItem(loudSpeakerItemStack)
        return true
    }
}