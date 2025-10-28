package src;

import java.io.File;
import java.util.Scanner;

public class SaisieUtilitaire {
    private static Scanner scanner = new Scanner(System.in);
    
    public static int saisirEntier(String message) {
        while (true) {
            try {
                System.out.print(message);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Quantité invalide (non numérique). Réessayez.");
            }
        }
    }
    
    public static String saisirString(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }
    
    public static String saisirCheminFichier(String message) {
        while (true) {
            String chemin = saisirString(message);
            try {
                File file = new File(chemin);
                if (file.exists() && file.canRead()) {
                    return chemin;
                } else {
                    System.out.println("Chemin introuvable ou inaccessible : " + chemin + " --- saisissez un autre chemin.");
                }
            } catch (SecurityException e) {
                System.out.println("Accès refusé : " + chemin + " --- saisissez un autre chemin.");
            }
        }
    }
}