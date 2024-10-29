package kr.cosine.loudspeaker.service

import kr.hqservice.framework.bukkit.core.netty.server.ProxiedNettyServer
import kr.hqservice.framework.global.core.component.Service
import kr.hqservice.framework.netty.api.NettyServer

@Service
class ProxyService(
    private val nettyServer: NettyServer
) {
    fun isProxyConnected(): Boolean {
        return nettyServer is ProxiedNettyServer
    }

    fun isNotProxyConnected(): Boolean {
        return !isProxyConnected()
    }
}