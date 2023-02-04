package net.greatcatlab.ToDo;

import java.io.IOException;
import java.util.ArrayList;

import net.greatcatlab.ToDo.HoverTodo;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ToDo {

	public static final Display display    = new Display();
	public static final String currentUser = System.getProperty("user.name");
	
	
	private void drawTasks(Shell shell, Composite composite) {
		Database db = new Database();
		ArrayList<String[]> resultsList = db.getTasks();
		for (String[] temp : resultsList) {
			new newTodo(composite, temp);
			shell.layout();
		}
	}
	
	private class newTodo{
		Label l1;
		
		newTodo(Composite c, String[] array){
			Composite newRow = new Composite(c,SWT.BORDER);
			newRow.setLayout(new GridLayout(3,false));
			newRow.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,1,1));
			newRow.setData(array[0]);
			
			l1 = new Label(c, SWT.LEFT).setText("Edit");
			Button b = new Button(c, SWT.LEFT).setImage(new Image(display, "./resources/trash-can-icon.svg"));
			Label l2 = new Label(newRow,SWT.LEFT).setText(array[1]);;
			
		    newRow.addListener(SWT.MouseHover, new Listener(){
				@Override
				public void handleEvent(Event event) {
					l.setVisible(true);
					newRow.layout();
					
					
				}
		    });
			
		    newRow.addListener(SWT.MouseExit, new Listener(){
		    	@Override
		    	public void handleEvent(Event event) {
//		    		newRowHover.shell.setVisible(false);
		    	}
		    });
		}
	}
	

//	private static void drawAddButton(Composite composite, Shell shell, Display display, List list) {
//		//Add item to table using text fields in new shell
//		Button addItemButton = new Button(composite,SWT.PUSH);
//		addItemButton.setText("ADD");
//		addItemButton.setSize(shell.getSize().x, 50);
//		addItemButton.addListener(SWT.Selection, new Listener()
//		{
//			@Override
//			public void handleEvent(Event event)
//			{
//				//new shell for add functionality
//				Shell addShell = new Shell(display, SWT.CLOSE);
//				addShell.setText("Add New To Do");
//				addShell.setSize(400, 400);
//				addShell.setLocation(shell.getLocation());
//				addShell.setLayout(new GridLayout());
//				GridData addShellGrid = new GridData(SWT.FILL, SWT.CENTER, true, false);
//
//				//Text for addition
//				Text addContentText = new Text(addShell,SWT.FILL);
//				addContentText.setSize(400,100);
//				addContentText.setLocation(addShell.getLocation().x, addShell.getLocation().y-75);
//				addContentText.setLayoutData(addShellGrid);
//
//				//Finalize adding task to SQL
//				Button internalAddButton = new Button(addShell,SWT.PUSH);
//				internalAddButton.setText("Add Task");
//				internalAddButton.setSize(20, 10);
//				internalAddButton.setLocation(addShell.getLocation().x, addShell.getLocation().y-350);
//				internalAddButton.addListener(SWT.Selection, new Listener()
//				{
//					@Override
//					public void handleEvent(Event event)
//					{
//						Database.insertTask(addContentText.getText());
//						addShell.close();
//						list.removeAll();
//						drawTasks(shell, list);
//					}
//				});
//
//				addShell.open();
//			}
//		});
//	}

//	private static void drawDeleteButton(Composite composite, Shell shell, Display display, List list) {
//		Button deleteButton = new Button(composite,SWT.PUSH);
//		deleteButton.setText("DELETE");
//		deleteButton.setSize(composite.getSize().x, 50);
//		deleteButton.addListener(SWT.Selection, new Listener()
//		{
//			@Override
//			public void handleEvent(Event event){
//				MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
//		        messageBox.setText("Delete");
//		        messageBox.setMessage("Are you sure you want to delete this task?");
//		        int buttonID = messageBox.open();
//		        switch(buttonID) {
//		          case SWT.OK: deleteItems(shell, list);
//		          case SWT.CANCEL:
//		            break;
//		        }
//			}
//		});
//	}

