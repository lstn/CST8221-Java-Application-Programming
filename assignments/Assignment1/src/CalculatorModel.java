/**
 * Filename: CalculatorModel.java
 * Author: Lucas Estienne, 040 819 959
 * Course: CST8221 - JAP, Lab Section: 302
 * Assignment: 1
 * Date: 11/02/2016
 * Professor: Svillen Ranev
 * Purpose: Responsible for performing the calculations of the Calculator application
 */


import java.awt.Color;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * This class implements the CalculatorModel to be used with the event controller in the Calculator application.
 * 
 * @author Lucas Estienne
 * @version 1.0
 * @since 1.8.0_112
*/
public class CalculatorModel {
    /**
     * List of the operator operands. Value: {@value}
     */
    private static final List<String> OPERATORS = Arrays.asList("+", "-", "*", "/");
    
    /**
     * the calculator mode, 0 for int, 1 for float
     */
    private int calcMode; // 0 is int, 1 is float
    /**
     * the floating point precision, 1 for single digit, 2 for two digits, 3 for scientific
     */
    private int floatingPrecision; // 1 = .0, 2=.00, 3=sci
    /**
     * the calculator error state
     */
    private boolean errorState = false;
    /**
     * the result
     */
    private float result = 0f;
    
    /**
     * the list of operands to be processed when doing the calculation
     */
    private LinkedList<String> operands = new LinkedList<>();
    
    /**
     * Default constructor for the CalculatorModel class.
    */
    public CalculatorModel(){
        calcMode = 1;
        floatingPrecision = 2;
    }
    
    /**
     * This method sets the calculator mode to Float or Int.
     * 
     * @param calcMode 1 for Float mode, 0 for Int mode
    */
    public void setCalcMode(int calcMode) {
        this.calcMode = calcMode;
    }
    
    /**
     * This method toggles the calculator mode between Int and Float.
     * 
     * @param errorDisplay the calculator error display label
     * @param dotButton the calculator dot button on the keypad
    */
    public void toggleCalcMode(JLabel errorDisplay, JButton dotButton) {
        calcMode = (calcMode == 1) ? 0 : 1;
        if(!errorDisplay.getText().equals("E")){
            errorDisplay.setText((calcMode == 1) ? "F" : "I");
            errorDisplay.setBackground(Color.YELLOW);
        }
        dotButton.setEnabled((calcMode == 1));
    }

    /**
     * This method sets the floating point precision for the calculator.
     * 
     * @param floatingPrecision 1 for single digit, 2 for two digit, 3 for scientific precision
    */
    public void setFloatingPrecision(int floatingPrecision) {
        this.floatingPrecision = floatingPrecision;
    }
    
    /**
     * This method adds an operand to the operand list to be processed in the calculation.
     * Appends to the end of the last operand if a number.
     * 
     * @param toAdd operand to add.
    */
    public void addOperand(String toAdd){
        if (operands.size() > 0){
            if(OPERATORS.contains(toAdd)){
                if(OPERATORS.contains(operands.peekLast())){
                    operands.pollLast();
                }
                operands.add(toAdd);
            } else {
                if(OPERATORS.contains(operands.peekLast())){
                    operands.add(toAdd);
                } else {
                    operands.add((operands.isEmpty()) ? toAdd : operands.pollLast().concat(toAdd));
                }
            }
        } else{
            operands.add(toAdd);
        }
    }
    
    /**
     * This method handles the backspace button of the calculator.
    */
    public void backspaceOperand(){
        if(!operands.isEmpty()){
            String op = operands.pollLast();
            if (op != null && op.length() > 0) {
                op = op.substring(0, op.length()-1);
                if (op.length() == 0) op = "0";
            }
            operands.add(op);
        }
    }
    
    /**
     * This inverts the current result, to be used with the PLUSMINUS button.
    */
    public void invertResult(){
        result = -result;
    }
    
    /**
     * This method clears the operand list and resets the result to 0,
     * also sets the error display text and background to their proper values.
     * 
     * @param errorDisplay the calculator error display label
    */
    public void clearOperands(JLabel errorDisplay){
        operands.clear();
        result = 0f;
        
        errorDisplay.setText((calcMode == 1) ? "F" : "I");
        errorDisplay.setBackground(Color.YELLOW);
    }
    
    /**
     * Returns the last operand in the operands list without removing it.
     * 
     * @return String the last operand in the operands list
    */
    public String peekLastOperand(){
        return operands.peekLast();
    }

    /**
     * Gets the calculator error state.
     * 
     * @return boolean calculator error state
    */
    public boolean getErrorState() {
        return errorState;
    }

    /**
     * sets the calculator error state.
     * 
     * @param errorState calculator error state
    */
    public void setErrorState(boolean errorState) {
        this.errorState = errorState;
    }

    /**
     * Gets the current result, formatted based on the current calculator settings.
     * 
     * @param errorDisplay the calculator error display
     * @return String result
    */
    public String getResult(JLabel errorDisplay) {
        String formattedRes = "NaN";
        if(calcMode == 1){
            switch(floatingPrecision){
                case 1:
                    formattedRes = String.format("%.1f", result);
                    break;
                case 2:
                    formattedRes = String.format("%.2f", result);
                    break;
                case 3:
                    formattedRes = String.format("%.5e", result);
                    break;
            }
        } else {
            formattedRes = String.valueOf((int) result);
        }
        if(formattedRes.equals("Infinity") || formattedRes.equals("NaN")){
            formattedRes = "--";
            errorDisplay.setText("E");
            errorDisplay.setBackground(Color.RED);
        }
        return formattedRes;
    }
    
    /**
     * Performs the calculations as currently set in the operands list and sets the result accordingly.
    */
    public void performCalculations(){
        String op;
        String next;

        while(!operands.isEmpty()){
            op = operands.pollFirst();
            next = operands.pollFirst();
            
            if(op != null && (op.length() - op.replace(".", "").length()) < 2 && next != null &&
                    (next.length() - next.replace(".", "").length()) < 2){
                if(OPERATORS.contains(op)){
                    if (next == null){
                        next = String.valueOf(result);
                    }
                    switch(op){
                        case "+":
                            result += Float.valueOf(next);
                            break;
                        case "-":
                            result -= Float.valueOf(next);
                            break;
                        case "*":
                            result *= Float.valueOf(next);
                            break;
                        case "/":
                            result /= Float.valueOf(next);
                            break;
                    }
                } else {
                    result = Float.valueOf(op);
                    if (next != null)
                        operands.addFirst(next);
                }
            }
            
        }
    }
    
}
