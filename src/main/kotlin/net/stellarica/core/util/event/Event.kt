package net.stellarica.core.util.event

/**
 * A callable, subscribable event.
 */
open class Event<T> {
	protected val listeners = mutableSetOf<Listener<T>>()

	/**
	 * Call all listeners subscribed to this event.
	 * @param data to be passed to the listeners.
	 */
	open fun call(data: T) {
		listeners.sortedBy { it.priority }.forEach {
			it.invoke(it, data)
		}
	}

	/**
	 * Create and register a new listener.
	 * @param invoke the function to call when this event is fired
	 * @param priority the priority of this listener. Lower runs first.
	 */
	fun listen(priority: Int, invoke: Listener<T>.(T) -> Unit) = Listener(this, invoke, priority)

	/**
	 * Wrapper for a thing that subscribes to an [Event]
	 */
	class Listener<T>(
		/** The event this is listening to **/
		val event: Event<T>,
		/** The function called when this event is fired **/
		val invoke: Listener<T>.(T) -> Unit,
		/** The priority of this listener. Lower runs first **/
		val priority: Int = 0
	) {

		init { event.listeners.add(this) }

		/** Unregister this listener from [event] */
		fun unregister() {
			event.listeners.remove(this)
		}
	}
}