//	private static void drawEditButton(Composite composite, Shell shell, Display display, List list) {
//		Button editButton = new Button(composite,SWT.PUSH);
//		editButton.setText("EDIT");
//		editButton.setSize(composite.getSize().x, 50);
//		editButton.addListener(SWT.Selection, new Listener()
//		{
//			@Override
//			public void handleEvent(Event event){
//				String[] taskToEdit = list.getSelection();
//
//				if(taskToEdit.length == 1){
//					//new shell for editing
//					Shell editShell = new Shell(display, SWT.CLOSE);
//					editShell.setText("Add New To Do");
//					editShell.setSize(400, 400);
//					editShell.setLocation(shell.getLocation());
//					editShell.setLayout(new GridLayout());
//					GridData editShellGrid = new GridData(SWT.FILL, SWT.CENTER, true, false);
//
//					//Text for addition
//					Text editContentText = new Text(editShell,SWT.FILL);
//					editContentText.setSize(400,100);
//					editContentText.setLocation(editShell.getLocation().x, editShell.getLocation().y-75);
//					editContentText.setLayoutData(editShellGrid);
//					editContentText.setText(taskToEdit[0]);
//
//					//SQL update statement
//					Button internalAddButton = new Button(editShell,SWT.PUSH);
//					internalAddButton.setText("Update Task");
//					internalAddButton.setSize(20, 10);
//					internalAddButton.setLocation(editShell.getLocation().x, editShell.getLocation().y-350);
//					internalAddButton.addListener(SWT.Selection, new Listener()
//					{
//						@Override
//						public void handleEvent(Event event)
//						{
//							Database.updateTask(editContentText.getText(), taskToEdit[0]);
//							editShell.close();
//							list.removeAll();
//							drawTasks(shell, list);
//						}
//					});
//					editShell.open();
//				}
//
//			}
//		});
//	}

//	private static void deleteItems(Shell shell, List list){
//
//		String[] searchStrings = list.getSelection();
//		for (String tempString : searchStrings) {
//			Database.deleteItem(tempString);
//		}
//		list.removeAll();
//		drawTasks(shell, list);
//	}

//	private static String chooseSaveLocation() {
//		Shell saveShell = new Shell(display);
//		FileDialog fileSave = new FileDialog(saveShell, SWT.SAVE);
//		fileSave.open();
//		String name = fileSave.getFilterPath() + "/" + fileSave.getFileName();
//		System.out.println(name);
//		return name;
//	}

//	private static void savePDF() {
//		ArrayList<String[]> resultsList = Database.getTasks();
//
//		try {
//			PDDocument document = new PDDocument();
//			PDPage newPage = new PDPage();
//			document.addPage(newPage);
//			PDPage tasks = document.getPage(0);
//			PDPageContentStream contentStream = new PDPageContentStream(document, tasks);
//			contentStream.setFont(PDType1Font.COURIER, 16);
//			contentStream.setLeading(1.15f);
//
//			//header
//			contentStream.beginText();
//			contentStream.newLineAtOffset(200, 750);
//			contentStream.showText(currentUser + "'s To-Dos");
//			contentStream.newLineAtOffset(0, -15);
//			contentStream.newLineAtOffset(0, -15);
//			contentStream.endText();
//
//			//tasks
//			contentStream.beginText();
//			contentStream.newLineAtOffset(25, 700);
//			for(int i = 0; i < resultsList.size(); i++) {
//				String[] temp = resultsList.get(i);
//				contentStream.newLineAtOffset(0, -15);
//				contentStream.showText(i+1 + ". " + temp[0]);
//			}
//
//			contentStream.endText();
//			contentStream.close();
//			String fileName = chooseSaveLocation();
//			document.save(fileName);
//			document.close();
//		}
//		catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//	}

	private static void drawGUI() {
		//variables for shell
		String title        = currentUser + "'s " + "To Dos";
		int    width        = 400;
		int    height       = 800;

		//shell
		Shell shell         = new Shell(display);
		shell.setText(title);
		shell.setSize(width,height);
		shell.setLayout(new GridLayout(1,false));
		shell.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,1,1));
		shell.layout(true,true);

	    //menus
		Menu menuBar = new Menu(shell, SWT.BAR);

		//file
		MenuItem fileMenu = new MenuItem(menuBar, SWT.CASCADE);
		fileMenu.setText("File");
		Menu fileSubMenu = new Menu(shell, SWT.DROP_DOWN);
	    fileMenu.setMenu(fileSubMenu);

	    MenuItem exportAsPDF = new MenuItem(fileSubMenu, SWT.PUSH);
	    exportAsPDF.setText("Save as PDF");
	    exportAsPDF.addListener(SWT.Selection, new Listener() {
	    	 @Override
			public void handleEvent(Event event) {
//	    		 savePDF();
	    	 }
	    });

	    //help
		MenuItem helpMenu = new MenuItem(menuBar, SWT.CASCADE);
		helpMenu.setText("Help");

		shell.setMenuBar(menuBar);

//		// Create a List
//	    List list = new List(shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
//	    list.setLayoutData(shellGridData);
	    
	    //row for the buttons
	    Composite todoComposite = new Composite(shell, 1);
	    todoComposite.setLayout(new GridLayout(1,false));

	    //row for the buttons
	    Composite buttonComposite = new Composite(shell, 1);
	    buttonComposite.setLayout(new GridLayout(3,false));

	    //Add buttons for functions
//		drawAddButton(buttonComposite, shell, display, list);
//		drawEditButton(buttonComposite, shell, display, list);
//		drawDeleteButton(buttonComposite, shell, display, list);

		//Populate the tasks in the list
		new drawTasks(shell, todoComposite);
		
		todoComposite.layout();
		shell.pack();
		shell.open();
		while(!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	public static void main(String[] args) {
		drawGUI();
	}
}