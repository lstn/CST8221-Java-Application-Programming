/* CST8221-JAP: LAB 01, Example 2R
   File name: SimpleSwingGUIe2R.java
   Images source: http://www.tech-faq.com/emoticons/
   To run this example in Eclipse or Netbeans first copy the image files into
   the source (src) folder of the project folder.
*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/** 
 Simple Java GUI Example.
 This example is a modification of Example 2.
 It uses a a resource locator to indicate the location of the image files (see line 26 and line 119)
 @version 1.16.2
 @author Svillen Ranev
*/
public class SimpleSwingGUIe2R extends JFrame implements ActionListener {
    //default serial version ID - Swing components implement the Serializable interface 
    private static final long serialVersionUID = 1L;
    private JButton button;
    private JPanel pane;
    private JLabel label;
    private final static String FRAME_TITLE = "Simple Swing GUI - E2R";
    private final String frameImageName = "duke.gif";
   //using a resource locator to indicate the location of the image files
    private final Icon buttonImage = new ImageIcon(getClass().getResource("happy.gif"));   
    private final String labelText = "Number of happy button clicks: ";
    private int mouseClickCounter;
/**
  Default constructor. Sets the frame properties.
*/ 
   public SimpleSwingGUIe2R(){ 
     super(FRAME_TITLE);
     setFrameProperties();
   }   
/**
   Sets the GUI makes it visible
*/
   public void setAndLaunch() {
	// Create and set up containers and components.
	// Create a button with an image and text
	button = new JButton("A Happy New Semester JButton",buttonImage);
        // Create a keyboard shortcut: Pressing ALT-I will act as a mouse click on the button	
	button.setMnemonic('i');
	// Create a Tool Tip. Will show up when the mouse hovers over the button
	button.setToolTipText("Please Click Me");
        // Set the frame to be the event handler for the button click
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
	Container contentPane = getContentPane();
        contentPane.add(pane);

	//Handle the UI events.   
        // Create a handler class for the frame closing event
        // Set up the Close button (X) of the frame.
	// Since a in Java class can extend one class only
	// anonymous inner class which inherits from WindowAdapter and overrides the WindowClosing() method
	// is used to handle the frame closing event	
	WindowListener wl = new WindowAdapter() {
	 @Override  public void windowClosing(WindowEvent e) {
		//Releases all of the native screen resources used by this frame (Window)
		//Check if all user documents have been saved
                SimpleSwingGUIe2R.this.dispose();
		//terminate the application and the Java VM
		System.exit(0);
	   }
	};
	// add the event handler wl to the frame
	addWindowListener(wl);
        // The frame is ready. Make it visible on the screen.
        //   pack();
	setVisible(true);
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
  Set the frame properties.
*/  
   public void setFrameProperties(){
    //get the current screen size     	   
      Toolkit tkit = Toolkit.getDefaultToolkit();  
      Dimension screenSize = tkit.getScreenSize();
      int screenHeight = screenSize.height;
      int screenWidth = screenSize.width;

      // set frame size - width and height
      setSize(screenWidth / 2, screenHeight / 5);
     
     // and let platform pick screen location
     // setLocationByPlatform(true);//ByPlatform
     //Display the frame in the center of the screen
     //Since Java 1.7 the same can be achieved with a single call. Try it.
     //setLocationRelativeTo(null); 
      setLocation(screenWidth / 4, screenHeight / 4);
 
     // set frame icon and title
     Image img = tkit.getImage(getClass().getResource(frameImageName));
     setIconImage(img);
   }

/** 
  The main method.The GUI will have the default Look and Feel - Metal Look and Feel 
  @param args not used
*/
   public static void main(String[] args) {
     new SimpleSwingGUIe2R().setAndLaunch();
   }
}
