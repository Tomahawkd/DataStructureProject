package view.record;

import jni.DataController;
import jni.Segment;

import javax.swing.*;
import java.text.ParseException;

class DataEditorView extends JDialog {
	private JPanel contentPane;
	private JButton buttonOK;
	private JButton buttonCancel;
	private JTextField indexTextField;
	private JTextField nameTextField;
	private JTextField authorTextField;
	private JTextField amountTextField;
	private JTextField remainTextField;

	DataEditorView(Segment segment) {
		setContentPane(contentPane);
		setModal(true);
		setBounds(350, 200, 640, 360);
		getRootPane().setDefaultButton(buttonCancel);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();

		if (segment != null) {
			setTitle("Edit");
			indexTextField.setText(String.valueOf(segment.getIndex()));
			nameTextField.setText(segment.getName());
			authorTextField.setText(segment.getAuthor());
			amountTextField.setText(String.valueOf(segment.getAmount()));
			remainTextField.setText(String.valueOf(segment.getRemaining()));
		} else {
			setTitle("New");
		}

		buttonOK.addActionListener(e -> {
			try {
				int index = Integer.parseInt(indexTextField.getText());
				String name = nameTextField.getText();
				String author = authorTextField.getText();
				int amount = Integer.parseInt(amountTextField.getText());
				int remain = Integer.parseInt(remainTextField.getText());

				if (amount < 0 || remain < 0 || amount < remain)
					throw new NumberFormatException();

				if (segment != null) {

					// change book index
					if (segment.getIndex() != index)
						DataController.getInstance().setDataIndex(segment.getIndex(), index);

					Segment oldSegment = DataController.getInstance().get(segment.getIndex());

					// check null
					if (oldSegment == null) throw new ParseException("Data Not found", 0);

					// update other data
					if (!segment.getName().equals(name)) oldSegment.setName(name);
					if (!segment.getAuthor().equals(author)) oldSegment.setAuthor(author);
					if (segment.getAmount() != amount) oldSegment.setAmount(amount);
					if (segment.getRemaining() != remain) oldSegment.setRemaining(remain);

				} else DataController.getInstance().add(new Segment(index, name, author, amount, remain));

				dispose();
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(null,
						"Invalid Data", "Invalid", JOptionPane.WARNING_MESSAGE);
			} catch (ParseException e1) {
				JOptionPane.showMessageDialog(null,
						e1.getMessage(), "Invalid", JOptionPane.WARNING_MESSAGE);
			}
		});

		buttonCancel.addActionListener(e -> dispose());

		setVisible(true);
	}
}
