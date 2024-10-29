package kr.cosine.loudspeaker.database.repository

import kotlinx.coroutines.Dispatchers
import kr.cosine.loudspeaker.database.entity.LoudSpeakerCooldownEntity
import kr.hqservice.framework.database.extension.findByIdForUpdate
import kr.hqservice.framework.database.repository.player.PlayerRepository
import kr.hqservice.framework.global.core.component.Component
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

@Component
class LoudSpeakerCooldownRepository : PlayerRepository<Long>() {
    override suspend fun load(player: Player): Long {
        val playerUniqueId = player.uniqueId
        return newSuspendedTransaction(Dispatchers.IO) {
            (LoudSpeakerCooldownEntity.findById(playerUniqueId)
                ?: LoudSpeakerCooldownEntity.new(playerUniqueId) {}).cooldown
        }
    }

    override suspend fun save(player: Player, value: Long) {
        newSuspendedTransaction(Dispatchers.IO) {
            LoudSpeakerCooldownEntity.findByIdForUpdate(player.uniqueId)?.apply {
                this.cooldown = value
            }
        }
    }

    override fun get(key: UUID): Long {
        return super.get(key) ?: 0
    }
}