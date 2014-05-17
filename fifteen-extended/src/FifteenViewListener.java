/**
 * Direction of Communication (where ML is model listener, VL is view listener):
 * ML (view) -> VL (ModelProxy) *-> server -> model
 *      -> ML (ViewProxy) ->  VL (ModelProxy) -> ML (View)
 *
 * @author Jenny Zhen
 * date: 05.17.14
 * language: Java
 * file: FifteenViewListener.java
 * assignment: Fifteen
 * http://www.cs.rit.edu/~wrc/courses/csci251/projects/3/
 */

/**
 * View communicates to the model through the view listener.
 */
public interface FifteenViewListener {
    // Player clicks on the button for a new game.
    public void newgame();

    // Player clicks on a digit to play.
    public void setDigit(int digit);

    // Player closes the game window.
    public void quit();
}
