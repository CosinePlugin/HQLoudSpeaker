package kr.cosine.loudspeaker.netty.module

import kr.cosine.loudspeaker.netty.packet.LoudSpeakerPacket
import kr.cosine.loudspeaker.service.LoudSpeakerService
import kr.hqservice.framework.bukkit.core.component.module.Module
import kr.hqservice.framework.bukkit.core.component.module.Setup
import kr.hqservice.framework.bukkit.core.netty.server.LocalNettyServer
import kr.hqservice.framework.netty.api.NettyServer

@Module
class PacketModule(
    private val nettyServer: NettyServer,
    private val loudSpeakerService: LoudSpeakerService
) {
    @Setup
    fun setup() {
        if (nettyServer is LocalNettyServer) return
        nettyServer.registerInnerPacket(LoudSpeakerPacket::class) { broadcastPacket, _ ->
            loudSpeakerService.broadcast(
                broadcastPacket.playerName,
                broadcastPacket.playerDisplayName,
                broadcastPacket.message
            )
        }
        nettyServer.registerOuterPacket(LoudSpeakerPacket::class)
    }
}