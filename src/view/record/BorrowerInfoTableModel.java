package view.record;

import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.List;

public class BorrowerInfoTableModel implements TableModel {

	private List<Pair<Long, String>> borrowerInfoList;

	BorrowerInfoTableModel(@NotNull List<Pair<Long, String>> list) {
		this.borrowerInfoList = list;
	}



	@Override
	public int getRowCount() {
		return borrowerInfoList.size();
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
			case 0: return "Borrower ID";
			case 1: return "Returning Date";
			default: return "";
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
			case 0: return borrowerInfoList.get(rowIndex).getKey().toString();
			case 1: return borrowerInfoList.get(rowIndex).getValue();
			default: return "";
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
