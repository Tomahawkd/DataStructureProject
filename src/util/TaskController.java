package util;

import org.jetbrains.annotations.NotNull;
import view.exitNotify.ExitViewController;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Consumer;

public class TaskController {

	private static TaskController controller;
	private ConcurrentLinkedDeque<Task> tasksList;
	private ConcurrentLinkedDeque<ViewTask> viewTasksList;

	public synchronized static TaskController getController() {
		if (controller == null) controller = new TaskController();
		return controller;
	}

	private TaskController() {
		tasksList = new ConcurrentLinkedDeque<>();
		viewTasksList = new ConcurrentLinkedDeque<>();
	}

	public void showExitDialog() {
		ExitViewController.getView();
	}

	public synchronized void addTask(@NotNull Task task) {
		tasksList.add(task);
	}

	public synchronized void addViewTask(@NotNull ViewTask task) {
		viewTasksList.add(task);
	}

	public void removeTask(@NotNull Task task) {
		task.stop();
		tasksList.remove(task);
	}

	public void removeViewTask(ViewTask task) {
		task.stop();
		viewTasksList.remove(task);
	}

	public synchronized void terminate() {
		viewTasksList.forEach(this::removeViewTask);
		tasksList.forEach(this::removeTask);
		System.gc();
	}
}
