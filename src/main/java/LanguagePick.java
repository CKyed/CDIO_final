import Utilities.PathExpert;
import controller.SystemController;
import jdk.nashorn.internal.scripts.JO;

import javax.swing.*;

public class LanguagePick {
    public void pickLanguage(){
        Object[] flags = {
                new ImageIcon(getClass().getClassLoader().getResource("language/da.png")),
                new ImageIcon(getClass().getClassLoader().getResource("language/en.png")),
                new ImageIcon(getClass().getClassLoader().getResource("language/es.png")),
                new ImageIcon(getClass().getClassLoader().getResource("language/ar.png")),
                new ImageIcon(getClass().getClassLoader().getResource("language/sw.png")),
                new ImageIcon(getClass().getClassLoader().getResource("language/bg.png")),
                new ImageIcon(getClass().getClassLoader().getResource("language/de.png"))
        };
        int box = JOptionPane.showOptionDialog(
                null,
                "",
                "Choose Language",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                flags,
               null);
        switch (box) {
            case 0: {
                PathExpert.setSprog("da");
                SystemController systemController = new SystemController();
                break;
            }
            case 1: {
                PathExpert.setSprog("en");
                SystemController systemController = new SystemController();
                break;
            }
            case 2: {
                PathExpert.setSprog("es");
                SystemController systemController = new SystemController();
                break;
            }
            case 3: {
                PathExpert.setSprog("ar");
                SystemController systemController = new SystemController();
                break;
            }
            case 4: {
                PathExpert.setSprog("sw");
                SystemController systemController = new SystemController();
                break;
            }
            case 5: {
                PathExpert.setSprog("bg");
                SystemController systemController = new SystemController();
                break;
            }
            case 6: {
                PathExpert.setSprog("de");
                SystemController systemController = new SystemController();
                break;
            }
            default:
                System.exit(0);
        }
    }
}
