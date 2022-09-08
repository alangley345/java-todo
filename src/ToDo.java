import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import java.io.*;
import java.sql.*;
import java.util.*;

public class ToDo {
	
	public static final String currentUser = System.getProperty("user.name");
	public static final String url = "jdbc:sqlite:/home/" + currentUser + "/.local/java-todo/" + currentUser + "_" + "todos.db";
	
	private static void createNewDB() {
		
		File appDirectory = new File("/home/" + currentUser + "/.local/java-todo");
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
	
	public static void main(String[] args) {
		createNewDB();
		
		//variables for shell
		String title        = "My To Dos";
		int    width        = 400;
		int    height       = 800;
		
		//shell & bar
		Display display = new Display();
		Shell shell     = new Shell(display);
		shell.setText(title);
		shell.setSize(width,height);
		shell.setLayout(new GridLayout()); 
		ExpandBar expandBar = new ExpandBar (shell, SWT.NONE);
		
		//composite for inside the expand bar
		final Composite barComposite = new Composite(expandBar, SWT.NONE);
		GridLayout barLayout             = new GridLayout();
		barLayout.verticalSpacing    = 10;
		barComposite.setLayout(barLayout);
		
		//shell layout
		//final Composite composite = new Composite(shell, SWT.NONE);
		//GridLayout grid = new GridLayout();
	  	//grid.numColumns = 2;
		//composite.setLayout(grid);
		
		//Add item to table
		/*Button addItemButton = new Button(shell,SWT.PUSH);
		addItemButton.setText("+");
		addItemButton.addListener(SWT.Selection, new Listener()
		{
			public void handleEvent(Event event)
			{
				String title = "Grocery Shopping";
				String content = "This is a test";
				String sql = "INSERT INTO items(title,content) VALUES(?,?)";

			    try (Connection conn = DriverManager.getConnection(url)){
			    	PreparedStatement pstmt = conn.prepareStatement(sql);
			        pstmt.setString(1, title);
			        pstmt.setString(2, content);
			        pstmt.executeUpdate();
			    } 
			    catch (SQLException e) {
			        System.out.println(e.getMessage());
			    }
			}  	    
		});
		*/
		
		String getAllTasks = "SELECT title, content FROM items;";
		try (Connection conn = DriverManager.getConnection(url)){
			Statement selectAllItems = conn.createStatement();
	    	ResultSet allItems = selectAllItems.executeQuery(getAllTasks);
	    
	    	ArrayList<String[]> resultsList = new ArrayList<>();
	    	
	    	while(allItems.next()) {
	    		String resultTitle   = allItems.getString("title");
	    		String resultContent = allItems.getString("content");
	    		String[] resultArray = new String[] {resultTitle, resultContent};
	    		resultsList.add(resultArray);
	    	}
	    
	    	for(int i = 0; i < resultsList.size(); i++) {
	    		String[] temp = resultsList.get(i);
	    		Composite itemComposite = new Composite(expandBar, SWT.NONE);
	    		
	    		//content
	    		Text contentText = new Text(itemComposite,SWT.NONE);
	    		contentText.setText(temp[1]);
	    		contentText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	    		contentText.setSize(shell.computeSize(SWT.DEFAULT, SWT.DEFAULT).y, 50);
	    		
	    		//title
	    		ExpandItem item = new ExpandItem (expandBar, SWT.NONE);
	    		item.setText(temp[0]);
	    		item.setExpanded(true);
	    		item.setHeight(itemComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
	    		item.setControl(itemComposite);	
	    	}
	    } 
	    catch (SQLException e) {
	        System.out.println(e.getMessage());
	    }
				
		shell.open();			
		while(!shell.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();
		display.dispose();
	}
}