package jni;

import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * <p>Book information class.</p>
 * <p>
 * <p>Contains book index, name, author, amount and remaining.</p>
 * <p>Generally, the book name, author, amount and remaining is
 * readable and writeable outside the package. Instead, the index
 * is a key to access all of the information, so it is only readable
 * outside the package.</p>
 * <p>
 * <h3>Usage:</h3>
 * <p>Get: <code>segment = DataController.getInstance().get(bookIndex);</code></p>
 * <p>Update: <code>DataController.getInstance().get(bookIndex).setXXX(xxx);</code></p>
 * <p>Update index: <code>DataController.getInstance().changeIndex(oldIndex, newIndex);</code></p>
 *
 * @see DataController
 * @see DataSource
 */
public class Segment {

	private int index;
	private String name;
	private String author;
	private int amount;
	private int remaining;
	private BorrowerInfoList infoList;


	public static final String HEADER = "\"index\",\"name\",\"author\",\"amount\",\"remaining\",\"borrow record\"\n";

	public Segment(int index, String name, String author, int amount, int remaining) {
		this(index, name, author, amount, remaining, null);
	}

	private Segment(int index, String name, String author, int amount, int remaining, BorrowerInfoList list) {
		this.index = index;
		this.name = name;
		this.author = author;
		this.amount = amount;
		this.remaining = remaining;
		if (list == null) this.infoList = new BorrowerInfoList();
		else this.infoList = list;
	}

	public static Segment parse(String line) throws ParseException {
		line = line.trim();

		String[] split = line.split(",");
		if (split.length == 6) {

			// remove ""
			for (int i = 0; i < split.length; i++) {
				split[i] = split[i].substring(1, split[i].length() - 1);
			}

			try {

				int amount = Integer.parseInt(split[3]);
				int remain = Integer.parseInt(split[4]);
				if (amount < 0 || remain < 0 || amount < remain)
					throw new ParseException("line(" + line + ") book remaining or amount invalid", 0);

				return new Segment(Integer.parseInt(split[0]),
						split[1], split[2], amount, remain,
						BorrowerInfoList.parse(split[5]));
			} catch (NumberFormatException e) {
				throw new ParseException("line(" + line + ") parse error", 0);
			}
		} else throw new ParseException("line(" + line + ") format error", 0);
	}

	public int getIndex() {
		return index;
	}

	void setIndex(int index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getRemaining() {
		return remaining;
	}

	public void setRemaining(int remaining) {
		this.remaining = remaining;
	}

	public void addRecord(long borrowerId, String returningDate) throws ParseException {
		this.infoList.add(borrowerId, returningDate);
	}

	public void addRecordList(List<Pair<Long, String>> recordList) throws ParseException {
		for (Pair<Long, String> info : recordList) {
			this.infoList.add(info.getKey(), info.getValue());
		}
	}

	public int removeRecord(long borrowerId) {
		try {
			this.infoList.delete(borrowerId);
			return 0;
		} catch (ParseException e) {
			return 1;
		}
	}

	@NotNull
	public List<Pair<Long, String>> getRecordList() {
		return this.infoList.toList();
	}

	@Override
	public String toString() {
		return "\"" + index + "\"," +
				"\"" + name + "\"," +
				"\"" + author + "\"," +
				"\"" + amount + "\"," +
				"\"" + remaining + "\"," +
				"\"" + infoList + "\"\n";
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj || obj != null && obj instanceof Segment &&
				index == ((Segment) obj).index &&
				Objects.equals(name, ((Segment) obj).name) &&
				Objects.equals(author, ((Segment) obj).author);
	}
}

class BorrowerInfo {

	private long borrowerId;
	private String returningDate;

	private BorrowerInfo(long borrowerId, String returningDate) {
		this.borrowerId = borrowerId;
		this.returningDate = returningDate;
	}

	@NotNull
	static BorrowerInfo createInfo(long borrowerId, String returningDate) throws ParseException {

		// check date
		if (!Pattern.compile("(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]|[0-9][1-9][0-9]{2}|[1-9][0-9]{3})" +
				"-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|" +
				"(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|" +
				"((0[48]|[2468][048]|[3579][26])00))-02-29)").matcher(returningDate).matches())
			throw new ParseException("Date: " + returningDate + " is invalid.", 0);

		else return new BorrowerInfo(borrowerId, returningDate);
	}

	long getBorrowerId() {
		return borrowerId;
	}

	String getReturningDate() {
		return returningDate;
	}

	public boolean equals(long borrowerId) {
		return this.borrowerId == borrowerId;
	}

	@Override
	public String toString() {
		return "\"" + borrowerId + ":" + returningDate + "\";";
	}
}

class BorrowerInfoList {

	private List<BorrowerInfo> infoList;

	BorrowerInfoList() {
		infoList = new ArrayList<>();
	}

	private BorrowerInfoList(@NotNull List<BorrowerInfo> list) {
		this.infoList = list;
	}

	@NotNull
	static BorrowerInfoList parse(@NotNull String data) throws ParseException {
		data = data.trim();

		if (data.isEmpty()) return new BorrowerInfoList();

		List<BorrowerInfo> list = new ArrayList<>();
		String[] split = data.split(";");
		for (int i = 0; i < split.length; i++) {
			split[i] = split[i].substring(1, split[i].length() - 1);
			String[] seg = split[i].split(":");
			if (seg.length == 2) {
				long borrowerId = Long.parseLong(seg[0]);
				String returningDate = seg[1];
				list.add(BorrowerInfo.createInfo(borrowerId, returningDate));
			}
		}

		return new BorrowerInfoList(list);
	}

	void add(long borrowerId, String returningDate) throws ParseException {
		infoList.add(BorrowerInfo.createInfo(borrowerId, returningDate));
	}

	void delete(long borrowerId) throws ParseException {
		for (BorrowerInfo info : infoList) {
			if (info.equals(borrowerId)) {
				infoList.remove(info);
				return;
			}
		}

		throw new ParseException("ID record not found", 0);
	}

	@NotNull
	List<Pair<Long, String>> toList() {

		ArrayList<Pair<Long, String>> list = new ArrayList<>();
		infoList.forEach(borrowerInfo ->
				list.add(new Pair<>(
						borrowerInfo.getBorrowerId(),
						borrowerInfo.getReturningDate()
				)));

		return list;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		infoList.forEach(sb::append);
		return sb.toString();
	}
}
