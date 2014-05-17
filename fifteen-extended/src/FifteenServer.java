/**
 * The server for Fifteen.
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
import java.net.SocketException;
import java.util.HashMap;

public class FifteenServer {
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
        InetSocketAddress server = new InetSocketAddress(s_host, s_port);
        DatagramSocket mailbox = null;
        try {
            mailbox = new DatagramSocket(new InetSocketAddress(s_host, s_port));
            FifteenMailboxManager mailboxManager =
                    new FifteenMailboxManager(mailbox);
        } catch (SocketException e) {
            System.err.println(
                    "Error: Given host is unknown.");
            System.err.println("Usage: java Fifteen <s_host> <s_port>");
            System.exit(0);
        }

        // Keep track of all the games going on.
        HashMap<InetSocketAddress, FifteenModel> sessions =
                new HashMap<InetSocketAddress, FifteenModel>();
    }
}