package view.exitNotify;

import util.TaskController;
import util.ViewTask;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

class ExitView extends JFrame implements ViewTask {

	ExitView() {

		/*
		 * Self configuration
		 */

		var contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		/*
		 * Labels
		 */

		var lblSaveYourProjects = new JLabel("Save your projects before you quit the application.");
		lblSaveYourProjects.setBounds(66, 22, 317, 16);
		contentPane.add(lblSaveYourProjects);

		var lblDoYouWant = new JLabel("Do you want to quit?");
		lblDoYouWant.setBounds(66, 50, 317, 16);
		contentPane.add(lblDoYouWant);

		/*
		 * Buttons
		 */

		var btnOk = new JButton("OK");

		// Exit the application
		btnOk.addActionListener(e -> TaskController.getController().terminate());
		btnOk.setBounds(76, 78, 117, 29);
		contentPane.add(btnOk);

		var btnCancel = new JButton("Cancel");

		// Cancel exiting
		btnCancel.addActionListener(e -> TaskController.getController().removeViewTask(ExitView.this));
		btnCancel.setBounds(266, 78, 117, 29);
		contentPane.add(btnCancel);

	}

	@Override
	public void init() {

		// Add to task controller
		TaskController.getController().addViewTask(ExitView.this);

		// Property
		setResizable(false);
		setTitle("Confirm");
		setBounds(230, 250, 450, 150);

		setVisible(true);
	}

	@Override
	public void stop() {
		dispose();
	}
}
