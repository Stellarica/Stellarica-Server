package net.stellarica.server.commands

import com.mojang.brigadier.CommandDispatcher
import net.minecraft.command.CommandBuildContext
import net.minecraft.server.command.ServerCommandSource
import net.silkmc.silk.commands.command
import net.stellarica.server.utils.toRichText

fun registerMiscCommands() {
	command("icanhasbukkit") {
		runs {
			source.sendSystemMessage(
				"""
				<i>Checking version, please wait...</i>
				This server is running Quilt, not Bukkit or any of its forks.
				You can't has the bukkit.
				<green>It may or may not be running the latest version</green>
				<gray><i>Previous version: How the heck would I know? You tell me.
				""".trimIndent().toRichText()
			)
		}
	}

	command("reload") {
		literal("confirm") {
			runs {
				source.sendSystemMessage(
					"""
					You really fell for that? <rainbow>LMAO</rainbow>
					This server isn't running Bukkit, and you can't reload it.
					Even if you could, that would be a terrible idea.
					""".trimIndent().toRichText()
				)
			}
		}
		runs {
			source.sendSystemMessage((
					"<red>Are you sure you wish to reload your server? " +
							"Doing so may cause bugs and memory leaks. " +
							"It is recommended to restart instead of using /reload. " +
							"To confirm, please type <yellow>/reload confirm"
					).toRichText()
			)
		}
	}
}