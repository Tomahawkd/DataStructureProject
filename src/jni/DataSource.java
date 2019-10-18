package jni;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

class DataSource {

	private long indexPtr;
	private List<Segment> segmentList;

	static {
		System.loadLibrary("CoreIndexData");
	}

	DataSource() {
		segmentList = new ArrayList<>();
		indexPtr = alloc();
	}

	@Nullable
	Segment getSegment(int bookIndex) {
		int index = getIndex(indexPtr, bookIndex);
		if (index >= 0) {
			try {
				return segmentList.get(index);
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
				return null;
			}
		} else return null;
	}

	void addSegment(@NotNull Segment segment) throws ParseException, JniException {

		// check index
		if (segment.getIndex() < 0)
			throw new ParseException("Negative book index is not valid", 0);
		var index = getIndex(indexPtr, segment.getIndex());

		// No record
		if (index < 0) {

			// record data
			var res = setIndex(indexPtr, -1, segment.getIndex());
			if (res != 0) throw new JniException(res);
			segmentList.add(segment);

			// record index
			var arrIndex = segmentList.indexOf(segment);
			if (arrIndex != -1) {
				int result = updateArrayIndex(indexPtr, segment.getIndex(), arrIndex);
				if (result != 0) throw new JniException(result);
			}

		} else {
			Segment existence = segmentList.get(index);

			// check information
			if (segment.equals(existence)) {
				existence.setAmount(existence.getAmount() + segment.getAmount());
				existence.setRemaining(existence.getRemaining() + segment.getRemaining());
				existence.addRecordList(segment.getRecordList());
			} else throw new ParseException("Duplicated Index", 0);
		}
	}

	void changeIndex(int oldIndex, int newIndex) throws ParseException, JniException {

		// check index
		if (newIndex < 0) throw new ParseException("Negative book index is not valid", 0);

		int index = getIndex(indexPtr, oldIndex);

		// record found
		if (index >= 0) {

			// check index
			if (getIndex(indexPtr, newIndex) >= 0) throw new ParseException("Duplicated Index", 0);

			// record new index
			var result = setIndex(indexPtr, oldIndex, newIndex);
			if (result != 0) throw new JniException(result);
			segmentList.get(index).setIndex(newIndex);

		} else throw new ParseException("Index of the segment not found", 0);
	}

	void delete(@NotNull Segment segment) throws JniException, ParseException {

		// check index
		var index = getIndex(indexPtr, segment.getIndex());

		// check existence
		if (index >= 0 && segment.equals(segmentList.get(index))) {
			int result = deleteIndex(indexPtr, segment.getIndex());
			if (result != 0) throw new JniException(result);
			segmentList.remove(index);

		} else throw new ParseException("Segment not found", 0);
	}

	int[] exportIndex() {
		return exportIndexList(indexPtr);
	}

	Segment[] exportTable() {
		return segmentList.toArray(new Segment[segmentList.size()]);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		segmentList.forEach(sb::append);
		return sb.toString();
	}

	void stop() {
		dealloc(indexPtr);
		indexPtr = 0;
		segmentList.clear();
		segmentList = null;
	}


	/* Native code */

	/**
	 * Allocate a structure to store data at runtime.
	 *
	 * @return data structure address data
	 */
	private native long alloc();

	/**
	 * Deallocate created structure.
	 *
	 * @param dataPointer data structure address data
	 */
	private native void dealloc(long dataPointer);

	/**
	 * Get array index to get specific segment
	 *
	 * @param bookIndex specific book index
	 * @return book information index in array, -1 for not found
	 */
	private native int getIndex(long indexPtr, int bookIndex);

	/**
	 * Set book index.
	 *
	 * @param oldIndex previous book index, -1 if there is no record
	 * @param newIndex new book index to be set, must check negative number.
	 * @return <p>0 for successfully changed</p>
	 * <p>1 for duplication</p>
	 * <p>2 for record not found</p>
	 * <p>3 for parameter error</p>
	 */
	private native int setIndex(long indexPtr, int oldIndex, int newIndex);

	/**
	 * Update array index storing in native block. Using in add function.
	 *
	 * @param bookIndex  book Index
	 * @param arrayIndex the new array index
	 * @return <p>0 for successfully changed</p>
	 * <p>1 for duplication</p>
	 * <p>2 for record not found</p>
	 * <p>3 for parameter error</p>
	 */
	private native int updateArrayIndex(long indexPtr, int bookIndex, int arrayIndex);

	/**
	 * Delete book index.
	 *
	 * @param bookIndex new book index to be deleted, must check negative number.
	 * @return <p>0 for successfully changed</p>
	 * <p>1 for duplication</p>
	 * <p>2 for record not found</p>
	 * <p>3 for parameter error</p>
	 */
	private native int deleteIndex(long indexPtr, int bookIndex);

	/**
	 * Export native index block
	 *
	 * @return book index list
	 */
	private native int[] exportIndexList(long indexPtr);
}
