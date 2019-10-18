package view.main;

import util.TaskController;
import util.ViewTask;
import view.FileChooser;
import view.edit.EditViewController;
import view.record.RecordViewController;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class MainView extends JFrame implements ViewTask {

	private JPanel mainPanel;
	private JButton importButton;
	private JButton borrowButton;
	private JButton returnButton;
	private JButton browseButton;

	MainView() {

		setContentPane(mainPanel);

		importButton.addActionListener(e -> new FileChooser(FileChooser.ChooserType.READ));

		borrowButton.addActionListener(e -> EditViewController.getView(EditViewController.BORROW));

		returnButton.addActionListener(e -> EditViewController.getView(EditViewController.RETURN));

		browseButton.addActionListener(e -> {
			RecordViewController.getView();
			TaskController.getController().removeViewTask(MainView.this);
		});
	}

	@Override
	public void init() {

		// Add to task controller
		TaskController.getController().addViewTask(this);

		// Properties
		setBounds(350, 200, 640, 360);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setJMenuBar(new MenuView());
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				TaskController.getController().showExitDialog();
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
