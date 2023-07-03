import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * The type Player management gui.
 */
public class PlayerManagementGUI extends JFrame {

    //VARIABILI-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private final JPanel playersPanel;
    private final JButton playButton;
    private int playerCount;

    private final List<Player> playerList;

    /**
     * The Scoreboard panel.
     */
    JPanel scoreboardPanel;

    /**
     * The Image frame.
     */
    JFrame imageFrame;

    /**
     * The Audio frame.
     */
    JFrame audioFrame;

    /**
     * The Game panel.
     */
    JPanel gamePanel;

    private JButton[][] scoreButtonGrid;

    /**
     * The Is ripristina present.
     */
    boolean isRipristinaPresent = false;

    /**
     * The Is modify present.
     */
    boolean isModifyPresent = false;

    /**
     * The Selected point.
     */
    int selectedPoint = 0;

    /**
     * The Path.
     */
    static String path = "";



//GUI INIZIALE -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Instantiates a new Player management gui.
     * é la finestra in cui vengono impostati i giocatori
     */
    public PlayerManagementGUI() {
        setTitle("Player Management");
        if(Constants.icon!=null)
            setIconImage(Constants.icon);

        playersPanel = new JPanel();
        playersPanel.setLayout(new BoxLayout(playersPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(playersPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(250, 200));

        JButton addButton = new JButton("Aggiungi Giocatore");
        addButton.addActionListener(e -> addPlayerDialog());

        playButton = new JButton("Play");
        playButton.addActionListener(e -> openGameScreen());
        playButton.setEnabled(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));
        buttonPanel.add(addButton);
        buttonPanel.add(playButton);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        setWindowSize();
        setVisible(true);

        playerCount = 0;
        playerList = new ArrayList<>();

        setWindowSize();
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

    /**
     * Apre la finestra in cui digitare il nome del giocatore
     */
    private void addPlayerDialog() {
        if(Constants.icon!=null)
            setIconImage(Constants.icon);
        if (playerCount >= 10) {
            JOptionPane.showMessageDialog(this, "È possibile aggiungere un massimo di 10 giocatori.", "Limite raggiunto", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String playerName = JOptionPane.showInputDialog(this, "Inserisci il nome del giocatore:");

        if (playerName != null && !playerName.isEmpty()) {
            // Controllo se il nome è già presente nella lista dei giocatori
            for(Player p : playerList){
                if(p.getName().equals(playerName)) {
                    JOptionPane.showMessageDialog(this, "Il nome del giocatore è già stato inserito.", "Nome duplicato", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            Player newPlayer = new Player(playerName);
            playerCount++;
            playerList.add(newPlayer);

            // Disabilita il pulsante Play se non ci sono più giocatori
            playButton.setEnabled(playerCount > 1);

            // Creazione del pannello del giocatore
            JPanel playerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel nameLabel = new JLabel(playerName);
            JButton removeButton = new JButton("Rimuovi");
            removeButton.addActionListener(e -> {
                playerCount--;
                Iterator<Player> iterator = playerList.iterator();
                while (iterator.hasNext()) {
                    Player p = iterator.next();
                    if (p.getName().equals(playerName)) {
                        iterator.remove(); // Safely remove the current element
                        playersPanel.remove(playerPanel);
                        playersPanel.revalidate();
                        playersPanel.repaint();
                    }
                }

                // Enable the playButton if there are still players
                playButton.setEnabled(playerCount > 1);
            });

            playerPanel.add(nameLabel);
            playerPanel.add(removeButton);
            playersPanel.add(playerPanel);
            playersPanel.revalidate();
            playersPanel.repaint();
        }
    }

    //GUI PRINCIPALE -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     *Apre la finestra di gioco, con a SX la classifica e a DX le categorie con i punteggi
     */
    private void openGameScreen() {
        dispose();

        // Creazione della schermata del gioco
        JFrame gameFrame = new JFrame("Game Screen");
        gameFrame.setSize(1920, 1080);
        gameFrame.setLocationRelativeTo(null);
        if(Constants.icon!=null)
            gameFrame.setIconImage(Constants.icon);

        // Creazione del pannello dei giocatori
        JPanel playersPanel = new JPanel();
        playersPanel.setLayout(new BoxLayout(playersPanel, BoxLayout.Y_AXIS));
        playersPanel.setPreferredSize(new Dimension(200, playersPanel.getHeight()));
        gameFrame.add(playersPanel, BorderLayout.WEST);

        // Creazione del pannello della classifica dei giocatori
        scoreboardPanel = new JPanel();
        scoreboardPanel.setLayout(new BoxLayout(scoreboardPanel, BoxLayout.Y_AXIS));
        playersPanel.add(scoreboardPanel);

        // Aggiunta dei giocatori alla classifica
        ArrayList<Player> playerList = new ArrayList<>();
        for (Player value : this.playerList) {
            String playerName = value.getName();
            Player player = new Player(playerName);
            playerList.add(player);
        }

        // Ordina la lista dei giocatori in base ai punteggi
        playerList.sort((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()));

        for (int i = 0; i < playerList.size(); i++) {
            Player player = playerList.get(i);
            JLabel playerLabel = new JLabel((i + 1) + ". " + player.getName() + ": " + player.getScore());
            playerLabel.setForeground(Utility.getColor(i, Constants.playerColors));
            scoreboardPanel.add(playerLabel);
        }

        // Aggiunta dei componenti grafici per la tabella di gioco
        gamePanel = new JPanel(new GridLayout(6, 8));
        gameFrame.add(gamePanel, BorderLayout.CENTER);

        // Aggiunta delle intestazioni della tabella

        int i = 0;
        for (String header : Constants.headers) {
            JLabel headerLabel = new JLabel(header, SwingConstants.CENTER);
            headerLabel.setFont(headerLabel.getFont().deriveFont(Font.BOLD));
            headerLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            headerLabel.setOpaque(true);
            headerLabel.setBackground(Utility.getColor(i, Constants.headersColors));
            gamePanel.add(headerLabel);
            i++;
        }
        scoreButtonGrid = new JButton[5][Constants.headers.length];
        // Aggiunta delle celle con i punti
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < Constants.headers.length; col++) {
                int points = (row + 1) * 100;
                String imageName = path+"/"+Utility.replaceSpacesWithUnderscores(Constants.headers[col])+ "/" + points + ".png";
                String songName = path+Utility.replaceSpacesWithUnderscores(Constants.headers[col])+ "/" + points + ".wav";
                JButton cellButton = new JButton(Integer.toString(points));
                cellButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                cellButton.setBackground(Color.WHITE);
                int headerPosition = col;
                cellButton.addActionListener(e -> {
                    if (Constants.headers[headerPosition].equals("This sounds familiar") || Constants.headers[headerPosition].equals("I hear the voices")|| Constants.headers[headerPosition].equals("1 second ost")) {
                        openSongWindow(songName, points);
                    } else {
                        openImageWindow(imageName, points);

                    }
                    cellButton.setEnabled(false);
                    cellButton.setBackground(Color.BLACK);
                });
                cellButton.addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        if(cellButton.isEnabled()){
                            cellButton.setBackground(Color.BLUE);
                        }
                    }
                    public void mouseExited(MouseEvent e) {
                        if(cellButton.isEnabled()) {
                            cellButton.setBackground(Color.WHITE);
                        }
                    }
                });
                scoreButtonGrid[row][col] = cellButton;
                gamePanel.add(cellButton);
            }
        }

        gameFrame.setVisible(true);

        gameFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Chiudi la finestra del gioco
                gameFrame.dispose();

                // Apri la finestra Credits
                Credits credits = new Credits();
                credits.pack();
                credits.setVisible(true);
            }
        });
    }

    //FINESTRE IMMAGINI -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Apre la selezione del giocatore per un
     * gioco di immagini
     */
    private void openImageWindow(String imagePath, int points) {
        // Creazione della finestra di dialogo per la selezione del giocatore
        JFrame gamerSelectionImage = new JFrame("Selezione Giocatore");
        gamerSelectionImage.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gamerSelectionImage.setSize(300, 200);
        gamerSelectionImage.setLocationRelativeTo(null);
        if(Constants.icon!=null)
            gamerSelectionImage.setIconImage(Constants.icon);

        JPanel selectionPanel = new JPanel(new GridLayout(playerCount, 1));

        // Aggiunta dei pulsanti per i giocatori
        for (Player player : playerList) {
            String playerName = player.getName();
            JButton playerButton = new JButton(playerName);
            playerButton.addActionListener(e -> {
                openImageFrame(imagePath, playerName, points);
                gamerSelectionImage.dispose(); // Chiude la finestra di selezione
            });
            selectionPanel.add(playerButton);
        }

        gamerSelectionImage.getContentPane().add(selectionPanel, BorderLayout.CENTER);
        gamerSelectionImage.setVisible(true);
        gamerSelectionImage.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                // La finestra è stata chiusa, chiamare il metodo updatePlayerScoreboard
                updatePlayerScoreboard(null,0,null, false);
            }
        });
    }

    /**
     * Apre la finestra con un gioco di immagini
     */
    private void openImageFrame(String imagePath, String playerName, int points) {
        // Creazione della finestra dell'immagine
        imageFrame = new JFrame("Image Viewer - " + playerName);
        imageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        imageFrame.setSize(800, 600);
        imageFrame.setLocationRelativeTo(null);
        if(Constants.icon!=null)
            imageFrame.setIconImage(Constants.icon);

        ImageIcon imageIcon = new ImageIcon(imagePath);
        JLabel imageLabel = new JLabel(imageIcon);

        JPanel buttonPanel = new JPanel();
        JButton wrongButton = new JButton("Errato");
        wrongButton.setBackground(Color.RED);
        JButton correctButton = new JButton("Corretto");
        correctButton.setBackground(Color.GREEN);

        wrongButton.addActionListener(e -> incorrectButtonClicked(points, playerName));

        correctButton.addActionListener(e -> correctButtonClicked(points, playerName));

        buttonPanel.add(correctButton);
        buttonPanel.add(wrongButton);

        JScrollPane scrollPane = new JScrollPane(imageLabel);

        imageFrame.getContentPane().setLayout(new BorderLayout());
        imageFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        imageFrame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        imageFrame.setVisible(true);
        imageFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                // La finestra è stata chiusa, chiamare il metodo updatePlayerScoreboard
                updatePlayerScoreboard(null,0,null, false);
            }
        });
    }

    //FINESTRE MUSICALI -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Apre la selezione del giocatore per un
     * gioco di suoni
     */
    private void openSongWindow(String songName, int points) {
        // Creazione della finestra di dialogo per la selezione del giocatore
        JFrame gamerSelectionSong = new JFrame("Selezione Giocatore");
        gamerSelectionSong.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gamerSelectionSong.setSize(300, 200);
        gamerSelectionSong.setLocationRelativeTo(null);
        if(Constants.icon!=null)
            gamerSelectionSong.setIconImage(Constants.icon);

        JPanel selectionPanel = new JPanel(new GridLayout(playerCount, 1));

        // Aggiunta dei pulsanti per i giocatori
        for (Player player : playerList) {
            String playerName = player.getName();
            JButton playerButton = new JButton(playerName);
            playerButton.addActionListener(e -> {
                openSongFrame(songName, playerName, points);
                gamerSelectionSong.dispose(); // Chiude la finestra di selezione
            });
            selectionPanel.add(playerButton);
        }

        gamerSelectionSong.getContentPane().add(selectionPanel, BorderLayout.CENTER);
        gamerSelectionSong.setVisible(true);
        gamerSelectionSong.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                // La finestra è stata chiusa, chiamare il metodo updatePlayerScoreboard
                updatePlayerScoreboard(null,0,null, false);
            }
        });
    }

    /**
     * Apre la finestra con un gioco di suoni
     */
    private void openSongFrame(String songPath, String playerName, int points) {
        try {
            // Creazione della finestra dell'audio
            audioFrame = new JFrame("Audio Player - " + playerName);
            audioFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            audioFrame.setSize(400, 100);
            audioFrame.setLocationRelativeTo(null);
            if(Constants.icon!=null)
                audioFrame.setIconImage(Constants.icon);

            JButton playButton = new JButton("Play");
            JButton stopButton = new JButton("Stop");

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(songPath));

            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            playButton.addActionListener(e -> {
                clip.setFramePosition(0);
                clip.start();
            });

            stopButton.addActionListener(e -> clip.stop());

            JButton wrongButton = new JButton("Errato");
            wrongButton.setBackground(Color.RED);
            JButton correctButton = new JButton("Corretto");
            correctButton.setBackground(Color.GREEN);

            wrongButton.addActionListener(e -> {
                incorrectButtonClicked(points, playerName);
                clip.stop();
            });

            correctButton.addActionListener(e -> {
                correctButtonClicked(points, playerName);
                clip.stop();
            });



            JPanel buttonPanel = new JPanel();
            buttonPanel.add(playButton);
            buttonPanel.add(stopButton);
            buttonPanel.add(correctButton);
            buttonPanel.add(wrongButton);

            audioFrame.getContentPane().setLayout(new BorderLayout());
            audioFrame.getContentPane().add(buttonPanel, BorderLayout.CENTER);

            audioFrame.setVisible(true);
            audioFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    // La finestra è stata chiusa, chiamare il metodo updatePlayerScoreboard
                    updatePlayerScoreboard(null,0,null, false);
                }
            });
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        }
    }
    //RIPRISTINA PULSANTI DISABILITATI  -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Apre una finestra in cui é possibile riabilitare un gioco disabilitato
     */
    private void selectScoreButton() {
        // Creazione della finestra di dialogo per la selezione del pulsante
        JFrame ripristinaPulsanteFrame = new JFrame("Ripristina Pulsante");
        ripristinaPulsanteFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ripristinaPulsanteFrame.setSize(800, 600);
        ripristinaPulsanteFrame.setLocationRelativeTo(null);
        if(Constants.icon!=null)
            ripristinaPulsanteFrame.setIconImage(Constants.icon);

        JPanel selectionPanel = new JPanel(new GridLayout(6, Constants.headers.length));

        int i = 0;
        for (String header : Constants.headers) {
            JLabel headerLabel = new JLabel(header, SwingConstants.CENTER);
            headerLabel.setFont(headerLabel.getFont().deriveFont(Font.BOLD));
            headerLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            headerLabel.setOpaque(true);
            headerLabel.setBackground(Utility.getColor(i, Constants.headersColors));
            selectionPanel.add(headerLabel);
            i++;
        }

        // Aggiunta delle celle con i punti
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < Constants.headers.length; col++) {
                int points = (row + 1) * 100;
                int finalRow = row;
                int finalCol = col;
                JButton cellButton = new JButton(Integer.toString(points));
                cellButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                if( !scoreButtonGrid[row][col].isEnabled()){
                    cellButton.setBackground(Color.BLACK);
                }else{
                    cellButton.setBackground(Color.WHITE);
                }
                cellButton.addActionListener(e -> {
                    scoreButtonGrid[finalRow][finalCol].setEnabled(true); // Abilita il pulsante selezionato
                    scoreButtonGrid[finalRow][finalCol].setBackground(Color.WHITE);
                    ripristinaPulsanteFrame.dispose(); // Chiude la finestra di selezione
                });
                cellButton.addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        if(cellButton.isEnabled()){
                            cellButton.setBackground(Color.BLUE);
                        }
                    }
                    public void mouseExited(MouseEvent e) {
                        if(cellButton.isEnabled()) {
                            if( !scoreButtonGrid[finalRow][finalCol].isEnabled()){
                                cellButton.setBackground(Color.BLACK);
                            }else{
                                cellButton.setBackground(Color.WHITE);
                            }
                        }
                    }
                });
                selectionPanel.add(cellButton);
            }
        }

        ripristinaPulsanteFrame.getContentPane().add(selectionPanel, BorderLayout.CENTER);
        ripristinaPulsanteFrame.setVisible(true);
    }

    //SISTEMA PUNTEGGIO -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Apre la finestra in cui selezionare un giocatore
     * di cui dobbiamo modificare il punteggio
     */
    private void selectPlayerToModify() {
        if(Constants.icon!=null)
            setIconImage(Constants.icon);
        // Creazione della finestra di dialogo per la selezione del giocatore
        JFrame gamerSelectionImage = new JFrame("Selezione Giocatore");
        gamerSelectionImage.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gamerSelectionImage.setSize(300, 200);
        gamerSelectionImage.setLocationRelativeTo(null);
        gamerSelectionImage.setIconImage(Constants.icon);

        JPanel selectionPanel = new JPanel(new GridLayout(playerCount, 1));

        // Aggiunta dei pulsanti per i giocatori
        for (Player player : playerList) {
            String playerName = player.getName();
            JButton playerButton = new JButton(playerName);
            playerButton.addActionListener(e -> {
                modifyScore(playerName);
                gamerSelectionImage.dispose(); // Chiude la finestra di selezione
            });
            selectionPanel.add(playerButton);
        }

        gamerSelectionImage.getContentPane().add(selectionPanel, BorderLayout.CENTER);
        gamerSelectionImage.setVisible(true);
        gamerSelectionImage.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                // La finestra è stata chiusa, chiamare il metodo updatePlayerScoreboard
                updatePlayerScoreboard(null,0,null, false);
            }
        });
    }
    /**
     * Apre la finestra in cui selezionare quanti punti aggiungere
     * o rimuovere a un determinato giocatore
     */

    private void modifyScore(String playerName) {
        // Creazione della finestra di dialogo per la modifica del punteggio
        JFrame modifyScoreFrame = new JFrame("Modifica Punteggio");
        modifyScoreFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        modifyScoreFrame.setSize(800, 200);
        modifyScoreFrame.setLocationRelativeTo(null);
        if(Constants.icon!=null)
            modifyScoreFrame.setIconImage(Constants.icon);

        JPanel buttonPanel = new JPanel(new BorderLayout());



        // Pannello per i pulsanti dei punti
        JPanel pointsPanel = new JPanel(new GridLayout(1, 5));
        int[] points = {100, 200, 300, 400, 500};
        for (int pointValue : points) {
            JButton pointButton = new JButton(Integer.toString(pointValue));
            pointButton.addActionListener(e -> {
                // Memorizza il valore del punto selezionato
                selectedPoint = pointValue;
            });
            pointsPanel.add(pointButton);
        }
        buttonPanel.add(pointsPanel, BorderLayout.NORTH);

        // Spazio vuoto
        JPanel emptyPanel = new JPanel();
        emptyPanel.setPreferredSize(new Dimension(800, 10));
        buttonPanel.add(emptyPanel, BorderLayout.CENTER);

        // Pannello per i pulsanti Aggiungi e Sottrai
        JPanel operationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addButton = new JButton("Aggiungi");
        addButton.setBackground(Color.GREEN);
        addButton.addActionListener(e -> {
            // Esegui l'operazione di aggiunta del punteggio
            updatePlayerScoreboard(true, selectedPoint, playerName, true);
            modifyScoreFrame.dispose(); // Chiude la finestra di modifica del punteggio
        });
        JButton subtractButton = new JButton("Sottrai");
        subtractButton.setBackground(Color.RED);
        subtractButton.addActionListener(e -> {
            // Esegui l'operazione di sottrazione del punteggio
            updatePlayerScoreboard(false, selectedPoint, playerName, true);
            modifyScoreFrame.dispose(); // Chiude la finestra di modifica del punteggio
        });
        operationPanel.add(addButton);
        operationPanel.add(subtractButton);
        buttonPanel.add(operationPanel, BorderLayout.SOUTH);

        modifyScoreFrame.getContentPane().add(buttonPanel, BorderLayout.CENTER);
        modifyScoreFrame.setVisible(true);
    }



    //UTILITY -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Setta la dimensione predefinita della finestra
     */
    private void setWindowSize() {
        setSize(720, 540);
        centerWindow();
    }

    /**
     * Centra la finestra nello schermo
     */
    private void centerWindow() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);
    }

    /**
     * modifica il punteggio aggiungendo i punti della risposta corretta
     */
    private void correctButtonClicked(int points, String playerName) {
        updatePlayerScoreboard(true, points, playerName, true);
        if(imageFrame!=null)
            imageFrame.dispose();
        if(audioFrame!=null)
            audioFrame.dispose();

    }

    /**
     *modifica il punteggio sottraendo i punti della risposta errata
     */
    private void incorrectButtonClicked(int points, String playerName) {
        updatePlayerScoreboard(false, points, playerName, true);
        if(imageFrame!=null)
            imageFrame.dispose();
        if(audioFrame!=null)
            audioFrame.dispose();
    }

    /**
     * Aggiorna la classifica
     */
    private void updatePlayerScoreboard(Boolean isCorrect, int points, String playerName, boolean recalculate) {
        List<Player> players = new ArrayList<>(playerList);

        if(recalculate){
            for (Player value : players) {
                if (value.getName().equals(playerName)) {
                    int newScore;
                    if (isCorrect) {
                        newScore = value.getScore() + points;
                    } else {
                        newScore = value.getScore() - points;
                    }
                    value.setScore(newScore);
                }
            }
            Collections.sort(players);

            isRipristinaPresent = false;
            isModifyPresent = false;
            scoreboardPanel.removeAll();

            for (int i = 0; i < players.size(); i++) {
                Player player = players.get(i);
                JLabel playerLabel = new JLabel((i + 1) + ". " +  player.getName() + ": " + player.getScore());
                playerLabel.setForeground(Utility.getColor(i, Constants.playerColors)); // Imposta il colore del giocatore
                scoreboardPanel.add(playerLabel);
            }
        }

        if(!isRipristinaPresent){
            isRipristinaPresent = true;
            JButton ripristina = new JButton("RIPRISTINA");
            ripristina.addActionListener(e -> selectScoreButton());
            scoreboardPanel.add(ripristina, BorderLayout.SOUTH);
        }

        if(!isModifyPresent){
            isModifyPresent = true;
            JButton ripristina = new JButton("MODIFICA PUNTEGGIO");
            ripristina.addActionListener(e -> selectPlayerToModify());
            scoreboardPanel.add(ripristina, BorderLayout.SOUTH);
        }


        scoreboardPanel.revalidate();
        scoreboardPanel.repaint();
    }

}