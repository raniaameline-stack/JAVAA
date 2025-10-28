package src;

import java.io.*;
import java.util.Scanner;

public class Catalogue {
    private Article[] articles;
    private int tailleCourante;
    private int capacite;
    private int[][] statistiques; // [ventesDuJour, retoursDuJour] par article
    
    public Catalogue() {
        this.capacite = 10;
        this.articles = new Article[capacite];
        this.tailleCourante = 0;
        this.statistiques = new int[capacite][2];
    }
    
    public void ajouterArticle(Article a) {
        agrandirCatalogueSiNecessaire();
        articles[tailleCourante] = a;
        tailleCourante++;
    }
    
    public int trouverIndexParId(String id) {
        for (int i = 0; i < tailleCourante; i++) {
            if (articles[i].getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }
    
    private void agrandirCatalogueSiNecessaire() {
        if (tailleCourante >= capacite) {
            capacite += 10;
            Article[] nouveauTableau = new Article[capacite];
            System.arraycopy(articles, 0, nouveauTableau, 0, tailleCourante);
            articles = nouveauTableau;
            
            // Redimensionner aussi les statistiques
            int[][] nouvellesStats = new int[capacite][2];
            System.arraycopy(statistiques, 0, nouvellesStats, 0, tailleCourante);
            statistiques = nouvellesStats;
        }
    }
    
    public void afficherCatalogue() {
        System.out.println("[CATALOGUE]");
        for (int i = 0; i < tailleCourante; i++) {
            System.out.println(articles[i]);
        }
    }
    
    public Article getArticle(int index) {
        if (index < 0 || index >= tailleCourante) {
            return null;
        }
        return articles[index];
    }
    
    public int getTailleCourante() {
        return tailleCourante;
    }
    
    public int chargerDepuisFichier(String chemin) {
        Scanner scanner = null;
        int articlesCharges = 0;
        
        try {
            File file = new File(chemin);
            scanner = new Scanner(file);
            
            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine().trim();
                if (ligne.isEmpty() || ligne.startsWith("#")) {
                    continue; // Ignorer les lignes vides et les commentaires
                }
                
                try {
                    String[] parties = ligne.split("\\|");
                    if (parties.length >= 4) {
                        String id = parties[0].trim();
                        String libelle = parties[1].trim();
                        int prix = Integer.parseInt(parties[2].trim());
                        int stock = Integer.parseInt(parties[3].trim());
                        
                        Article article = new Article(id, libelle, prix, stock);
                        ajouterArticle(article);
                        articlesCharges++;
                    }
                } catch (Exception e) {
                    System.out.println("Ligne mal formée ignorée: " + ligne);
                }
            }
            
            System.out.println(articlesCharges + " articles chargés depuis " + chemin);
            return articlesCharges;
            
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Chemin introuvable : " + chemin + " --- saisissez un autre chemin.");
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }
    
    public int sauvegarderVersFichier(String chemin) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(chemin))) {
            for (int i = 0; i < tailleCourante; i++) {
                Article a = articles[i];
                writer.println(a.getId() + " | " + a.getLibelle() + " | " + 
                             a.getPrixCentimes() + " | " + a.getStock());
            }
            System.out.println(tailleCourante + " articles sauvegardés dans " + chemin);
            return tailleCourante;
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde: " + e.getMessage());
        }
    }
}