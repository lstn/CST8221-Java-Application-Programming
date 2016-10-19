/**
 * Filename: CalculatorSplashScreen.java
 * Author: Lucas Estienne, 040 819 959
 * Course: CST8221 – JAP, Lab Section: 302
 * Assignment: 1
 * Date: 10/19/2016
 * Professor: Svillen Ranev
 * Purpose: Launches the Calculator application. Contains the main method for this program.
 * Class list: CalculatorView, Controller
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 *
 * @author Lucas
 */
public class CalculatorView extends JPanel{
    private JTextField display; // the calculator display field reference
    private JLabel error; // the error display label reference
    private JButton dotButton; //the decimal point (dot) button reference
    
    public CalculatorView(){
        Controller calcViewController = new Controller();
    }
    
    private JButton createButton(String text, String ac, Color fg, ActionListener handler){
        return null;
    }
    
    private class Controller implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent e) { 
            //code that reacts to the action... 
        }
    }
}
