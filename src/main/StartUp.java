package main;

import domein.DomeinController;
import gui.PixelSchermController;
import gui.Uitvoer;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class StartUp extends Application
{
    
    @Override
    public void start(Stage primaryStage)
    {
        //DomeinController dc = new DomeinController();
        //new Uitvoer(dc);
        PixelSchermController ps = new PixelSchermController();
        Scene scene = new Scene(ps, 840, 400);
        
        primaryStage.setTitle("Project Pixel");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }
    
}
