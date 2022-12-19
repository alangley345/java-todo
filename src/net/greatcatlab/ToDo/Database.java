package net.greatcatlab.ToDo;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Database {

	static final String currentUser = System.getProperty("user.name");
	static final String path = "/home/"+currentUser+"/.local/share/java-todo/"+
			currentUser+"_todos.db";
	static final String url = "jdbc:sqlite:"+path;
	String sqlCreateUserTable = "CREATE TABLE IF NOT EXISTS users (\n"
			+ "     id integer PRIMARY KEY,\n"
			+ "     user text NOT NULL\n"
			+ ");";

	String sqlCreateTaskTable = "CREATE TABLE IF NOT EXISTS items (\n"
			+ "     id integer PRIMARY KEY,\n"
			+ "     user_id integer NOT NULL,\n"
			+ "     task text NOT NULL\n"
			+ ");";

	public Database(){
		//create new DB if not there
		File appDirectory = new File(path);
		appDirectory.mkdir();
		createTable(sqlCreateUserTable);
		createTable(sqlCreateTaskTable);
	}

	private static void createTable(String sql) {
		try(Connection c = DriverManager.getConnection(url)){
			Statement createTable = c.createStatement();
	        createTable.execute(sql);
	        createTable.close();
	        System.out.println(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch blocks
			e.printStackTrace();
		}
	}

	public static ArrayList<String[]> getTasks() {
		String getAllTasks = "SELECT task FROM items;";
		System.out.println(getAllTasks);
		ArrayList<String[]> resultsList = new ArrayList<>();

		try(Connection c = DriverManager.getConnection(url)){
			Statement selectAllItems = c.createStatement();
			ResultSet allItems       = selectAllItems.executeQuery(getAllTasks);
			while(allItems.next()) {
				String resultTasks   = allItems.getString("task");
				String[] resultArray = new String[] {resultTasks};
				resultsList.add(resultArray);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return resultsList;
	}


	public static void insertTask(String task) {
		String sql = "INSERT INTO items(task) VALUES(?)";
	    try(Connection c = DriverManager.getConnection(url)){
			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setString(1, task);
			pstmt.executeUpdate();
			System.out.println(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void updateTask(String oldTask, String newTask) {
		String sql = "UPDATE items SET task=? WHERE task=?";
		try(Connection c = DriverManager.getConnection(url)){
			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setString(1, oldTask);
			pstmt.setString(2, newTask);
			pstmt.executeUpdate();
			System.out.println(sql);
		} catch(SQLException e) {
			e.printStackTrace();
		}

	}

	public static void deleteItem(String tempString) {
		String sql = "DELETE FROM items WHERE task=?";
		System.out.println(sql);
		try(Connection c = DriverManager.getConnection(url)){
			PreparedStatement pstmt = c.prepareStatement(sql);
			pstmt.setString(1, tempString);
			pstmt.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
