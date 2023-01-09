package io.github.hydrazinemc.hzmod.util.event

import java.lang.ref.WeakReference

/**
 * A callable, subscribable event.
 */
open class Event<T> {
	protected val listeners = mutableSetOf<Pair<Int, WeakReference<(T) -> Unit>>>()

	/**
	 * Add [listener] to this event. Lower [priority] runs first.
	 */
	fun addListener(listener: (T) -> Unit, priority: Int = 0) {
		listeners.add(Pair(priority, WeakReference(listener)))
	}

	/**
	 * Remove [listener] from this event.
	 */
	fun removeListener(listener: (T) -> Unit) {
		listeners.removeIf { it.second.get() == listener }
	}

	/**
	 * Call all listeners subscribed to this event.
	 * @param data to be passed
	 */
	open fun call(data: T) {
		listeners.sortedBy { it.first }.forEach {
			it.second.get()?.invoke(data)
		}
		// kind of dumb to iterate through twice
		listeners.removeIf { it.second.get() == null }
	}
}

