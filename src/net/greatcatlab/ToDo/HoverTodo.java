package net.greatcatlab.ToDo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


//	Reference found @ https://sites.google.com/site/javatipstocode/how-to/how-to-display-hover-text-in-swt

public class HoverTodo {
	Shell shell;
	Label label;
	
	public HoverTodo(Display d, Composite c){
		
		shell = new Shell(d, SWT.ON_TOP | SWT.TOOL);
		shell.setLayout(new GridLayout(3,false));
		shell.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,1,1));
		shell.setLocation(c.getLocation());
	}

} 