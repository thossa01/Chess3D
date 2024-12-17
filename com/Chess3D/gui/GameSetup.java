package com.Chess3D.gui;

import com.Chess3D.core.player.Player;
import com.Chess3D.core.playerColor;
import com.Chess3D.gui.Table.PlayerType;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.*;

class GameSetup extends JDialog {
    private PlayerType whitePlayerType;
    private PlayerType blackPlayerType;

    private static final String HUMAN_TEXT = "Human";
    private static final String AI_TEXT = "AI";

    public GameSetup(final JFrame jFrame, final boolean modal) {
        super(jFrame, modal);
        final JPanel myPanel = new JPanel(new GridLayout(0, 1));
        final JRadioButton whiteHumanButton = new JRadioButton(HUMAN_TEXT);
        final JRadioButton whiteAIButton = new JRadioButton(AI_TEXT);
        final JRadioButton blackHumanButton = new JRadioButton(HUMAN_TEXT);
        final JRadioButton blackAIButton = new JRadioButton(AI_TEXT);
        whiteHumanButton.setActionCommand(HUMAN_TEXT);
        final ButtonGroup whiteGroup = new ButtonGroup();
        whiteGroup.add(whiteHumanButton);
        whiteGroup.add(whiteAIButton);
        whiteHumanButton.setSelected(true);

        final ButtonGroup blackGroup = new ButtonGroup();
        blackGroup.add(blackHumanButton);
        blackGroup.add(blackAIButton);
        blackHumanButton.setSelected(true);

        getContentPane().add(myPanel);
        myPanel.add(new JLabel("White"));
        myPanel.add(whiteHumanButton);
        myPanel.add(whiteAIButton);
        myPanel.add(new JLabel("Black"));
        myPanel.add(blackHumanButton);
        myPanel.add(blackAIButton);

        myPanel.add(new JLabel("Search"));
        addLabeledSpinner(myPanel, "Search Depth", new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));

        final JButton cancelButton = new JButton("Cancel");
        final JButton okButton = new JButton("OK");

        okButton.addActionListener((ActionEvent e) -> {
            whitePlayerType = whiteAIButton.isSelected() ? PlayerType.AI : PlayerType.HUMAN;
            blackPlayerType = blackAIButton.isSelected() ? PlayerType.AI : PlayerType.HUMAN;
            GameSetup.this.setVisible(false);
        });

        cancelButton.addActionListener((ActionEvent e) -> {
            System.out.println("Cancel");
            GameSetup.this.setVisible(false);
        });

        myPanel.add(cancelButton);
        myPanel.add(okButton);

        setLocationRelativeTo(jFrame);
        pack();
        setVisible(false);
    }

    void promptUser(){
        setVisible(true);
        repaint();
    }

    boolean isAIPlayer(final Player player){
        if (player.getPlayerColor() == playerColor.WHITE){
            return getWhitePlayerType() == PlayerType.AI;
        }

        return getBlackPlayerType() == PlayerType.AI;
    }

    private JSpinner addLabeledSpinner(Container c, String search_Depth, SpinnerNumberModel spinnerNumberModel) {
        final JLabel search = new JLabel(search_Depth);
        c.add(search);
        final JSpinner spinner = new JSpinner(spinnerNumberModel);
        search.setLabelFor(spinner);
        c.add(spinner);
        return spinner;
    }

    private PlayerType getWhitePlayerType() {
        return this.whitePlayerType;
    }

    private PlayerType getBlackPlayerType() {
        return this.blackPlayerType;
    }

    
}
