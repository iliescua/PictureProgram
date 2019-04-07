/*
* SE1021
* Spring 2018
* Lab 8 - KernelController Class
* Created: 5/11/2018
* Updated: 5/15/18
*/
package iliescua;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.logging.Logger;

/**
 * This is the controller class for the second GUI
 * It will controller the blur, sharpen, and edge effects
 */
public class KernelController {
    private Stage primaryStage;
    private Stage secondStage;
    private Logger logger;
    private Controller controller;
    private Image image;

    @FXML
    private TextField txtTopLeft;
    @FXML
    private TextField txtTopMiddle;
    @FXML
    private TextField txtTopRight;
    @FXML
    private TextField txtMidLeft;
    @FXML
    private TextField txtMidMiddle;
    @FXML
    private TextField txtMidRight;
    @FXML
    private TextField txtBotLeft;
    @FXML
    private TextField txtBotMiddle;
    @FXML
    private TextField txtBotRight;
    @FXML
    private TextField txtDivide;

    /**
     * This method allows for the creation of a second GUI
     * @param secondStage  Second layer to the UI
     * @param primaryStage primary GUI
     * @param logger       keeps track of important events
     */
    public void setStages(Stage secondStage, Stage primaryStage, Logger logger) {
        this.secondStage = secondStage;
        this.primaryStage = primaryStage;
        this.logger = logger;
    }

    /**
     * Used to allow us to set the second GUI's controller
     * to active
     * @param controller passing in the first GUI's
     *                   controller class
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    @FXML
    private void makeBlur() {
        txtTopLeft.setText("0");
        txtTopMiddle.setText("1");
        txtTopRight.setText("0");
        txtMidLeft.setText("1");
        txtMidMiddle.setText("5");
        txtMidRight.setText("1");
        txtBotLeft.setText("0");
        txtBotMiddle.setText("1");
        txtBotRight.setText("0");
    }

    @FXML
    private void makeSharpen() {
        txtTopLeft.setText("0");
        txtTopMiddle.setText("-1");
        txtTopRight.setText("0");
        txtMidLeft.setText("-1");
        txtMidMiddle.setText("5");
        txtMidRight.setText("-1");
        txtBotLeft.setText("0");
        txtBotMiddle.setText("-1");
        txtBotRight.setText("0");
    }

    @FXML
    private void makeEdge() {
        txtTopLeft.setText("0");
        txtTopMiddle.setText("-1");
        txtTopRight.setText("0");
        txtMidLeft.setText("-1");
        txtMidMiddle.setText("4");
        txtMidRight.setText("-1");
        txtBotLeft.setText("0");
        txtBotMiddle.setText("-1");
        txtBotRight.setText("0");
    }

    @FXML
    private void applyChanges() {
        image = controller.getImageHolder().getImage();
        float boxTopLeft = Integer.parseInt(txtTopLeft.getText());
        float boxTopMiddle = Integer.parseInt(txtTopMiddle.getText());
        float boxTopRight = Integer.parseInt(txtTopRight.getText());
        float boxMidLeft = Integer.parseInt(txtMidLeft.getText());
        float boxMidMiddle = Integer.parseInt(txtMidMiddle.getText());
        float boxMidRight = Integer.parseInt(txtMidRight.getText());
        float boxBotLeft = Integer.parseInt(txtBotLeft.getText());
        float boxBotMiddle = Integer.parseInt(txtBotMiddle.getText());
        float boxBotRight = Integer.parseInt(txtBotRight.getText());
        float divisorBox = Integer.parseInt(txtDivide.getText());
        int kernWidth = 3;
        int kernHeight = 3;

        double sum = boxTopLeft + boxTopMiddle + boxTopRight + boxMidLeft +
                boxMidMiddle + boxMidRight + boxBotLeft +
                boxBotMiddle + boxBotRight;

        if (sum > 0 && divisorBox > 0) {
            float[] divide = {boxTopLeft / divisorBox, boxTopMiddle / divisorBox,
                    boxTopRight / divisorBox, boxMidLeft / divisorBox, boxMidMiddle / divisorBox,
                    boxMidRight / divisorBox, boxBotLeft / divisorBox,
                    boxBotMiddle / divisorBox, boxBotRight / divisorBox};

            BufferedImageOp op = new ConvolveOp(new Kernel(kernWidth, kernHeight, divide));
            BufferedImage result = op.filter(SwingFXUtils.fromFXImage(image, null), null);
            controller.getImageHolder().setImage(SwingFXUtils.toFXImage(result, null));
        } else {
            Alert error = new Alert(Alert.AlertType.INFORMATION,
                    "Image was not filtered, since the sum was less than 0.");
            error.showAndWait();
            logger.severe("Filter was not applied to image: Sum was less than 0.");
        }
    }
}