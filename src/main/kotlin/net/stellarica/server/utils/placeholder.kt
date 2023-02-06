package net.stellarica.server.utils

import eu.pb4.placeholders.api.TextParserUtils
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

fun PlayerEntity.sendRichMessage(message: String, actionBar: Boolean = false) {
	this.sendMessage(TextParserUtils.formatText(message), actionBar)
}

fun ItemStack.setRichName(name: String) {
	this.setCustomName(TextParserUtils.formatText(name))
}

fun String.toRichText(): Text = TextParserUtils.formatText(this)