package view.main;

public class MainViewController {

	private static MainView view = new MainView();

	public synchronized static MainView getView() {
		if (view == null) view = new MainView();
		view.init();
		return view;
	}
}
