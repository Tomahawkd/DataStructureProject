package view.record;

import jni.DataController;
import jni.Segment;
import util.TaskController;
import util.ViewTask;
import view.main.MainViewController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;

class RecordView extends JFrame implements ViewTask {

	private JPanel mainPanel;
	private JButton addButton;
	private JButton editButton;
	private JButton deleteButton;
	private JTable informationTable;
	private JButton infoButton;

	RecordView() {

		setContentPane(mainPanel);

		informationTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		informationTable.setModel(new RecordTableModel());

		infoButton.addActionListener(e -> {
			if (informationTable.getSelectedRow() < 0) return;

			RecordTableModel model = (RecordTableModel) informationTable.getModel();
			Segment segment = DataController.getInstance()
					.get(model.get(informationTable.getSelectedRow()).getIndex());
			new BorrowerInfoView(segment);
		});

		addButton.addActionListener(e -> {
			new DataEditorView(null);
			((RecordTableModel) informationTable.getModel()).updateData();
			informationTable.updateUI();
		});

		editButton.addActionListener(e -> {

			// check table selection
			if (informationTable.getSelectedRow() < 0) return;

			RecordTableModel model = (RecordTableModel) informationTable.getModel();
			Segment segment = DataController.getInstance()
					.get(model.get(informationTable.getSelectedRow()).getIndex());

			new DataEditorView(segment);

			// update data
			model.updateData();
			informationTable.updateUI();
		});

		deleteButton.addActionListener(e -> {

			// check table selection
			if (informationTable.getSelectedRow() < 0) return;

			RecordTableModel model = (RecordTableModel) informationTable.getModel();
			try {
				DataController.getInstance().delete(model.get(informationTable.getSelectedRow()));
			} catch (ParseException e1) {
				JOptionPane.showMessageDialog(null,
						e1.getMessage(), "Not Found",
						JOptionPane.WARNING_MESSAGE);
			}

			// update data
			model.updateData();
			informationTable.updateUI();
		});
	}

	void updateData() {
		((RecordTableModel) informationTable.getModel()).updateData();
		informationTable.updateUI();
	}

	@Override
	public void init() {

		// Add to task controller
		TaskController.getController().addViewTask(RecordView.this);

		// Property
		setBounds(350, 200, 640, 360);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				MainViewController.getView();
				TaskController.getController().removeViewTask(RecordView.this);
			}
		});
		pack();

		setVisible(true);
	}

	@Override
	public void stop() {
		dispose();
	}
}
