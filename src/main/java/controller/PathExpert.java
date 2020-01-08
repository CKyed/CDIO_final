package controller;

public class PathExpert {
    private static String language = "da";
    public static final String initiateGamePath    = "language/" + language +"/gamecontroller/initgame";
    public static final String turnPath            = "language/" + language +"/gamecontroller/turn";
    public static final String namePath            = "language/" + language +"/fields/name";
    public static final String descriptionPath     = "language/" + language +"/fields/description";
    public static final String fieldAttributesPath = "fieldAttributes";

    public static void setSprog(String sprog) {
        language = sprog;
    }
}

