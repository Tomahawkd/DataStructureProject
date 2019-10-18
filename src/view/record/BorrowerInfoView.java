package view.record;

import jni.Segment;

import javax.swing.*;

public class BorrowerInfoView extends JDialog {
	private JTable borrowInfoTable;
	private JButton closeButton;
	private JPanel contentPane;

	BorrowerInfoView(Segment segment) {
		setContentPane(contentPane);
		setModal(true);
		setBounds(350, 200, 640, 360);
		getRootPane().setDefaultButton(closeButton);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();

		borrowInfoTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		if (segment != null) {
			borrowInfoTable.setModel(new BorrowerInfoTableModel(segment.getRecordList()));
		}

		closeButton.addActionListener(e -> dispose());

		setVisible(true);
	}
}
