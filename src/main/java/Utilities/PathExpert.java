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
        namePath            = "language/" + language +"/fields/name";
        fieldAttributesPath = "fieldAttributes";
        descriptionMessagesPath   = "language/" + language +"/fields/description";
        setupMessagesPath   = "language/" + language +"/messages/setupMessages";
        turnMessagesPath   = "language/" + language +"/messages/turnMessages";
        chanceCardPath   = "language/" + language +"/chanceCardTexts";
        endMessagePath   = "language/" + language +"/messages/endGameMessages";
    }

    public static String getLanguage() {
        return language;
    }

    public static String getNamePath() {
        return namePath;
    }

    public static String getFieldAttributesPath() {
        return fieldAttributesPath;
    }

    public static String getDescriptionMessagesPath() {
        return descriptionMessagesPath;
    }

    public static String getSetupMessagesPath() {
        return setupMessagesPath;
    }

    public static String getTurnMessagesPath() {
        return turnMessagesPath;
    }

    public static String getChanceCardPath() {
        return chanceCardPath;
    }

    public static String getEndMessagePath() {
        return endMessagePath;
    }
}

