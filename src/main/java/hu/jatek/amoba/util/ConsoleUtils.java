package hu.jatek.amoba.util;

public class ConsoleUtils {

    private ConsoleUtils() {
    }

    public static void clearScreen() {
        try {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        } catch (Exception e) {
            // ha nem működik, nem baj
        }
    }
}
