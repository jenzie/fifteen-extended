/**
 * Direction of Communication (where ML is model listener, VL is view listener):
 * ML (view) -> VL (ModelProxy) *-> server -> model
 *      -> ML (ViewProxy) ->  VL (ModelProxy) -> ML (View)
 *
 * @author Jenny Zhen
 * date: 05.17.14
 * language: Java
 * file: FifteenMailboxManager.java
 * assignment: Fifteen
 * http://www.cs.rit.edu/~wrc/courses/csci251/projects/4/
 */

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;

public class FifteenMailboxManager {
    private DatagramSocket mailbox; // sends and receives packets (messages)
    private SocketAddress sender; // sender of the previous packet

    /**
     * Construct a new mailbox manager.
     *
     * @param mailbox Mailbox from which to read datagrams.
     */
    public FifteenMailboxManager(DatagramSocket mailbox)
    {
        this.mailbox = mailbox;
    }

    /**
     * Sends a datagram to the recipient.
     *  @param message the message to send.
     * @param recipient the receiver of the message.
     */
    public void sendMessage(String message, SocketAddress recipient) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(baos);

        try {
            output.writeUTF(message);
            byte[] payload = baos.toByteArray();
            this.mailbox.send(
                    new DatagramPacket(payload, payload.length, recipient));
        } catch (IOException e ) {
            System.err.println(
                    "Error: Connection to the given host and port failed.");
        }

        try {
            output.close();
        } catch (IOException e) {
            System.err.println(
                    "Error: Connection to the given host and port failed.");
        }
    }

    /**
     * Receive and process the next datagram.
     *
     * @return the received message.
     */
    public String receiveMessage() {
        byte[] payload = new byte[128]; // 128 is likely the largest data size
        DatagramPacket packet = new DatagramPacket(payload, payload.length);

        try {
            this.mailbox.receive(packet);
            this.sender = packet.getSocketAddress();
            DataInputStream input = new DataInputStream(
                    new ByteArrayInputStream(payload, 0, packet.getLength()));
            return input.readUTF();
        } catch (IOException e) {
            // Happens when quitting/exiting the session.
        }
        return null;
    }

    /**
     * Get the previous sender.
     * @return the previous sender.
     */
    public SocketAddress getSender() {
        return this.sender;
    }

    /**
     * Close the connection to the mailbox.
     */
    public void close() {
        this.mailbox.close();
    }
}
