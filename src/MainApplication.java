import jni.DataController;
import util.TaskController;
import view.intro.IntroViewController;

import javax.swing.*;
import java.awt.*;

public class MainApplication {

	public static void main(String[] args) {

		String path = "./jni/libCoreIndexData";
		String os = System.getProperty("os.name");

		// Add Extension
		if (os.contains("Mac OS")) path += ".dylib";
		else if (os.contains("Windows")) path += ".dll";
		else if (os.contains("Linux")) path += ".so";
		else {
			JOptionPane.showMessageDialog(null,
					"System determination failed, treating Linux as default",
					"Unknown System", JOptionPane.INFORMATION_MESSAGE);
			path += ".so";
		}

		// Check Library
		if (MainApplication.class.getResource(path) == null) {
			JOptionPane.showMessageDialog(null,
					"Library is missing",
					"Missing File", JOptionPane.WARNING_MESSAGE);
		} else {

			//Initialize TaskController
			TaskController.getController();

			// Initialize DataController
			DataController.getInstance();

			// Initialize GUI
			EventQueue.invokeLater(() -> {
				try {
					IntroViewController.getView();
				} catch (Exception e) {
					e.printStackTrace();
					TaskController.getController().terminate();
				}
			});
		}
	}
}
