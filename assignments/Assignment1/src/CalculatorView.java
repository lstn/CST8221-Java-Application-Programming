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
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
/**
 *
 * @author Lucas
 */
public class CalculatorView extends JPanel{
    private static final Dimension DIM_25 = new Dimension(25, 25);
    private static final String[] KEYPAD_BUTTONS = { 
        "7", "8", "9", "/",
        "4", "5", "6", "*",
        "1", "2", "3", "-",
        "±", "0", ".", "+"
    };
    private static final String[] RADIO_BUTTONS = {".0", ".00", "Sci"};
    
    private JTextField display; // the calculator display field reference
    private JLabel error; // the error display label reference
    private JButton dotButton; //the decimal point (dot) button reference
    
    public CalculatorView(){
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLACK));
        
        Controller calcViewController = new Controller();

        JPanel displayContainer = createCalcDisplayContainer(new FlowLayout(FlowLayout.CENTER), calcViewController);
        JPanel modesContainer = createCalcModesContainer(new FlowLayout(FlowLayout.CENTER, 0, 0), calcViewController);
        Box keypadContainer = createCalcKeypadContainer(new GridLayout(4, 4, 5, 5), new BorderLayout(), new BorderLayout(), calcViewController);

        this.add(displayContainer, BorderLayout.NORTH);
        this.add(keypadContainer, BorderLayout.SOUTH);
        this.add(modesContainer, BorderLayout.CENTER); 
    }
    
    private JPanel createCalcDisplayContainer(LayoutManager layout, ActionListener handler){
        JPanel displayContainer = new JPanel(layout); // top flow layout panel
        
        this.display = new JTextField(16);
        this.display.setPreferredSize(new Dimension(this.display.getWidth(), 30));
        this.display.setBackground(Color.WHITE);
        this.display.setHorizontalAlignment(SwingConstants.RIGHT);
        this.display.setText(String.valueOf(0.0));
        this.display.setEditable(false);
        
        this.error = new JLabel("F", SwingConstants.CENTER);
        this.error.setOpaque(true);
        this.error.setBackground(Color.YELLOW);
        this.error.setPreferredSize(DIM_25);
        
        JButton backspaceBtn = new JButton("←");//createButton(, null, Color.RED, Color.RED, calcViewController);
        backspaceBtn.setForeground(Color.RED);
        backspaceBtn.setBackground(Color.RED);
        backspaceBtn.setBorder(new LineBorder(Color.RED));
        backspaceBtn.setContentAreaFilled(false);
        backspaceBtn.setPreferredSize(DIM_25);
        backspaceBtn.setToolTipText("Backspace (Alt-B)");
        backspaceBtn.setMnemonic(KeyEvent.VK_B);
        backspaceBtn.addActionListener(handler);
        
        displayContainer.add(this.error);
        displayContainer.add(this.display);
        displayContainer.add(backspaceBtn);
        
        return displayContainer;
    }
    
    private JPanel createCalcModesContainer(LayoutManager layout, ActionListener handler){
        JPanel modesContainer = new JPanel(layout); // middle flow layout panel
        ButtonGroup checkGroup = new ButtonGroup();
        
        modesContainer.setBorder(BorderFactory.createMatteBorder(8, 1, 8, 1, Color.BLACK));
        modesContainer.setBackground(Color.BLACK);
        
        JCheckBox intCheckBox = new JCheckBox("Int");
        intCheckBox.setBackground(Color.GREEN);
        intCheckBox.setOpaque(true);
        intCheckBox.addActionListener(handler);
        
        modesContainer.add(intCheckBox);
        modesContainer.add(Box.createHorizontalStrut(15));
        
        for (String radioStr : RADIO_BUTTONS){
            JRadioButton curRadio = new JRadioButton(radioStr);
            curRadio.setBackground(Color.YELLOW);
            curRadio.setOpaque(true);
            
            String ac = (radioStr.equals("Sci")) ? radioStr : "0" + radioStr;
            curRadio.setActionCommand(ac);
            curRadio.addActionListener(handler);
            if (ac.equals("0.00")) { curRadio.setSelected(true); }
            
            checkGroup.add(curRadio);
            modesContainer.add(curRadio);
        }

        return modesContainer;
    }
    
    private Box createCalcKeypadContainer(LayoutManager numKeysLayout, LayoutManager leftBtnLayout, LayoutManager rightBtnLayout, ActionListener handler){
        Box keypadContainer = Box.createHorizontalBox(); // bottom box
        JPanel clearBtnContainer = new JPanel(leftBtnLayout); // bottom left panel
        JPanel numericBtnsContainer = new JPanel(numKeysLayout); // bottom middle panel
        JPanel equalsBtnContainer = new JPanel(rightBtnLayout); // bottom right panel
        
        JButton clearButton = createButton("C", null, Color.BLACK, Color.RED, handler);
        clearBtnContainer.add(clearButton);
        
        for (int i=0; i < KEYPAD_BUTTONS.length; i++){
            Color curBtnTextCol = ((i+1)%4 == 0) ? Color.YELLOW: Color.BLACK;
            JButton curKpButton = createButton(KEYPAD_BUTTONS[i], null, curBtnTextCol, Color.BLUE, handler);
            curKpButton.setBorder(new EmptyBorder(45, 17, 45, 16));
            
            if(KEYPAD_BUTTONS[i].equals(".")){ this.dotButton = curKpButton; }
            
            numericBtnsContainer.add(curKpButton);
        }
        
        JButton equalsButton = createButton("=", null, Color.BLACK, Color.YELLOW, handler);
        equalsBtnContainer.add(equalsButton);
        
        keypadContainer.add(clearBtnContainer);
        keypadContainer.add(Box.createHorizontalStrut(7));
        keypadContainer.add(numericBtnsContainer);
        keypadContainer.add(Box.createHorizontalStrut(7));
        keypadContainer.add(equalsBtnContainer);
        
        keypadContainer.setPreferredSize(new Dimension(keypadContainer.getWidth(), 270));
        
        return keypadContainer;
    }
    
    private JButton createButton(String text, String ac, Color fg, Color bg, ActionListener handler){
        JButton createdButton = new JButton(text);
        
        createdButton.setBackground(bg);
        createdButton.setForeground(fg);
        
        if (ac != null){ createdButton.setActionCommand(ac); }
        
        createdButton.setFont(createdButton.getFont().deriveFont(20.0f));
        createdButton.addActionListener(handler);
        
        return createdButton;
    }
    
    private class Controller implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent e) { 
            String ac = e.getActionCommand();
            display.setText(ac);
        }
    }
}
