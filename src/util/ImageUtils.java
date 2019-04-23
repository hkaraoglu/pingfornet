package util;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author hasankaraoglu
 */
public class ImageUtils
{
    public static ImageIcon createImageIcon(Class<?> className, String path, String description, int width, int height)
    {
        java.net.URL imgURL = className.getResource(path);
        if (imgURL != null)
        {
           Image image = new ImageIcon(imgURL, description).getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
            return new ImageIcon(image);
        } else
        {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
