/**
 * The type Player.
 */
class Player implements Comparable<Player> {
    private final String name;
    private int score;

    /**
     * Instantiates a new Player.
     *
     * @param name the name
     */
    public Player(String name) {
        this.name = name;
        this.score = 0;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets score.
     *
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets score.
     *
     * @param score the score
     */
    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int compareTo(Player otherPlayer) {
        // Ordina in base al punteggio (in ordine decrescente)
        return Integer.compare(otherPlayer.getScore(), this.getScore());
    }
}