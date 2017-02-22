package Table;

import java.util.ArrayList;
import java.util.List;

public class Table {
	private String name;
	private List<Column> columns;
	
	public Table(String name) {
		this.name = name.toLowerCase();
		this.columns = new ArrayList<Column>();
	}
	
	public boolean addColumn(String x, ColumnType t) {
		return this.columns.add(new Column(x, t));
	}
	
	public boolean delColumn(String key) {
		return this.columns.remove(key);
	}
	
	public List<Column> getColumns() {
		return this.columns;
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		System.out.println(columns);
		String y = this.name + " " + "(";
		if (columns.size() > 0) {
			for (int i = 0; i < (columns.size() - 1); i++) {
				y = y + columns.get(i) + ", ";
			}
			y = y + columns.get(columns.size() - 1);
		}
		y += ")";
		return y;
	}
}
