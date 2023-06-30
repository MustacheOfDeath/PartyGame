import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 * The type Utility.
 */
public class Utility {

    /**
     * Replace spaces with underscores string.
     *
     * @param input the input
     * @return the string
     */
    public static String replaceSpacesWithUnderscores(String input) {
        return input.replace(" ", "_");
    }

    /**
     * Gets color.
     *
     * @param index  the index
     * @param colors the colors
     * @return the color
     */
    public static Color getColor(int index, Color[] colors) {
        int colorIndex = index % colors.length;
        return colors[colorIndex];
    }

    /**
     * Carica icona.
     */
    public static void caricaIcona() {
        try {
            // Carica l'immagine dall'URL
            URL imageUrl = new URL(Constants.iconUrl);
            Constants.icon = ImageIO.read(imageUrl);
        } catch (IOException e) {
            Constants.icon = null;
            e.printStackTrace();
        }
    }
}
