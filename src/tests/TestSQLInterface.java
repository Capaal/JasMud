package tests;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import interfaces.DatabaseInterface;
import processes.SQLInterface;


// Should probably run delete all sql, then re-make tables, then make what it needs (or start with default). Every test though?
public class TestSQLInterface extends SQLInterface implements DatabaseInterface {
	
	public TestSQLInterface() {
		this.url = "jdbc:mysql://localhost:3306/testjasmud";
	}
	
}
