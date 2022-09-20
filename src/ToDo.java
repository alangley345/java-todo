import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import java.io.*;
import java.sql.*;
import java.util.*;

public class ToDo {
	
	public static final String currentUser = System.getProperty("user.name");
	public static final String url = "jdbc:sqlite:/home/" + currentUser + "/.local/share/java-todo/" + currentUser + "_" + "todos.db";
	
	public static ArrayList<String[]> resultsList = new ArrayList<>();
	
	private static void createNewDB() {
		//check for individuals tables and create it if it doesn't exist
		File appDirectory = new File("/home/" + currentUser + "/.local/share/java-todo");
		appDirectory.mkdir();
		String sqlCreateTable = "CREATE TABLE IF NOT EXISTS items (\n"
				+ "     id integer PRIMARY KEY,\n"
				+ "     title text NOT NULL,\n"
				+ "     content text NOT NULL\n"
				+ ");";
				
		try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
                
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
		String getAllTasks = "SELECT title, content FROM items;";
		ArrayList<String[]> resultsList = new ArrayList<>();
		
		try(Connection conn = DriverManager.getConnection(url)){
			Statement selectAllItems = conn.createStatement();
			ResultSet allItems = selectAllItems.executeQuery(getAllTasks);
			    	
			while(allItems.next()) {
				String resultTitle   = allItems.getString("title");
				String resultContent = allItems.getString("content");
				String[] resultArray = new String[] {resultTitle, resultContent};
				resultsList.add(resultArray);
			}
		}
		catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return resultsList;
	}
	
	private static void drawTasks(Shell shell, ArrayList<String[]> resultsList) {
		//expandbar for tasks
	    ExpandBar expandBar          = new ExpandBar(shell, SWT.FILL);
	    GridData expandBarGridData   = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
	    expandBarGridData.widthHint  = (shell.getSize().x);
	    expandBar.setLayoutData(expandBarGridData);
	   
		for(int i = 0; i < resultsList.size(); i++) {
			String[] temp = resultsList.get(i);
			Composite itemComposite = new Composite(expandBar, SWT.NONE);
			GridLayout itemGrid = new GridLayout();
			itemGrid.numColumns = 1;
			itemComposite.setLayout(itemGrid);
			    		
			//content
			Text contentText = new Text(itemComposite,SWT.CENTER);
			contentText.setText(temp[1]);
			GridData contentData =  new GridData(SWT.FILL, SWT.CENTER, true, false);
			contentText.setLayoutData(contentData);
			contentText.setSize(shell.getSize().x,50);
			    		
			//title
			ExpandItem item = new ExpandItem (expandBar, SWT.FILL);
			item.setText(temp[0]);
			item.setExpanded(true);
			item.setHeight(100);
			item.setControl(itemComposite);	
			    		
			Button deleteButton = new Button(itemComposite,SWT.PUSH);
			deleteButton.setText("Delete");
			deleteButton.addListener(SWT.Selection, new Listener()
			{
				public void handleEvent(Event event){
					String searchString = contentText.getText();
			    	String sql = "DELETE FROM items WHERE content=?";
			    	try(Connection conn = DriverManager.getConnection(url)){
			    		PreparedStatement pstmt = conn.prepareStatement(sql);
			    		pstmt.setString(1, searchString);
			    		pstmt.executeUpdate();
			    	}
			    	catch(SQLException e) {
	    			    System.out.println(e.getMessage());
	    			}
			    } 	    
			});
		}
	}
	
	private static void drawAddButton(Shell shell, Display display) {
		
		//Add item to table using text fields in new shell
		Button addItemButton = new Button(shell,SWT.PUSH);
		addItemButton.setText("+");
		addItemButton.addListener(SWT.Selection, new Listener()
		{
			public void handleEvent(Event event)
			{
				//Display addDisplay = new Display();
				Shell addShell = new Shell(display, SWT.CLOSE);
				addShell.setText("Add New To Do");
				addShell.setSize(400, 400);
				addShell.setLocation(shell.getLocation());
				addShell.setLayout(new GridLayout());
				
				Text addTitleText   = new Text(addShell,SWT.FILL);
				addTitleText.setSize(addShell.getSize().x,25);
				addTitleText.setLocation(addShell.getLocation());
				
				Text addContentText = new Text(addShell,SWT.FILL);
				addContentText.setSize(100,addShell.getSize().x);
				addContentText.setLocation(addShell.getLocation().x, addShell.getLocation().y-75);
				
				Button internalAddButton = new Button(addShell,SWT.PUSH);
				internalAddButton.setText("Add Task");
				internalAddButton.setSize(20, 10);
				internalAddButton.setLocation(addShell.getLocation().x, addShell.getLocation().y-350);
				internalAddButton.addListener(SWT.Selection, new Listener()
				{
					public void handleEvent(Event event)
					{	
						String sql     = "INSERT INTO items(title,content) VALUES(?,?)";
					    try (Connection conn = DriverManager.getConnection(url)){
							PreparedStatement pstmt = conn.prepareStatement(sql);
							pstmt.setString(1, addTitleText.getText());
							pstmt.setString(2, addContentText.getText());
							pstmt.executeUpdate();
						} 
						catch (SQLException e) {
							System.out.println(e.getMessage());
						}
						addShell.close();					
					}  	    
				});
				
				addShell.open();	
			}  	    
		});
	}
	
	public static void drawGUI() {
		//variables for shell
		String title        = "My To Dos";
		int    width        = 400;
		int    height       = 600;
								
		//shell & display
		Display display = new Display();
		Shell shell     = new Shell(display, SWT.CLOSE);		
		shell.setText(title);
		shell.setSize(width,height);
		shell.setLayout(new GridLayout());
		shell.layout(true);
		
		drawAddButton(shell, display);
		
		Display.getDefault().asyncExec(new Query() {
			public void run() {
				getTasks();
				display.asyncExec(this);
	        }
	    });
		
		//ArrayList<String[]> resultsList = getTasks();
		drawTasks(shell, resultsList);
		
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