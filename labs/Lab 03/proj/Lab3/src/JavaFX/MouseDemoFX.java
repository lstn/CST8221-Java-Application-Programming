/* CST8221-JAP: LAB 03, JavaFX Demo
   File name: MouseDemoFX.java
*/

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 Demonstrates how to handle mouse events in JavaFX.
 @version 1.16.2
 @author Svillen Ranev
 @since JavaFX 2.0
 @see javafx.scene.input.MouseButton
 @see javafx.scene.input.MouseEvent
 */
public class MouseDemoFX extends Application {

    @Override
    public void start(Stage primaryStage) {

        /**
         * Setup Button.
         */
        Button button = new Button();
        button.setText("_Enter mouse then click");
        button.setTooltip(new Tooltip("Please play with the mouse"));
        /**
         * Setup Scene.
         */
        StackPane root = new StackPane();
        root.getChildren().add(button);
        Scene scene = new Scene(root, 350, 250);
        /*set mouse entered event handler*/
        button.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                System.out.println("Mouse entered");
                System.out.println("Mouse location: X = " + me.getX()+ " Y = "+me.getY());
            }
        });
        /*set mouse clicked event handler*/
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {              
                System.out.println("Mouse clicked"); 
                System.out.println("Mouse location: X = " + me.getX()+ " Y = "+me.getY());
                MouseButton mb = me.getButton();
                /*find which mouse button has been clicked*/
                /* using if statement
                if(mb == MouseButton.PRIMARY){
                 System.out.println("Mouse left button clicked");
                }
                */
                /*using switch statement with enum constants*/
                switch(mb){
                    case PRIMARY:
                     System.out.println("Mouse left button clicked");
                     System.out.println("Click count: "+ me.getClickCount());
                     break;
                    case MIDDLE:
                     System.out.println("Mouse middle(wheel) button clicked");
                     break;
                    case SECONDARY:
                     System.out.println("Mouse right button clicked");
                     System.out.println("Click count: "+ me.getClickCount());
                     break;
                    case NONE:
                     System.out.println("Mouse right button clicked");
                     System.out.println("Click count: "+ me.getClickCount());
                    break;   
                }
                
            }    
            
        });

        /*Set stage title, add sceme, and show the GUI */
        primaryStage.setTitle("Mouse Example FX");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}

