package util;

/**
 * <p>The interface is to mark various task for task controller.</p>
 * <p>In order to shut down the task, you need to implement this
 * interface and add the class to the task controller using
 * <code>TaskController.getController().addTask(task);</code></p>
 * <p>When the task need to be closed, you should invoke
 * <code>TaskController.getController().removeTask(task);</code></p>
 *
 * @see TaskController
 */
public interface Task {

	/**
	 * This method is to declare what the controller should
	 * do while closing the task.
	 *
	 */
	void stop();
}
