package br.com.game.service;

import br.com.game.entity.Node;
import br.com.game.entity.Tree;
import br.com.game.enumeration.Direction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.Queue;

public class GameGourmet {

    private static final String TITLE_GAME = "Game Gourmet";
    private static final String TITLE_CONFIRM = "Confirm";
    private static final String MESSAGE_START = "Pense um prato que gosta";
    private static final String MESSAGE_QUESTION = "O prato que você pensou é $?";
    private static final String MESSAGE_QUESTION_FOOD = "Qual prato você pensou?";
    private static final String MESSAGE_COMPLETE = "$1 é _______ mas $2 não.";
    private static final String MESSAGE_HIT = "Acertei de novo!";
    private static final String BUTTON_TEXT = "OK";
    private static final int FRAME_SIZE_WIDTH = 300;
    private static final int FRAME_SIZE_HEIGHT = 150;

    private Tree tree;

    public GameGourmet() {
        this.tree = new Tree();
        this.tree.addRoot("Massa");
        this.tree.add(this.tree.getRoot(), "Lasanha", Direction.LEFT);
        this.tree.add(this.tree.getRoot(), "Bolo de Chocolate", Direction.RIGHT);
    }

    private void procedure() {
        Queue<Node> nodes = new LinkedList<>();
        nodes.add(this.tree.getRoot());

        while (!nodes.isEmpty()) {
            Node current = nodes.remove();
            int response = JOptionPane.showConfirmDialog(null,
                    MESSAGE_QUESTION.replace("$", current.getData()),
                    TITLE_CONFIRM,
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            switch (response) {
                case JOptionPane.YES_OPTION:
                    if (current.getLeft() == null) {
                        JOptionPane.showMessageDialog(null, MESSAGE_HIT);
                    } else {
                        nodes.add(current.getLeft());
                    }
                    break;
                case JOptionPane.NO_OPTION:
                    if (current.getRight() == null) {
                        String food = null;
                        String attribute = null;

                        while (food == null || "".equals(food.trim())) {
                            food = JOptionPane.showInputDialog(null, MESSAGE_QUESTION_FOOD);
                        }

                        while (attribute == null || "".equals(attribute.trim())) {
                            attribute = JOptionPane.showInputDialog(null,
                                    MESSAGE_COMPLETE.replace("$1", food).replace("$2", current.getData()));
                        }

                        String value = current.getData();
                        current.setData(attribute);
                        //current.setLeft(new Node(food));
                        //current.setRight(new Node(value));
                        this.tree.add(current, food, Direction.LEFT);
                        this.tree.add(current, value, Direction.RIGHT);

                        this.tree.bfs();
                    } else {
                        nodes.add(current.getRight());
                    }
                    break;
            }
        }
    }

    public void start() {
        JFrame frame = new JFrame(TITLE_GAME);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.white);
        frame.setResizable(false);
        frame.setSize(new Dimension(FRAME_SIZE_WIDTH,FRAME_SIZE_HEIGHT));
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(MESSAGE_START);
        JButton button = new JButton(BUTTON_TEXT);
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                procedure();
            }
        });

        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalGlue());
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(button);
        panel.add(Box.createVerticalGlue());

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }
}