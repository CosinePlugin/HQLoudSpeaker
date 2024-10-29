package kr.cosine.loudspeaker.database.entity

import kr.hqservice.framework.database.component.Table
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

@Table
object LoudSpeakerCooldownTable : UUIDTable("hqloudspeaker") {
    val cooldown = long("cooldown").default(0)
}

class LoudSpeakerCooldownEntity(
    id: EntityID<UUID>
) : UUIDEntity(id) {
    companion object : UUIDEntityClass<LoudSpeakerCooldownEntity>(LoudSpeakerCooldownTable)

    var cooldown by LoudSpeakerCooldownTable.cooldown
}