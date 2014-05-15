/**
 * @author Jenny Zhen
 * date: 05.17.14
 * language: Java
 * file: FifteenModelProxy.java
 * assignment: Fifteen
 * http://www.cs.rit.edu/~wrc/courses/csci251/projects/4/
 */

import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * * Proxy to the actual Model, that is located on the server.
 */
public class FifteenModelProxy implements Runnable, FifteenViewListener {
    private InetSocketAddress server; // maintains the session
    private DatagramSocket mailbox; // sends and receives packets (messages)
    private Scanner in; // server-to-client messages
    private PrintStream out; // client-to-server messages
    private FifteenModelListener fifteenML; // communicate to the view

    /**
     * Constructor for FifteenModelProxy.
     *
     * @param server maintains the session
     * @param mailbox sends and receives packets (messages)
     * @param playerName the name of the current player
     */
    public FifteenModelProxy(InetSocketAddress server, DatagramSocket mailbox,
                             String playerName) {
        this.server = server;
        this.mailbox = mailbox;

        // Set up the I/O (input/output) for client-to-server messages.
        try {
            this.in = new Scanner(socket.getInputStream());
            this.out = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println(
                    "Error: Connection to the given host and port failed.");
            System.exit(0);
        }

        this.joinServer(playerName);
    }

    /**
     * Set the model listener to communicate to the view.
     * @param fifteenML the model listener.
     */
    public void setModelListener(FifteenModelListener fifteenML) {
        this.fifteenML = fifteenML;
    }

    /**
     * Client-to-server message:
     * join n
     * Sent when the client starts up; n is replaced with the player's name.
     *
     * @param playerName username of the player.
     */
    public void joinServer(String playerName) {
        out.println("join " + playerName);
        out.flush();
    }

    /**
     * Client-to-server message:
     * digit d
     * Sent when the player clicks a number button;
     * d is replaced with the number (1 to 9).
     *
     * @param digit the value the player played.
     */
    public void digitServer(int digit) {
        out.println("digit " + digit);
        out.flush();
    }

    /**
     * Client-to-server message:
     * newgame
     * Sent when the player clicks the New Game button.
     */
    public void newgameServer() {
        out.println("newgame");
        out.flush();
    }

    /**
     * Client-to-server message:
     * quit
     * Sent when the player closes the window.
     */
    public void quitServer() {
        out.close();
        in.close();
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println(
                    "Error: Connection to the given host and port failed.");
            System.exit(0);
        }
    }

    @Override
    /**
     * Tell the server to process inputs.
     */
    public void run() {
        while (in.hasNextLine()) {
            String line = in.nextLine();
            String[] message = line.split(" ");

            if (message[0].equals("id")) {
                fifteenML.setID(Integer.parseInt(message[1]));
            } else if (message[0].equals("name")) {
                fifteenML.setName(Integer.parseInt(message[1]), message[2]);
            } else if (message[0].equals("digits")) {
                fifteenML.setDigits(message[1]);
            } else if (message[0].equals("score")) {
                fifteenML.setScore(Integer.parseInt(message[1]),
                        Integer.parseInt(message[2]));
            } else if (message[0].equals("turn")) {
                fifteenML.setTurn(Integer.parseInt(message[1]));
            } else if (message[0].equals("win")) {
                fifteenML.setWin(Integer.parseInt(message[1]));
            } else if (message[0].equals("quit")) {
                quitServer();
                fifteenML.quit();
            } else {
                System.err.println(
                        "Error: Invalid server-to-client message.");
                System.exit(0);
            }
        }
    }

    @Override
    /**
     * Tell the server to create a new game.
     */
    public void newgame() {
        newgameServer();
    }

    @Override
    /**
     * Tell the server to set the available digits.
     */
    public void setDigit(int digit) {
        digitServer(digit);
    }

    @Override
    /**
     * Tell the server a player wants to quit.
     */
    public void quit() {
        out.println("quit");
        out.flush();
        quitServer();
    }
}
