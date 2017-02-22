package Table;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class Row {
	private Table table;
	private PreparedStatement stmt;
	
	public Row(Table t, Connection c) {
		this.table = t;
	}
}
