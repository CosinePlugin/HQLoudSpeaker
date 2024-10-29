package kr.cosine.loudspeaker.enums

import org.bukkit.entity.Player

enum class Message(
    private var message: String
) {
    HOLD_LOUD_SPEAKER("§c손에 확성기를 들어주세요."),
    INPUT_MESSAGE("§c메시지를 입력해주세요."),
    APPLIED_COOLDOWN("§c%cooldown%초 후에 다시 사용할 수 있습니다.");

    fun setMessage(message: String) {
        this.message = message
    }

    fun sendMessage(player: Player, replace: (String) -> String = { it }) {
        if (message.isEmpty()) return
        player.sendMessage(replace(message))
    }

    companion object {
        fun of(text: String): Message? {
            return runCatching { valueOf(text.uppercase().replace("-", "_")) }.getOrNull()
        }
    }
}