/**
 * Model communicates to the view through the model listener.
 *
 * @author Jenny Zhen
 *         date: 02.20.14
 *         language: Java
 *         file: FifteenModelListener.java
 *         assignment: Fifteen
 *         http://www.cs.rit.edu/~wrc/courses/csci251/projects/3/
 */

public interface FifteenModelListener {

    // The ID of my player and the opponent in the game.
    public void setID(int player);

    // The name of the player with the corresponding ID.
    public void setName(int player, String name);

    // The digits from 1-9 available (1) and unavailable (0) to be played.
    public void setDigits(String digits);

    // The score of the player with the corresponding ID.
    public void setScore(int player, int score);

    // The player who has the current turn.
    public void setTurn(int player);

    // The player who won the game.
    public void setWin(int player);

    // The game ended.
    public void quit();
}
