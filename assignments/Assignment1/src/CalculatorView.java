/**
 * Filename: CalculatorView.java
 * Author: Lucas Estienne, 040 819 959
 * Course: CST8221 - JAP, Lab Section: 302
 * Assignment: 1
 * Date: 10/19/2016
 * Professor: Svillen Ranev
 * Purpose: Responsible for building the calculator GUI.
 * Class list: CalculatorView, Controller
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.nio.charset.StandardCharsets;
/**
 * This class is responsible for building the Calculator GUI.
 * 
 * @author Lucas Estienne
 * @version 1.0
 * @see javax.swing.JPanel
 * @since 1.8.0_112
*/
public class CalculatorView extends JPanel{
    /**
     * A 25x25 Dimension object. Value: {@value}
     */
    private static final Dimension DIM_25 = new Dimension(25, 25);
    /**
     * String representing the LEFTWARD_ARROW character. Value: {@value}
     */
    private static final String LEFTWARD_ARROW_CHAR = new String("\u2190".getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
    /**
     * String representing the PLUSMINUS character. Value: {@value}
     */
    private static final String PLUS_MINUS_CHAR = new String("\u00B1".getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
    /**
     * Array of the (string) buttons to be displayed in the calculator keypad. Value: {@value}
     */
    private static final String[] KEYPAD_BUTTONS = { 
        "7",             "8", "9", "/",
        "4",             "5", "6", "*",
        "1",             "2", "3", "-",
        PLUS_MINUS_CHAR, "0", ".", "+"
    };
    /**
     * Array of the (string) radio buttons to be displayed in the calculator modes. Value: {@value}
     */
    private static final String[] RADIO_BUTTONS = {".0", ".00", "Sci"};
    
    /**
     * the calculator display field reference
     */
    private JTextField display; 
    /**
     * the error display label reference
     */
    private JLabel error; 
    /**
     * the decimal point (dot) button reference
     */
    private JButton dotButton; 
    
    /**
     * Default constructor for the CalculatorView class.
     * Builds a CalculatorView GUI panel.
    */
    public CalculatorView(){
        // initialize the CalculatorView
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLACK));
        
        // create a Controller for the buttons in this frame
        Controller calcViewController = new Controller();
        
        // create Calculator panels
        JPanel displayContainer = createCalcDisplayContainer(new FlowLayout(FlowLayout.CENTER), calcViewController);
        JPanel modesContainer = createCalcModesContainer(new FlowLayout(FlowLayout.CENTER, 0, 0), calcViewController);
        Box keypadContainer = createCalcKeypadContainer(new GridLayout(4, 4, 5, 5), new BorderLayout(), new BorderLayout(), calcViewController);
        
        // add panels to CalculatorView
        this.add(displayContainer, BorderLayout.NORTH);
        this.add(keypadContainer, BorderLayout.SOUTH);
        this.add(modesContainer, BorderLayout.CENTER); 
    }
    
    /**
     * This method builds the GUI for the top (display) portion of the calculator GUI.
     * Also sets the `display` and `error` fields.
     * 
     * @param layout LayoutManager for which to instantiate the JPanel with.
     * @param handler ActionLister handler for the buttons in this frame.
     * @return JPanel a panel containing the top (display) portion of the calculator GUI.
    */
    private JPanel createCalcDisplayContainer(LayoutManager layout, ActionListener handler){
        JPanel displayContainer = new JPanel(layout); // create top flow layout panel
        
        // create display text field
        this.display = new JTextField(16);
        this.display.setPreferredSize(new Dimension(this.display.getWidth(), 30));
        this.display.setBackground(Color.WHITE);
        this.display.setHorizontalAlignment(SwingConstants.RIGHT); // text has right-alignment
        this.display.setText(String.valueOf(0.0)); // default value on startup
        this.display.setEditable(false); 
        
        // create error label
        this.error = new JLabel("F", SwingConstants.CENTER);
        this.error.setOpaque(true); // show the background
        this.error.setBackground(Color.YELLOW);
        this.error.setPreferredSize(DIM_25); // size 25x25
        
        // create backspace button
        
        JButton backspaceBtn = new JButton(LEFTWARD_ARROW_CHAR);
        backspaceBtn.setForeground(Color.RED);
        backspaceBtn.setBackground(Color.RED);
        backspaceBtn.setBorder(new LineBorder(Color.RED));
        backspaceBtn.setContentAreaFilled(false); // transparent
        backspaceBtn.setPreferredSize(DIM_25);
        backspaceBtn.setActionCommand("Backspace");
        backspaceBtn.setToolTipText("Backspace (Alt-B)");
        backspaceBtn.setMnemonic(KeyEvent.VK_B); // bind key combo Alt+B to this button
        backspaceBtn.addActionListener(handler);
        
        // add textfield, label and backspace to display container
        displayContainer.add(this.error);
        displayContainer.add(this.display);
        displayContainer.add(backspaceBtn);
        
        return displayContainer;
    }
    
