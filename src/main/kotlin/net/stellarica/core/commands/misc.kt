package net.stellarica.core.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.minecraft.server.command.ServerCommandSource
import net.stellarica.core.util.toRichText
import org.quiltmc.qsl.command.api.CommandRegistrationCallback

fun registerMiscCommands() {
	// Also temporary, just for the memes
	CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher, registry, environment ->
		dispatcher.register(
			LiteralArgumentBuilder.literal<ServerCommandSource?>("icanhasbukkit")
				.executes { context ->
					context.source.sendSystemMessage(
						"""
					<i>Checking version, please wait...</i>
					This server is running Quilt, not Bukkit or any of its forks.
					You can't has the bukkit.
					<green>It may or may not be running the latest version</green>
					<gray><i>Previous version: How the heck would I know? You tell me.
				""".trimIndent().toRichText()
					)
					return@executes 1
				}
		)
		dispatcher.register(
			LiteralArgumentBuilder.literal<ServerCommandSource?>("reload")
				.then(
					LiteralArgumentBuilder.literal<ServerCommandSource?>("confirm")
						.executes { context ->
							context.source.sendSystemMessage(
								"""
								You really fell for that? <rainbow>LMAO</rainbow>
								This server isn't running Bukkit, and you can't reload it.
								Even if you could, that would be a terrible idea.
							""".trimIndent().toRichText()
							)
							return@executes 1
						}
				)
				.executes { context ->
					context.source.sendSystemMessage(
						(
								"<red>Are you sure you wish to reload your server? " +
										"Doing so may cause bugs and memory leaks. " +
										"It is recommended to restart instead of using /reload. " +
										"To confirm, please type <yellow>/reload confirm"
								).toRichText()
					)
					return@executes 1
				}
		)
	})
}