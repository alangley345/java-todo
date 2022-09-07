import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import java.io.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

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
		
		Display display = new Display();
		Shell shell     = new Shell(display);
		shell.setText(title);
		shell.setSize(width,height);
		shell.setLayout(new GridLayout()); 
		
		final Composite composite = new Composite(shell, SWT.NONE);
		GridLayout grid = new GridLayout();
	  	grid.numColumns = 2;
		composite.setLayout(grid);
		
		//Add item to table
		Button addItemButton = new Button(composite,SWT.PUSH);
		addItemButton.setText("+");
		addItemButton.addListener(SWT.Selection, new Listener()
		{
			public void handleEvent(Event event)
			{
				String title = "Test";
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
				
		shell.open();
		
		while(!shell.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();
		display.dispose();
	}
}