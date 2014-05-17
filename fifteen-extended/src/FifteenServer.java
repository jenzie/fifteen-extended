/**
 * The server for Fifteen.
 *
 * Direction of Communication (where ML is model listener, VL is view listener):
 * ML (view) -> VL (ModelProxy) *-> server -> model
 *      -> ML (ViewProxy) ->  VL (ModelProxy) -> ML (View)
 *
 * @author Jenny Zhen
 * date: 05.17.14
 * language: Java
 * file: FifteenServer.java
 * assignment: Fifteen
 * http://www.cs.rit.edu/~wrc/courses/csci251/projects/4/
 */

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.HashMap;

public class FifteenServer {
    /**
     * Runs the server.
     * @param args server host, server port.
     */
    public static void main(String[] args) {
        // Check command line arguments.
        if (args.length != 2) {
            System.err.println("Usage: java Fifteen <s_host> <s_port>");
            System.exit(0);
        }

        // Sanitize and retrieve arguments.
        String s_host = args[0].trim();
        int s_port = -1;
        try {
            s_port = Integer.parseInt(args[1].trim());
        } catch (NumberFormatException e) {
            System.err.println(
                    "Error: Given port is not an integer value.");
            System.err.println("Usage: java Fifteen <s_host> <s_port>");
            System.exit(0);
        }

        // Check the hostname and port number, and attempt to connect.
        DatagramSocket mailbox = null;
        FifteenMailboxManager mailboxManager = null;
        try {
            mailbox = new DatagramSocket(new InetSocketAddress(s_host, s_port));
            mailboxManager = new FifteenMailboxManager(mailbox);
        } catch (SocketException e) {
            System.err.println(
                    "Error: Given host is unknown.");
            System.err.println("Usage: java Fifteen <s_host> <s_port>");
            System.exit(0);
        }

        // Keep track of all the games going on.
        HashMap<SocketAddress, FifteenModel> sessions =
                new HashMap<SocketAddress, FifteenModel>();

        // If session is empty, a new session needs to be created.
        // Else, add the beta player to the alpha player's session.
        FifteenModel session = null;

        // Server runs indefinitely.
        while(true) {
            String line = mailboxManager.receiveMessage();
            String[] message = line.split(" ");
            SocketAddress sender = mailboxManager.getSender();

            FifteenModel model = sessions.get(sender);
            FifteenViewProxy fifteenVP;

            // If session if empty, Beta joined.
            // Else, Alpha joined.
            if(message[0].equals("join")) {
                if(session == null) {
                    fifteenVP = new FifteenViewProxy(mailbox, sender);
                    session = new FifteenModel(message[1], fifteenVP);
                    sessions.put(sender, session);
                } else {
                    fifteenVP = (FifteenViewProxy) session.getModelListener();
                    fifteenVP.addPlayer(mailbox, sender);
                    session.addPlayer(message[1]);
                    sessions.put(sender, session);
                    session = null;
                }
            } else if(message[0].equals("digit"))
                model.setDigit(Integer.parseInt(message[1]));
            else if(message[0].equals("newgame"))
                model.newgame();
            else if(message[0].equals("quit"))
                model.quit();
        }
    }
}