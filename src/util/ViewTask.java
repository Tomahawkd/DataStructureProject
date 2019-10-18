package util;

/**
 * <p>The interface is a tag for task controller to recognize GUI.</p>
 * <p>The GUI need to be disposed first in order to reject illegal access
 * via GUI.</p>
 *
 * @see Task
 * @see TaskController
 */
public interface ViewTask extends Task {

	/**
	 * The initialization is to add task to the controller, and self
	 * property settings need to be declared.
	 */
	void init();
}
