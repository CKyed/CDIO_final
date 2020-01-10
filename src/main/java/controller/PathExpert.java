package controller;

public class PathExpert {
    private static String language = "da";
    public static final String namePath            = "language/" + language +"/fields/name";
    public static final String fieldAttributesPath = "fieldAttributes/";
    public static final String setupMessagesPath   = "language/" + language +"/messages/setupMessages";
    public static final String turnMessagesPath   = "language/" + language +"/messages/turnMessages";

    public static void setSprog(String sprog) {
        language = sprog;
    }
}

