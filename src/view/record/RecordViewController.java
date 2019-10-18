package view.record;

public class RecordViewController {

	private static RecordView view;

	public static RecordView getView() {
		if (view == null) view = new RecordView();
		view.init();
		view.updateData();
		return view;
	}
}
