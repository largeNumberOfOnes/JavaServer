package muzzle;

import initial.MyLogger;
import server.ConsoleCommandHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {

    private javax.swing.JPanel mainPan;
    private JTabbedPane tabbedPane1;
    private JPanel consolePan;
    private JTextArea consoleSpace;
    private JTextField consoleInput;
    private JButton button1;
    private JTextPane textPane2;
    private JList list1;

    public MainFrame(MyLogger logger) {

        setContentPane(mainPan);
//        setContentPane(this.getContentPane());
        setTitle("Server View");
        setSize(700, 500);
        setMinimumSize(new Dimension(700, 500));
        setTitle("server ");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
//        button2.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent actionEvent) {
//                // IntelliJTheme.setup( MainFrame.class.getResourceAsStream("/com/formdev/flatlaf/intellijthemes/themes/arc-theme.theme.json") );
//                System.out.println("---");
//                IntelliJTheme.setup( MainFrame.class.getResourceAsStream("/com/formdev/flatlaf/intellijthemes/themes/arc_theme_dark.theme.json") );
//                SwingUtilities.updateComponentTreeUI(mainPan);
//            }
//        });

//        consolePan.getLayout().addLayoutComponent();

        // Console command handler
        consoleInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

//                var exchanger = new Exchanger<ConsoleCommand>();

                String text = consoleInput.getText();
//                consoleInput.setText("");
//                System.out.println(">>> " + text);
//                consoleSpace.append(text + "\n");
//                exchanger.exchange()
                consoleSpace.append(">>> " + text + "\n");
                try {
                    String ans = ConsoleCommandHandler.getInstance().commandHandler(text);
                    consoleSpace.append(ans + "\n");
                }
                catch (Exception e) {
//                    consoleSpace.append(">>>" + text + "\n");
                    consoleSpace.append("Error: Command cannot be executed");
                    e.printStackTrace();
                }


            }
        });

    }

}
