package view.edit;

public class EditViewController {

	private static EditView view;
	public static int BORROW = 0;
	public static int RETURN = 1;

	public static synchronized EditView getView(int type) {
		if (view == null) view = new EditView();
		view.setType(type);
		view.init();
		return view;
	}
}
