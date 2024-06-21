package com.util;

import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

public class SwingUtil {

    public ImageIcon createImageIcon(String path) {
        URL imgURL = getClass().getResource(path);
        if (imgURL != null) {

            ImageIcon icon = new ImageIcon(imgURL);

            Image image = icon.getImage();
            Image scaledImage = image.getScaledInstance(25, 25, Image.SCALE_SMOOTH); // Adjust size as needed
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            return scaledIcon;
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

}
