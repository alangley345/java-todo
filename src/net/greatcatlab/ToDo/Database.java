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

	String currentUser = System.getProperty("user.name");
	String path = "/home/"+currentUser+"/.local/share/java-todo/"+currentUser+"_todos.db";
	String url = "jdbc:sqlite:"+path;
	Connection connection;
	
	String sqlCreateUsersTable = "CREATE TABLE IF NOT EXISTS users (\n"
			+ "     id integer PRIMARY KEY,\n"
			+ "     user text NOT NULL\n"
			+ ");";

	String sqlCreateItemsTable = "CREATE TABLE IF NOT EXISTS items (\n"
			+ "     id integer PRIMARY KEY,\n"
			+ "     user_id integer,\n"
			+ "     task text NOT NULL,\n"
			+ "     task_index integer UNIQUE NOT NULL\n"
			+ ");";

	public Database(){
		//create new DB if not there
		try(Connection connection = DriverManager.getConnection(url)) {
			if (connection!=null) {
				createTable(sqlCreateItemsTable);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void createTable(String sql) {
		try(Connection connection = DriverManager.getConnection(url)){
			Statement createTable = connection.createStatement();
			createTable.execute(sql);
			createTable.close();
			System.out.println(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String[]> getAllTasks() {
		String sql = "SELECT id,task,task_index FROM items;";
		System.out.println(sql);
		ArrayList<String[]> resultsList = new ArrayList<>();
		try(Connection connection = DriverManager.getConnection(url)){
			Statement selectAllItems = connection.createStatement();
			ResultSet allItems = selectAllItems.executeQuery(sql);
			while(allItems.next()) {
				String id   = allItems.getString("id");
				String task   = allItems.getString("task");
				String index  = allItems.getString("task_index");
				String[] resultArray = new String[] {id, task, index};
				resultsList.add(resultArray);
				System.out.println(id + " " + task);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return resultsList;
	}

	public void addTask(int task_index, String task) {
		String sql = "INSERT INTO items(task_index,task) VALUES(?,?)";
	    try(Connection connection = DriverManager.getConnection(url)){
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, task_index);
			pstmt.setString(2, task);
			pstmt.executeUpdate();
			System.out.println(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateTask(String oldTask, String newTask) {
		String sql = "UPDATE items SET task=? WHERE task=?";
		try(Connection connection = DriverManager.getConnection(url)){
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, oldTask);
			pstmt.setString(2, newTask);
			pstmt.executeUpdate();
			System.out.println(sql);
		} catch(SQLException e) {
			e.printStackTrace();
		}

	}

	public void deleteTask(String id) {
		String sql = "DELETE FROM items WHERE id=?";
		System.out.println(sql);
		try(Connection connection = DriverManager.getConnection(url)){
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
