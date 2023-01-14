package net.stellarica.core.crafts.starships

import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.BlockRotation
import org.quiltmc.qkl.library.math.toVec3i

class PlayerController(ship: Starship) : Controller(ship) {
	var counter = 0
	private val itemUse =  { player: ServerPlayerEntity, item: ItemStack ->
		if (player != pilot)
		when (player.inventory.selectedSlot) {
			0 -> {
				cruiseDirection = player.rotationVector.normalize()
				cruiseSpeed = 5
			}

			1 -> cruiseSpeed = 0
			2 -> ship.move(player.rotationVector.toVec3i())
			4 -> ship.rotate(BlockRotation.CLOCKWISE_90)
			5 -> ship.rotate(BlockRotation.COUNTERCLOCKWISE_90)
			8 -> ship.unpilot()
		}
	}


	private var hotbar = mutableListOf<ItemStack>()
	lateinit var pilot: ServerPlayerEntity


	// these could definitely be cleaned up a bit
	override fun onPilot(player: ServerPlayerEntity) {
		this.pilot = player
		for (slot in 0..10) {
			hotbar.add(player.inventory.getStack(slot))
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

	override fun onUnpilot() {
		for (slot in 0..10) {
			pilot.inventory.setStack(slot, hotbar[slot])
		}
	}
}