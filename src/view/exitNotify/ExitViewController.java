package view.exitNotify;

public class ExitViewController {
	private static ExitView view;

	public static synchronized ExitView getView() {
		if (view == null) view = new ExitView();
		view.init();
		return view;
	}
}
