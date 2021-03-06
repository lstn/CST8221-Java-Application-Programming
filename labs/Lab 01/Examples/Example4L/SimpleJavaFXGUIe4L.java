/* CST8221-JAP: LAB 01, Example 4L
   File name: SimpleJavaFXGUIe4L.java
   Modification of Example 4
   You need Java 1.8.XX to compile and run this example.
   If you want to use an IDE to compile and run this example,
   you need to install latest Eclipse IDE or NetBeans 8 IDE.
 */ 
 
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 Simple JavaFX GUI Example.
 This class is a modification of example 4.
 It uses the newly (Java 8) introduced lambda expressions.
 Lambda expression syntax is used instead of anonymous inner class syntax.
 The main class for a JavaFX application extends the javafx.application.Application class.
 The start() method is the main entry point for all JavaFX applications.
 A JavaFX application defines the user interface container by means of a stage and a scene.
 The JavaFX Stage class is the top-level JavaFX container.
 The JavaFX Scene class is the container for all content.
 The program sets the stage and creates a scene with two UI controls (Button and Label).
 At the end it makes the stage visible to the user.
 For more detailed information visit:
  http://docs.oracle.com/javase/8/javase-clienttechnologies.htm
 @version 1.16.2
 @author Svillen Ranev
*/
public class SimpleJavaFXGUIe4L extends Application {

    private static final Color COLOR = Color.web("#464646");
    private static final String LABEL_TEXT = "Don't do that: ";
    private Button button;
    private DropShadow shadow;
    private Label label;
    private int mouseClickCounter;
    
   /**
    The main() method is not required for JavaFX applications 
    when the JAR file for the application is created with the JavaFX Packager tool
    which embeds the JavaFX Launcher in the JAR file.
    The main method is not needed if you launch the application from the command line.
    When you run a JavaFX application without a main() method, the Java Virtual Machine (JVM)
    will use automatically the static launch() method to run the application.
    However, it is useful to include the main() method 
    so you can run the applications that were created without the JavaFX Launcher,
    such as when using an IDE in which the JavaFX tools are not fully integrated. 
    Also, Swing applications that embed JavaFX code require the main() method.
    @param args command line arguments
    */
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        GridPane pane = new GridPane();
        Scene scene = new Scene(pane);
        stage.setTitle("Simple JavaFX GUI - E4L");
        stage.setWidth(400);
        stage.setHeight(170);
        
        label = new Label();
        label.setFont(Font.font("Times New Roman", 26));
        label.setTextFill(COLOR);

 
        Image imageAccept = new Image(getClass().getResourceAsStream("ok.png"));

        button = new Button("Press me", new ImageView(imageAccept));
        button.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");
       
        //Handle the action event generated by the button
        //Display the mouse clicks count
        button.setOnAction((ActionEvent e) -> {
            mouseClickCounter++;
            label.setText(LABEL_TEXT+ mouseClickCounter+"!");
            button.setEffect(null);
        });
        
        /*
        //Comment lines 86-94 before trying the code below.
        //If you do not comment the lines both event handlers will be called.
        //Handle directly the mouse event generated by the button
        //Display the mouse clicks count
        button.setOnMouseClicked((MouseEvent e) -> {
            // button.setOnMouseClicked(null);
            mouseClickCounter++;
            label.setText(LABEL_TEXT+ mouseClickCounter+"!");
            button.setEffect(null);
        });
        */   
        shadow = new DropShadow();
        //Handle the mouse event generated by the button
        button.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            button.setEffect(shadow);
        });
       /* Set the pane border(insets), cell content alignment, and vertical cell gaps*/
        pane.setPadding(new Insets(10.0, 10.0, 10.0, 10));//top,right,bittom,left
        pane.setAlignment(Pos.CENTER);
        pane.setVgap(30.0);
        
        /*
        In JavaFX, the content of the scene is represented as a hierarchical scene graph of nodes.
        In this example, the root node is a GridPane object.
        The root node contains two children nodes a Button control with text and image and a Label.
        The Button has an event handler to display a message using the label when the button is pressed.
        */
        pane.add(button,0,0);
        pane.add(label,0,1); 
   
        //set the stage with the current scene 
        stage.setScene(scene);
        //make the GUI visible
        stage.show();
    }
}

