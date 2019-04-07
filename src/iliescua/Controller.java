/*
* SE1021
* Spring 2018
* Lab 8 - Controller Class
* Created: 5/5/2018
* Updated: 5/15/2018
*/
package iliescua;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * This class is the brain that provides the logic to
 * run Image Manipulator GUI
 */
public class Controller implements Initializable {

    @FXML
    private Button btnShowFilter;
    @FXML
    private ImageView imageHolder;
    @FXML
    private TextField txtXCord;
    @FXML
    private TextField txtYCord;

    private static FileChooser fileChooser = new FileChooser();
    private FileChooser.ExtensionFilter png = new FileChooser.ExtensionFilter("PNG", "*.png");
    private FileChooser.ExtensionFilter gif = new FileChooser.ExtensionFilter("GIF", "*.gif");
    private FileChooser.ExtensionFilter jpg = new FileChooser.ExtensionFilter("JPG", "*.jpg");
    private FileChooser.ExtensionFilter msoe = new FileChooser.ExtensionFilter("MSOE", "*.msoe");
    private FileChooser.ExtensionFilter bmsoe = new FileChooser.ExtensionFilter("BMSOE", "*.bmsoe");
    private ImageIO imageIO = new ImageIO();
    private Image image;
    private Logger logger;
    private Stage secondStage;

    /**
     * This method is used to get the x and y coordinates of the image
     * and display them
     * @param location used when overriding initialize
     * @param resources used when overriding initialize
     */
    @Override
    public void initialize(URL location, ResourceBundle resources){
        imageHolder.setOnMouseDragged(mouseEvent -> {
            txtXCord.setText(Double.toString(mouseEvent.getX()));
            txtYCord.setText(Double.toString(mouseEvent.getY()));
        });
    }

    @FXML
    private void openFile() {
        try {
            fileChooser.setTitle("Open File");
            fileChooser.getExtensionFilters().addAll(jpg, png, gif, msoe, bmsoe);
            File selectedFile = fileChooser.showOpenDialog(null);
            image = imageIO.read(selectedFile);
            imageHolder.setImage(image);
        } catch (FileNotFoundException e) {
            showAlert("No File", "File couldn't be found");
        } catch (IOException e) {
            showAlert("IOException", "Relaunch the app");
        }
    }

    @FXML
    private void saveFile() {
        fileChooser.setTitle("Save File");
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(jpg, png, gif, msoe, bmsoe);
        File file = fileChooser.showSaveDialog(null);
        imageIO.write(imageHolder.getImage(), file, logger);
    }

    @FXML
    private void reloadFile() {
        imageHolder.setImage(image);
    }

    @FXML
    private void makeGrayscale() {
        try {
            imageHolder.setImage(transformImage(image, toGray));
        } catch (NullPointerException e) {
            showAlert("No File", "Open file first and then try again");
        }
    }

    @FXML
    private void makeRed() {
        try {
            imageHolder.setImage(transformImage(image, toRed));
        } catch (NullPointerException e) {
            showAlert("No File", "Open file first and then try again");
        }
    }

    @FXML
    private void makeRedGray() {
        try {
            imageHolder.setImage(transformImage(image, toRedGray));
        } catch (NullPointerException e) {
            showAlert("No File", "Open file first and then try again");
        }
    }

    @FXML
    private void makeNegative() {
        try {
            imageHolder.setImage(transformImage(image, toNegative));
        } catch (NullPointerException e) {
            showAlert("No File", "Open file first and then try again");
        }
    }

    @FXML
    private void openShowFilter() {
        if(btnShowFilter.getText().equals("Show Filter")) {
            btnShowFilter.setText("Hide Filter");
            secondStage.show();
        } else {
            btnShowFilter.setText("Show Filter");
            secondStage.hide();
        }
    }

    private Transformable toGray = (x, y, color) -> color.grayscale();

    private Transformable toNegative = (x, y, color) -> {
        double red = 1 - color.getRed();
        double green = 1 - color.getGreen();
        double blue = 1 - color.getBlue();
        return new Color(red, green, blue, color.getOpacity());
    };

    private Transformable toRed = (x, y, color) -> {
        double green = 0;
        double blue = 0;
        return new Color(color.getRed(), green, blue, color.getOpacity());
    };

    private Transformable toRedGray = (x, y, color) -> {
        if (y == 0 || y % 2 == 0) {
            return color.grayscale();
        } else {
            return new Color(color.getRed(), 0, 0, color.getOpacity());
        }
    };

    private Image transformImage(Image image, Transformable transform) {
        WritableImage newImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
        PixelReader reader = image.getPixelReader();
        PixelWriter writer = newImage.getPixelWriter();
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color color = reader.getColor(i, j);
                Color transformedColor = transform.transform(i, j, color);
                writer.setColor(i, j, transformedColor);
            }
        }
        return newImage;
    }

    private void showAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * This method allows for the creation of a second GUI
     * @param secondStage Second layer to the UI
     * @param primaryStage primary GUI
     * @param logger keeps track of important events
     */
    public void setStages(Stage secondStage, Stage primaryStage, Logger logger) {
        this.secondStage = secondStage;
        primaryStage = new Stage();
        this.logger = logger;
    }

    /**
     * Used to get the private variable imageHolder
     * @return imageHolder
     */
    public ImageView getImageHolder(){
        return imageHolder;
    }
}