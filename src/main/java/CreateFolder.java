import javax.swing.*;
import java.io.File;

/**
 * The type Create folder.
 */
public class CreateFolder extends JFrame {
    /**
     * Instantiates a new Create folder.
     */
    public CreateFolder() {
        // Crea un'istanza di JFileChooser
        JFileChooser fileChooser = new JFileChooser();

        // Imposta il titolo del dialogo di selezione
        fileChooser.setDialogTitle("Seleziona il percorso per la cartella");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if(Constants.icon!=null)
            setIconImage(Constants.icon);

        // Mostra il dialogo di selezione del percorso
        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            // Ottieni il percorso selezionato dall'utente
            String selectedFolderPath = fileChooser.getSelectedFile().getAbsolutePath();

            String folderName = JOptionPane.showInputDialog(null, "Enter folder name:");

            if (folderName != null && !folderName.trim().isEmpty()) {
                String newFolderPath = selectedFolderPath + File.separator + folderName;
                File newFolder = new File(newFolderPath);
                newFolder.mkdir();
                // Salva il percorso nella variabile 'path'
                String path = newFolder.getAbsolutePath();

                if (newFolder.exists()) {
                    for (String folder : Constants.headers) {
                        String folderPath = path + "\\" + Utility.replaceSpacesWithUnderscores(folder);
                        File newCatergoryFolder = new File(folderPath);
                        newCatergoryFolder.mkdir();
                    }
                }

            }
        }
    }
}
