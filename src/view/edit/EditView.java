package view.edit;

import jni.DataController;
import jni.Segment;
import util.TaskController;
import util.ViewTask;
import view.main.MainViewController;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;

class EditView extends JFrame implements ViewTask {

	private JPanel mainPanel;
	private JComboBox<Integer> bookComboBox;
	private JButton cancelButton;
	private JButton confirmButton;
	private JLabel bookNameLabel;
	private JTextField idTextField;
	private JLabel returningDateLabel;
	private JTextField returningDateTextField;

	private int type = 0;

	EditView() {

		setContentPane(mainPanel);

		bookComboBox.setModel(new IndexComboBoxModel());

		bookNameLabel.setText("");
		bookComboBox.addActionListener(e -> {

			Segment seg = DataController.getInstance()
					.get(bookComboBox.getItemAt(bookComboBox.getSelectedIndex()));

			if (seg != null) bookNameLabel.setText(seg.getName());
		});

		confirmButton.addActionListener(e -> {
			try {

				// check borrower id
				long borrowerId = Long.parseLong(idTextField.getText());

				// get book information
				int index = bookComboBox.getItemAt(bookComboBox.getSelectedIndex());
				Segment segment = DataController.getInstance().get(index);

				if (segment == null) {
					JOptionPane.showMessageDialog(null,
							"Segment Not Exist",
							"Data Error",
							JOptionPane.WARNING_MESSAGE);
					return;
				}

				int remaining = segment.getRemaining();

				// Borrow
				if (type == 0) {
					if (remaining <= 0) {
						JOptionPane.showMessageDialog(null,
								"Not enough book.",
								"Warning",
								JOptionPane.INFORMATION_MESSAGE);
						return;
					}

					segment.addRecord(borrowerId, returningDateTextField.getText());
					remaining--;
				}

				// Return
				else if (type == 1) {
					if (remaining >= segment.getAmount()) {
						JOptionPane.showMessageDialog(null,
								"Remaining books amount greater than total amount.",
								"Data Error", JOptionPane.INFORMATION_MESSAGE);
						return;
					}

					int result = segment.removeRecord(borrowerId);
					if (result == 0) remaining++;
					else JOptionPane.showMessageDialog(null,
							"Borrower ID not found",
							"Not Found", JOptionPane.WARNING_MESSAGE);

				} else {
					JOptionPane.showMessageDialog(null,
							"Error occurs while retreating type.",
							"Internal Error", JOptionPane.WARNING_MESSAGE);
					TaskController.getController().removeViewTask(EditView.this);
				}

				Segment seg = DataController.getInstance().get(index);
				if (seg != null) {
					seg.setRemaining(remaining);
				} else {
					JOptionPane.showMessageDialog(null,
							"Error", "Book Segment Not Found",
							JOptionPane.ERROR_MESSAGE);
				}

				// dispose window
				TaskController.getController().removeViewTask(EditView.this);


			} catch (ParseException e1) {
				JOptionPane.showMessageDialog(null,
						"Date invalid",
						"Value Error", JOptionPane.WARNING_MESSAGE);
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(null,
						"ID invalid",
						"Value Error", JOptionPane.WARNING_MESSAGE);
			}

		});

		cancelButton.addActionListener(e -> TaskController.getController().removeViewTask(EditView.this));
	}

	void setType(int type) {
		this.type = type;

		// clear input
		bookNameLabel.setText("");
		idTextField.setText("");
		((IndexComboBoxModel) bookComboBox.getModel()).updateIndex();

		if (type == EditViewController.RETURN) {
			returningDateLabel.setVisible(false);
			returningDateTextField.setVisible(false);
		} else {
			returningDateLabel.setVisible(true);
			returningDateTextField.setVisible(true);
		}
	}

	@Override
	public void init() {

		// Add to task controller
		TaskController.getController().addViewTask(EditView.this);

		// Property
		setBounds(350, 200, 500, 150);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				MainViewController.getView();
				TaskController.getController().removeViewTask(EditView.this);
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
