package kr.cosine.loudspeaker.config

import kr.cosine.loudspeaker.enums.Message
import kr.cosine.loudspeaker.registry.SettingRegistry
import kr.hqservice.framework.bukkit.core.extension.colorize
import kr.hqservice.framework.bukkit.core.extension.editMeta
import kr.hqservice.framework.global.core.component.Bean
import kr.hqservice.framework.yaml.config.HQYamlConfiguration
import kr.hqservice.framework.yaml.config.HQYamlConfigurationSection
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

@Bean
class SettingConfig(
    private val config: HQYamlConfiguration,
    private val settingRegistry: SettingRegistry
) {
    fun load() {
        loadLoudSpeaker()
        loadMessage()
    }

    private fun loadLoudSpeaker() {
        config.getSection("loud-speaker")?.apply {
            getSection("item")?.apply itemApply@ {
                val materialText = getString("material")
                val material = Material.getMaterial(materialText) ?: return@itemApply
                val displayName = getString("display-name").colorize()
                val lore = getColorizedStringList("lore")
                val customModelData = getInt("custom-model-data")
                val itemStack = ItemStack(material).editMeta {
                    if (displayName.isNotEmpty()) {
                        setDisplayName(displayName)
                    }
                    if (lore.isNotEmpty()) {
                        setLore(lore)
                    }
                    if (customModelData != 0) {
                        setCustomModelData(customModelData)
                    }
                }
                settingRegistry.setLoudSpeaker(itemStack)
            }
            val loudSpeakerMessages = getColorizedStringList("messages")
            settingRegistry.setLoudSpeakerMessages(loudSpeakerMessages)
        }
    }

    private fun HQYamlConfigurationSection.getColorizedStringList(path: String): List<String> {
        return getStringList(path).map(String::colorize)
    }

    private fun loadMessage() {
        config.getSection("message")?.apply {
            getKeys().forEach { messageText ->
                val message = Message.of(messageText) ?: return@forEach
                message.setMessage(getString(messageText).colorize())
            }
        }
    }

    fun reload() {
        settingRegistry.clear()
        config.reload()
        load()
    }
}