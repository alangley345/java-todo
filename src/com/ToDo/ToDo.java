package com.ToDo;
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.List;

import java.io.*;
import java.sql.*;
import java.util.*;

public class ToDo {
	
	public static final String currentUser = System.getProperty("user.name");
	public static final String url = "jdbc:sqlite:/home/" + currentUser + "/.local/share/java-todo/" + currentUser + "_" + "todos.db";
	
	private static void createNewDB() {
		//check for individuals tables and create it if it doesn't exist
		File appDirectory = new File("/home/" + currentUser + "/.local/share/java-todo");
		appDirectory.mkdir();
		String sqlCreateTable = "CREATE TABLE IF NOT EXISTS items (\n"
				+ "     id integer PRIMARY KEY,\n"
				+ "     task text NOT NULL\n"
				+ ");";
				
		try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("Connection to DB.");
                
                Statement createTable = conn.createStatement();
                createTable.execute(sqlCreateTable);
                createTable.close();
            }
        } 
		catch (SQLException e) {
            System.out.println(e.getMessage());
        }
	}
	
	private static ArrayList<String[]> getTasks() {
		String getAllTasks = "SELECT task FROM items;";
		ArrayList<String[]> resultsList = new ArrayList<>();
		
		try(Connection conn = DriverManager.getConnection(url)){
			Statement selectAllItems = conn.createStatement();
			ResultSet allItems       = selectAllItems.executeQuery(getAllTasks);
			    	
			while(allItems.next()) {
				String resultTasks   = allItems.getString("task");
				String[] resultArray = new String[] {resultTasks};
				resultsList.add(resultArray);
			}
		}
		catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return resultsList;
	}
	
	private static void drawTasks(Shell shell, List list) {
		ArrayList<String[]> resultsList = getTasks();
		for(int i = 0; i < resultsList.size(); i++) {
			String[] temp = resultsList.get(i);
			list.add(temp[0]);
		}
	}
	
	private static void drawAddButton(Composite composite, Shell shell, Display display, List list) {
		//Add item to table using text fields in new shell
		Button addItemButton = new Button(composite,SWT.PUSH);
		addItemButton.setText("ADD");
		addItemButton.setSize(shell.getSize().x, 50);
		addItemButton.addListener(SWT.Selection, new Listener()
		{
			public void handleEvent(Event event)
			{
				//new shell for add functionality
				Shell addShell = new Shell(display, SWT.CLOSE);
				addShell.setText("Add New To Do");
				addShell.setSize(400, 400);
				addShell.setLocation(shell.getLocation());
				addShell.setLayout(new GridLayout());
				GridData addShellGrid = new GridData(SWT.FILL, SWT.CENTER, true, false);
				
				//Text for addition
				Text addContentText = new Text(addShell,SWT.FILL);
				addContentText.setSize(400,100);
				addContentText.setLocation(addShell.getLocation().x, addShell.getLocation().y-75);
				addContentText.setLayoutData(addShellGrid);
				
				//Finalize adding task to SQL
				Button internalAddButton = new Button(addShell,SWT.PUSH);
				internalAddButton.setText("Add Task");
				internalAddButton.setSize(20, 10);
				internalAddButton.setLocation(addShell.getLocation().x, addShell.getLocation().y-350);
				internalAddButton.addListener(SWT.Selection, new Listener()
				{
					public void handleEvent(Event event)
					{	
						String sql = "INSERT INTO items(task) VALUES(?)";
					    try (Connection conn = DriverManager.getConnection(url)){
							PreparedStatement pstmt = conn.prepareStatement(sql);
							pstmt.setString(1, addContentText.getText());
							pstmt.executeUpdate();
						} 
						catch (SQLException e) {
							System.out.println(e.getMessage());
						}
						addShell.close();
						list.removeAll();
						drawTasks(shell, list);
					}  	    
				});
				
				addShell.open();	
			}  	    
		});
	}
	
	private static void deleteItems(Shell shell, List list){
		
		String[] searchStrings = list.getSelection();
		for(int i = 0; i < searchStrings.length; i++) {
			String tempString = searchStrings[i];
			String sql = "DELETE FROM items WHERE task=?";
			try(Connection conn = DriverManager.getConnection(url)){
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, tempString);
				pstmt.executeUpdate();
			}
			catch(SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		list.removeAll();
		drawTasks(shell, list);
	}
	
	private static void drawDeleteButton(Composite composite, Shell shell, Display display, List list) {	
		Button deleteButton = new Button(composite,SWT.PUSH);
		deleteButton.setText("DELETE");
		deleteButton.setSize(composite.getSize().x, 50);
		deleteButton.addListener(SWT.Selection, new Listener()
		{
			public void handleEvent(Event event){
				MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
		        messageBox.setText("Delete");
		        messageBox.setMessage("Are you sure you want to delete this task?");
		        int buttonID = messageBox.open();
		        switch(buttonID) {
		          case SWT.OK: deleteItems(shell, list);
		          case SWT.CANCEL:
		            break;
		        }
			} 	    
		});
	}
	
	private static void drawEditButton(Composite composite, Shell shell, Display display, List list) {	
		Button editButton = new Button(composite,SWT.PUSH);
		editButton.setText("EDIT");
		editButton.setSize(composite.getSize().x, 50);
		editButton.addListener(SWT.Selection, new Listener()
		{
			public void handleEvent(Event event){
				String[] taskToEdit = list.getSelection();
				
				if(taskToEdit.length == 1){
					//new shell for editing
					Shell editShell = new Shell(display, SWT.CLOSE);
					editShell.setText("Add New To Do");
					editShell.setSize(400, 400);
					editShell.setLocation(shell.getLocation());
					editShell.setLayout(new GridLayout());
					GridData editShellGrid = new GridData(SWT.FILL, SWT.CENTER, true, false);
					
					//Text for addition
					Text editContentText = new Text(editShell,SWT.FILL);
					editContentText.setSize(400,100);
					editContentText.setLocation(editShell.getLocation().x, editShell.getLocation().y-75);
					editContentText.setLayoutData(editShellGrid);
					editContentText.setText(taskToEdit[0]);
				
					//SQL update statement
					Button internalAddButton = new Button(editShell,SWT.PUSH);
					internalAddButton.setText("Update Task");
					internalAddButton.setSize(20, 10);
					internalAddButton.setLocation(editShell.getLocation().x, editShell.getLocation().y-350);
					internalAddButton.addListener(SWT.Selection, new Listener()
					{
						public void handleEvent(Event event)
						{	
							String sql = "UPDATE items SET task=? WHERE task=?";
							try(Connection conn = DriverManager.getConnection(url)){
								PreparedStatement pstmt = conn.prepareStatement(sql);
								pstmt.setString(1, editContentText.getText());
								pstmt.setString(2, taskToEdit[0]);
								pstmt.executeUpdate();
							}
							catch(SQLException e) {
								System.out.println(e.getMessage());
							}
							editShell.close();
							list.removeAll();
							drawTasks(shell, list);
						}	    
					});
					editShell.open();
				}
				
			} 	    
		});
	}
	
	public static void drawGUI() {
		//variables for shell
		String title        = currentUser + "'s " + "To Dos";
		int    width        = 400;
		int    height       = 800;
								
		//shell & display
		Display display     = new Display();
		Shell shell         = new Shell(display, SWT.CLOSE);
		shell.setText(title);
		shell.setSize(width,height);
		shell.setLayout(new GridLayout());
		
		GridData shellGridData                = new GridData(SWT.FILL, SWT.FILL, true, false);
		shellGridData.horizontalAlignment     = GridData.FILL;
		shellGridData.grabExcessVerticalSpace = true;
		shellGridData.widthHint               = shell.getSize().x;
		shell.setLayoutData(shellGridData);
		shell.layout(true,true);
		
		
		//row for the menu
	    Composite menuComposite = new Composite(shell, 1);
	    menuComposite.setLayout(new RowLayout());
	    
		Menu menuBar = new Menu(shell, SWT.BAR);
		MenuItem fileMenu = new MenuItem(menuBar, SWT.CASCADE);
		fileMenu.setText("File");
		MenuItem helpMenu = new MenuItem(menuBar, SWT.CASCADE);
		helpMenu.setText("Help");
		
		
		// Create a List
	    List list = new List(shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
	    list.setLayoutData(shellGridData);
		
	    //row for the buttons
	    Composite buttonComposite = new Composite(shell, 1);
	    buttonComposite.setLayout(new RowLayout());
	    
	    //Add buttons for functions
		drawAddButton(buttonComposite, shell, display, list);
		drawEditButton(buttonComposite, shell, display, list);
		drawDeleteButton(buttonComposite, shell, display, list);
		
		//Populate the tasks in the list
		drawTasks(shell, list);
		
		shell.open();
		while(!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
	
	public static void main(String[] args) {
		createNewDB();
		drawGUI();
	}
}