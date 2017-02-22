package Server;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import Table.ColumnType;
import Table.ColumnType.Parameter;
import Table.Row;
import Table.Table;
import User.User;

public class SQLServer {
	public static void main(String[] args) throws IOException {
		//		SQLServer srv = new SQLServer(new User("root", "test"));
		//		HashMap<File, BasicFileAttributes> files = DiskOps.getFiles(new File("G:\\"));
		//		SortedSet<File> keys = new TreeSet<File>(files.keySet());
		//		srv.startConnection();
		//		srv.createDatabase("storage");
		Table table = new Table("Drive_G");
		table.addColumn("id", ColumnType.INT.setSuffix(Parameter.NOT_NULL, Parameter.AUTO_INC, Parameter.UNSIGNED));
		table.addColumn("name", ColumnType.STRING.setParameter(32).setSuffix(Parameter.NOT_NULL));
		table.addColumn("path", ColumnType.STRING.setParameter(255).setSuffix(Parameter.NOT_NULL));
		table.addColumn("size", ColumnType.BIGINT.setSuffix(Parameter.NOT_NULL, Parameter.UNSIGNED));
		table.addColumn("created_date", ColumnType.DATETIME.setSuffix(Parameter.NOT_NULL));
		table.addColumn("accessed_date", ColumnType.DATETIME.setSuffix(Parameter.NOT_NULL));
		table.addColumn("modified_date", ColumnType.DATETIME.setSuffix(Parameter.NOT_NULL));
		table.addColumn("pid", ColumnType.PRIMARY_KEY.setParameter("id"));
		System.out.println(table);
		//		srv.createTable(table);
		//		for (File f : keys) {
		//			if (f.getName().length() <= 32) {
		//				String x =
		//				("NULL,'" + f.getName() + "','" + f.getCanonicalPath().replace("\\" + f.getName(), "") + "', " + f.length() + ", '" + DiskOps.getCreationTime(files.get(f))
		//				+ "', '" + DiskOps.getAccessTime(files.get(f)) + "', '" + DiskOps.getModifiedTime(files.get(f)) + "'");
		//				srv.createRow(table, x);
		//			}
		//		}
	}
	
	private String dbms = "mysql";
	private String addr = "localhost";
	private String port = "3306";
	private Connection connection;
	private User user;
	private String database = "";
	
	public SQLServer(User user) {
		this.user = user;
	}
	
	public SQLServer(User user, String database) {
		this(user);
		this.database = database + "/";
	}
	
	public SQLServer(User user, String addr, String port) {
		this(user);
		this.addr = addr;
		this.port = port;
	}
	
	public SQLServer(User user, String addr, String port, String database) {
		this(user, addr, port);
		this.database = database + "/";
	}
	
	public void createDatabase(String data) {
		String x = "CREATE DATABASE " + (data = data.toLowerCase());
		Statement stmt;
		try {
			stmt = this.getConnection().createStatement();
			stmt.executeUpdate(x);
			System.out.println("CREATED DATABASE: " + data);
		} catch (SQLException e) {
			System.out.println(e.getMessage().split(";")[1].toUpperCase().trim() + ": " + data);
		} finally {
			this.selectDatabase(data);
		}
	}
	
	public void createRow(Table t, Row data) {
		String x = "INSERT INTO " + t.getName() + " VALUES " + data;
		Statement stmt;
		try {
			stmt = this.getConnection().createStatement();
			stmt.executeUpdate(x);
			System.out.println("INSERTED: " + data);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void createTable(Table data) {
		String x = "CREATE TABLE " + data;
		Statement stmt;
		try {
			stmt = this.getConnection().createStatement();
			stmt.executeUpdate(x);
			System.out.println("CREATED TABLE: " + data);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void dropDatabase(String data) {
		String x = "DROP DATABASE " + (data = data.toLowerCase());
		Statement stmt;
		try {
			stmt = this.getConnection().createStatement();
			stmt.executeUpdate(x);
			System.out.println("DELETED DATABASE: " + data);
		} catch (SQLException e) {
			System.out.println(e.getMessage().split(";")[1].toUpperCase().trim() + ": " + data);
		}
	}
	
	public void dropRow(String x) {
	}
	
	public void dropTable(String data) {
		String x = "DROP TABLE " + data;
		Statement stmt;
		try {
			stmt = this.getConnection().createStatement();
			stmt.executeUpdate(x);
			System.out.println("DELETED TABLE: " + data);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public Connection getConnection() {
		return this.connection;
	}
	
	public void selectDatabase(String data) {
		String x = "USE " + (this.database = data.toLowerCase());
		Statement stmt;
		try {
			stmt = this.getConnection().createStatement();
			stmt.executeUpdate(x);
			System.out.println("SELECTED DATABASE: " + data);
		} catch (SQLException e) {
			System.out.println(e.getMessage().split(";")[1].toUpperCase().trim() + ": " + data);
		}
	}
	
	public void startConnection() {
		String conStr = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Properties connProp = new Properties();
			connProp.put("user", this.user.getName());
			connProp.put("password", this.user.getPassword());
			conStr = "jdbc:" + this.dbms + "://" + this.addr + ":" + this.port + "/" + this.database;
			this.connection = DriverManager.getConnection(conStr, connProp);
			System.out.println("SERVER CONNECTION SUCCESS: " + conStr);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("SERVER CONNECTION FAILURE: " + conStr);
			e.printStackTrace();
		}
	}
}