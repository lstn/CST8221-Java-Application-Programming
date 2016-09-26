package Example1;
/* CST8221-JAP: LAB 01, Example 1
   File name: SimpleSwingGUIe1.java
*/   
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 Simple Java Swing GUI Example.
 The application class serves as a GUI builder and an event handler - controller.
 @version 1.16.2
 @author Svillen Ranev
*/ 
public class SimpleSwingGUIe1 extends WindowAdapter implements ActionListener {
    private JFrame frame;
    private JButton button;
    private JPanel pane;
    private JLabel label;   
    private final String labelText = "Number of happy button clicks: ";
    private int mouseClickCounter;

/**
   Sets the GUI makes it visible
*/
   public void setAndLaunch() {
	// Create and set up containers and components.
	frame = new JFrame("Simple Swing GUI - E1");
	button = new JButton("A Happy New Semester JButton");
        // Create a keyboard shortcut: Pressing ALT-I will act as a mouse click on the button	
	button.setMnemonic('i');
	// Create a Tool Tip. Will show up when the mouse hovers over the button
	button.setToolTipText("Please Click Me");
       // add the event handler to the button
       // the application class serves as an event handler - that is why this reference is used.
        button.addActionListener(this);
        label = new JLabel(labelText + "0");
	pane = new JPanel();
	// set a border around the JPanel
	pane.setBorder(BorderFactory.createEmptyBorder(25, 25, 10, 25));
	// change the default Layout Manager
	// use GridLayout with 2 rows and 1 column
	pane.setLayout(new GridLayout(2, 1));

	// Add components to JPanel container.
	pane.add(button);
	pane.add(label);
	// Install JPanel as a content pane
	// There are three different alternative ways to do that:

	// Alternative 1: works but not recomended
	// If you use this alternative you should make sure that the repacment container is opaque (for example JPanel)
	// Use the setContentPane() method of the JFrame class
	
//     frame.setContentPane(pane);

       // Alternative 2: (recomended)
       // Get the Content Pane of the JFrame class and add the component to it
       Container contentPane = frame.getContentPane();
       contentPane.add(pane);
 
        // Alternative 3: Works since Java 5. Actually it uses Alternative 2
//      frame.add(pane);   
	//add the event handler to the frame
	frame.addWindowListener(this);
        // Size the frame to the preferred size of its components
        // As a result the frame title will be truncated.
	frame.pack();
	// Make the frame not re-sizable. It is re-sizable by default.
	frame.setResizable(false);
	//Let the operating system decide where to place the window on the screen
	frame.setLocationByPlatform(true);  
	// The frame is ready. Make it visible on the screen.
	// The location of the frame on the screen will by determined by the operating system
	frame.setVisible(true);
   }
   //Handle the UI events.   
   /*Handle the frame closing event
     Set up the Close button (X) of the frame.
     You should use this method only if you need
     to free resources or close connections or files before the application closes.
     Override the windowClosing() method of Window Adapter
   */
	@Override
	public void windowClosing(WindowEvent e) {
		//Releases all of the native screen resources used by this frame (Window)
		//Check if all user documents have been saved
                frame.dispose();
		//terminate the application and the Java VM
		System.exit(0);
	}
   // Handle the button clicks. 
   // Override and Implement the actionPerformed() method of the Action Listener interface
    @Override
    public void actionPerformed(ActionEvent e) {
	   // Check which button was clicked and process the event
	   Object eventSource = e.getSource();
	   if (eventSource == button){
	     mouseClickCounter++;
	     label.setText(labelText + mouseClickCounter);
	   } else {/*check for another component*/}
    }
/** 
  The main method.The GUI will have the default Look and Feel - Metal Look and Feel 
  @param args not used
*/
   public static void main(String[] args) {
     new SimpleSwingGUIe1().setAndLaunch();
    }
}
