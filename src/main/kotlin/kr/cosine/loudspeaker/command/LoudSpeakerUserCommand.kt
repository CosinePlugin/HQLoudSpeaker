package kr.cosine.loudspeaker.command

import kr.cosine.loudspeaker.enums.Message
import kr.cosine.loudspeaker.service.LoudSpeakerService
import kr.hqservice.framework.global.core.component.Bean
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

@Bean
class LoudSpeakerUserCommand(
    private val loudSpeakerService: LoudSpeakerService
) : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val player = sender as? Player ?: return true
        val itemStack = player.inventory.itemInMainHand
        if (itemStack.type.isAir || !loudSpeakerService.isLoudSpeaker(itemStack)) {
            Message.HOLD_LOUD_SPEAKER.sendMessage(player)
            return true
        }
        if (args.isEmpty()) {
            Message.INPUT_MESSAGE.sendMessage(player)
            return true
        }
        val message = args.joinToString(" ")
        loudSpeakerService.useLoudSpeaker(player, itemStack, message)
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>): List<String> {
        return emptyList()
    }
}