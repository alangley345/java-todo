import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class ToDo {     	
	
	public static void main(String[] args) {
		String title        = "My To Dos";
		int    width        = 400;
		int    height       = 800;
		String storedValue = "";
		
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