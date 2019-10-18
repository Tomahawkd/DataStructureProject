package view.record;

import jni.DataController;
import jni.Segment;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class RecordTableModel implements TableModel {

	private Segment[] tableData;

	RecordTableModel() {
		updateData();
	}

	void updateData() {
		tableData = DataController.getInstance().exportToTable();
	}

	Segment get(int index) {
		return tableData[index];
	}

	@Override
	public int getRowCount() {
		return tableData.length;
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
			case 0:
				return "Index";
			case 1:
				return "Name";
			case 2:
				return "Author";
			case 3:
				return "Amount";
			case 4:
				return "Remaining";
			default:
				return "";
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
			case 0:
			case 3:
			case 4:
				return Integer.class;

			case 1:
			case 2:
				return String.class;

			default:
				return null;
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
			case 0:
				return tableData[rowIndex].getIndex();
			case 1:
				return tableData[rowIndex].getName();
			case 2:
				return tableData[rowIndex].getAuthor();
			case 3:
				return tableData[rowIndex].getAmount();
			case 4:
				return tableData[rowIndex].getRemaining();
			default:
				return null;
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
	}
}
