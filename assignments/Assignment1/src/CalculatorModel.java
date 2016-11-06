
import java.awt.Color;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * Filename: CalculatorModel.java
 * Author: Lucas Estienne, 040 819 959
 * Course: CST8221 - JAP, Lab Section: 302
 * Assignment: 1
 * Date: 11/02/2016
 * Professor: Svillen Ranev
 * Purpose: Responsible for performing the calculations of the Calculator application
 */

public class CalculatorModel {
    private static final List<String> OPERATORS = Arrays.asList("+", "-", "*", "/");
    
    private int calcMode; // 0 is int, 1 is float
    private int floatingPresision; // 1 = .0, 2=.00, 3=sci
    private boolean errorState = false;
    private float result = 0f;
    
    private LinkedList<String> operands = new LinkedList<>();
    
    
    public CalculatorModel(){
        calcMode = 1;
        floatingPresision = 2;
    }

    public void setCalcMode(int calcMode) {
        this.calcMode = calcMode;
    }
    
    public void toggleCalcMode(JLabel errorDisplay, JButton dotButton) {
        calcMode = (calcMode == 1) ? 0 : 1;
        errorDisplay.setText((calcMode == 1) ? "F" : "I");
        errorDisplay.setBackground(Color.YELLOW);
        dotButton.setEnabled((calcMode == 1));
    }

    public void setFloatingPresision(int floatingPresision) {
        this.floatingPresision = floatingPresision;
    }
    
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
        System.out.println(operands.toString());
    }
    
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
    
    public void invertResult(){
        result = -result;
    }
    
    public void clearOperands(JLabel errorDisplay){
        operands.clear();
        result = 0f;
        
        errorDisplay.setText((calcMode == 1) ? "F" : "I");
        errorDisplay.setBackground(Color.YELLOW);
    }
    
    public String peekLastOperand(){
        return operands.peekLast();
    }

    public boolean getErrorState() {
        return errorState;
    }

    public void setErrorState(boolean errorState) {
        this.errorState = errorState;
    }

    public String getResult(JLabel errorDisplay) {
        String formattedRes = "NaN";
        if(calcMode == 1){
            switch(floatingPresision){
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
    
    public void performCalculations(){
        String op;
        String next;
        System.out.println(operands.toString());

        while(!operands.isEmpty()){
            op = operands.pollFirst();
            next = operands.pollFirst();
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
