/**
 * Model <--> ViewProxy <--> ModelProxy <--> View
 *
 * @author Jenny Zhen
 * date: 05.17.14
 * language: Java
 * file: FifteenViewProxy.java
 * assignment: Fifteen
 * http://www.cs.rit.edu/~wrc/courses/csci251/projects/4/
 */

import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class FifteenViewProxy implements FifteenModelListener {
    private FifteenMailboxManager alpha_mailbox, beta_mailbox;
    private InetSocketAddress alpha_client, beta_client;

    public FifteenViewProxy(
            DatagramSocket alpha_mailbox, DatagramSocket beta_mailbox,
            InetSocketAddress alpha_client, InetSocketAddress beta_client) {
        this.alpha_mailbox = new FifteenMailboxManager(alpha_mailbox);
        this.beta_mailbox = new FifteenMailboxManager(beta_mailbox);
        this.alpha_client = alpha_client;
        this.beta_client = beta_client;
    }

    @Override
    public void setID(int player) {

    }

    @Override
    public void setName(int player, String name) {

    }

    @Override
    public void setDigits(String digits) {

    }

    @Override
    public void setScore(int player, int score) {

    }

    @Override
    public void setTurn(int player) {

    }

    @Override
    public void setWin(int player) {

    }

    @Override
    public void quit() {

    }
}
