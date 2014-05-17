/**
 * Talks to the view, through the client.
 *
 * Direction of Communication (where ML is model listener, VL is view listener):
 * ML (view) -> VL (ModelProxy) *-> server -> model
 *      -> ML (ViewProxy) ->  VL (ModelProxy) -> ML (View)
 *
 * @author Jenny Zhen
 * date: 05.17.14
 * language: Java
 * file: FifteenViewProxy.java
 * assignment: Fifteen
 * http://www.cs.rit.edu/~wrc/courses/csci251/projects/4/
 */

import java.net.DatagramSocket;
import java.net.SocketAddress;

public class FifteenViewProxy implements FifteenModelListener {
    private FifteenMailboxManager alpha_mailbox, beta_mailbox;
    private SocketAddress alpha_client, beta_client;

    /**
     * Constructor for FifteenViewProxy.
     * @param alpha_mailbox send/receive messages for alpha player
     * @param alpha_client session for alpha player
     */
    public FifteenViewProxy(
            DatagramSocket alpha_mailbox, SocketAddress alpha_client) {
        this.alpha_mailbox = new FifteenMailboxManager(alpha_mailbox);
        this.alpha_client = alpha_client;
    }

    /**
     * Used for adding the other player when he/she is known.
     * @param beta_mailbox send/receive messages for beta player
     * @param beta_client session for beta player
     */
    public void addPlayer(DatagramSocket beta_mailbox,
                          SocketAddress beta_client) {
        this.beta_mailbox = new FifteenMailboxManager(beta_mailbox);
        this.beta_client = beta_client;
    }

    /**
     * Tells the view the player id.
     * @param player the id of the player.
     */
    @Override
    public void setID(int player) {
        String message = "id " + player;

        if(beta_client == null)
            alpha_mailbox.sendMessage(message, alpha_client);
        else
            beta_mailbox.sendMessage(message, beta_client);
    }

    /**
     * Tells the view the name of the player, based on the id.
     * @param player the id of the player.
     * @param name the name of the player.
     */
    @Override
    public void setName(int player, String name) {
        String message = "name " + player + " " + name;

        alpha_mailbox.sendMessage(message, alpha_client);
        beta_mailbox.sendMessage(message, beta_client);
    }

    /**
     * Tells the view the available tiles on the board.
     * @param digits the tiles available.
     */
    @Override
    public void setDigits(String digits) {
        String message = "digits " + digits;

        alpha_mailbox.sendMessage(message, alpha_client);
        beta_mailbox.sendMessage(message, beta_client);
    }

    /**
     * Tells the view the score of the player.
     * @param player the id of the player.
     * @param score the score of the player.
     */
    @Override
    public void setScore(int player, int score) {
        String message = "score " + player + " " + score;

        alpha_mailbox.sendMessage(message, alpha_client);
        beta_mailbox.sendMessage(message, beta_client);
    }

    /**
     * Tells the view the current player.
     * @param player the id of the player.
     */
    @Override
    public void setTurn(int player) {
        String message = "turn " + player;

        alpha_mailbox.sendMessage(message, alpha_client);
        beta_mailbox.sendMessage(message, beta_client);
    }

    /**
     * Tells the view the winner of the game.
     * @param player the id of the player.
     */
    @Override
    public void setWin(int player) {
        String message = "win " + player;

        alpha_mailbox.sendMessage(message, alpha_client);
        beta_mailbox.sendMessage(message, beta_client);
    }

    /**
     * Tells the view to close.
     */
    @Override
    public void quit() {
        String message = "quit";

        alpha_mailbox.sendMessage(message, alpha_client);
        beta_mailbox.sendMessage(message, beta_client);
    }
}
