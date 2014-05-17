/**
 * The model stores the game information.
 *
 * Direction of Communication (where ML is model listener, VL is view listener):
 * ML (view) -> VL (ModelProxy) *-> server -> model
 *      -> ML (ViewProxy) ->  VL (ModelProxy) -> ML (View)
 *
 * @author Jenny Zhen
 * date: 05.17.14
 * language: Java
 * file: FifteenModel.java
 * assignment: Fifteen
 * http://www.cs.rit.edu/~wrc/courses/csci251/projects/4/
 */

public class FifteenModel implements FifteenViewListener {
    public static final int WIN_SCORE = 15; // win the game with this score
    public static final int MAX_TILES = 9; // maximum available tiles

    private int tilesAvailable; // number of tiles available
    private boolean[] board; // tiles available (true if available)

    private String alpha; // name of the Alpha player
    private boolean isCurrentPlayerAlpha; // tracks the current player
    private int alphaScore, betaScore; // scores of the players
    private FifteenModelListener fifteenML; // the view proxy

    /**
     * Constructor for FifteenModel.
     * @param alphaName the name of the first player.
     * @param fifteenML the model listener that talks to the view.
     */
    public FifteenModel(String alphaName, FifteenModelListener fifteenML) {
        this.alpha = alphaName; // save to use when both names are known

        this.board = new boolean[MAX_TILES];
        for(int i = 0; i < MAX_TILES; i++)
            board[i] = false;

        this.tilesAvailable = MAX_TILES;
        this.alphaScore = 0;
        this.betaScore = 0;

        this.fifteenML = fifteenML;
        this.fifteenML.setID(1);
    }

    /**
     * Adds the Beta player to the game.
     * @param betaName the name of the second player.
     */
    public void addPlayer(String betaName) {
        this.fifteenML.setID(2);
        this.fifteenML.setName(1, alpha);
        this.fifteenML.setName(2, betaName);
        this.isCurrentPlayerAlpha = true;
        this.fifteenML.setTurn(1);
        this.fifteenML.setScore(1, 0);
        this.fifteenML.setScore(2, 0);
    }

    /**
     * Starts a new game; resets the game.
     */
    @Override
    public void newgame() {
        for(int i = 0; i < MAX_TILES; i++)
            board[i] = false;

        this.tilesAvailable = MAX_TILES;
        this.alphaScore = 0;
        this.betaScore = 0;
        this.fifteenML.setScore(1, 0);
        this.fifteenML.setScore(2, 0);
        this.isCurrentPlayerAlpha = true;

        String digits = "";
        for (boolean ignored : this.board)
            digits += "1";

        this.fifteenML.setDigits(digits);
        this.fifteenML.setTurn(1);
    }

    /**
     * Adds a tile to the board.
     * @param digit the value of the tile to add.
     */
    @Override
    public void setDigit(int digit) {
        int index = digit - 1;

        // If tile hasn't been set, set tile.
        if(!this.board[index]) {
            this.board[index] = true;
            this.tilesAvailable--;

            // Tell the view to update the board.
            String digits = "";
            for (boolean tile : this.board) {
                digits += tile ? "0" : "1";
            }
            this.fifteenML.setDigits(digits);

            // Figure out who played the tile.
            if(this.isCurrentPlayerAlpha) {
                this.alphaScore += digit;
                this.fifteenML.setScore(1, alphaScore);

                // If win, set win to Alpha. Else, continue.
                if(this.alphaScore == WIN_SCORE)
                    this.fifteenML.setWin(1);
                else {
                    this.isCurrentPlayerAlpha = false;
                    this.fifteenML.setTurn(2);
                }
            } else {
                this.betaScore += digit;
                this.fifteenML.setScore(2, betaScore);

                // If win, set win to Beta. Else, continue.
                if(this.betaScore == WIN_SCORE)
                    this.fifteenML.setWin(2);
                else {
                    this.isCurrentPlayerAlpha = true;
                    this.fifteenML.setTurn(1);
                }
            }

            // If no more tiles available to play, set win to draw.
            if(this.tilesAvailable == 0)
                this.fifteenML.setWin(0);
        }
    }

    /**
     * Quits the game between the two players.
     */
    @Override
    public void quit() {
        this.fifteenML.quit();
    }

    /**
     * @return the model listener that talks to the view.
     */
    public FifteenModelListener getModelListener() {
        return this.fifteenML;
    }
}
