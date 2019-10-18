package view.edit;

import jni.DataController;

import javax.swing.*;
import javax.swing.event.ListDataListener;

public class IndexComboBoxModel implements ComboBoxModel<Integer> {

	private int[] indexList;
	private int current;

	IndexComboBoxModel() {
		updateIndex();
	}

	void updateIndex() {
		indexList = DataController.getInstance().exportIndex();
	}

	@Override
	public void setSelectedItem(Object anItem) {
		current = ((Integer) anItem);
	}

	@Override
	public Object getSelectedItem() {
		return current;
	}

	@Override
	public int getSize() {
		return indexList.length;
	}

	@Override
	public Integer getElementAt(int index) {
		return indexList[index];
	}

	@Override
	public void addListDataListener(ListDataListener l) {
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
	}
}
