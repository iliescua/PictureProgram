/*
* SE1021
* Spring 2018
* Lab 8 - Transformable Interface
* Created: 5/11/2018
*/
package iliescua;

import javafx.scene.paint.Color;

/**
 * This interface is used to go through the and alter the
 * image pixel by pixel
 */
public interface Transformable {
    Color transform(int x, int y, Color color);
}