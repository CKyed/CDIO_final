package Utilities;

public class PathExpert {
    private static String language = "da";
    public static String namePath;
    public static String fieldAttributesPath ;
    public static String descriptionMessagesPath ;
    public static String setupMessagesPath  ;
    public static String turnMessagesPath  ;
    public static String chanceCardPath  ;
    public static String endMessagePath;

    public static void setSprog(String sprog) {
        language = sprog;
        namePath                    = "language/" + language +"/fields/name";
        fieldAttributesPath         = "fieldAttributes";
        descriptionMessagesPath     = "language/" + language +"/fields/description";
        setupMessagesPath           = "language/" + language +"/messages/setupMessages";
        turnMessagesPath            = "language/" + language +"/messages/turnMessages";
        chanceCardPath              = "language/" + language +"/chanceCardTexts";
        endMessagePath              = "language/" + language +"/messages/endGameMessages";
    }
}

