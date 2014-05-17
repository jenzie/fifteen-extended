/**
 * The client for Fifteen.
 *
 * Direction of Communication (where ML is model listener, VL is view listener):
 * ML (view) -> VL (ModelProxy) *-> server -> model
 *      -> ML (ViewProxy) ->  VL (ModelProxy) -> ML (View)
 *
 * @author Jenny Zhen
 * date: 05.17.14
 * language: Java
 * file: Fifteen.java
 * assignment: Fifteen
 * http://www.cs.rit.edu/~wrc/courses/csci251/projects/4/
 */

import java.net.*;

/**
 * In the game of Fifteen, two players take turns picking a number from 1 to 9.
 * Each number can only be picked once. Each player's score is the sum of the
 * numbers the player has picked. The game ends when one player's score is
 * exactly 15; that player wins. Otherwise, the game ends in a draw when all
 * numbers have been picked.
 */
public class Fifteen {
    /**
     * Runs the game of Fifteen for one player.
     * @param args the client host, client port, server host, server port.
     */
    public static void main(String[] args) {
        // Check command line arguments.
        if (args.length != 5) {
            System.err.println("Usage: java Fifteen <playername> " +
                "<c_host> <c_port> <s_host> <s_port>");
            System.exit(0);
        }

        // Sanitize and retrieve arguments.
        String playername = args[0].trim();
        String c_host = args[1].trim();
        String s_host = args[3].trim();
        int c_port = -1, s_port = -1;
        try {
            c_port = Integer.parseInt(args[2].trim());
            s_port = Integer.parseInt(args[4].trim());
        } catch (NumberFormatException e) {
            System.err.println(
                    "Error: Given port is not an integer value.");
            System.err.println("Usage: java Fifteen <playername> " +
                    "<c_host> <c_port> <s_host> <s_port>");
            System.exit(0);
        }

        // Check the hostname and port number, and attempt to connect.
        InetSocketAddress server = new InetSocketAddress(s_host, s_port);
        DatagramSocket mailbox = null;
        try {
            mailbox = new DatagramSocket(new InetSocketAddress(c_host, c_port));
        } catch (SocketException e) {
            System.err.println(
                    "Error: Given host is unknown.");
            System.err.println("Usage: java Fifteen <playername> " +
                    "<c_host> <c_port> <s_host> <s_port>");
            System.exit(0);
        }

        // Create the ModelProxy and View, and link to the listeners.
        FifteenModelProxy fifteenMP =
                new FifteenModelProxy(server, mailbox, playername);
        FifteenView fifteenV = new FifteenView(playername);
        fifteenV.setViewListener(fifteenMP);
        fifteenMP.setModelListener(fifteenV);
        Thread t = new Thread(fifteenMP);
        t.start();
    }
}
