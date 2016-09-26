/* CST8221-JAP: LAB 03  JavaFX Demo
   File name: CursorDemoFX.java
*/

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 This class provides a demonstration on 
 how to change the cursor in JavaFX.
 @version 1.16.2
 @since JavaFX 2.0
 @author Svillen Ranev
*/
public class CursorDemoFX extends Application {
    private boolean clicked = true; 
    @Override
    public void start(Stage primaryStage) {
        
        //set one button
        Button button = new Button();
        button.setText("Click me");

        //create a scene
        StackPane root = new StackPane();
        root.getChildren().add(button);
        Scene scene = new Scene(root, 300, 250);
        //set an event handler for the mouse entered event
        button.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mse) {
                //Change the cursor to a hand
                scene.setCursor(Cursor.HAND);  
            }
        });
        //set an event handler for the  mouse exited event
        button.setOnMouseExited(new EventHandler<MouseEvent>() {
            //Change the cursor to a cross hair
            @Override
            public void handle(MouseEvent mse) {
                scene.setCursor(Cursor.CROSSHAIR);  
            }
        });

        /**
         * Add Listeners.
         */
        //
        //set an event handler for the button clicked eventt
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              if(clicked){
               button.setText("Busy like a bee");
               scene.setCursor(Cursor.WAIT);
              }else{
               button.setText("Click me");
              }
              clicked=!clicked;
            }
        });
        //create a custom cursor
        Image image = new Image("happy.gif");
        scene.setCursor(new ImageCursor(image));

        //set and show the stage
        primaryStage.setTitle("Cursor Demo FX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Launches the application
     * @param args not used
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
}

