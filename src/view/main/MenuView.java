package view.main;

import jni.DataController;
import util.FileHandler;
import util.TaskController;
import view.FileChooser;

import javax.swing.*;
import java.io.IOException;

class MenuView extends JMenuBar {

	MenuView() {

		/*
		 * Menu: Project
		 */

		JMenu mnProject = new JMenu("Project");
		add(mnProject);

		/*
		 * Items
		 */

		JMenuItem mntmNew = new JMenuItem("New...");
		mnProject.add(mntmNew);
		mntmNew.addActionListener(e -> {
			int state = JOptionPane.showConfirmDialog(null,
					"Do you want to close current project?",
					"Close", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.INFORMATION_MESSAGE);

			if (state == JOptionPane.OK_OPTION) {
				TaskController.getController().removeViewTask(MainViewController.getView());
				new FileChooser(FileChooser.ChooserType.NEW);
			}
		});

		JMenuItem mntmLoad = new JMenuItem("Load");
		mnProject.add(mntmLoad);
		mntmLoad.addActionListener(e -> {
			int state = JOptionPane.showConfirmDialog(null,
					"Do you want to close current project?",
					"Close", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.INFORMATION_MESSAGE);

			if (state == JOptionPane.OK_OPTION) {
				TaskController.getController().removeViewTask(MainViewController.getView());
				new FileChooser(FileChooser.ChooserType.LOAD);
			}
		});

		JMenuItem mntmSave = new JMenuItem("Save");
		mnProject.add(mntmSave);
		mntmSave.addActionListener(e -> {
			try {
				FileHandler.getHandler().write(DataController.getInstance().exportToString());
			} catch (IOException e1) {
				e1.printStackTrace();

				JOptionPane.showMessageDialog(null,
						"Save Failed",
						"Error", JOptionPane.WARNING_MESSAGE);
			}
		});

		JMenuItem mntmSaveAs = new JMenuItem("Save as...");
		mnProject.add(mntmSaveAs);
		mntmSaveAs.addActionListener(e -> new FileChooser(FileChooser.ChooserType.SAVE));

		// Add separator
		mnProject.addSeparator();

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(e -> TaskController.getController().showExitDialog());
		mnProject.add(mntmExit);
	}
}
