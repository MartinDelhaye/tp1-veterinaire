package vet.client;

public final class Color {

    private Color() { }

    public static final String RESET = "\u001B[0m";

    public static final String SUCCESS = "\u001B[32m"; // vert
    public static final String ERROR = "\u001B[31m";   // rouge
    public static final String ALERT = "\u001B[33m";   // jaune
    public static final String INFO = "\u001B[36m";    // cyan
    public static final String TITLE = "\u001B[35m";   // magenta

    public static String success(String text) { return SUCCESS + text + RESET; }
    public static String error(String text) { return ERROR + text + RESET; }
    public static String alert(String text) { return ALERT + text + RESET; }
    public static String info(String text) { return INFO + text + RESET; }
    public static String title(String text) { return TITLE + text + RESET; }
}