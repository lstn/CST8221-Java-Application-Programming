/**
 * Filename: CalculatorSplashScreen.java
 * Author: Lucas Estienne, 040 819 959
 * Course: CST8221 - JAP, Lab Section: 302
 * Assignment: 1
 * Date: 10/18/2016
 * Professor: Svillen Ranev
 * Purpose: Implements the CalculatorSplashScreen class used to display a splash screen while
 *          the Calculator GUI loads.
 */

import java.awt.*;
import javax.swing.*;
/**
 * This class implements a splash screen for the Calculator application.
 * 
 * @author Lucas Estienne
 * @version 1.0
 * @see javax.swing.JWindow
 * @since 1.8.0_112
*/
public class CalculatorSplashScreen extends JWindow {
    /**
     * Default duration in milliseconds to display the splash screen for. Value: {@value}
     */
    private static final int DEFAULT_SPLASH_DURATION = 10000;
    /**
     * Filename of the image to be displayed on the splash screen. Value: {@value}
     */
    private static final String SPLASH_IMAGE_FILENAME = "LucasSplash.png";
    /**
     * Text to be displayed on the splash screen. Value: {@value}
     */
    private static final String SPLASH_TEXT = "<html><font color='white'>Lucas Estienne 040 819 959</font></html>";
    
    /**
     * Swing components are serializable and require serialVersionUID. Value: {@value}
     */
    private static final long serialVersionUID = 6248477390124803341L;
    
    /**
     * Contains the duration in milliseconds to display the splash screen for.
     */
    private int splashDuration;
    
    /**
     * Default constructor for the CalculatorSplashScreen class.
     * Sets the duration of the splash screen to {@link #DEFAULT_SPLASH_DURATION}
    */
    public CalculatorSplashScreen() {
        this.splashDuration = DEFAULT_SPLASH_DURATION;
    }
    
    /**
     * Default constructor for the CalculatorSplashScreen class.
     * Sets the duration of the splash screen to the passed parameter.
     * 
     * @param splashDuration duration for which to display the splash screen
    */
    public CalculatorSplashScreen(int splashDuration) {
        this.splashDuration = splashDuration;
    }
    
    /**
     * Shows a splash screen in the center of the desktop
     * for the amount of time given in the constructor.
    */
    public void showSplashWindow() {
        //create content pane
        JPanel content = new JPanel(new BorderLayout());
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
        JLabel splashImage = new JLabel(new ImageIcon(SPLASH_IMAGE_FILENAME));
        JLabel splashText = new JLabel(SPLASH_TEXT, JLabel.CENTER);
        splashText.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 14));
        content.add(splashImage, BorderLayout.CENTER);
        content.add(splashText, BorderLayout.SOUTH);
        // create custom RGB color
        Color customColor = new Color(44, 197, 211);
        content.setBorder(BorderFactory.createLineBorder(customColor, 10));

        //replace the window content pane with the content JPanel
        setContentPane(content);

        // make the splash window visible
        setVisible(true);

        // Wait a little while doing nothing, while the application is loading
        try {
            Thread.sleep(splashDuration);
        } catch (Exception e) {e.printStackTrace();}
        //destroy the window and release all resources
        
        dispose(); 
    }
}