    /**
     * This method builds the GUI for the middle (mode check/radio boxes) portion of the calculator GUI.
     * 
     * @param layout LayoutManager for which to instantiate the JPanel with.
     * @param handler ActionLister handler for the buttons in this frame.
     * @return JPanel a panel containing the middle (mode check/radio boxes) portion of the calculator GUI.
    */
    private JPanel createCalcModesContainer(LayoutManager layout, ActionListener handler){
        JPanel modesContainer = new JPanel(layout); // middle flow layout panel
        ButtonGroup checkGroup = new ButtonGroup(); // button group to group the radio buttons together
        
        // initialize modes container
        modesContainer.setBorder(BorderFactory.createMatteBorder(8, 1, 8, 1, Color.BLACK));
        modesContainer.setBackground(Color.BLACK);
        
        // create checkbox
        JCheckBox intCheckBox = new JCheckBox("Int");
        intCheckBox.setBackground(Color.GREEN);
        intCheckBox.setOpaque(true);
        intCheckBox.addActionListener(handler);
        
        // add checkbox
        modesContainer.add(intCheckBox);
        modesContainer.add(Box.createHorizontalStrut(15)); // add separator strut (whitespace) between checkbox and radios
        
        for (String radioStr : RADIO_BUTTONS){ // loop to create radios
            JRadioButton curRadio = new JRadioButton(radioStr);
            curRadio.setBackground(Color.YELLOW);
            curRadio.setOpaque(true);
            
            String ac = (radioStr.equals("Sci")) ? radioStr : "f" + radioStr; // adds a 0 in front of strings that represent the .0 or .00 buttons
            curRadio.setActionCommand(ac); // set ac
            curRadio.addActionListener(handler);
            if (ac.equals("f.00")) { curRadio.setSelected(true); } // set the default radio that is checked on launch
            
            // add radio to button group and modes container
            checkGroup.add(curRadio);
            modesContainer.add(curRadio);
        }

        return modesContainer;
    }
    
