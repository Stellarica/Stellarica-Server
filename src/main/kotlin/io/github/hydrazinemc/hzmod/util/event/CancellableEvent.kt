package io.github.hydrazinemc.hzmod.util.event

/**
 * An [Event] that can be cancelled.
 */
class CancellableEvent<T>: Event<T>() {
	/**
	 * Whether the event is cancelled.
	 * Once its cancelled, it stops running lower priority listeners.
	 * Resets every time [call] is called.
	 */
	var cancelled = false

	override fun call(data: T) {
		cancelled = false
		listeners.sortedBy { it.first }.forEach {
			if (cancelled) return@forEach
			it.second.get()?.invoke(data)
		}
		// kind of dumb to iterate through twice
		listeners.removeIf { it.second.get() == null }
	}
}