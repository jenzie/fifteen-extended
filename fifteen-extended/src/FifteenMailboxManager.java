import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * @author Jenny Zhen
 * date: 05.17.14
 * language: Java
 * file: FifteenMailboxManager.java
 * assignment: Fifteen
 * http://www.cs.rit.edu/~wrc/courses/csci251/projects/4/
 */

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
     */
    public void sendMessage(String message, InetSocketAddress recipient) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(baos);

        try {
            output.writeUTF(message);
            byte[] payload = baos.toByteArray();
            mailbox.send(
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
            mailbox.receive(packet);
            sender = packet.getSocketAddress();
            DataInputStream input = new DataInputStream(
                    new ByteArrayInputStream(payload, 0, packet.getLength()));
            return input.readUTF();
        } catch (IOException e) {
            System.err.println(
                    "Error: Connection to the given host and port failed.");
        }
        return null;
    }
}
