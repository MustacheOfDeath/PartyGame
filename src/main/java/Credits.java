import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The type Credits.
 */
public class Credits extends JFrame {
    /**
     * Instantiates a new Credits.
     */
    public Credits() {
        super("Credits");
        if(Constants.icon!=null)
            setIconImage(Constants.icon);
        Color backgroundColor = Color.BLACK;
        getContentPane().setBackground(backgroundColor);

        setLayout(new FlowLayout());

        JLabel autoreLabel = new JLabel("Autore: " + Constants.AUTORE);
        JLabel versioneLabel = new JLabel("Versione: " + Constants.VERSIONE);
        JLabel dataVersioneLabel = new JLabel("Data Versione: " + Constants.DATA_VERSIONE);

        int fontSize = 16;
        Font labelFont = new Font(Font.SANS_SERIF, Font.BOLD, fontSize);
        autoreLabel.setFont(labelFont);
        versioneLabel.setFont(labelFont);
        dataVersioneLabel.setFont(labelFont);

        Color fontColor = Color.GREEN;
        autoreLabel.setForeground(fontColor);
        versioneLabel.setForeground(fontColor);
        dataVersioneLabel.setForeground(fontColor);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(autoreLabel);
        infoPanel.add(versioneLabel);
        infoPanel.add(dataVersioneLabel);
        infoPanel.setBackground(Color.BLACK);

        add(infoPanel, BorderLayout.CENTER);

        // Imposta la dimensione desiderata per la finestra
        int width = 400;  // Larghezza in pixel
        int height = 125; // Altezza in pixel
        setSize(width, height);

        setLocationRelativeTo(null);
        setVisible(true);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                closeInfoGUI(); // Chiude la finestra InfoGUI dopo 10 secondi
            }
        }, 5000); // Ritardo di 10 secondi (10000 millisecondi)
    }

    /**
     *Chiude la finestra
     */
    private void closeInfoGUI() {
        dispose(); // Chiude la finestra InfoGUI
        System.exit(0); // Chiude la JVM
    }
}
