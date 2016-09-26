/* CST8221-JAP: LAB 02, Application Splash Screen
   File name: Lab02.java
*/
import java.awt.*;
import javax.swing.*;
/**
 A simple class demonstrating how to create a splash screen for an application using JWindow as a container.
 The splash screen will appear on the screen for the duration given to the constructor.
 The class includes a main() method for testing purposes, but it should be used as a part of
 an application.
 Note: Since JWindow uses a default frame when made visible it does not receives events.
       If you want to process events using JWindow, you must create an undecorated frame
       and attach JWindow to this frame using an appropriate JWindow constructor.
 
 * Modified by Lucas Estienne for the purposes of Lab 02
 
 @version 1.16.2
 @author Svillen Ranev
 @author Lucas Estienne
*/
public class Lab02 extends JWindow {
 /* Swing components are serializable and require serialVersionUID */
  private static final long serialVersionUID = 6248477390124803341L;
  private final int duration;
/**
  Default constructor. Sets the show time of the splash screen.
*/
  public Lab02(int duration) {
    this.duration = duration;
  }
/**
 Shows a splash screen in the center of the desktop
 for the amount of time given in the constructor.
*/
  public void showSplashWindow() {
   //create content pane
     JPanel content = new JPanel(new BorderLayout());
   // or use the window content pane
   //  JPanel content = (JPanel)getContentPane();
     content.setBackground(Color.DARK_GRAY);
    
    // Set the window's bounds, position the window in the center of the screen
    int width =  906+10;
    int height = 499+10;
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (screen.width-width)/2;
    int y = (screen.height-height)/2;
    //set the location and the size of the window
    setBounds(x,y,width,height);

    // create the splash screen
    JLabel label = new JLabel(new ImageIcon("LucasSplash.png"));
    JLabel demo = new JLabel("<html><font color='white'>Lucas Estienne</font></html>", JLabel.CENTER);
    demo.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 14));
    content.add(label, BorderLayout.CENTER);
    content.add(demo, BorderLayout.SOUTH);
    // create custom RGB color
    Color customColor = new Color(44, 197, 211);
    content.setBorder(BorderFactory.createLineBorder(customColor, 10));
    
//   Container contentPane = getContentPane();
//   contentPane.add(content);
//      add(content);
     //replace the window content pane with the content JPanel
      setContentPane(content);

    // Display it
//    pack();
    //make the splash window visible
    setVisible(true);

    // Wait a little while doing nothing, while the application is loading
    try {
    	
    	 Thread.sleep(duration); }
    catch (Exception e) {e.printStackTrace();}
    //destroy the window and release all resources
    dispose(); 
    //hide the splash window, the resources will not be released
    //setVisible(false);
  }

/** 
  The main method. Used for testing purposes.
  @param args the time duration of the splash screen in milliseconds.
              If not specified, the default duration is 10000 msec (10 sec).  
*/
  public static void main(String[] args) {
    int duration = 10000;
    if(args.length == 1){
    	try{
    	 duration = Integer.parseInt(args[0]);
    	}catch (NumberFormatException mfe){
    	  System.out.println("Wrong command line argument: must be an integer number");
    	  System.out.println("The default duration 10000 milliseconds will be used");
    	  //mfe.printStackTrace();	
    	} 
    }
    // Create the screen
    Lab02 splashWindow = new Lab02(duration);
    // Normally,the splash.showSplashWindow() will be called by the main application class.
    splashWindow.showSplashWindow();
  }//end main
}// end Lab02 class
