import Utilities.PathExpert;
import controller.SystemController;
import jdk.nashorn.internal.scripts.JO;

import javax.swing.*;

public class LanguagePick {
    public void pickLanguage(){
        Object[] flags = {new ImageIcon(getClass().getClassLoader().getResource("language/da.png")),new ImageIcon(getClass().getClassLoader().getResource("language/en.png"))};
        int box = JOptionPane.showOptionDialog(
                null,
                "",
                "Choose Language",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                flags,
               null);
        if (box == JOptionPane.YES_OPTION){
            PathExpert.setSprog("da");
            SystemController systemController = new SystemController();

        }
        else if (box == JOptionPane.NO_OPTION){
            PathExpert.setSprog("en");
            SystemController systemController = new SystemController();
        }
        else {
            System.exit(0);
        }
    }
}
