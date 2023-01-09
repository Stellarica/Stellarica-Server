package io.github.hydrazinemc.hzmod.util

import eu.pb4.placeholders.api.TextParserUtils
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack

fun PlayerEntity.sendRichMessage(message: String, actionBar: Boolean = false) {
	this.sendMessage(TextParserUtils.formatText(message), actionBar)
}

fun ItemStack.setRichName(name: String) {
	this.setCustomName(TextParserUtils.formatText(name))
}