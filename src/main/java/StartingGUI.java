import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The type Starting gui.
 */
public class StartingGUI extends JFrame{
    /**
     * Instantiates a new Starting gui.
     */
    public StartingGUI() {

        super("Game Setup");
        if(Constants.icon!=null)
            setIconImage(Constants.icon);
        setLayout(new FlowLayout());

        JButton playButton = new JButton("GIOCA");
        JButton setFolder = new JButton("SELEZIONA CARTELLE");
        JButton createFolder = new JButton("CREA CARTELLE");

        if(PlayerManagementGUI.path.isEmpty()){
            playButton.setEnabled(false);
        }else{
            playButton.setEnabled(true);
        }

        playButton.addActionListener(e -> {
            new PlayerManagementGUI();
            dispose();
        });

        setFolder.addActionListener(e -> {
            new SetFolder();
            if(PlayerManagementGUI.path.isEmpty()){
                playButton.setEnabled(false);
            }else{
                playButton.setEnabled(true);
            }
        });

        createFolder.addActionListener(e -> new CreateFolder());

        add(playButton);
        add(setFolder);
        add(createFolder);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Credits credits = new Credits();
                credits.pack();
                credits.setVisible(true);
            }
        });
    }
}
