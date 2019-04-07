/*
* SE1021
* Spring 2018
* Lab 8 - ImageIO Class
* Created: 5/5/2018
* Updated 5/11/18
*/
package iliescua;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Class deals with reading and writing the different types of files
 */
public class ImageIO {

    private static final int COLOR_MODIFIER = 255;
    private Logger logger;
    private static final int VALUE5 = 5;

    /**
     * Method is used to read the file and determine its file type
     * @param file the file being read or written
     * @return returns the image that will be edited
     * @throws IOException To be caught in a later method
     */
    public Image read(File file) throws IOException {
        final String[] extensions = new String[] { "gif", "jpg", "png", "GIF", "JPG", "PNG"};
        Image image;
        for (String a : extensions) {
            if (file.toString().endsWith(a)) {
                BufferedImage bufferedImage = javax.imageio.ImageIO.read(file);
                image = SwingFXUtils.toFXImage(bufferedImage, null);
                return image;
            } else if (file.toString().endsWith(".msoe") || file.toString().endsWith(".MSOE")) {
                return readMSOE(file, logger);
            } else if (file.toString().endsWith(".bmsoe") || file.toString().endsWith(".BMSOE")) {
                return readBMSOE(file, logger);
            }
        }
        return null;
    }

    /**
     * Method is used to write the files
     * @param image  Takes the image that will be editted
     * @param file   The file that holds the picture
     * @param logger keeps a log of important actions
     */
    public void write(Image image, File file, Logger logger) {
        try {
            if (getFileExtension(file.getName()).equals("msoe") ||
                    getFileExtension(file.getName()).equals("MSOE")) {
                writeMSOE(image, file, logger);
            } else if (getFileExtension(file.getName()).equals("bmsoe")
                    || getFileExtension(file.getName()).equals("BMSOE")) {
                writeBMSOE(image, file, logger);
            } else {
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
                javax.imageio.ImageIO.write(bufferedImage, getFileExtension(file.getName()), file);
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "The file can not be saved");
            alert.showAndWait();
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a valid location");
            alert.showAndWait();
        }
    }

    private String getFileExtension(String a) {
        return a.substring(a.lastIndexOf(".") + 1);
    }

    private Image readMSOE(File file, Logger logger) {
        try {
            Scanner fileIn = new Scanner(file);
            if (fileIn.next().equals("MSOE")) {
                int width = fileIn.nextInt(); //width
                int height = fileIn.nextInt(); //height

                WritableImage writableImage = new WritableImage(width, height);
                PixelWriter writer = writableImage.getPixelWriter();

                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        Color color = Color.web((fileIn.next()));
                        int r = (int) (color.getRed() * COLOR_MODIFIER);
                        int g = (int) (color.getGreen() * COLOR_MODIFIER);
                        int b = (int) (color.getBlue() * COLOR_MODIFIER);
                        int a = (int) (color.getOpacity() * COLOR_MODIFIER);
                        int rgba = new java.awt.Color(r, g, b, a).getRGB();
                        writer.setArgb(x, y, rgba);
                    }
                }
                return writableImage;
            }
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "File wasn't found");
            alert.showAndWait();
            logger.severe("Could not read .MSOE file");
        }
        return null;
    }

    private void writeMSOE(Image image, File file, Logger logger) {
        try {
            FileWriter output = new FileWriter(file);
            PixelReader reader = image.getPixelReader();

            int imageWidth = (int) image.getWidth();
            int imageHeight = (int) image.getHeight();
            String width = imageWidth + " ";
            String height = imageHeight + " ";

            output.write("MSOE");
            output.write(width);
            output.write(height);

            for (int i = 0; i < image.getHeight(); i++) {
                for (int j = 0; j < image.getWidth(); i++) {
                    Color color = reader.getColor(j, i);
                    output.write(color.toString() + " ");
                }
            }
            output.close();

        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "The file could not be saved.");
            alert.showAndWait();
            logger.severe("The file could not be saved");
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "");
            alert.showAndWait();
            logger.severe("Java IOException found");
        }
    }

    private Image readBMSOE(File file, Logger logger) {
        try {
            DataInputStream inputStream = new DataInputStream(new FileInputStream(file));
            inputStream.skipBytes(VALUE5);
            int width = inputStream.readInt();
            int height = inputStream.readInt();

            WritableImage writeImage = new WritableImage(width, height);
            PixelWriter pixWriter = writeImage.getPixelWriter();

            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    double r = (double) inputStream.readUnsignedByte() / COLOR_MODIFIER;
                    double g = (double) inputStream.readUnsignedByte() / COLOR_MODIFIER;
                    double b = (double) inputStream.readUnsignedByte() / COLOR_MODIFIER;
                    double a = (double) inputStream.readUnsignedByte() / COLOR_MODIFIER;
                    Color color = new Color(r, g, b, a);
                    pixWriter.setColor(y, x, color);
                }
            }
            inputStream.close();
            return writeImage;

        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "The file could not be found");
            alert.showAndWait();
            logger.severe("The file could not be found");
        } catch (IOException e) {
            logger.severe("Java IOException caught");
        }
        return null;
    }

    private void writeBMSOE(Image image, File file, Logger logger) {
        if (file != null) {
            try (DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(file))) {
                PixelReader pixReader = image.getPixelReader();

                outputStream.writeBytes("BMSOE");
                outputStream.writeInt((int) image.getWidth());
                outputStream.writeInt((int) image.getHeight());

                for (int i = 0; i < image.getHeight(); i++) {
                    for (int j = 0; j < image.getWidth(); j++) {
                        outputStream.writeByte((int) pixReader.getColor(j, i).getRed()
                                * COLOR_MODIFIER);
                        outputStream.writeByte((int) pixReader.getColor(j, i).getGreen()
                                * COLOR_MODIFIER);
                        outputStream.writeByte((int) pixReader.getColor(j, i).getBlue()
                                * COLOR_MODIFIER);
                        outputStream.writeByte((int) pixReader.getColor(j, i).getOpacity()
                                * COLOR_MODIFIER);
                    }
                }

            } catch (FileNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "The file could not be found");
                alert.showAndWait();
                logger.severe("The file could not be found");
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "There was a problem loading the file");
                alert.showAndWait();
                logger.severe("There was a problem loading the file");
            }
        }
    }
}