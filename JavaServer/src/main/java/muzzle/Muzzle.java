package muzzle;

import com.formdev.flatlaf.IntelliJTheme;
import initial.MyLogger;

import javax.swing.*;

public class Muzzle implements Runnable {

    public Muzzle(MyLogger logger) {
        // FlatDarkLaf.setup();
        IntelliJTheme.setup( MainFrame.class.getResourceAsStream("/com/formdev/flatlaf/intellijthemes/themes/arc_theme_dark_orange.theme.json") );
        JFrame.setDefaultLookAndFeelDecorated( true );
        JDialog.setDefaultLookAndFeelDecorated( true );

        var mainFrame = new MainFrame(logger);
    }

    @Override
    public void run() {

    }

}
