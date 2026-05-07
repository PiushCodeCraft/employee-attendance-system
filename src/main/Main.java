package main;

import ui.LoginFrame;
import utils.Theme;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Use system rendering for better fonts
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Global UI defaults
        UIManager.put("OptionPane.background",        Theme.BG_CARD);
        UIManager.put("Panel.background",             Theme.BG_CARD);
        UIManager.put("OptionPane.messageForeground", Theme.TEXT_PRIMARY);
        UIManager.put("Button.background",            Theme.ACCENT_BLUE);
        UIManager.put("Button.foreground",            java.awt.Color.WHITE);

        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
