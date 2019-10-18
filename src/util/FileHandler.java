package util;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.FileAlreadyExistsException;
import java.util.regex.Pattern;

public class FileHandler {

	private static File file;
	private static FileHandler handler;

	public static final String EXTENSION = "csvd";

	public static FileHandler getHandler() {
		return getHandler(FileHandler.file);
	}

	public static FileHandler getHandler(@NotNull File file) {
		if (handler == null) handler = new FileHandler();
		FileHandler.file = file;
		return handler;
	}


	/**
	 * Using on read operation
	 *
	 * @return data in string
	 * @throws IOException exception when file reading error occurs
	 */

	public String read() throws IOException {
		return read(FileHandler.file);
	}

	/**
	 * Using on import operation
	 *
	 * @param file data path File instance
	 * @return string data
	 * @throws IOException exception when file reading error occurs
	 */
	public String read(@NotNull File file) throws IOException {
		try (FileInputStream in = new FileInputStream(file)) {
			return new String(in.readAllBytes(), Charset.forName("utf8"));
		}
	}


	/**
	 * Using on save operation
	 *
	 * @param content string data
	 * @throws IOException exception when file writing error occurs
	 */

	public void write(String content) throws IOException {
		write(FileHandler.file, content);
	}


	/**
	 * Using on save as operation
	 *
	 * @param file    data path File instance, default as save operation
	 * @param content string data
	 * @throws IOException exception when file writing error occurs
	 */

	public void write(@NotNull File file, String content) throws IOException {

		String path = file.getAbsolutePath();
		if (!path.endsWith(EXTENSION)) file = new File(path + "." + EXTENSION);

		String name = file.getName();
		if (Pattern.compile("[\\\\/:\"*?<>|]").matcher(name).find() |
				Pattern.compile("^[.]").matcher(name).find())
			throw new IllegalArgumentException("File name invalid");

		if (file.exists()) {
			if (file != FileHandler.file) {
				int value = JOptionPane.showConfirmDialog(null,
						"File exist. Overwrite?",
						"Exist",
						JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.WARNING_MESSAGE);

				if (value != JOptionPane.YES_OPTION) return;
			}
		} else {
			int value = JOptionPane.showConfirmDialog(null,
					"File not found. Create?",
					"Not Found",
					JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE);
			if (value == JOptionPane.YES_OPTION) create(file);
			else return;
		}

		try (BufferedWriter csvWriter = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file), Charset.forName("utf8")), 1024)) {
			csvWriter.write(content);
		}
	}

	/**
	 * Create a new empty file.
	 *
	 * @param file File instance
	 * @throws IOException exception when file creating error occurs
	 */
	private void create(@NotNull File file) throws IOException {
		String path = file.getAbsolutePath();
		if (!path.endsWith(EXTENSION)) file = new File(path + "." + EXTENSION);

		String name = file.getName();
		if (Pattern.compile("[\\\\/:\"*?<>|]").matcher(name).find() |
				Pattern.compile("^[.]").matcher(name).find())
			throw new IllegalArgumentException("File name invalid");

		if (!file.createNewFile()) {
			throw new FileAlreadyExistsException("File exist at " + file.getAbsolutePath());
		}
	}

	/**
	 * Create a new empty file.
	 *
	 * @throws IOException exception when file creating error occurs
	 */

	public void create() throws IOException {
		create(FileHandler.file);
	}
}
