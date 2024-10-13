package kr.cosine.loudspeaker.command

import kr.cosine.loudspeaker.config.SettingConfig
import kr.cosine.loudspeaker.service.LoudSpeakerService
import kr.hqservice.framework.command.Command
import kr.hqservice.framework.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Command(label = "확성기관리", isOp = true)
class LoudSpeakerAdminCommand(
    private val settingConfig: SettingConfig,
    private val loudSpeakerService: LoudSpeakerService
) {
    @CommandExecutor("지급", "확성기를 지급받습니다.", priority = 1)
    fun giveLoudSpeaker(player: Player) {
        if (loudSpeakerService.giveLoudSpeaker(player)) {
            player.sendMessage("§a확성기를 지급받았습니다.")
        } else {
            player.sendMessage("§c확성기가 설정되어 있지 않습니다.")
        }
    }

    @CommandExecutor("리로드", "config.yml을 리로드합니다.", priority = 2)
    fun reload(sender: CommandSender) {
        settingConfig.reload()
        sender.sendMessage("§aconfig.yml을 리로드하였습니다.")
    }
}