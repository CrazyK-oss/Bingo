package com.takalace;

import javax.swing.*;

public class App {

    public static void main(String[] args) {
        // Configurar la apariencia de la interfaz gráfica (opcional)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Crear una instancia de la interfaz de usuario BingoUI
        SwingUtilities.invokeLater(() -> {
            BingoUI bingoUI = new BingoUI();
            bingoUI.setVisible(true);
        });
    }
}
