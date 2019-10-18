package view;

import jni.DataController;
import org.jetbrains.annotations.NotNull;
import util.FileHandler;
import util.TaskController;
import view.intro.IntroViewController;
import view.main.MainViewController;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.IOException;

public class FileChooser extends JFileChooser {

	public enum ChooserType {

		NEW("New File", DIRECTORIES_ONLY),
		LOAD("Load File", FILES_AND_DIRECTORIES),
		SAVE("Save As...", FILES_AND_DIRECTORIES),
		READ("Load File", FILES_AND_DIRECTORIES);

		private String title;
		private int mode;

		ChooserType(String title, int mode) {
			this.title = title;
			this.mode = mode;
		}
	}

	public FileChooser(ChooserType type) {
		super();

		if (type != ChooserType.NEW) {
			FileFilter filter = new FileNameExtensionFilter(
					"Comma-Separated Values Data File",
					FileHandler.EXTENSION
			);
			setFileFilter(filter);
		}

		setFileSelectionMode(type.mode);
		setDialogTitle(type.title);
		setVisible(true);
		try {
			showDialog(type);
		} catch (IOException e) {
			e.printStackTrace();

			JOptionPane.showMessageDialog(null,
					"File Chooser Error Occurs.",
					"Error", JOptionPane.ERROR_MESSAGE);
		} catch (IllegalArgumentException e) {

			JOptionPane.showMessageDialog(null,
					"File Name Invalid",
					"Invalid", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void showDialog(@NotNull ChooserType type) throws IOException {

		int state;
		switch (type) {
			case NEW:
				state = showSaveDialog(null);
				if (state == APPROVE_OPTION) {
					FileHandler.getHandler(this.getSelectedFile()).create();
					MainViewController.getView();
					IntroViewController.dispose();
				}
				break;

			case LOAD:
				state = showOpenDialog(null);
				if (state == APPROVE_OPTION) {
					TaskController.getController().removeTask(DataController.getInstance());
					DataController.getInstance().importData(
							FileHandler.getHandler(this.getSelectedFile()).read()
					);
					MainViewController.getView();
					IntroViewController.dispose();
				}
				break;

			case SAVE:
				state = showSaveDialog(null);
				if (state == APPROVE_OPTION) {
					FileHandler.getHandler()
							.write(this.getSelectedFile(), DataController.getInstance().exportToString());
				}
				break;

			case READ:
				state = showOpenDialog(null);
				if (state == APPROVE_OPTION) {
					DataController.getInstance().importData(
							FileHandler.getHandler().read(this.getSelectedFile())
					);
				}
				break;
		}
	}
}