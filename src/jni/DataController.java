package jni;

import org.jetbrains.annotations.Nullable;
import util.Task;
import util.TaskController;

import javax.swing.*;
import java.text.ParseException;

public class DataController implements Task {

	private static DataController dataController;
	private DataSource dataSource;

	public synchronized static DataController getInstance() {
		if (dataController == null) {
			dataController = new DataController();
			TaskController.getController().addTask(dataController);
		}
		return dataController;
	}

	private DataController() {
		dataSource = new DataSource();
	}

	/**
	 * Load csv file from file system.
	 *
	 * @param data csv file string
	 */
	public void importData(String data) {
		if (data.isEmpty()) return;
		String[] list = data.split("\n");
		int count = 0;

		for (int i = 1; i < list.length; i++) {
			if (list[i].equals(Segment.HEADER)) continue;

			try {
				add(Segment.parse(list[i]));
				count++;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		JOptionPane.showMessageDialog(null,
				count + " pieces of data has been loaded.",
				"Info", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Add a piece of new data.
	 * Should implement with comparing book index to check if is already exist.
	 *
	 * @param segment data
	 */
	public void add(Segment segment) throws ParseException {
		try {
			dataSource.addSegment(segment);
		} catch (JniException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the specific piece of data with given book index.
	 *
	 * @param index book index
	 * @return data
	 */

	@Nullable
	public Segment get(int index) {
		return dataSource.getSegment(index);
	}

	/**
	 * Delete a piece of data
	 *
	 * @param segment book information
	 */
	public void delete(Segment segment) throws ParseException {
		try {
			dataSource.delete(segment);
		} catch (JniException e) {
			e.printStackTrace();
		}
	}

	public void setDataIndex(int oldIndex, int newIndex) throws ParseException {
		try {
			dataSource.changeIndex(oldIndex, newIndex);
		} catch (JniException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Export data to csv format String
	 *
	 * @return csv data String
	 */
	public String exportToString() {
		return Segment.HEADER + dataSource.toString();
	}

	/**
	 * Export all book index
	 *
	 * @return book index array
	 */
	public int[] exportIndex() {
		return dataSource.exportIndex();
	}

	/**
	 * Export to record view table.
	 *
	 * @return Segment data list
	 */
	public Segment[] exportToTable() {
		return dataSource.exportTable();
	}

	@Override
	public void stop() {
		dataSource.stop();
		dataSource = null;
		dataController = null;
	}
}
