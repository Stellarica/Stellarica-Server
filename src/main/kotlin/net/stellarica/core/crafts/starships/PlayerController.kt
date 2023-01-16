package net.stellarica.core.crafts.starships

import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.BlockRotation
import net.stellarica.core.util.EventPriority
import net.stellarica.core.util.HotbarMenu
import net.stellarica.core.util.setRichName
import net.stellarica.oxidizer.event.Event
import net.stellarica.oxidizer.event.server.StartServerTickEvent
import org.quiltmc.qkl.library.math.toVec3i

class PlayerController(private val ship: Starship, private val pilot: ServerPlayerEntity) : HotbarMenu(pilot) {

	private var counter = 0;

	val onTick = StartServerTickEvent.listen(EventPriority.LOWEST.ordinal) { _ ->
		counter++
		if (counter == 10 && ship.cruiseSpeed > 0) {
			counter = 0
			ship.move(ship.cruiseDirection.multiply(ship.cruiseSpeed.toDouble()).toVec3i())
			println("moved")
		}
		return@listen Event.Result.CONTINUE
	}

	override fun onButtonClicked(index: Int) {
		when (index) {
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
		onTick.unregister()
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