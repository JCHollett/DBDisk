package Table;

public class Column {
	private String id;
	private String column;
	private ColumnType type;
	
	public Column(String id, ColumnType col) {
		this.id = id;
		this.column = col.toString();
	}
	
	@Override
	public String toString() {
		return this.id + " " + this.column;
	}
}
