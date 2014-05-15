/**
 * @author Jenny Zhen
 * date: 02.20.14
 * language: Java
 * file: Fifteen.java
 * assignment: Fifteen
 * http://www.cs.rit.edu/~wrc/courses/csci251/projects/3/
 */

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * In the game of Fifteen, two players take turns picking a number from 1 to 9.
 * Each number can only be picked once. Each player's score is the sum of the
 * numbers the player has picked. The game ends when one player's score is
 * exactly 15; that player wins. Otherwise, the game ends in a draw when all
 * numbers have been picked.
 */
public class Fifteen {
    public static void main(String[] args) {
        // Check command line arguments.
        if (args.length != 3) {
            System.err.println(
                    "Usage: java Fifteen <playerName> <host> <port>");
            System.exit(0);
        }

        // Sanitize and retrieve arguments.
        String host = args[1].trim();
        int port = -1;
        try {
            port = Integer.parseInt(args[2].trim());
        } catch (NumberFormatException e) {
            System.err.println(
                    "Error: Given port is not an integer value.");
            System.err.println(
                    "Usage: java Fifteen <playerName> <host> <port>");
            System.exit(0);
        }

        // Check the hostname and port number, and attempt to connect.
        Socket socket = null;
        try {
            socket = new Socket(host, port);
        } catch (UnknownHostException e) {
            System.err.println(
                    "Error: Given host is unknown.");
            System.err.println(
                    "Usage: java Fifteen <playerName> <host> <port>");
            System.exit(0);
        } catch (IOException e) {
            System.err.println(
                    "Error: Connection to the given host and port failed.");
            System.err.println(
                    "Usage: java Fifteen <playerName> <host> <port>");
            System.exit(0);
        }

        // Create the ModelProxy and View, and link to the listeners.
        FifteenModelProxy fifteenMP = new FifteenModelProxy(socket, args[0]);
        FifteenView fifteenV = new FifteenView(args[0]);
        fifteenV.setViewListener(fifteenMP);
        fifteenMP.setModelListener(fifteenV);
        Thread t = new Thread(fifteenMP);
        t.start();
    }
}
