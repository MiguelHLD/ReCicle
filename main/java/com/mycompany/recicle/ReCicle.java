
package com.mycompany.recicle;

import javax.swing.SwingUtilities;

public class ReCicle {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            Database.initialize();
            new Home().setVisible(true);
        });

    }
}
