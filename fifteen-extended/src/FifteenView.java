/**
 * @author Jenny Zhen
 * @author Alan Kaminsky
 * date: 02.20.14
 * language: Java
 * file: FifteenView.java
 * assignment: Fifteen
 * http://www.cs.rit.edu/~wrc/courses/csci251/projects/3/
 */

import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;

/**
 * FifteenView provides the user interface for the Fifteen network game.
 */
public class FifteenView extends JFrame implements FifteenModelListener {
    private static final int GAP = 10;
    private static final int COLS = 12;
    private FifteenViewListener fifteenVL;
    private int myID;
    private String theirName;

    /**
     * Class DigitButton provides a button labeled with a digit.
     */
    private class DigitButton extends JButton {
        private int digit;
        private boolean enabled = true;
        private boolean available = true;

        /**
         * Construct a new digit button.
         *
         * @param digit Digit for the button label.
         */
        public DigitButton(int digit) {
            super("" + digit);
            this.digit = digit;
            addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    onDigitButton(DigitButton.this.digit);
                }
            });
        }

        /**
         * Make this digit button available or unavailable. When available, the
         * button displays its digit. When not available, the button is blank.
         *
         * @param available True if available, false if not.
         */
        public void available(boolean available) {
            this.available = available;
            setText(available ? "" + digit : " ");
            updateButton();
        }

        /**
         * Enable or disable this digit button. When enabled and available,
         * clicking the button performs the appropriate action. Otherwise,
         * clicking the button has no effect.
         *
         * @param enabled True if enabled, false if not.
         */
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
            updateButton();
        }

        /**
         * Update this digit button's label and enabled state.
         */
        private void updateButton() {
            super.setEnabled(available && enabled);
        }
    }

    /**
     * User interface widgets.
     */
    private String myName;
    private DigitButton[] digitButton;
    private JTextField myScoreField;
    private JTextField theirScoreField;
    private JTextField winnerField;
    private JButton newGameButton;

    /**
     * Construct a new FifteenView object.
     *
     * @param myName Player's name.
     */
    public FifteenView(String myName) {
        super("Fifteen -- " + myName);
        this.myName = myName;
        JPanel panel = new JPanel();
        add(panel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
        JPanel panel_a = new JPanel();
        panel.add(panel_a);
        panel_a.setLayout(new BoxLayout(panel_a, BoxLayout.Y_AXIS));
        digitButton = new DigitButton[9];
        for (int i = 0; i < 9; ++i) {
            panel_a.add(digitButton[i] = new DigitButton(i + 1));
            digitButton[i].setAlignmentX(0.5f);
            digitButton[i].setEnabled(false);
            digitButton[i].setMinimumSize(digitButton[i].getPreferredSize());
            digitButton[i].setMaximumSize(digitButton[i].getPreferredSize());
            digitButton[i].setSize(digitButton[i].getPreferredSize());
        }
        panel.add(Box.createHorizontalStrut(GAP));
        JPanel panel_b = new JPanel();
        panel.add(panel_b);
        panel_b.setLayout(new BoxLayout(panel_b, BoxLayout.Y_AXIS));
        panel_b.add(myScoreField = new JTextField(COLS));
        myScoreField.setAlignmentX(0.5f);
        myScoreField.setEditable(false);
        myScoreField.setMaximumSize(myScoreField.getPreferredSize());
        myScoreField.setText(this.myName);
        panel_b.add(Box.createRigidArea(new Dimension(0, GAP)));
        panel_b.add(theirScoreField = new JTextField(COLS));
        theirScoreField.setAlignmentX(0.5f);
        theirScoreField.setEditable(false);
        theirScoreField.setMaximumSize(theirScoreField.getPreferredSize());
        theirScoreField.setText("Waiting for partner");
        panel_b.add(Box.createRigidArea(new Dimension(0, GAP)));
        panel_b.add(winnerField = new JTextField(COLS));
        winnerField.setAlignmentX(0.5f);
        winnerField.setEditable(false);
        winnerField.setMaximumSize(winnerField.getPreferredSize());
        panel_b.add(Box.createVerticalGlue());
        panel_b.add(newGameButton = new JButton("New Game"));
        newGameButton.setAlignmentX(0.5f);
        newGameButton.setMaximumSize(newGameButton.getPreferredSize());
        newGameButton.setEnabled(false);
        newGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onNewGameButton();
            }
        });
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onClose();
            }
        });
        pack();
        this.setVisible(true);
    }

    /**
     * Take action when a digit button is clicked.
     *
     * @param digit Digit that was clicked.
     */
    private void onDigitButton(int digit) {
        fifteenVL.setDigit(digit);
    }

    /**
     * Take action when the New Game button is clicked.
     */
    private void onNewGameButton() {
        fifteenVL.newgame();
    }

    /**
     * Take action when the Fifteen window is closing.
     */
    private void onClose() {
        fifteenVL.quit();
        System.exit(0);
    }

    /**
     * Set the view listener for the view to communicate to the model.
     * View does not know that the model is the model.
     * @param fifteenVL the view listener.
     */
    public void setViewListener(FifteenViewListener fifteenVL) {
        this.fifteenVL = fifteenVL;
    }

    @Override
    /**
     * Store my player's ID.
     */
    public void setID(int player) {
        this.myID = player;
    }

    @Override
    /**
     * Store their name. Set up game.
     */
    public void setName(int player, String name) {
        // Check if this is our player's ID
        if(player != this.myID) {
            this.theirName = name;
            this.theirScoreField.setText(name);
            this.newGameButton.setEnabled(true);
        }
    }

    @Override
    /**
     * Set the available digits on the board, as necessary.
     */
    public void setDigits(String digits) {
        boolean newgame = true;

        // Set the unavailable digits.
        for(int i = 0; i < 9; i++) {
            if(digits.charAt(i) == '0') {
                this.digitButton[i].available(false);
                newgame = false;
            }
        }

        // If it is a new game, clean the board.
        if(newgame) {
            for(int i = 0; i < 9; i++)
                this.digitButton[i].available(true);
            this.winnerField.setText("");
        }
    }

    @Override
    /**
     * Set the score on the board for the respective player.
     */
    public void setScore(int player, int score) {
        if(player == this.myID)
            this.myScoreField.setText(this.myName + " = " + score);
        else
            this.theirScoreField.setText(this.theirName + " = " + score);
    }

    @Override
    /**
     * Set the turn for the respective player.
     */
    public void setTurn(int player) {
        // If it is our turn/draw, enable available digits.
        if(player == this.myID || player == 0) {
            for (int i = 0; i < 9; i++) {
                if (this.digitButton[i].available)
                    this.digitButton[i].setEnabled(true);
            }
        }
        // If it is our opponent's turn, disable all digits.
        else {
            for (int i = 0; i < 9; i++) {
                this.digitButton[i].setEnabled(false);
            }
        }
    }

    @Override
    /**
     * Set the winner for the respective player.
     */
    public void setWin(int player) {
        if(player == 0)
            this.winnerField.setText("Draw!");
        else if(player == this.myID)
            this.winnerField.setText(this.myName + " wins!");
        else
            this.winnerField.setText(this.theirName + " wins!");

        // Disable further digit input, until a new game.
        for(int i = 0; i < 9; i++)
            this.digitButton[i].setEnabled(false);
    }

    @Override
    /**
     * Quit the game.
     */
    public void quit() {
        System.exit(0);
    }
}