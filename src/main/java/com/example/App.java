
package com.example;

import com.example.ui.LoginFrame;

import javax.swing.SwingUtilities;

public class App {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new LoginFrame();
        });
    }
}
