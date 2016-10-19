/**
 * Filename: Calculator.java
 * Author: Lucas Estienne, 040 819 959
 * Course: CST8221 – JAP, Lab Section: 302
 * Assignment: 1
 * Date: 10/19/2016
 * Professor: Svillen Ranev
 * Purpose: Launches the Calculator application. Contains the main method for this program.
 */
import java.awt.*;
import javax.swing.*;
/**
 * This class is responsible for launching the calculator. Contains the main method.
 * 
 * @author Lucas Estienne
 * @version 1.0
 * @see javax.swing.JFrame
 * @since 1.8.0_112
*/
public class Calculator {
    /**
     * Name of the Calculator window. Value: {@value}
     */
    private static final String CALCULATOR_FRAME_TITLE = "Calculator";
    /**
     * Width of the initial Calculator frame. Value: {@value}
     */
    private static final int CALCULATOR_FRAME_WIDTH = 330;
    /**
     * Height of the initial Calculator frame. Value: {@value}
     */
    private static final int CALCULATOR_FRAME_HEIGHT = 400;
    /**
     * Duration to display the splash screen for. Value: {@value}
     */
    private static final int SPLASH_SCREEN_DURATION = 5000; 
    
    /**
     * Main method for the Calculator application. Creates a splash screen for the application
     * and then creates & displays a JFrame containing a CalculatorView.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        // instanciate a splash screen that will be displayed for 5000 milliseconds
        CalculatorSplashScreen splashScreen = new CalculatorSplashScreen(SPLASH_SCREEN_DURATION);
        // show the splash screen
        splashScreen.showSplashWindow();
        
        // Make all components to configured by the event dispatch thread
        EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
                // create frame
                JFrame calcFrame = new JFrame();
                calcFrame.setTitle(CALCULATOR_FRAME_TITLE);
                calcFrame.setSize(CALCULATOR_FRAME_WIDTH, CALCULATOR_FRAME_HEIGHT);
                calcFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // set up close button 
                
                // set frame location
                Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
                int x = (screen.width-CALCULATOR_FRAME_WIDTH)/2; // middle of the screen
                int y = (screen.height-CALCULATOR_FRAME_HEIGHT)/2; // middle of the screen
                calcFrame.setLocation(x, y);
                
                // add CalculatorView to frame
                CalculatorView calcView = new CalculatorView();
                calcFrame.add(calcView);
                
                calcFrame.setVisible(true);
            }
         });
    }
    
    
}
