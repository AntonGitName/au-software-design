package ru.mit.spbau.antonpp.messanger;

import ru.mit.spbau.antonpp.messanger.ui.MainFrame;

import javax.swing.*;

/**
 * @author Anton Mordberg
 * @since 17.02.17
 */
public class Application {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}
