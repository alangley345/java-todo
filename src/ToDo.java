import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import java.io.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ToDo {
	
	public static void createNewDB(String fileName) {
		String currentUser = System.getProperty("user.name");
		File appDirectory = new File("/home/" + currentUser + "/.local/java-todo");
		appDirectory.mkdir();
		
		String url = "jdbc:sqlite:/home/" + currentUser + "/.local/java-todo/" + currentUser + "_" + fileName;
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
                conn.close();
         
            }

        } 
		
		catch (SQLException e) {
            System.out.println(e.getMessage());
        }
	}
	
	public static void main(String[] args) {
		createNewDB("todos.db");
		String title        = "My To Dos";
		int    width        = 400;
		int    height       = 800;
		
		Display display = new Display();
		Shell shell     = new Shell(display);
		shell.setText(title);
		shell.setSize(width,height);
				
		shell.open();
		
		while(!shell.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();
		display.dispose();
	}
}