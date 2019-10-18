package view.intro;

import util.TaskController;

public class IntroViewController {
	private static IntroView view = new IntroView();

	public synchronized static IntroView getView() {
		if (view == null) view = new IntroView();
		view.init();
		return view;
	}

	public static void dispose() {
		if (view != null) TaskController.getController().removeViewTask(view);
	}
}
