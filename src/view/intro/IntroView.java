package view.intro;

import util.TaskController;
import util.ViewTask;
import view.FileChooser;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class IntroView extends JFrame implements ViewTask {

	private JPanel mainPanel;
	private JButton newFileButton;
	private JButton loadFileButton;
	private JButton exitButton;

	IntroView() {

		setContentPane(mainPanel);

		newFileButton.addActionListener(e -> new FileChooser(FileChooser.ChooserType.NEW));

		loadFileButton.addActionListener(e -> new FileChooser(FileChooser.ChooserType.LOAD));

		exitButton.addActionListener(e -> TaskController.getController().terminate());
	}

	@Override
	public void init() {

		//Add to task controller
		TaskController.getController().addViewTask(IntroView.this);

		// Properties
		setBounds(400, 250, 320, 180);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				TaskController.getController().terminate();
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
