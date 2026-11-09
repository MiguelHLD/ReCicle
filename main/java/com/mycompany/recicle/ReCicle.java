
package com.mycompany.recicle;

import com.mycompany.recicle.database.Database;
import com.mycompany.recicle.view.Home;
import javax.swing.SwingUtilities;

public class ReCicle {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            Database.initialize();
            new Home().setVisible(true);
        });

    }
}
