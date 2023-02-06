package net.stellarica.server.crafts.starships

import eu.pb4.sgui.api.ClickType
import eu.pb4.sgui.api.elements.GuiElementInterface
import eu.pb4.sgui.api.gui.HotbarGui
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.screen.slot.SlotActionType
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.BlockRotation
import net.stellarica.server.crafts.starships.weapons.Weapons
import net.stellarica.server.utils.setRichName
import net.stellarica.server.utils.toVec3i


class PlayerController(private val ship: Starship, private val pilot: ServerPlayerEntity) : HotbarGui(pilot) {

	private var counter = 0;

	override fun onClick(
		index: Int,
		type: ClickType?,
		action: SlotActionType?,
		element: GuiElementInterface?
	): Boolean {
		when (index) {
			0 -> {
				ship.cruiseDirection = player.rotationVector.normalize()
				ship.cruiseSpeed = 5
			}

			1 -> ship.cruiseSpeed = 0
			2 -> ship.move(player.rotationVector.multiply(1.5).toVec3i())
			4 -> ship.rotate(BlockRotation.COUNTERCLOCKWISE_90)
			5 -> ship.rotate(BlockRotation.CLOCKWISE_90)
			7 -> {
				ship.components.firstOrNull { it is Weapons }?.let {
					(it as Weapons).fire()
				}
			}

			8 -> ship.unpilot()
		}
		return super.onClick(index, type, action, element)
	}

	override fun onTick() {
		super.onTick()
		counter++
		if (counter >= 10 && ship.cruiseSpeed > 0) {
			counter = 0
			ship.move(ship.cruiseDirection.multiply(ship.cruiseSpeed.toDouble()).toVec3i())
		}
	}

	override fun canPlayerClose(): Boolean {
		return false;
	}

	// these could definitely be cleaned up a bit
	init {
		for (slot in 0..8) {
			this.setSlot(slot, when (slot) {
				0 -> ItemStack(Items.GREEN_STAINED_GLASS_PANE).also { it.setRichName("<b>Cruise") }
				1 -> ItemStack(Items.RED_STAINED_GLASS_PANE).also { it.setRichName("<b>Stop") }
				2 -> ItemStack(Items.YELLOW_STAINED_GLASS_PANE).also { it.setRichName("<b>Precision") }
				3 -> ItemStack(Items.LIGHT_GRAY_STAINED_GLASS_PANE).also { it.setRichName(".") }
				4 -> ItemStack(Items.BLUE_STAINED_GLASS_PANE).also { it.setRichName("<b>Left") }
				5 -> ItemStack(Items.BLUE_STAINED_GLASS_PANE).also { it.setRichName("<b>Right") }
				6 -> ItemStack(Items.LIGHT_GRAY_STAINED_GLASS_PANE).also { it.setRichName(".") }
				7 -> ItemStack(Items.NETHER_STAR).also { it.setRichName("<b>Fire") }
				8 -> ItemStack(Items.BARRIER).also { it.setRichName("<b>Unpilot") }
				else -> ItemStack.EMPTY
			})
		}
		open()
	}
}
