/** This class demonstrates the basics of Java 8 Lambda Expressions.
A Lambda Expression is, essentially, an anonymous (unnamed) method. 
Since it is anonymous it cannot be executed on its own. Instead, the lambda expression
is used to implement a method defined by a target functional interface.
Thus, a lambda expression results in a form of anonymous class. 
Lambda expressions are also referred as closures.

A Functional Interface is an interface that contains one and only one abstract method
(since Java 8 an interface can also have default methods). 
Thus, a functional interface typically represents a single action.
A functional interface can be used as a target type of a lambda expression. 
A lambda expression can be used only in a context in which a target type is specified. 
A Functional Interfaces is also known as a SAM type, 
where SAM stands for Single Abstract Method.

 *@author S.Ranev
 *@version 1.16.2
 *@since Java 8
*/
public class LambdaDemo {
  private MyListener [] al = new MyListener[1];
  public static void main(String[] args) {
  // Java Lambda Expression syntax
  // (parameter list) -> expression;
  // (parameter list) -> {body with return from all branches};
  
  // the body of the value() method will be implemented as 
  //return 5;
  MyValue mvalue = ()-> 5;
  
  System.out.println("Result: "+ mvalue.value());
  
  // the body of the calc()method will be implemented as 
  // return x*2;
  MyCalc mcalc = (x)-> x*2;
  System.out.println("Result: "+ mcalc.calc(5.0));
  
  MyCalcL mcalcL = (v)-> (v.value())*2;
  System.out.println("Result: "+ mcalcL.calcL(()->10));
  //The lines below show how listeners work with lambda expressions
  LambdaDemo ld = new LambdaDemo();
  // the body of the myActionPerformed()method will be implemented as 
  // System.out.println("I am a handler"));
  ld.addMyListener(()->System.out.println("I am a handler"));
  ld.handleEvent(0); 
}
  
  //lambda targer type functional inteface
  public interface MyCalc {
    //method with a parameter and return value
    //public abstract are redundant in Java 8
    public abstract double calc(double x);	
  }
  //lambda targer type functional inteface
  public interface MyValue {
    //method with a return value
    //public abstract are redundant in Java 8
   /* public abstract*/ int value ();	
  }
 //lambda targer type functional inteface
 public interface MyCalcL {
    //method with a functional interface as a parameter
    //public abstract are redundant in Java 8
    /* public abstract*/ double calcL(MyValue v);	
  }
 //functional interface for a listener
 public interface MyListener{
    /* public abstract*/ void myActionPerformed();
 } 
 //method for adding a listener
 public void addMyListener(MyListener ml){
    al[0] = ml; 
 }
 //method simulating an event handler
 public void handleEvent(int i){
   al[i].myActionPerformed();
 }
}
