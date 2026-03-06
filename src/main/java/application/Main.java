package application;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The {@code Main} class serves as the official entry point for the Cardflow application.
 * <p>
 * It extends the JavaFX {@link Application} class, initiating the framework's 
 * lifecycle and providing the primary window (Stage) to the game's internal 
 * initialization logic.
 */
public class Main extends Application {

    /**
     * The main entry point for all JavaFX applications.
     * <p>
     * This method is called after the system is ready for the application to 
     * begin running. It delegates all heavy lifting and setup to the 
     * {@link Game#init(Stage)} method to keep the main class concise and focused.
     * * @param primaryStage The primary stage for this application, onto which 
     * the application scene will be set.
     * @throws Exception If an error occurs during initialization.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Hands off the Stage to the Game orchestrator for bootstrapping
        Game.init(primaryStage); 
    }
    
    /**
     * The standard Java main method.
     * <p>
     * It serves as the fallback launcher that invokes the {@link #launch(String...)} 
     * method to start the JavaFX runtime.
     * * @param args The command line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }

}