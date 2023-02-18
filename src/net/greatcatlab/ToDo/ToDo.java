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
import org.eclipse.swt.graphics.ImageData;
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
	Composite newRow;
	Button editButton, deleteButton, taskButton;
		
	ToDo(Display display, Composite composite, todoData data){
		Shell s = composite.getShell();
		newRow = new Composite(composite,SWT.NONE);
		newRow.setLayout(new GridLayout(3,false));
		newRow.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,1,1));
		newRow.setData(data.id);
				
		taskButton = new Button(newRow,SWT.LEFT);
		taskButton.setText(data.task);
		taskButton.setSize(32, 32);
		
		editButton = imageButton(display, newRow, 32, "./resources/pencil-black.svg");
		deleteButton = imageButton(display, newRow, 32, "./resources/trash-can-icon.svg");
		deleteButton.addListener(SWT.Selection, new Listener()
		{
			public void handleEvent(Event event){
				MessageBox messageBox = new MessageBox(new Shell(display), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
		        messageBox.setText("Delete");
		        messageBox.setMessage("Are you sure you want to delete this task?");
		        int buttonID = messageBox.open();
		        switch(buttonID) {
		          case SWT.OK: 
		        	  new Database().deleteTask(String.valueOf((Integer) newRow.getData()));
		        	  newRow.dispose();
		        	  composite.pack();
		        	  s.layout();
		        	  
		          case SWT.CANCEL:
		            break;
		        }
			}
		});
		
		taskButton.addListener(SWT.Selection, new Listener(){
			public void handleEvent(Event event) {
				editButton.setVisible(!editButton.getVisible());
				deleteButton.setVisible(!deleteButton.getVisible());
					
				newRow.layout();
				newRow.redraw();
				composite.layout();
			}
	    });
			
	    newRow.addListener(SWT.MouseExit, new Listener(){
	    	public void handleEvent(Event event) {
//		   		editLabel.setVisible(false);
//				deleteButton.setVisible(false);
//				newRow.layout();
	    	}		    
	    });

	}
	
	public static class todoData{
		int id;
		String task;
		
		todoData(String[] temp){
			this.id=Integer.valueOf(temp[0]);
			this.task=temp[1];
		}
	}
	
	private static Button imageButton(Display display, Composite composite, int size, String imageLocation) {
		Button button = new Button(composite, SWT.LEFT);
		button.setImage(new Image(display, new ImageData(imageLocation).scaledTo(size,size)));
		button.setSize(size,size);
		button.setVisible(false);
		return button;
	}
}