     /**
     * This method builds the GUI for the bottom (keypad) portion of the calculator GUI.
     * 
     * @param numKeysLayout LayoutManager for which to instantiate actual keypad keys.
     * @param leftBtnLayout LayoutManager for the left hand side button
     * @param rightBtnLayout LayoutManager for the right hand side button
     * @param handler ActionLister handler for the buttons in this frame.
     * @return Box a Box containing the bottom (keypad) portion of the calculator GUI.
    */
    private Box createCalcKeypadContainer(LayoutManager numKeysLayout, LayoutManager leftBtnLayout, LayoutManager rightBtnLayout, ActionListener handler){
        Box keypadContainer = Box.createHorizontalBox(); // bottom box
        JPanel clearBtnContainer = new JPanel(leftBtnLayout); // bottom left panel
        JPanel numericBtnsContainer = new JPanel(numKeysLayout); // bottom middle panel
        JPanel equalsBtnContainer = new JPanel(rightBtnLayout); // bottom right panel
        
        // create clear button and add it to the clear button container
        JButton clearButton = createButton("C", null, Color.BLACK, Color.RED, handler);
        clearBtnContainer.add(clearButton);
        
        for (int i=0; i < KEYPAD_BUTTONS.length; i++){ // loop through to create the actual keypad buttons
            Color curBtnTextCol = ((i+1)%4 == 0) ? Color.YELLOW: Color.BLACK;  // operators are yellow text, normal keys are black
            JButton curKpButton = createButton(KEYPAD_BUTTONS[i], null, curBtnTextCol, Color.BLUE, handler);
            curKpButton.setBorder(new EmptyBorder(45, 17, 45, 16));
            
            if(KEYPAD_BUTTONS[i].equals(".")){ this.dotButton = curKpButton; } // set dotButton class field
            
            // add created button to numeric buttons container
            numericBtnsContainer.add(curKpButton);
        }
        
        // create equal button and add it to the equal button container
        JButton equalsButton = createButton("=", null, Color.BLACK, Color.YELLOW, handler);
        equalsBtnContainer.add(equalsButton);
        
        // add the 3 containers to the Box
        keypadContainer.add(clearBtnContainer);
        keypadContainer.add(Box.createHorizontalStrut(7)); // adding strut for formatting
        keypadContainer.add(numericBtnsContainer);
        keypadContainer.add(Box.createHorizontalStrut(7)); // adding strut for formatting
        keypadContainer.add(equalsBtnContainer);
        
        // set keypad Box height
        keypadContainer.setPreferredSize(new Dimension(keypadContainer.getWidth(), 270)); 
        
        return keypadContainer;
    }
    
    /**
     * This method builds the GUI for the bottom (keypad) portion of the calculator GUI.
     * 
     * @param text String button text
     * @param ac String action command for the button
     * @param fg Color foreground color for the button
     * @param bg Color background color for the button
     * @param handler ActionLister handler for the buttons in this frame.
     * @return JButton the button created by this method
    */
    private JButton createButton(String text, String ac, Color fg, Color bg, ActionListener handler){
        JButton createdButton = new JButton(text); // create button
        
        // set back and foregrounds
        createdButton.setBackground(bg);
        createdButton.setForeground(fg);
        if(text.equals(PLUS_MINUS_CHAR)){
            ac = "PLUS_MINUS_CHAR";
        }
        if (ac != null){ createdButton.setActionCommand(ac); } // only set ation command if ac is not null
        
        createdButton.setFont(createdButton.getFont().deriveFont(20.0f)); // increase font size to 20
        createdButton.addActionListener(handler);
        
        return createdButton;
    }
    
    /**
    * This class implements an ActionListener controller.
    * 
    * @author Lucas Estienne
    * @version 1.0
    * @see java.awt.event.ActionListener
    * @since 1.8.0_112
    */
    private class Controller implements ActionListener {
        
        CalculatorModel calcModel = new CalculatorModel();
        /**
        * Overrides ActionListener.actionPerformed(), handles calculator action commands.
        * 
        * @param e ActionEvent that has occurred
        */
        @Override
        public void actionPerformed(ActionEvent e) { 
            String ac = e.getActionCommand(); // get action string
            System.out.println(ac);
            switch(ac){
                case "Int":
                    calcModel.toggleCalcMode(error, dotButton);
                    break;
                case "f.00":
                    calcModel.setFloatingPresision(2);
                    break;
                case "f.0":
                    calcModel.setFloatingPresision(1);
                    break;
                case "Sci":
                    calcModel.setFloatingPresision(3);
                    break;
                case "Backspace":
                    calcModel.backspaceOperand();
                    display.setText(calcModel.peekLastOperand());
                    break;
                case "PLUS_MINUS_CHAR":
                    calcModel.invertResult();
                    display.setText(calcModel.getResult(error));
                    break;
                case "C":
                    calcModel.clearOperands(error);
                    display.setText(calcModel.getResult(error));
                    break;
                case "=":
                    calcModel.performCalculations();
                    display.setText(calcModel.getResult(error)); // set the display
                    break;
                default:
                    calcModel.addOperand(ac);
                    display.setText(calcModel.peekLastOperand());
                    break;
            }
            
        }
    }
}
