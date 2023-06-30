import javax.swing.*;

/**
 * The type Set folder.
 */
public class SetFolder {
    /**
     * Instantiates a new Set folder.
     */
    public SetFolder(){
        // Crea un'istanza di JFileChooser
        JFileChooser fileChooser = new JFileChooser();

        // Imposta il titolo del dialogo di selezione
        fileChooser.setDialogTitle("Seleziona il percorso per la cartella");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        // Mostra il dialogo di selezione del percorso
        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            // Ottieni il percorso selezionato dall'utente
            PlayerManagementGUI.path = fileChooser.getSelectedFile().getAbsolutePath();
        }
    }
}
