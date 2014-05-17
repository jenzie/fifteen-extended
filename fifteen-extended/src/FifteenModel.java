/**
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

    private String alpha, beta; // name of the players
    private boolean isCurrentPlayerAlpha; // tracks the current player
    private int alphaScore, betaScore; // scores of the players
    private FifteenModelListener fifteenML; //

    public FifteenModel(String alphaName, FifteenModelListener fifteenML) {
        this.alpha = alphaName;

        this.board = new boolean[MAX_TILES];
        for(int i = 0; i < MAX_TILES; i++)
            board[i] = false;

        this.tilesAvailable = MAX_TILES;
        this.alphaScore = 0;
        this.betaScore = 0;

        this.fifteenML = fifteenML;
    }

    public void addPlayer(String betaName) {
        this.beta = betaName;
    }

    @Override
    public void newgame() {
        for(int i = 0; i < MAX_TILES; i++)
            board[i] = false;

        this.tilesAvailable = MAX_TILES;
        this.alphaScore = 0;
        this.betaScore = 0;
        this.isCurrentPlayerAlpha = true;
    }

    @Override
    public void setDigit(int digit) {
        int index = digit - 1;

        if(this.board[index] == false) {
            this.board[index] = true;
            this.tilesAvailable--;

            if(this.isCurrentPlayerAlpha) {
                this.alphaScore += digit;
                this.isCurrentPlayerAlpha = false;
            } else {
                this.betaScore += digit;
                this.isCurrentPlayerAlpha = true;
            }
        }
    }

    @Override
    public void quit() {
        this.fifteenML.quit();
    }
}
