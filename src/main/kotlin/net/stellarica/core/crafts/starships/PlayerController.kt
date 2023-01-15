package net.stellarica.core.crafts.starships

import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.BlockRotation
import net.stellarica.core.util.HotbarMenu
import net.stellarica.core.util.setRichName
import org.quiltmc.qkl.library.math.toVec3i

class PlayerController(private val ship: Starship, private val pilot: ServerPlayerEntity) : HotbarMenu(pilot) {

	//val onTick = StartServerTickEvent

	override fun onButtonClicked(index: Int) {
		if (player != pilot)
		when (player.inventory.selectedSlot) {
			0 -> {
				ship.cruiseDirection = player.rotationVector.normalize()
				ship.cruiseSpeed = 5
			}

			1 -> ship.cruiseSpeed = 0
			2 -> ship.move(player.rotationVector.toVec3i())
			4 -> ship.rotate(BlockRotation.CLOCKWISE_90)
			5 -> ship.rotate(BlockRotation.COUNTERCLOCKWISE_90)
			8 -> ship.unpilot()
		}
	}

	override fun onMenuClosed() {
	//	onTick.unregister()
	}

	// these could definitely be cleaned up a bit
	init {
		for (slot in 0..8) {
			player.inventory.setStack(slot, when (slot) {
				0 -> ItemStack(Items.GREEN_CONCRETE).also { it.setRichName("<b>Cruise") }
				1 -> ItemStack(Items.RED_CONCRETE).also { it.setRichName("<b>Stop") }
				2 -> ItemStack(Items.YELLOW_CONCRETE).also { it.setRichName("<b>Precision") }
				4 -> ItemStack(Items.BLUE_CONCRETE).also { it.setRichName("<b>Left") }
				5 -> ItemStack(Items.BLUE_CONCRETE).also { it.setRichName("<b>Right") }
				7 -> ItemStack(Items.NETHER_STAR).also { it.setRichName("<b>Fire") }
				8 -> ItemStack(Items.BARRIER).also { it.setRichName("<b>Unpilot") }
				else -> ItemStack.EMPTY
			})
		}
	}
}