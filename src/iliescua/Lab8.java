/*
* SE1021
* Spring 2018
* Lab 8 - Lab8 (Driver) Class
* Created: 5/5/2018
*/
package iliescua;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * This class is the main class which runs the program
 * and makes the GUI pop up
 */
public class Lab8 extends Application {

    private static final double SECOND_WIDTH = 420;
    private static final double SECOND_HEIGHT = 200;
    private static final double X_OFFSET = 650;
    private static final double Y_OFFSET = 370;
    /**
     * This method has the GUI appear and it gets loaded
     * automatically by Intellij, simply the strings for the
     * file name and the GUI name need to be altered
     * @param primaryStage to create the initial stage
     * @throws Exception throws the exception to be handled later
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Logger logger = Logger.getLogger("ImageIO");
        logger.setUseParentHandlers(false);
        try{
            FileHandler handler = new FileHandler(System.getProperty("user.dir")
                    + File.separator + "Log.log", true);
            logger.addHandler(handler);
        } catch (IOException e) {
            logger.severe("Could not create log file");
        }

        primaryStage.setTitle("Image Manipulator");
        FXMLLoader loader1 = new FXMLLoader();
        FXMLLoader loader2 = new FXMLLoader();
        Stage secondStage = new Stage();
        Parent root1 = loader1.load(getClass().getResource("Lab8.fxml").openStream());
        Parent root2 = loader2.load(getClass().getResource("KernelController.fxml").openStream());
        primaryStage.setScene(new Scene(root1));
        secondStage.setScene(new Scene(root2, SECOND_WIDTH, SECOND_HEIGHT));
        Controller controller = loader1.getController();
        KernelController kController = loader2.getController();
        controller.setStages(secondStage, primaryStage, logger);
        kController.setStages(secondStage, primaryStage, logger);
        kController.setController(controller);
        secondStage.setTitle("Filter Kernel");
        secondStage.setX(X_OFFSET);
        secondStage.setY(Y_OFFSET);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}