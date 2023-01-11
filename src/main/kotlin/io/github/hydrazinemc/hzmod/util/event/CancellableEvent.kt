package io.github.hydrazinemc.hzmod.util.event

/**
 * An [Event] that can be cancelled.
 */
class CancellableEvent<T>: Event<T>() {
	/**
	 * Whether the event is cancelled.
	 * Once it's cancelled, it stops running lower priority listeners.
	 * Resets every time [call] is called.
	 */
	var cancelled = false
		private set

	override fun call(data: T) {
		cancelled = false
		listeners.sortedBy { it.priority }.forEach {
			it.invoke(it, data)
			if (cancelled) return
		}
	}

	/**
	 * Call this event with [data]. Afterward, run [callback] with the cancelled status.
	 */
	fun call(data: T, callback: (Boolean) -> Unit) {
		call(data)
		callback(cancelled)
	}

	/**
	 * Cancel this event.
	 * Once it's cancelled, it stops running lower priority listeners.
	 */
	fun cancel() {
		this.cancelled = true
	}
}